package com.ctre.phoenix.MotorControl.CAN;

import com.ctre.phoenix.MotorControl.IMotorController;

public class VictorSPX extends com.ctre.phoenix.MotorControl.CAN.BaseMotorController
    implements IMotorController {

  public VictorSPX(int deviceNumber) {
    super(deviceNumber | 0x01040000);
  }
}
