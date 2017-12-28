package com.snobot.simulator.simulator_components.ctre;

import org.junit.Test;

import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.MotorControl.ControlFrame;
import com.ctre.phoenix.MotorControl.ControlMode;
import com.ctre.phoenix.MotorControl.Faults;
import com.ctre.phoenix.MotorControl.FeedbackDevice;
import com.ctre.phoenix.MotorControl.LimitSwitchNormal;
import com.ctre.phoenix.MotorControl.LimitSwitchSource;
import com.ctre.phoenix.MotorControl.NeutralMode;
import com.ctre.phoenix.MotorControl.RemoteFeedbackDevice;
import com.ctre.phoenix.MotorControl.RemoteLimitSwitchSource;
import com.ctre.phoenix.MotorControl.StatusFrame;
import com.ctre.phoenix.MotorControl.StatusFrameEnhanced;
import com.ctre.phoenix.MotorControl.StickyFaults;
import com.ctre.phoenix.MotorControl.VelocityMeasPeriod;
import com.ctre.phoenix.MotorControl.CAN.TalonSRX;
import com.snobot.test.utilities.BaseSimulatorTest;

public class TestUnsupportedTalonOperations extends BaseSimulatorTest
{
    @Test
    public void testAllFunctions()
    {
        TalonSRX motorToFollow = new TalonSRX(0);
        TalonSRX motorToTest = new TalonSRX(11);

        motorToTest.getDeviceID();
        for(ControlMode mode : ControlMode.values())
        {
            motorToTest.set(mode, 0);
            motorToTest.set(mode, 0, 0);
        }
        motorToTest.neutralOutput();
        motorToTest.setNeutralMode(NeutralMode.Brake);
        motorToTest.setNeutralMode(NeutralMode.Coast);
        motorToTest.setNeutralMode(NeutralMode.EEPROMSetting);
        motorToTest.setSensorPhase(false);
        motorToTest.setInverted(false);
        motorToTest.getInverted();
        motorToTest.configOpenloopRamp(0, 0);
        motorToTest.configClosedloopRamp(0, 0);
        motorToTest.configPeakOutputForward(0, 0);
        motorToTest.configPeakOutputReverse(0, 0);
        motorToTest.configNominalOutputForward(0, 0);
        motorToTest.configNominalOutputReverse(0, 0);
        motorToTest.configNeutralDeadband(0, 0);
        motorToTest.configVoltageCompSaturation(0, 0);
        motorToTest.configVoltageMeasurementFilter(0, 0);
        motorToTest.enableVoltageCompensation(false);
        motorToTest.getBusVoltage();
        motorToTest.getMotorOutputPercent();
        motorToTest.getMotorOutputVoltage();
        motorToTest.getOutputCurrent();
        motorToTest.getTemperature();
        motorToTest.configSelectedFeedbackSensor(new RemoteFeedbackDevice(0, FeedbackDevice.Analog), 0);
        motorToTest.configSelectedFeedbackSensor(FeedbackDevice.Analog, 0);
        motorToTest.getSelectedSensorPosition();
        motorToTest.getSelectedSensorVelocity();
        motorToTest.getSelectedSensorVelocity(0);
        motorToTest.setSelectedSensorPosition(0, 0);
        motorToTest.setControlFramePeriod(ControlFrame.Control_2_Enable_50m, 0);
        motorToTest.setStatusFramePeriod(StatusFrame.Status_12_RobotPose_, 0, 0);
        motorToTest.getStatusFramePeriod(StatusFrame.Status_10_MotionMagic_, 0);
        motorToTest.getStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 0);
        motorToTest.configVelocityMeasurementPeriod(VelocityMeasPeriod.Period_100Ms, 0);
        motorToTest.configVelocityMeasurementWindow(0, 0);
        motorToTest.configForwardLimitSwitchSource(RemoteLimitSwitchSource.Disabled__, LimitSwitchNormal.NormallyClosed, 0, 0);
        motorToTest.configReverseLimitSwitchSource(RemoteLimitSwitchSource.Disabled__, LimitSwitchNormal.NormallyClosed, 0, 0);
        motorToTest.configForwardLimitSwitchSource(LimitSwitchSource.Disabled_, LimitSwitchNormal.NormallyClosed, 0);
        motorToTest.enableLimitSwitches(false);
        motorToTest.configForwardSoftLimit(0, 0);
        motorToTest.configReverseSoftLimit(0, 0);
        motorToTest.enableSoftLimits(false);
        motorToTest.config_kP(0, 0, 0);
        motorToTest.config_kI(0, 0, 0);
        motorToTest.config_kD(0, 0, 0);
        motorToTest.config_kF(0, 0, 0);
        motorToTest.config_IntegralZone(0, 0, 0);
        motorToTest.configAllowableClosedloopError(0, 0, 0);
        motorToTest.configMaxIntegralAccumulator(0, 0, 0);
        motorToTest.setIntegralAccumulator(0, 0);
        motorToTest.getClosedLoopError();
        motorToTest.getIntegralAccumulator();
        motorToTest.getErrorDerivative();
        motorToTest.getErrorDerivative(0);
        motorToTest.selectProfileSlot(0);
        motorToTest.configMotionCruiseVelocity(0, 0);
        motorToTest.configMotionAcceleration(0, 0);
        motorToTest.getLastError();
        motorToTest.getFaults(new Faults());
        motorToTest.getStickyFaults(new StickyFaults());
        motorToTest.clearStickyFaults(0);
        motorToTest.getFirmwareVersion();
        motorToTest.hasResetOccurred();
        motorToTest.configSetCustomParam(0, 0, 0);
        motorToTest.configGetCustomParam(0, 0);
        motorToTest.configSetParameter(ParamEnum.eAnalogPosition, 0, 0, 0, 0);
        motorToTest.configGetParameter(ParamEnum.eAnalogPosition, 0, 0);
        motorToTest.getBaseID();
        motorToTest.follow(motorToFollow);
        motorToTest.valueUpdated();
        motorToTest.getWPILIB_SpeedController();
    }
}
