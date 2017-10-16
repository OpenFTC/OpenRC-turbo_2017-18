/*
 * Copyright (c) 2015 Qualcomm Technologies Inc
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
 * Neither the name of Qualcomm Technologies Inc nor the names of its contributors may be used to
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

package com.qualcomm.robotcore.util;

import android.os.Build;

/**
 * A few constants that help us detect which device we are running on
 */
@SuppressWarnings("WeakerAccess")
public class Device {

  public final static String MANUFACTURER_ZTE = "zte";
  public final static String MODEL_ZTE_SPEED = "N9130";

  /**
   * Answers whether this is a ZTE Speed phone. For the Speed's, the model number
   * is sufficiently distinctive and universal that we can reasonably key off of that.
   * Generally, however, this is not true, as model numbers are by definition primarlily
   * for human consumption, not programmatic use
   */
  public static boolean isZteSpeed() {
    return Build.MANUFACTURER.equalsIgnoreCase(MANUFACTURER_ZTE) && Build.MODEL.equalsIgnoreCase(MODEL_ZTE_SPEED);
    }

  /**
   * When running on the ZTE, should we use the ZTE-provided channel-changing utility app,
   * or should we just go with our own UI. Testing shows that we don't have any method of
   * channel changing on the ZTEs that will work other than using the ZTE app. Unfortunate.
   */
  public static boolean useZteProvidedWifiChannelEditorOnZteSpeeds() {
    return true;
  }

  /**
   * Is it possible to remote channel change from a driver station to this device?
   */
  public static boolean wifiP2pRemoteChannelChangeWorks() {
    return !(isZteSpeed() && useZteProvidedWifiChannelEditorOnZteSpeeds());
  }
}
