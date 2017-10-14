package com.snobot.simulator.simulator_components.gyro;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.jni.SpiI2CSimulatorHal;
import com.snobot.simulator.module_wrapper.ASensorWrapper;

public class SpiGyro extends ASensorWrapper implements IGyroWrapper
{
    private final String mType;
    private final long mNativePointer;

    public SpiGyro(String aType, long aNativePointer, int aBasePort)
    {
        super(aType);

        mType = aType;
        mNativePointer = aNativePointer;

        if (aNativePointer == -1)
        {
            throw new IllegalArgumentException("Native pointer not set up correctly");
        }

        SensorActuatorRegistry.get().register(this, aBasePort);
    }

    @Override
    public double getAngle()
    {
        return SpiI2CSimulatorHal.getSpiGyroAngle(mType, mNativePointer);
    }

    @Override
    public void setAngle(double aAngle)
    {
        SpiI2CSimulatorHal.setSpiGyroAngle(mType, mNativePointer, aAngle);
    }
}
