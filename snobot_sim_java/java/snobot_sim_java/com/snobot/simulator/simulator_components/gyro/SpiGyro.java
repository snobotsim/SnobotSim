package com.snobot.simulator.simulator_components.gyro;

import java.nio.ByteBuffer;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.jni.adx_family.SpiI2CSimulatorJni;
import com.snobot.simulator.module_wrapper.ASensorWrapper;
import com.snobot.simulator.simulator_components.ISpiWrapper;

public class SpiGyro extends ASensorWrapper implements IGyroWrapper, ISpiWrapper
{
    private static final Logger sLOGGER = LogManager.getLogger(SpiGyro.class);

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
    public void handleRead(ByteBuffer aBuffer)
    {
        sLOGGER.log(Level.ERROR, "This shouldn't be called directly");
    }

    @Override
    public void handleWrite(ByteBuffer aBuffer)
    {
        sLOGGER.log(Level.ERROR, "This shouldn't be called directly");
    }

    @Override
    public void shutdown()
    {
        SpiI2CSimulatorJni.deleteSpiGyro(mType, mNativePointer);
    }
}
