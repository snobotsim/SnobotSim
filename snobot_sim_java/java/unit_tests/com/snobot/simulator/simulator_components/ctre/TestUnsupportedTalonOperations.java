package com.snobot.simulator.simulator_components.ctre;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TrajectoryPoint;
import com.snobot.simulator.motor_sim.DcMotorModelConfig;
import com.snobot.simulator.motor_sim.StaticLoadMotorSimulationConfig;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorTest;

public class TestUnsupportedTalonOperations extends BaseSimulatorTest
{

    @Test
    public void testUnsupportedSend()
    {
        TalonSrxDeviceManager deviceManager = new TalonSrxDeviceManager();

        deviceManager.handleSend(0xFFFFFFFF, 6, ByteBuffer.allocate(8), 8);
    }

    @Test
    public void testUnsupportedReceive()
    {
        TalonSrxDeviceManager deviceManager = new TalonSrxDeviceManager();

        Assert.assertEquals(0, deviceManager.handleReceive(0xFFFFFFFF, 6, ByteBuffer.allocate(8)));
    }

    @Test
    public void testUnsupportedTxRequest1()
    {
        TalonSrxDeviceManager deviceManager = new TalonSrxDeviceManager();

        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(0xFFFFFFFFFFFFFFFFL);
        deviceManager.handleSend(0x0204000F, 6, buffer, 8);
    }

    @Test
    public void testUnsupportedDemandCommand()
    {
        TalonSrxDeviceManager deviceManager = new TalonSrxDeviceManager();

        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(0x000000000022EE00L);
        deviceManager.handleSend(0x0204000F, 6, buffer, 8);
    }

    @Test
    public void testUnsupportedSetParam()
    {
        TalonSrxDeviceManager deviceManager = new TalonSrxDeviceManager();

        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(0xFFFFFFFFFFFFFFFFL);
        deviceManager.handleSend(0x02041880, 6, buffer, 8);
    }

    @Test
    public void testUnsupportedParamRequest()
    {
        TalonSrxDeviceManager deviceManager = new TalonSrxDeviceManager();

        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(0xFFFFFFFFFFFFFFFFL);
        deviceManager.handleSend(0x02041800, 6, buffer, 8);
    }

    @Test
    public void testSetEncoderPosition()
    {
        int handle = 10;
        int mRawHandle = handle + 100;
        CANTalon talon = new CANTalon(handle);

        DcMotorModelConfig motorConfig = DataAccessorFactory.getInstance().getSimulatorDataAccessor().createMotor("CIM", 1, 10, 1);
        Assert.assertTrue(DataAccessorFactory.getInstance().getSimulatorDataAccessor().setSpeedControllerModel_Static(mRawHandle, motorConfig,
                new StaticLoadMotorSimulationConfig(0.01)));

        System.out.println(talon.getEncPosition());
        Assert.assertEquals(0, talon.getEncPosition(), .05);
        Assert.assertEquals(0, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPosition(mRawHandle), .05);

        talon.setEncPosition(50);
        Assert.assertEquals(50, talon.getEncPosition(), .05);
        Assert.assertEquals(50, DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPosition(mRawHandle), .05);

    }

    @Test
    public void testAllFunctions()
    {
        CANTalon climber = new CANTalon(11);
        climber.setInverted(true);
        climber.enableLimitSwitch(true, true);

        climber.changeControlMode(CANTalon.TalonControlMode.Voltage);
        climber.clearIAccum();
        climber.clearMotionProfileHasUnderrun();
        climber.clearMotionProfileTrajectories();
        climber.clearStickyFaults();
        climber.configEncoderCodesPerRev(100);
        climber.ConfigFwdLimitSwitchNormallyOpen(true);
        climber.ConfigFwdLimitSwitchNormallyOpen(false);
        climber.configMaxOutputVoltage(10);
        climber.configNominalOutputVoltage(-5, 5);
        climber.configPeakOutputVoltage(1, -4);
        climber.configPotentiometerTurns(4);
        climber.ConfigRevLimitSwitchNormallyOpen(true);
        climber.ConfigRevLimitSwitchNormallyOpen(false);
        climber.disableControl();
        climber.enable();
        climber.enableBrakeMode(true);
        climber.enableBrakeMode(false);
        climber.enableControl();
        climber.enableForwardSoftLimit(true);
        climber.enableForwardSoftLimit(false);
        climber.enableZeroSensorPositionOnIndex(true, true);
        climber.enableZeroSensorPositionOnIndex(true, false);
        climber.enableZeroSensorPositionOnIndex(false, true);
        climber.enableZeroSensorPositionOnIndex(false, false);
        climber.get();
        climber.getAnalogInPosition();
        climber.getAnalogInRaw();
        climber.getAnalogInVelocity();
        climber.getBrakeEnableDuringNeutral();
        climber.getBusVoltage();
        climber.getClosedLoopError();
        climber.getCloseLoopRampRate();
        climber.getControlMode();
        climber.getD();
        climber.getDescription();
        climber.getDeviceID();
        climber.getEncPosition();
        climber.getEncVelocity();
        climber.getError();
        climber.getExpiration();
        climber.getF();
        climber.getFaultForLim();
        climber.getFaultForSoftLim();
        climber.getFaultHardwareFailure();
        climber.getFaultOverTemp();
        climber.getFaultRevLim();
        climber.getFaultRevSoftLim();
        climber.getFaultUnderVoltage();
        climber.getForwardSoftLimit();
        climber.getI();
        climber.GetIaccum();
        climber.getInverted();
        climber.getIZone();
        // climber.getMotionProfileStatus();
        climber.getMotionProfileTopLevelBufferCount();
        climber.getNumberOfQuadIdxRises();
        climber.getOutputCurrent();
        climber.getOutputVoltage();
        climber.getP();
        // climber.getParameter(param_t.eAinPosition);
        climber.getPIDSourceType();
        climber.getPinStateQuadA();
        climber.getPinStateQuadB();
        climber.getPinStateQuadIdx();
        climber.getPosition();
        climber.getPulseWidthPosition();
        climber.getPulseWidthRiseToFallUs();
        climber.getPulseWidthRiseToRiseUs();
        climber.getPulseWidthVelocity();
        climber.getReverseSoftLimit();
        climber.getSetpoint();
        climber.getSpeed();
        climber.getStickyFaultForLim();
        climber.getStickyFaultForSoftLim();
        climber.getStickyFaultOverTemp();
        climber.getStickyFaultRevLim();
        climber.getStickyFaultRevSoftLim();
        climber.getStickyFaultUnderVoltage();
        climber.getTemperature();
        climber.isAlive();
        climber.isControlEnabled();
        climber.isEnabled();
        climber.isForwardSoftLimitEnabled();
        climber.isFwdLimitSwitchClosed();
        climber.isMotionProfileTopLevelBufferFull();
        climber.isReverseSoftLimitEnabled();
        climber.isRevLimitSwitchClosed();
        climber.isSafetyEnabled();
        climber.isSensorPresent(FeedbackDevice.AnalogEncoder);
        climber.pidGet();
        // climber.pidWrite(.6);
        climber.processMotionProfileBuffer();
        climber.pushMotionProfileTrajectory(new TrajectoryPoint());
        climber.reset();
        climber.reverseOutput(true);
        climber.reverseOutput(false);
        climber.reverseSensor(true);
        climber.reverseSensor(false);
        climber.set(0);
        climber.setAllowableClosedLoopErr(10);
        climber.setAnalogPosition(30);
        climber.setCloseLoopRampRate(4);
        climber.setControlMode(4);
        climber.setD(0);
        climber.setEncPosition(50);
        climber.setExpiration(40);
        climber.setF(30);
        climber.setFeedbackDevice(FeedbackDevice.AnalogPot);
        climber.setForwardSoftLimit(44);
        climber.setI(.5);
        climber.setInverted(true);
        climber.setIZone(0);
        climber.setP(11);
        climber.setPID(4, 5, 4);;
        climber.setPosition(40);
        climber.setProfile(1);
        climber.setPulseWidthPosition(40);
        climber.setReverseSoftLimit(3);
        climber.setSafetyEnabled(true);
        climber.setSetpoint(40);
        climber.setVoltageRampRate(55);

        // System.out.println("STARTING");
        // climber.set(4);
        // System.out.println("STARTING");
        // climber.set(4);
        // System.out.println("STARTING");
        // climber.set(4);
    }
}
