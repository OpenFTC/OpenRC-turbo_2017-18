package com.qualcomm.robotcore.hardware.configuration;

import com.qualcomm.robotcore.util.SerialNumber;

import java.util.List;

public class MatrixControllerConfiguration extends ControllerConfiguration<DeviceConfiguration> {

  private List<ServoConfiguration> servos;
  private List<MotorConfiguration> motors;

  public MatrixControllerConfiguration(String name, List<MotorConfiguration> motors, List<ServoConfiguration> servos, SerialNumber serialNumber) {
    super(name, serialNumber, BuiltInConfigurationType.MATRIX_CONTROLLER);
    this.servos = servos;
    this.motors = motors;
  }

  public List<ServoConfiguration> getServos() {
    return servos;
  }

  public void setServos(List<ServoConfiguration> servos) {
    this.servos = servos;
  }

  public List<MotorConfiguration> getMotors() {
    return motors;
  }

  public void setMotors(List<MotorConfiguration> motors){
    this.motors = motors;
  }
}
