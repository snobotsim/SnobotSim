package com.snobot.simulator.simulator_components.navx;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.jni.NavxSimulatorHal;
import com.snobot.simulator.jni.NavxSimulatorHal.DataType;
import com.snobot.simulator.module_wrapper.ASensorWrapper;
import com.snobot.simulator.simulator_components.accelerometer.IAccelerometerWrapper;
import com.snobot.simulator.simulator_components.gyro.IGyroWrapper;

public class NavxSimulatorWrapper
{
    private class NavxAccelWrapper extends ASensorWrapper implements IAccelerometerWrapper
    {
        private final long mNativePointer;
        private final String mType;
        private final NavxSimulatorHal.DataType mDataType;

        public NavxAccelWrapper(long aNativePointer, String aType, String aExtraName, NavxSimulatorHal.DataType aDataType)
        {
            super(aType + aExtraName);

            mNativePointer = aNativePointer;
            mType = aType;
            mDataType = aDataType;
        }

        @Override
        public void setAcceleration(double aAcceleration)
        {
            NavxSimulatorHal.setNavxData(mType, mNativePointer, mDataType, aAcceleration);
        }

        @Override
        public double getAcceleration()
        {
            return NavxSimulatorHal.getNavxData(mType, mNativePointer, mDataType);
        }

    }

    private class NavxGyroWrapper extends ASensorWrapper implements IGyroWrapper
    {
        private final long mNativePointer;
        private final String mType;
        private final NavxSimulatorHal.DataType mDataType;

        public NavxGyroWrapper(long aNativePointer, String aType, String aExtraName, NavxSimulatorHal.DataType aDataType)
        {
            super(aType + aExtraName);

            mNativePointer = aNativePointer;
            mType = aType;
            mDataType = aDataType;
        }

        @Override
        public void setAngle(double aAngle)
        {
            NavxSimulatorHal.setNavxData(mType, mNativePointer, mDataType, aAngle);
        }

        @Override
        public double getAngle()
        {
            return NavxSimulatorHal.getNavxData(mType, mNativePointer, mDataType);
        }

    }

    public NavxSimulatorWrapper(String aType, long aNativePointer, int aBasePort)
    {
        if (aNativePointer == -1)
        {
            throw new IllegalArgumentException("Native pointer not set up correctly");
        }

        IAccelerometerWrapper xWrapper = new NavxAccelWrapper(aNativePointer, aType, " X Accel", DataType.X);
        IAccelerometerWrapper yWrapper = new NavxAccelWrapper(aNativePointer, aType, " Y Accel", DataType.Y);
        IAccelerometerWrapper zWrapper = new NavxAccelWrapper(aNativePointer, aType, " Z Accel", DataType.Z);
        IGyroWrapper yawWrapper = new NavxGyroWrapper(aNativePointer, aType, " Yaw", DataType.Yaw);
        IGyroWrapper pitchWrapper = new NavxGyroWrapper(aNativePointer, aType, " Pitch", DataType.Pitch);
        IGyroWrapper rollWrapper = new NavxGyroWrapper(aNativePointer, aType, " Roll", DataType.Roll);

        SensorActuatorRegistry.get().register(xWrapper, aBasePort + 0);
        SensorActuatorRegistry.get().register(yWrapper, aBasePort + 1);
        SensorActuatorRegistry.get().register(zWrapper, aBasePort + 2);

        SensorActuatorRegistry.get().register(yawWrapper, aBasePort + 0);
        SensorActuatorRegistry.get().register(pitchWrapper, aBasePort + 1);
        SensorActuatorRegistry.get().register(rollWrapper, aBasePort + 2);
    }
}
