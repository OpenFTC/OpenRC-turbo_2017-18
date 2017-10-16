/* Copyright (c) 2014, 2015 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.robotcore.util;

import android.content.Context;

import java.io.Serializable;
import java.util.UUID;

import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;

/**
 * Instances of {@link SerialNumber} represent serial numbers of devices on the USB bus.
 * 'Fake' serial numbers are serial numbers that will *never* appear for a real device; they
 * are useful, for example, as the serial number of a {@link ControllerConfiguration} that
 * has not yet been associated with a actual USB controller device.
 *
 * Note that *all* serial numbers loaded in memory at any given instant are guaranteed unique and
 * different, even the fake ones; this allows code that processes USB-device-bound {@link
 * ControllerConfiguration}s to operate easily on unbound ones as well, a significant coding
 * simplification. The technology used in fake serial numbers, {@link UUID}s, in fact guarantees
 * uniqueness across space and time, so fake serial numbers can be recorded persistently and
 * still maintain uniqueness. Historically, non-unique 'fake' serial numbers were also used: these
 * appeared int the form of "-1" or "N/A". When loaded from persistent storage, such legacy
 * fake serial numbers are converted to unique ones to maintain the uniqueness guarantee.
 */
public class SerialNumber implements Serializable {

  private final String serialNumber;
  private static final String fakePrefix = "FakeUSB:";

  /**
   * Constructs a new unique, fake serial number
   */
  public SerialNumber() {
    serialNumber = generateFake();
  }

  private static String generateFake() {
    return fakePrefix + UUID.randomUUID().toString();
  }

  /**
   * Constructs a serial number using the supplied initialization string. If the initialization
   * string is a legacy form of fake serial number, a unique fake serial number is created.
   *
   * @param initializer the initialization string for the serial number.
   */
  public SerialNumber(String initializer) {
    this.serialNumber = isLegacyFake(initializer) ? generateFake() : initializer;
  }

  /**
   * Returns whether or not this serial number is a fake one or not
   * @return whether or not this serial number is a fake one or not
   */
  public boolean isFake() {
    return serialNumber.startsWith(fakePrefix) || isLegacyFake(serialNumber);
  }

  /**
   * Returns whether or not this serial number is a real one or not
   * @return whether or not this serial number is a real one or not
   */
  public boolean isReal() {
    return !isFake();
  }

  /**
   * Returns whether the indicated serial number initialization string is one of the legacy
   * fake serial number forms or not.
   * @param initializer the serial number initialization string to test
   * @return whether the the serial number initialization string is a legacy fake form of serial number
   */
  public static boolean isLegacyFake(String initializer) {
    return initializer==null || initializer.equals("-1") || initializer.equalsIgnoreCase("N/A") || initializer.trim().isEmpty();
  }

  @Override
  public boolean equals(Object object) {
    if (object == null) return false;
    if (object == this) return true;

    if (object instanceof SerialNumber) {
      return serialNumber.equals(((SerialNumber) object).serialNumber);
    }

    if (object instanceof String) {
      return serialNumber.equals(object);
    }

    return false;
  }

  @Override
  public int hashCode() {
    return serialNumber.hashCode();
  }

  @Override
  public String toString() {
    return serialNumber;
  }

  public String toString(Context context) {
    return isFake() ? context.getString(R.string.noSerialNumber) : serialNumber;
  }

  /** @deprecated no need to use; use toString() if string form is sought */
  @Deprecated
  public String getSerialNumber() {
    return serialNumber;
  }
}
