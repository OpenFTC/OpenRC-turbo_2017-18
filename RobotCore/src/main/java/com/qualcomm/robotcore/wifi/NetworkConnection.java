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
 * Neither the name of Molly Nicholas nor the names of her contributors may be used to
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

package com.qualcomm.robotcore.wifi;

import android.support.annotation.NonNull;

import org.firstinspires.ftc.robotcore.internal.network.CallbackResult;

import java.net.InetAddress;

public abstract class NetworkConnection {

  public enum Event {
    DISCOVERING_PEERS,
    PEERS_AVAILABLE,
    GROUP_CREATED,
    CONNECTING,
    CONNECTED_AS_PEER,
    CONNECTED_AS_GROUP_OWNER,
    DISCONNECTED,
    CONNECTION_INFO_AVAILABLE,
    AP_CREATED,
    ERROR,
    UNKNOWN
  }

  public enum ConnectStatus {
    NOT_CONNECTED,
    CONNECTING,
    CONNECTED,
    GROUP_OWNER,
    ERROR
  }

  public interface NetworkConnectionCallback {
    CallbackResult onNetworkConnectionEvent(NetworkConnection.Event event);
  }

  public abstract NetworkType getNetworkType();

  public abstract void enable();
  public abstract void disable();

  public abstract void setCallback(@NonNull NetworkConnectionCallback callback);

  public abstract void discoverPotentialConnections();

  public abstract void cancelPotentialConnections();

  public abstract void createConnection();

  public abstract void connect(String deviceAddress);

  public abstract void connect(String connectionName, String connectionPassword);

  public abstract InetAddress getConnectionOwnerAddress();

  public abstract String getConnectionOwnerName();

  public abstract String getConnectionOwnerMacAddress();

  public abstract boolean isConnected();

  public abstract String getDeviceName();

  public abstract String getInfo();

  public abstract String getFailureReason();

  public abstract String getPassphrase();

  public abstract WifiDirectAssistant.ConnectStatus getConnectStatus();

  /**
   * Return true if the device name is valid. A valid device name is greater
   * than 0 characters and contains only alphanumeric or punctuation characters
   * only.
   * @return true if device name is valid
   */
  public static boolean isDeviceNameValid(String deviceName) {
    return deviceName.matches("^\\p{Print}+$");
  }
}
