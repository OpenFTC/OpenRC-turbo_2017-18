package org.firstinspires.ftc.robotcore.internal.network;

import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.robocol.Heartbeat;
import com.qualcomm.robotcore.robocol.RobocolDatagram;
import com.qualcomm.robotcore.robocol.RobocolDatagramSocket;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.ui.RobotCoreGamepadManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("WeakerAccess")
public class SendOnceRunnable implements Runnable {

    //----------------------------------------------------------------------------------------------
    // Types
    //----------------------------------------------------------------------------------------------

    public interface ClientCallback {

        /**
         * Notifies that the peer is currently connected. Note: this is a level-state
         * notification, not a transition notification from disconnected to connected,
         * though the peerLikelyChanged parameter provides an indication of the latter.
         * @param peerLikelyChanged if false, then the peer is the same as the last notification.
         */
        void peerConnected(boolean peerLikelyChanged);

        /**
         * Notifies that the peer is currently disconnected. Note: this is a level-state
         * notification, not a transition notification from connected to disconnected.
         */
        void peerDisconnected();
    }

    public static class Parameters {
        public boolean                 disconnectOnTimeout = true;
        public boolean                 originateHeartbeats = AppUtil.getInstance().isDriverStation();
        public RobotCoreGamepadManager gamepadManager      = null;

        public Parameters() { }
        public Parameters(RobotCoreGamepadManager gamepadManager) {
            this.gamepadManager = gamepadManager;
        }
    }

    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    public static final String          TAG = RobocolDatagram.TAG;
    public static       boolean         DEBUG = false;

    public static final double          ASSUME_DISCONNECT_TIMER = 2.0; // in seconds
    public static final int             MAX_COMMAND_ATTEMPTS = 10;
    public static final long            GAMEPAD_UPDATE_THRESHOLD = 1000; // in milliseconds
    public static final int             MS_HEARTBEAT_TRANSMISSION_INTERVAL = 100;

    protected ElapsedTime               lastRecvPacket;
    protected List<Command>             pendingCommands = new CopyOnWriteArrayList<Command>();
    protected Heartbeat                 heartbeatSend = new Heartbeat();
    protected RobocolDatagramSocket     socket;
    protected ClientCallback            clientCallback;
    protected Context                   context;
    protected final Parameters          parameters;
    protected final Object              issuedDisconnectLogMessageLock = new Object();
    protected boolean                   issuedDisconnectLogMessage;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public SendOnceRunnable(@NonNull  Context context,
                            @Nullable ClientCallback clientCallback,
                            @NonNull  RobocolDatagramSocket socket,
                            @Nullable ElapsedTime lastRecvPacket,
                            @NonNull  Parameters parameters) {
        this.context            = context;
        this.clientCallback     = clientCallback;
        this.socket             = socket;
        this.lastRecvPacket     = lastRecvPacket;
        this.parameters         = parameters;
        this.issuedDisconnectLogMessage = false;

        RobotLog.vv(TAG, "SendOnceRunnable created");
    }

    //----------------------------------------------------------------------------------------------
    // Operations
    //----------------------------------------------------------------------------------------------

    public void onPeerConnected(boolean peerLikelyChanged) {
        synchronized (issuedDisconnectLogMessageLock) {
            if (this.issuedDisconnectLogMessage) {
                RobotLog.vv(TAG, "resetting peerDisconnected()");
                this.issuedDisconnectLogMessage = false;
            }
        }
    }

    @Override
    public void run() {
        try {
            // skip if we haven't received a packet in a while. The RC is the center
            // of the world and never disconnects from anyone.
            double seconds = lastRecvPacket.seconds();
            if (parameters.disconnectOnTimeout && lastRecvPacket != null && seconds > ASSUME_DISCONNECT_TIMER) {
                if (clientCallback != null ) {
                    synchronized (issuedDisconnectLogMessageLock) {
                        if (!issuedDisconnectLogMessage) {
                            issuedDisconnectLogMessage = true;
                            RobotLog.vv(TAG, "issuing peerDisconnected(): lastRecvPacket=%.3f s", seconds);
                        }
                    }
                    clientCallback.peerDisconnected();
                }
                return;
            }

            // send heartbeat if we're on the driver station, as heartbeats are
            // originated by the DS and merely echoed by the RC.
            if (parameters.originateHeartbeats && heartbeatSend.getElapsedSeconds() > 0.001 * MS_HEARTBEAT_TRANSMISSION_INTERVAL) {
                // generate a new heartbeat packet and send it
                heartbeatSend = Heartbeat.createWithTimeStamp();
                // keep the next three lines as close together in time as possible
                heartbeatSend.t0 = Heartbeat.getMsTimeSyncTime();
                RobocolDatagram packetHeartbeat = new RobocolDatagram(heartbeatSend);
                send(packetHeartbeat);
                // Do any logging after the transmission so as to minimize disruption of timing calculation
            }

            // send gamepads if we have the info to do so (which will only be on the DS)
            if (parameters.gamepadManager != null) {
                long now = SystemClock.uptimeMillis();

                for (Gamepad gamepad : parameters.gamepadManager.getGamepadsForTransmission()) {

                    // don't send stale gamepads
                    if (now - gamepad.timestamp > GAMEPAD_UPDATE_THRESHOLD && gamepad.atRest())
                        continue;

                    gamepad.setSequenceNumber();
                    RobocolDatagram packetGamepad = new RobocolDatagram(gamepad);
                    send(packetGamepad);
                }
            }

            long nanotimeNow = System.nanoTime();

            // send commands
            List<Command> commandsToRemove = new ArrayList<Command>();
            for (Command command : pendingCommands) {

                // if this command has exceeded max attempts, give up
                if (command.getAttempts() > MAX_COMMAND_ATTEMPTS) {
                    String msg = String.format(context.getString(R.string.configGivingUpOnCommand), command.getName(), command.getSequenceNumber(), command.getAttempts());
                    RobotLog.vv(TAG, msg);
                    commandsToRemove.add(command);
                    continue;
                }

                // Commands that we originate we only send out every once in a while so as to give ack's a chance to get back to us
                if (command.isAcknowledged() || command.shouldTransmit(nanotimeNow)) {
                    // log commands we initiated, ack the ones we didn't
                    if (!command.isAcknowledged()) {
                        RobotLog.vv(TAG, "sending %s(%d), attempt: %d", command.getName(), command.getSequenceNumber(), command.getAttempts());
                    } else if (DEBUG) {
                        RobotLog.vv(TAG, "acking %s(%d)", command.getName(), command.getSequenceNumber());
                    }

                    // send the command
                    RobocolDatagram packetCommand = new RobocolDatagram(command);
                    send(packetCommand);

                    // if this is a command we handled, remove it
                    if (command.isAcknowledged()) commandsToRemove.add(command);
                }
            }
            pendingCommands.removeAll(commandsToRemove);
        }
        // For robustness and attempted ongoing liveness of the app, we catch
        // *all* types of exception. This will help minimize disruption to the sendLoopService.
        // With (a huge amount of) luck, the next time we're run, things might work better. Though
        // that's unlikely, it seems better than outright killing the app here and now.
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void send(RobocolDatagram datagram) {
        if (socket.getInetAddress() != null) {
            socket.send(datagram);
        }
    }

    public void sendCommand(Command cmd) {
        pendingCommands.add(cmd);
    }

    public boolean removeCommand(Command cmd) {
        return pendingCommands.remove(cmd);
    }

    public void clearCommands() { pendingCommands.clear();}
}
