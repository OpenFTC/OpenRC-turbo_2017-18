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

package com.qualcomm.robotcore.hardware.configuration;

import android.support.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class MotorConfiguration extends DeviceConfiguration {

  public MotorConfiguration(int port, String name, boolean enabled) {
    super(port, MotorConfigurationType.getUnspecifiedMotorType(), name, enabled);
  }

  public MotorConfiguration(int port) {
    this(port, DISABLED_DEVICE_NAME, false);
  }

  public @NonNull MotorConfigurationType getMotorType() {
    if (getConfigurationType() instanceof MotorConfigurationType) {
      return (MotorConfigurationType) getConfigurationType();
    } else {
      return MotorConfigurationType.getUnspecifiedMotorType();
    }
  }

  @Override public ConfigurationType getSpinnerChoiceType() {
    if (getMotorType() != null) {
      return getMotorType();
    }
    return super.getSpinnerChoiceType();
  }

  /** returns the list of the extant motor configuration types */
  public static ConfigurationType[] getAllMotorConfigurationTypes() {
    List<ConfigurationType> list = new LinkedList<ConfigurationType>();
    list.add(BuiltInConfigurationType.NOTHING);
    list.addAll(UserConfigurationTypeManager.getInstance().allUserTypes(UserConfigurationType.Flavor.MOTOR));
    return list.toArray(new ConfigurationType[list.size()]);
  }
}