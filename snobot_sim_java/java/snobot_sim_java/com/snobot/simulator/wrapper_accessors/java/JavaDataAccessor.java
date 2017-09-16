package com.snobot.simulator.wrapper_accessors.java;

import java.io.File;

import org.apache.log4j.PropertyConfigurator;

import com.snobot.simulator.jni.RegisterCallbacksJni;
import com.snobot.simulator.wrapper_accessors.AccelerometerWrapperAccessor;
import com.snobot.simulator.wrapper_accessors.AnalogSourceWrapperAccessor;
import com.snobot.simulator.wrapper_accessors.DigitalSourceWrapperAccessor;
import com.snobot.simulator.wrapper_accessors.EncoderWrapperAccessor;
import com.snobot.simulator.wrapper_accessors.GyroWrapperAccessor;
import com.snobot.simulator.wrapper_accessors.IDataAccessor;
import com.snobot.simulator.wrapper_accessors.RelayWrapperAccessor;
import com.snobot.simulator.wrapper_accessors.SimulatorDataAccessor;
import com.snobot.simulator.wrapper_accessors.SolenoidWrapperAccessor;
import com.snobot.simulator.wrapper_accessors.SpeedControllerWrapperAccessor;

public class JavaDataAccessor implements IDataAccessor
{
    private AccelerometerWrapperAccessor accelerometer;
    private GyroWrapperAccessor gyro;
    private AnalogSourceWrapperAccessor analog;
    private DigitalSourceWrapperAccessor digital;
    private EncoderWrapperAccessor encoder;
    private RelayWrapperAccessor relay;
    private SolenoidWrapperAccessor solenoid;
    private SpeedControllerWrapperAccessor pwm;
    private SimulatorDataAccessor simulator;

    public JavaDataAccessor()
    {
        String logFile = new File("log4j.properties").getAbsolutePath();
        PropertyConfigurator.configure(logFile);

        RegisterCallbacksJni.registerAllCallbacks();

        accelerometer = new JavaAccelerometerWrapperAccessor();
        gyro = new JavaGyroWrapperAccessor();
        analog = new JavaAnalogSourceWrapperAccessor();
        digital = new JavaDigitalSourceWrapperAccessor();
        encoder = new JavaEncoderWrapperAccessor();
        relay = new JavaRelayWrapperAccessor();
        solenoid = new JavaSolenoidWrapperAccessor();
        pwm = new JavaSpeedControllerWrapperAccessor();
        simulator = new JavaSimulatorDataAccessor();
    }

    @Override
    public String getAccessorType()
    {
        return "Java";
    }

    @Override
    public AccelerometerWrapperAccessor getAccelerometerAccessor()
    {
        return accelerometer;
    }

    @Override
    public GyroWrapperAccessor getGyroAccessor()
    {
        return gyro;
    }

    @Override
    public AnalogSourceWrapperAccessor getAnalogAccessor()
    {
        return analog;
    }

    @Override
    public DigitalSourceWrapperAccessor getDigitalAccessor()
    {
        return digital;
    }

    @Override
    public EncoderWrapperAccessor getEncoderAccessor()
    {
        return encoder;
    }

    @Override
    public RelayWrapperAccessor getRelayAccessor()
    {
        return relay;
    }

    @Override
    public SolenoidWrapperAccessor getSolenoidAccessor()
    {
        return solenoid;
    }

    @Override
    public SpeedControllerWrapperAccessor getSpeedControllerAccessor()
    {
        return pwm;
    }

    @Override
    public SimulatorDataAccessor getSimulatorDataAccessor()
    {
        return simulator;
    }
}
