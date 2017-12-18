package com.snobot.simulator.simulator_components.accelerometer;

import java.nio.ByteBuffer;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.jni.adx_family.SpiI2CSimulatorJni;
import com.snobot.simulator.jni.adx_family.SpiI2CSimulatorJni.DataType;
import com.snobot.simulator.module_wrapper.ASensorWrapper;
import com.snobot.simulator.simulator_components.II2CWrapper;
import com.snobot.simulator.simulator_components.ISpiWrapper;

public class SpiI2CAccelerometer implements ISpiWrapper, II2CWrapper
{
    private static final Logger sLOGGER = Logger.getLogger(SpiI2CAccelerometer.class);

    private class AdxAccelWrapper extends ASensorWrapper implements IAccelerometerWrapper
    {
        private final long mNativePointer;
        private final String mType;
        private final SpiI2CSimulatorJni.DataType mDataType;

        public AdxAccelWrapper(long aNativePointer, String aType, String aExtraName, SpiI2CSimulatorJni.DataType aDataType)
        {
            super(aType + aExtraName);

            mNativePointer = aNativePointer;
            mType = aType;
            mDataType = aDataType;
        }

        @Override
        public void setAcceleration(double aAcceleration)
        {
            SpiI2CSimulatorJni.setSpiI2cAccelerometerData(mType, mNativePointer, mDataType, aAcceleration);
        }

        @Override
        public double getAcceleration()
        {
            return SpiI2CSimulatorJni.getSpiI2cAccelerometerData(mType, mNativePointer, mDataType);
        }

    }

    private final long mNativePointer;
    private final String mType;

    public SpiI2CAccelerometer(String aType, long aNativePointer, int aBasePort)
    {
        if (aNativePointer == -1)
        {
            throw new IllegalArgumentException("Native pointer not set up correctly");
        }

        mNativePointer = aNativePointer;
        mType = aType;

        IAccelerometerWrapper xWrapper = new AdxAccelWrapper(aNativePointer, aType, " X Accel", DataType.X);
        IAccelerometerWrapper yWrapper = new AdxAccelWrapper(aNativePointer, aType, " Y Accel", DataType.Y);
        IAccelerometerWrapper zWrapper = new AdxAccelWrapper(aNativePointer, aType, " Z Accel", DataType.Z);

        SensorActuatorRegistry.get().register(xWrapper, aBasePort + 0);
        SensorActuatorRegistry.get().register(yWrapper, aBasePort + 1);
        SensorActuatorRegistry.get().register(zWrapper, aBasePort + 2);
    }

    @Override
    public void handleRead(ByteBuffer buffer)
    {
        sLOGGER.log(Level.ERROR, "This shouldn't be called directly");
    }

    @Override
    public void handleWrite(ByteBuffer buffer)
    {
        sLOGGER.log(Level.ERROR, "This shouldn't be called directly");
    }

    @Override
    public void shutdown()
    {
        SpiI2CSimulatorJni.deleteAccelerometer(mType, mNativePointer);
    }
}
