/*
 * Copyright (c) 2017 DEKA Research and Development
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
 * Neither the name of DEKA Research and Development nor the names of contributors may be used to
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

package org.firstinspires.ftc.robotcore.internal.webserver;

import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.wifi.NetworkConnection;
import com.qualcomm.robotcore.wifi.NetworkConnectionFactory;
import com.qualcomm.robotcore.wifi.NetworkType;
import com.qualcomm.robotcore.wifi.WifiDirectAssistant;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

/**
 * WebServer.
 *
 * Most of this WebServer is derived from the ProgrammingModeServer.java file.
 * @author shager
 */
@SuppressWarnings("WeakerAccess")
public class WebServer
{
    public static final String TAG = WebServer.class.getSimpleName();

    private static final int DEFAULT_PORT = 8080;

    private final NetworkType networkType;
    private final NanoHTTPD nanoHttpd;
    private final WebHandlerManager webHandlerManager;
    private final Object startStopLock; // might be redundant w/ lock, but we're not sure, so we keep separate for now
    private final Object lock;

    // mutable state variables, all accesses are guarded by lock
    private long timeServerStartedMillis;
    private NetworkConnection networkConnection;
    private String networkName;
    private String passphrase;
    private InetAddress connectionOwnerAddress;
    private boolean serverIsAlive;
    private int port;

    public WebServer()
    {
        this(DEFAULT_PORT);
    }

    public WebServer(int port)
    {
        this.port = port;
        this.networkType = NetworkType.WIFIDIRECT;
        this.nanoHttpd = createNanoHttpd(port);
        this.webHandlerManager = new WebHandlerManager(this);
        this.lock = new Object();
        this.startStopLock = new Object();
    }

    public WebHandlerManager getWebHandlerManager()
    {
        return webHandlerManager;
    }

    // convenience for error logging
    private static void logError(String message)
    {
        RobotLog.ee(TAG, message);
    }

    /**
     * Check if the WebServer has been started.
     *
     * @return true if the server was started and false otherwise.
     */
    public boolean wasStarted()
    {
        synchronized (startStopLock) {
            return nanoHttpd.wasStarted();
        }
    }

    /**
     * start the WebServer.
     */
    public void start()
    {
        synchronized (startStopLock)
        {
            try {
                if (wasStarted())
                {
                    return;
                }
                RobotLog.vv(TAG, "starting port=%d", port);
                nanoHttpd.start();
                synchronized (lock)
                {
                    timeServerStartedMillis = System.currentTimeMillis();

                    // By this time, the NetworkConnection should already have been created. Therefore it should
                    // be OK to pass null for the context.
                    networkConnection = NetworkConnectionFactory.getNetworkConnection(networkType, null);
                    if (networkConnection == null)
                    {
                        logError("Cannot start Network Connection of type: " + networkType);
                    }
                    networkConnection.enable();

                    if (networkConnection instanceof WifiDirectAssistant)
                    {
                        networkName = ((WifiDirectAssistant) networkConnection).getGroupNetworkName();
                    }
                    else
                    {
                        networkName = networkConnection.getConnectionOwnerName();
                    }
                    passphrase = networkConnection.getPassphrase();
                    connectionOwnerAddress = networkConnection.getConnectionOwnerAddress();

                    serverIsAlive = nanoHttpd.isAlive();
                }
                RobotLog.vv(TAG, "started port=%d", port);

            } catch (IOException e) {
                logError(e.getMessage());
            }
        }
    }

    /**
     * stop the WebServer.
     */
    public void stop()
    {
        synchronized (startStopLock)
        {
            RobotLog.vv(TAG, "stopping port=%d", port);
            nanoHttpd.stop();

            synchronized (lock)
            {
                if (networkConnection != null)
                {
                    networkConnection.disable();
                    networkConnection = null;
                }
            }
            RobotLog.vv(TAG, "stopped port=%d", port);
        }
    }

    /**
     * Get {@link RobotControllerWebInfo}
     *
     * @return an instance of {@link RobotControllerWebInfo}
     */
    public RobotControllerWebInfo getConnectionInformation()
    {
        synchronized (lock)
        {
            String serverUrl = "(unavailable)";
            if (connectionOwnerAddress != null)
            {
                final int port = nanoHttpd.getListeningPort();
                if (port != -1)
                {
                    serverUrl = "http://" + connectionOwnerAddress.getHostAddress() + ":" + port;
                }
            }

            return new RobotControllerWebInfo(
                    networkName, passphrase, serverUrl,
                    serverIsAlive, timeServerStartedMillis);
        }
    }

    /**
     * create the underlying nanoHttpd server.
     *
     * @return the newly instantiated nanoHttpd server.
     */
    private NanoHTTPD createNanoHttpd(int port)
    {
		RobotLog.vv(TAG, "creating NanoHTTPD(%d)", port);
        return new NanoHTTPD(port)
        {
            /**
             * Forward all sessions to the single RequestHandlerService to process a Response.
             *
             * @param session The HTTP session
             * @return a Response for the current session
             */
            @Override
            public Response serve(IHTTPSession session)
            {
                Method method = session.getMethod();
                if (Method.GET==method || Method.PUT==method || Method.POST==method) {
                    return webHandlerManager.serve(session);
                } else {
                    return newFixedLengthResponse(Response.Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT, "");
                }
            }
        };
    }

    /** a debugging utility */
    public static void logSession(NanoHTTPD.IHTTPSession session)
    {
        String prefix = "\n   ";
        StringBuilder builder = new StringBuilder();
        builder.append(prefix).append(String.format("uri='%s'", session.getUri()));
        builder.append(prefix).append(String.format("method='%s'", session.getMethod()));
        if (session.getQueryParameterString() != null && session.getQueryParameterString().length() > 0) {
            builder.append(prefix).append(String.format("query='%s'", session.getQueryParameterString()));
        }
        for (Map.Entry<String, List<String>> param : session.getParameters().entrySet()) {
            builder.append(prefix).append(String.format("param('%s')=[",param.getKey()));
            boolean first = true;
            for (String value : param.getValue()) {
                if (!first) builder.append(", ");
                builder.append(String.format("'%s'", value));
                first = false;
            }
            builder.append("]");
        }
        for (Map.Entry<String,String> header : session.getHeaders().entrySet()) {
            builder.append(prefix).append(String.format("header %s=%s", header.getKey(), header.getValue()));
        }
        RobotLog.dd(TAG, "session(0x%08x)=%s", session.hashCode(), builder.toString());
    }
}
