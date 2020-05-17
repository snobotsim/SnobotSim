package com.snobot.simulator.simulator_components.ctre;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.SimDeviceDumpHelper;
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
    SimDeviceSim  mSimDevice;
    SimDouble m_Yaw_angleDeg;
    SimDouble m_RawGyro_xyz_dps_0;
    SimDouble m_RawGyro_xyz_dps_1;
    SimDouble m_RawGyro_xyz_dps_2;
    SimDouble m_YawPitchRoll_ypr_0;
    SimDouble m_YawPitchRoll_ypr_1;
    SimDouble m_YawPitchRoll_ypr_2;
    SimDouble m_FusedHeading2_value;
    ///////////////////////////////////////////////

    public CtrePigeonImuSim(int canHandle, int aBasePort)
    {
        super("Pigeon IMU");

        mCanHandle = canHandle;



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

    private SimDouble getSimDouble(String name)
    {
        SimDouble output = mSimDevice.getDouble(name);
        if(output == null)
        {
            throw new IllegalArgumentException("Sim device '" + name + "' is not set");
        }

        return output;
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
        mSimDevice = new SimDeviceSim(deviceName);

        SimDeviceDumpHelper.dumpSimDevices();

        m_Yaw_angleDeg = getSimDouble("Yaw_angleDeg");
        m_RawGyro_xyz_dps_0 = getSimDouble("RawGyro_xyz_dps_0");
        m_RawGyro_xyz_dps_1 = getSimDouble("RawGyro_xyz_dps_1");
        m_RawGyro_xyz_dps_2 = getSimDouble("RawGyro_xyz_dps_2");
        m_YawPitchRoll_ypr_0 = getSimDouble("YawPitchRoll_ypr_0");
        m_YawPitchRoll_ypr_1 = getSimDouble("YawPitchRoll_ypr_1");
        m_YawPitchRoll_ypr_2 = getSimDouble("YawPitchRoll_ypr_2");
        m_FusedHeading2_value = getSimDouble("FusedHeading1_value");
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
            ((CtrePigeonImuSim.PigeonGyroWrapper) getYawWrapper()).setDesiredYaw(m_Yaw_angleDeg.get());
    }


    public void handleGetRawGyro()
    {
        m_RawGyro_xyz_dps_0.set(getYawWrapper().getAngle());
        m_RawGyro_xyz_dps_1.set(getPitchWrapper().getAngle());
        m_RawGyro_xyz_dps_2.set(getRollWrapper().getAngle());
//            aData.putDouble(wrapper.getYawWrapper().getAngle());
//            aData.putDouble(wrapper.getPitchWrapper().getAngle());
//            aData.putDouble(wrapper.getRollWrapper().getAngle());
    }
    public void handleGetYawPitchRoll()
    {
        m_YawPitchRoll_ypr_0.set(getYawWrapper().getAngle());
        m_YawPitchRoll_ypr_1.set(getPitchWrapper().getAngle());
        m_YawPitchRoll_ypr_2.set(getRollWrapper().getAngle());
    }
    public void handleGetFusedHeading()
    {
        m_FusedHeading2_value.set(getYawWrapper().getAngle());
    }
    ///////////////////////////////////////////////

}
