package com.snobot.simulator.simulator_components.ctre;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.SimDeviceHelper;
import com.snobot.simulator.module_wrapper.ASensorWrapper;
import com.snobot.simulator.module_wrapper.interfaces.IAccelerometerWrapper;
import com.snobot.simulator.module_wrapper.interfaces.IGyroWrapper;
import edu.wpi.first.hal.SimDouble;
import edu.wpi.first.hal.sim.SimDeviceSim;

public class CtrePigeonImuSim extends ASensorWrapper
{
    public static final int sCTRE_OFFSET = 400;

    private final PigeonAccelWrapper mXAccel;
    private final PigeonAccelWrapper mYAccel;
    private final PigeonAccelWrapper mZAccel;

    private final PigeonGyroWrapper mYawGyro;
    private final PigeonGyroWrapper mPitchGyro;
    private final PigeonGyroWrapper mRollGyro;

    private final int mCanHandle;

    ///////////////////////////////////////////////
    private SimDouble mYawAngleDeg;
    private SimDouble mRawGyroXyzDps0;
    private SimDouble mRawGyroXyzDps1;
    private SimDouble mRawGyroXyzDps2;
    private SimDouble mYawPitchRollYpr0;
    private SimDouble mYawPitchRollYpr1;
    private SimDouble mYawPitchRollYpr2;
    private SimDouble mFusedHeading2Value;
    ///////////////////////////////////////////////

    public CtrePigeonImuSim(int aCanHandle, int aBasePort)
    {
        super("Pigeon IMU");

        mCanHandle = aCanHandle;



        mXAccel = new PigeonAccelWrapper("Pigeon X");
        mYAccel = new PigeonAccelWrapper("Pigeon Y");
        mZAccel = new PigeonAccelWrapper("Pigeon Z");

        mYawGyro = new PigeonGyroWrapper("Pigeon Yaw");
        mPitchGyro = new PigeonGyroWrapper("Pigeon Pitch");
        mRollGyro = new PigeonGyroWrapper("Pigeon Roll");

        SensorActuatorRegistry.get().register(mXAccel, aBasePort + 0);
        SensorActuatorRegistry.get().register(mYAccel, aBasePort + 1);
        SensorActuatorRegistry.get().register(mZAccel, aBasePort + 2);

        SensorActuatorRegistry.get().register(mYawGyro, aBasePort + 0);
        SensorActuatorRegistry.get().register(mPitchGyro, aBasePort + 1);
        SensorActuatorRegistry.get().register(mRollGyro, aBasePort + 2);
    }


    @Override
    public void setInitialized(boolean aInitialized)
    {
        super.setInitialized(aInitialized);

        mXAccel.setInitialized(aInitialized);
        mYAccel.setInitialized(aInitialized);
        mZAccel.setInitialized(aInitialized);

        mYawGyro.setInitialized(aInitialized);
        mPitchGyro.setInitialized(aInitialized);
        mRollGyro.setInitialized(aInitialized);

        int deviceId = mCanHandle;
        String deviceName = "CtrePigeonIMUWrapper " + deviceId + "[" + deviceId + "]";
        System.out.println("----------------- Initializing " + deviceName);
        SimDeviceSim simDevice = new SimDeviceSim(deviceName);

        mYawAngleDeg = SimDeviceHelper.getSimDouble(simDevice, "Yaw_angleDeg");
        mRawGyroXyzDps0 = SimDeviceHelper.getSimDouble(simDevice, "RawGyro_xyz_dps_0");
        mRawGyroXyzDps1 = SimDeviceHelper.getSimDouble(simDevice, "RawGyro_xyz_dps_1");
        mRawGyroXyzDps2 = SimDeviceHelper.getSimDouble(simDevice, "RawGyro_xyz_dps_2");
        mYawPitchRollYpr0 = SimDeviceHelper.getSimDouble(simDevice, "YawPitchRoll_ypr_0");
        mYawPitchRollYpr1 = SimDeviceHelper.getSimDouble(simDevice, "YawPitchRoll_ypr_1");
        mYawPitchRollYpr2 = SimDeviceHelper.getSimDouble(simDevice, "YawPitchRoll_ypr_2");
        mFusedHeading2Value = SimDeviceHelper.getSimDouble(simDevice, "FusedHeading1_value");
    }

    public IGyroWrapper getYawWrapper()
    {
        return mYawGyro;
    }

    public IGyroWrapper getPitchWrapper()
    {
        return mPitchGyro;
    }

    public IGyroWrapper getRollWrapper()
    {
        return mRollGyro;
    }

    public IAccelerometerWrapper getXWrapper()
    {
        return mXAccel;
    }

    public IAccelerometerWrapper getYWrapper()
    {
        return mYAccel;
    }

    public IAccelerometerWrapper getZWrapper()
    {
        return mZAccel;
    }

    public static class PigeonGyroWrapper extends ASensorWrapper implements IGyroWrapper
    {
        private double mAngle;
        private double mAngleOffset;

        public PigeonGyroWrapper(String aName)
        {
            super(aName);
        }

        public void setDesiredYaw(double aOffset)
        {
            mAngleOffset = aOffset - mAngle;
        }

        @Override
        public double getAngle()
        {
            return mAngle + mAngleOffset;
        }

        @Override
        public void setAngle(double aAngle)
        {
            mAngle = aAngle;
        }
    }

    private static class PigeonAccelWrapper extends ASensorWrapper implements IAccelerometerWrapper
    {
        private double mAccel;

        public PigeonAccelWrapper(String aName)
        {
            super(aName);
        }

        @Override
        public double getAcceleration()
        {
            return mAccel;
        }

        @Override
        public void setAcceleration(double aAccel)
        {
            mAccel = aAccel;
        }
    }

    ///////////////////////////////////////////////
    public void handleSetYaw()
    {
        ((CtrePigeonImuSim.PigeonGyroWrapper) getYawWrapper()).setDesiredYaw(mYawAngleDeg.get());
    }


    public void handleGetRawGyro()
    {
        mRawGyroXyzDps0.set(getYawWrapper().getAngle());
        mRawGyroXyzDps1.set(getPitchWrapper().getAngle());
        mRawGyroXyzDps2.set(getRollWrapper().getAngle());
    }

    public void handleGetYawPitchRoll()
    {
        mYawPitchRollYpr0.set(getYawWrapper().getAngle());
        mYawPitchRollYpr1.set(getPitchWrapper().getAngle());
        mYawPitchRollYpr2.set(getRollWrapper().getAngle());
    }

    public void handleGetFusedHeading()
    {
        mFusedHeading2Value.set(getYawWrapper().getAngle());
    }
    ///////////////////////////////////////////////

}
