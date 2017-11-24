package com.snobot.simulator.simulator_components.gyro;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.jni.adx_family.SpiI2CSimulatorJni;
import com.snobot.simulator.module_wrapper.ASensorWrapper;
import com.snobot.simulator.simulator_components.ISpiWrapper;

public class SpiGyro extends ASensorWrapper implements IGyroWrapper, ISpiWrapper
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

        SensorActuatorRegistry.get().register((IGyroWrapper) this, aBasePort);
    }

    @Override
    public double getAngle()
    {
        return SpiI2CSimulatorJni.getSpiGyroAngle(mType, mNativePointer);
    }

    @Override
    public void setAngle(double aAngle)
    {
        SpiI2CSimulatorJni.setSpiGyroAngle(mType, mNativePointer, aAngle);
    }

    @Override
    public void shutdown()
    {
        SpiI2CSimulatorJni.deleteSpiGyro(mType, mNativePointer);
    }
}
