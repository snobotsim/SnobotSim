package com.snobot.simulator.simulator_components.accelerometer;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.jni.SpiI2CSimulatorHal;
import com.snobot.simulator.jni.SpiI2CSimulatorHal.DataType;
import com.snobot.simulator.module_wrapper.ASensorWrapper;

public class SpiI2CAccelerometer
{

    private class AdxAccelWrapper extends ASensorWrapper implements IAccelerometerWrapper
    {
        private final long mNativePointer;
        private final String mType;
        private final SpiI2CSimulatorHal.DataType mDataType;

        public AdxAccelWrapper(long aNativePointer, String aType, String aExtraName, SpiI2CSimulatorHal.DataType aDataType)
        {
            super(aType + aExtraName);

            mNativePointer = aNativePointer;
            mType = aType;
            mDataType = aDataType;
        }

        @Override
        public void setAcceleration(double aAcceleration)
        {
            SpiI2CSimulatorHal.setSpiI2cAccelerometerData(mType, mNativePointer, mDataType, aAcceleration);
        }

        @Override
        public double getAcceleration()
        {
            return SpiI2CSimulatorHal.getSpiI2cAccelerometerData(mType, mNativePointer, mDataType);
        }

    }

    public SpiI2CAccelerometer(String aType, long aNativePointer, int aBasePort)
    {
        if (aNativePointer == -1)
        {
            throw new IllegalArgumentException("Native pointer not set up correctly");
        }

        IAccelerometerWrapper xWrapper = new AdxAccelWrapper(aNativePointer, aType, " X Accel", DataType.X);
        IAccelerometerWrapper yWrapper = new AdxAccelWrapper(aNativePointer, aType, " Y Accel", DataType.Y);
        IAccelerometerWrapper zWrapper = new AdxAccelWrapper(aNativePointer, aType, " Z Accel", DataType.Z);

        SensorActuatorRegistry.get().register(xWrapper, aBasePort + 0);
        SensorActuatorRegistry.get().register(yWrapper, aBasePort + 1);
        SensorActuatorRegistry.get().register(zWrapper, aBasePort + 2);
    }
    

//    public double 
}
