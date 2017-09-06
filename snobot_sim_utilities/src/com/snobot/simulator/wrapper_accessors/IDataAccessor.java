package com.snobot.simulator.wrapper_accessors;

public interface IDataAccessor
{
    String getAccessorType();

    AccelerometerWrapperAccessor getAccelerometerAccessor();

    GyroWrapperAccessor getGyroAccessor();

    AnalogSourceWrapperAccessor getAnalogAccessor();

    DigitalSourceWrapperAccessor getDigitalAccessor();

    EncoderWrapperAccessor getEncoderAccessor();

    RelayWrapperAccessor getRelayAccessor();

    SolenoidWrapperAccessor getSolenoidAccessor();

    SpeedControllerWrapperAccessor getSpeedControllerAccessor();

    SimulatorDataAccessor getSimulatorDataAccessor();
}
