/*
 * Copyright (c) 2016 Molly Nicholas
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * (subject to the limitations in the disclaimer below) provided that the following conditions are
 * met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 * and the following disclaimer in the documentation and/or other materials provided with the
 * distribution.
 *
 * Neither the name of Molly Nicholas nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS LICENSE. THIS
 * SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.robotcore.internal.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.robocol.PeerDiscovery;
import com.qualcomm.robotcore.robocol.RobocolDatagram;
import com.qualcomm.robotcore.robocol.RobocolDatagramSocket;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.wifi.NetworkConnection;
import com.qualcomm.robotcore.wifi.NetworkConnectionFactory;
import com.qualcomm.robotcore.wifi.NetworkType;
import com.qualcomm.robotcore.wifi.SoftApAssistant;
import com.qualcomm.robotcore.wifi.WifiDirectAssistant;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("WeakerAccess")
public class NetworkConnectionHandler {

    //----------------------------------------------------------------------------------------------
    // Static State
    //----------------------------------------------------------------------------------------------

    public static final String TAG = "NetworkConnectionHandler";
    private static final NetworkConnectionHandler theInstance = new NetworkConnectionHandler();

    public static NetworkConnectionHandler getInstance() {
        return theInstance;
    }

    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    protected @Nullable WifiManager.WifiLock wifiLock;
    protected boolean setupNeeded = true;

    protected Context context;
    protected ElapsedTime lastRecvPacket = new ElapsedTime();
    protected InetAddress remoteAddr;
    protected RobocolDatagramSocket socket;
    protected ScheduledExecutorService sendLoopService = Executors.newSingleThreadScheduledExecutor();
    protected ScheduledFuture<?> sendLoopFuture;
    protected SendOnceRunnable sendOnceRunnable;
    protected SetupRunnable setupRunnable;
    protected @Nullable String connectionOwner;
    protected @Nullable String connectionOwnerPassword;

    protected NetworkConnection networkConnection = null;
    protected final NetworkConnectionCallbackChainer theNetworkConnectionCallback = new NetworkConnectionCallbackChainer();
    protected RecvLoopRunnable recvLoopRunnable;
    protected final RecvLoopCallbackChainer theRecvLoopCallback = new RecvLoopCallbackChainer();
    protected final Object callbackLock = new Object(); // paranoia more than reality, but better safe than sorry. Guards the..Callback vars

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public static WifiManager.WifiLock newWifiLock(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF, "");
    }

    public NetworkType getDefaultNetworkType(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return NetworkType.fromString(preferences.getString(context.getString(R.string.pref_network_connection_type), NetworkType.WIFIDIRECT.toString()));
    }

    // Init on DS
    public void init(@NonNull WifiManager.WifiLock wifiLock, @NonNull NetworkType networkType, @NonNull String owner, @NonNull String password, @NonNull Context context) {
        this.wifiLock = wifiLock;
        this.connectionOwner = owner;
        this.connectionOwnerPassword = password;
        this.context = context;

        shutdown();
        this.networkConnection = null;
        initNetworkConnection(networkType);

        startWifiAndDiscoverConnections();
    }

    // Init on RC. This method is idempotent.
    public void init(@NonNull NetworkType networkType, @NonNull Context context) {
        this.context = context;
        initNetworkConnection(networkType);
    }

    private void initNetworkConnection(NetworkType networkType) {
        if (this.networkConnection != null && this.networkConnection.getNetworkType() != networkType) {
            // We're switching network types
            shutdown();
            this.networkConnection = null;
        }
        if (this.networkConnection == null) {
            this.networkConnection = NetworkConnectionFactory.getNetworkConnection(networkType, context);
            synchronized (callbackLock) {
                this.networkConnection.setCallback(theNetworkConnectionCallback);
            }
        }
    }

    public void setRecvLoopRunnable(RecvLoopRunnable recvLoopRunnable) {
        synchronized (callbackLock) {
            this.recvLoopRunnable = recvLoopRunnable;
            this.recvLoopRunnable.setCallback(theRecvLoopCallback);
        }
    }

    public NetworkType getNetworkType() {
        return networkConnection.getNetworkType();
    }

    public void startWifiAndDiscoverConnections() {
        acquireWifiLock();
        networkConnection.enable();
        if (!networkConnection.isConnected()) networkConnection.discoverPotentialConnections();
    }

    public void startConnection(@NonNull String owner, @NonNull String password) {
        connectionOwner = owner;
        connectionOwnerPassword = password;
        networkConnection.connect(connectionOwner, connectionOwnerPassword);
    }

    public boolean connectedWithUnexpectedDevice() {
        if (connectionOwner != null) {
            if (!connectionOwner.equals(networkConnection.getConnectionOwnerMacAddress())) {
                RobotLog.ee(TAG,"Network Connection - connected to " + networkConnection.getConnectionOwnerMacAddress() + ", expected " + connectionOwner);
                return true;
            }
        }
        return false;
    }

    public void acquireWifiLock() {
        if (wifiLock != null) wifiLock.acquire();
    }

    public boolean isNetworkConnected() {
        return networkConnection.isConnected();
    }

    public boolean isWifiDirect() {
        return networkConnection.getNetworkType().equals(NetworkType.WIFIDIRECT);
    }

    public void discoverPotentialConnections() {
        networkConnection.discoverPotentialConnections();
    }

    public void cancelConnectionSearch() {
        networkConnection.cancelPotentialConnections();
    }

    public String getFailureReason() {
        return networkConnection.getFailureReason();
    }

    public String getConnectionOwnerName() {
        return networkConnection.getConnectionOwnerName();
    }

    public String getDeviceName() {
        return networkConnection.getDeviceName();
    }

    public void stop() {
        networkConnection.disable();
        if (wifiLock != null && wifiLock.isHeld()) wifiLock.release();
    }

    public boolean connectingOrConnected() {
        NetworkConnection.ConnectStatus status = networkConnection.getConnectStatus();
        return status == WifiDirectAssistant.ConnectStatus.CONNECTED ||
                status == WifiDirectAssistant.ConnectStatus.CONNECTING;
    }

    public boolean connectionMatches(String name) {
        return connectionOwner != null && connectionOwner.equals(name);
    }

    public synchronized CallbackResult handleConnectionInfoAvailable(SocketConnect socketConnect) {
        CallbackResult result = CallbackResult.HANDLED;
        if (networkConnection.isConnected() && setupNeeded ) {
            setupNeeded = false;

            if (networkConnection.getNetworkType() == NetworkType.SOFTAP) {
                try {
                    Thread.sleep(2000); // in milliseconds.
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            synchronized (callbackLock) {
                setupRunnable = new SetupRunnable(theRecvLoopCallback, networkConnection, lastRecvPacket, socketConnect);
            }
            (new Thread(setupRunnable)).start();
        }
        return result;
    }

    public synchronized CallbackResult handlePeersAvailable() {
        CallbackResult result = CallbackResult.NOT_HANDLED;
        NetworkType networkType = networkConnection.getNetworkType();
        switch (networkType) {
        case WIFIDIRECT:
            result = handleWifiDirectPeersAvailable();
            break;
        case SOFTAP:
            result = handleSoftAPPeersAvailable();
            break;
        case LOOPBACK:
        case UNKNOWN_NETWORK_TYPE:
            RobotLog.e("Unhandled peers available event: " + networkType.toString());
            break;
        }
        return result;
    }

    private CallbackResult handleSoftAPPeersAvailable() {
        CallbackResult result = CallbackResult.NOT_HANDLED;
        // look for driver station
        List<ScanResult> scanResults = ((SoftApAssistant) networkConnection).getScanResults();
        for (ScanResult scanResult : scanResults) {
            RobotLog.v(scanResult.SSID);
            if (scanResult.SSID.equalsIgnoreCase(connectionOwner)) {
                // driver station found; connect
                networkConnection.connect(connectionOwner, connectionOwnerPassword);
                result = CallbackResult.HANDLED;
                break;
            }
        }
        return result;
    }

    private CallbackResult handleWifiDirectPeersAvailable() {
        CallbackResult result = CallbackResult.NOT_HANDLED;
        List<WifiP2pDevice> peers = ((WifiDirectAssistant) networkConnection).getPeers();
        for (WifiP2pDevice peer : peers) {
            if (peer.deviceAddress.equalsIgnoreCase(connectionOwner)) {
                // driver station found; connect
                networkConnection.connect(peer.deviceAddress);
                result = CallbackResult.HANDLED;
                break;
            }
        }
        return result;
    }

    public synchronized void updateConnection
            (
            @NonNull RobocolDatagram packet,
            @Nullable SendOnceRunnable.Parameters parameters,
            SendOnceRunnable.ClientCallback clientCallback
            ) throws RobotCoreException {

        if (packet.getAddress().equals(remoteAddr)) {
            if (sendOnceRunnable != null) sendOnceRunnable.onPeerConnected(false);
            if (clientCallback != null) clientCallback.peerConnected(false);
            return;
        }

        if (parameters==null) parameters = new SendOnceRunnable.Parameters();

        // Actually parse the packet in order to verify Robocol version compatibility
        PeerDiscovery peerDiscovery = PeerDiscovery.forReceive();
        peerDiscovery.fromByteArray(packet.getData());

        // update remoteAddr with latest address
        remoteAddr = packet.getAddress();
        RobotLog.vv(PeerDiscovery.TAG,"new remote peer discovered: " + remoteAddr.getHostAddress());

        if (socket==null && setupRunnable != null) {
            socket = setupRunnable.getSocket();
        }

        if (socket != null) {
            try {
                socket.connect(remoteAddr);
            } catch (SocketException e) {
                throw RobotCoreException.createChained(e, "unable to connect to %s", remoteAddr.toString());
            }

            // start send loop, if needed
            if (sendLoopFuture == null || sendLoopFuture.isDone()) {
                RobotLog.vv(TAG, "starting sending loop");
                sendOnceRunnable = new SendOnceRunnable(context, clientCallback,  socket, lastRecvPacket, parameters);
                sendLoopFuture = sendLoopService.scheduleAtFixedRate(sendOnceRunnable, 0, 40, TimeUnit.MILLISECONDS);
            }

            if (sendOnceRunnable != null) sendOnceRunnable.onPeerConnected(true);
            if (clientCallback != null) clientCallback.peerConnected(true);
        }
    }

    // synchronized avoids race with shutdown()
    public synchronized boolean removeCommand(Command cmd) {
        return (sendOnceRunnable != null) && sendOnceRunnable.removeCommand(cmd);
    }

    // synchronized avoids race with shutdown()
    public synchronized void sendCommand(Command cmd) {
        if (sendOnceRunnable != null) sendOnceRunnable.sendCommand(cmd);
    }

    // synchronized avoids race with shutdown()
    public synchronized void sendReply(Command commandRequest, Command commandResponse) {
        if (wasTransmittedRemotely(commandRequest)) {
            sendCommand(commandResponse);
        } else {
            injectReceivedCommand(commandResponse);
        }
    }

    protected boolean wasTransmittedRemotely(Command command) {
        return !command.isInjected();
    }

    /** Inject the indicated command into the reception infrastructure as if it had been transmitted remotely */
    public synchronized void injectReceivedCommand(Command cmd) {
        if (setupRunnable != null) {
            cmd.setIsInjected(true);
            setupRunnable.injectReceivedCommand(cmd);
        } else {
            RobotLog.vv(TAG, "injectReceivedCommand(): setupRunnable==null; command ignored");
        }
    }

    public CallbackResult processAcknowledgments(Command command) throws RobotCoreException {
        if (command.isAcknowledged()) {
            if (SendOnceRunnable.DEBUG) RobotLog.vv(SendOnceRunnable.TAG, "received ack: %s(%d)", command.getName(), command.getSequenceNumber());
            removeCommand(command);
            return CallbackResult.HANDLED;
        }
        // Note: this is an expensive approach to exactly-once datagram transmission. We should avoid (re)sending the message body in the ack
        command.acknowledge();
        sendCommand(command);
        return CallbackResult.NOT_HANDLED;
    }

    public synchronized void sendDatagram(RobocolDatagram datagram) {
        if (socket!=null && socket.getInetAddress()!=null ) socket.send(datagram);
    }

    public synchronized void clientDisconnect() {
        if (sendOnceRunnable != null) sendOnceRunnable.clearCommands();
        remoteAddr = null;
    }

    public synchronized void shutdown() {
        // shutdown logic tries to take state back to what it was before setup, etc, happened

        if (setupRunnable != null) {
            setupRunnable.shutdown();
            setupRunnable = null;
        }

        if (sendLoopFuture != null) {
            sendLoopFuture.cancel(true);
            sendOnceRunnable = null;
            sendLoopFuture = null;
        }

        // close the socket as well
        if (socket != null) {
            socket.close();
            socket = null;
        }

        // reset the client
        remoteAddr = null;

        // reset need for handleConnectionInfoAvailable
        setupNeeded = true;
    }

    //----------------------------------------------------------------------------------------------
    // Callback chainers
    // Here we find the *actual* classes we register for callback notifications of various
    // forms. Internally, they maintain chains of external, registered callbacks, to whom they
    // delegate.
    //----------------------------------------------------------------------------------------------

    public void pushNetworkConnectionCallback(@Nullable NetworkConnection.NetworkConnectionCallback callback) {
        synchronized (callbackLock) {
            this.theNetworkConnectionCallback.push(callback);
        }
    }

    public void removeNetworkConnectionCallback(@Nullable NetworkConnection.NetworkConnectionCallback callback) {
        synchronized (callbackLock) {
            this.theNetworkConnectionCallback.remove(callback);
        }
    }

    protected class NetworkConnectionCallbackChainer implements NetworkConnection.NetworkConnectionCallback {

        protected final CopyOnWriteArrayList<NetworkConnection.NetworkConnectionCallback> callbacks = new CopyOnWriteArrayList<NetworkConnection.NetworkConnectionCallback>();

        void push(@Nullable NetworkConnection.NetworkConnectionCallback callback) {
            synchronized (callbacks) {  // for uniqueness testing
                remove(callback);
                if (callback != null && !callbacks.contains(callback)) {
                    callbacks.add(0, callback);
                }
            }
        }

        void remove(@Nullable NetworkConnection.NetworkConnectionCallback callback) {
            synchronized (callbacks) {
                if (callback != null) callbacks.remove(callback);
            }
        }

        @Override public CallbackResult onNetworkConnectionEvent(NetworkConnection.Event event) {
            for (NetworkConnection.NetworkConnectionCallback callback : callbacks) {
                CallbackResult result = callback.onNetworkConnectionEvent(event);
                if (result.stopDispatch()) {
                    return CallbackResult.HANDLED;
                }
            }
            return CallbackResult.NOT_HANDLED;
        }
    }

    public void pushReceiveLoopCallback(@Nullable RecvLoopRunnable.RecvLoopCallback callback) {
        synchronized (callbackLock) {
            this.theRecvLoopCallback.push(callback);
        }
    }

    public void removeReceiveLoopCallback(@Nullable RecvLoopRunnable.RecvLoopCallback callback) {
        synchronized (callbackLock) {
            this.theRecvLoopCallback.remove(callback);
        }
    }

    protected class RecvLoopCallbackChainer implements RecvLoopRunnable.RecvLoopCallback {

        protected final CopyOnWriteArrayList<RecvLoopRunnable.RecvLoopCallback> callbacks = new CopyOnWriteArrayList<RecvLoopRunnable.RecvLoopCallback>();

        void push(@Nullable RecvLoopRunnable.RecvLoopCallback callback) {
            synchronized (callbacks) {  // for uniqueness testing
                remove(callback);
                if (callback != null && !callbacks.contains(callback)) {
                    callbacks.add(0, callback);
                }
            }
        }

        void remove(@Nullable RecvLoopRunnable.RecvLoopCallback callback) {
            synchronized (callbacks) {
                if (callback != null) callbacks.remove(callback);
            }
        }

        @Override public CallbackResult packetReceived(RobocolDatagram packet) throws RobotCoreException {
            for (RecvLoopRunnable.RecvLoopCallback callback : callbacks) {
                CallbackResult result = callback.packetReceived(packet);
                if (result.stopDispatch()) {
                    return CallbackResult.HANDLED;
                }
            }
            return CallbackResult.NOT_HANDLED;
        }

        @Override public CallbackResult peerDiscoveryEvent(RobocolDatagram packet) throws RobotCoreException {
            for (RecvLoopRunnable.RecvLoopCallback callback : callbacks) {
                CallbackResult result = callback.peerDiscoveryEvent(packet);
                if (result.stopDispatch()) {
                    return CallbackResult.HANDLED;
                }
            }
            return CallbackResult.NOT_HANDLED;
        }

        @Override public CallbackResult heartbeatEvent(RobocolDatagram packet, long tReceived) throws RobotCoreException {
            for (RecvLoopRunnable.RecvLoopCallback callback : callbacks) {
                CallbackResult result = callback.heartbeatEvent(packet, tReceived);
                if (result.stopDispatch()) {
                    return CallbackResult.HANDLED;
                }
            }
            return CallbackResult.NOT_HANDLED;
        }

        @Override public CallbackResult commandEvent(Command command) throws RobotCoreException {
            boolean handled = false;
            for (RecvLoopRunnable.RecvLoopCallback callback : callbacks) {
                CallbackResult result = callback.commandEvent(command);
                handled = handled || result.isHandled();
                if (result.stopDispatch()) {
                    return CallbackResult.HANDLED;
                    }
                }

            if (!handled) {
                // Make an informative trace message as to who was around that all refused to process the command
                StringBuilder callbackNames = new StringBuilder();
                for (RecvLoopRunnable.RecvLoopCallback callback : callbacks) {
                    if (callbackNames.length() > 0) callbackNames.append(",");
                    callbackNames.append(callback.getClass().getSimpleName());
                }
                RobotLog.vv(RobocolDatagram.TAG, "unable to process command %s callbacks=%s", command.getName(), callbackNames.toString());
            }
            return handled ? CallbackResult.HANDLED : CallbackResult.NOT_HANDLED;
        }

        @Override public CallbackResult telemetryEvent(RobocolDatagram packet) throws RobotCoreException {
            for (RecvLoopRunnable.RecvLoopCallback callback : callbacks) {
                CallbackResult result = callback.telemetryEvent(packet);
                if (result.stopDispatch()) {
                    return CallbackResult.HANDLED;
                }
            }
            return CallbackResult.NOT_HANDLED;
        }

        @Override public CallbackResult gamepadEvent(RobocolDatagram packet) throws RobotCoreException {
            for (RecvLoopRunnable.RecvLoopCallback callback : callbacks) {
                CallbackResult result = callback.gamepadEvent(packet);
                if (result.stopDispatch()) {
                    return CallbackResult.HANDLED;
                }
            }
            return CallbackResult.NOT_HANDLED;
        }

        @Override public CallbackResult emptyEvent(RobocolDatagram packet) throws RobotCoreException {
            for (RecvLoopRunnable.RecvLoopCallback callback : callbacks) {
                CallbackResult result = callback.emptyEvent(packet);
                if (result.stopDispatch()) {
                    return CallbackResult.HANDLED;
                }
            }
            return CallbackResult.NOT_HANDLED;
        }

        @Override public CallbackResult reportGlobalError(String error, boolean recoverable) {
            for (RecvLoopRunnable.RecvLoopCallback callback : callbacks) {
                CallbackResult result = callback.reportGlobalError(error,  recoverable);
                if (result.stopDispatch()) {
                    return CallbackResult.HANDLED;
                }
            }
            return CallbackResult.NOT_HANDLED;
        }
    }
}
