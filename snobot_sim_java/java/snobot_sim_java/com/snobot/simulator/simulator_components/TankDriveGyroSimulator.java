package com.snobot.simulator.simulator_components;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.module_wrapper.EncoderWrapper;
import com.snobot.simulator.simulator_components.gyro.IGyroWrapper;

public class TankDriveGyroSimulator implements ISimulatorUpdater
{
    private static final Logger sLOGGER = Logger.getLogger(TankDriveGyroSimulator.class);

    private final TankDriveConfig mConfig;
    private final EncoderWrapper mLeftEncoder;
    private final EncoderWrapper mRightEncoder;
    private final IGyroWrapper mGyroWrapper;
    private double mKP;
    private boolean mIsSetup;

    private double mAngle; // degrees


    public TankDriveGyroSimulator(TankDriveConfig aConfig)
    {
        mConfig = aConfig;
        mRightEncoder = SensorActuatorRegistry.get().getEncoders().get(aConfig.getmRightEncoderHandle());
        mLeftEncoder = SensorActuatorRegistry.get().getEncoders().get(aConfig.getmLeftEncoderHandle());
        mGyroWrapper = SensorActuatorRegistry.get().getGyros().get(aConfig.getmGyroHandle());
        mKP = aConfig.mTurnKp;

        mIsSetup = mLeftEncoder != null && mRightEncoder != null && mGyroWrapper != null;

        // TODO I have no idea what this black magic number means
        // mKP = 22.0 / 12.0;
        // mKP = 110.0 / 12.0;

        if (!mIsSetup)
        {
            sLOGGER.log(Level.ERROR, "Can't simulate gyro, some inputs are null");
        }

        SensorActuatorRegistry.get().register(this);
    }

    @Override
    public void update()
    {
        if (mIsSetup)
        {

            double rightDist = mRightEncoder.getPosition();
            double leftDist = mLeftEncoder.getPosition();

            mAngle = (leftDist - rightDist) / (Math.PI * mKP) * (180.0);

            mGyroWrapper.setAngle(mAngle);
            sLOGGER.log(Level.TRACE,
                    "SIMULATOR : angle=" + mAngle + ", right=" + rightDist + ", left=" + leftDist);
        }
    }

    public boolean isSetup()
    {
        return mIsSetup;
    }
    
    public Object getConfig()
    {
        return mConfig;
    }

    public static class TankDriveConfig
    {

        private int mLeftEncoderHandle;
        private int mRightEncoderHandle;
        private int mGyroHandle;
        private double mTurnKp;

        public TankDriveConfig()
        {
            this(-1, -1, -1, 1);
        }

        public TankDriveConfig(int aLeftHandle, int aRightHandle, int aGyroHandle, double aTurnKp)
        {
            mLeftEncoderHandle = aLeftHandle;
            mRightEncoderHandle = aRightHandle;
            mGyroHandle = aGyroHandle;
            mTurnKp = aTurnKp;
        }

        public int getmLeftEncoderHandle()
        {
            return mLeftEncoderHandle;
        }

        public void setmLeftEncoderHandle(int mLeftEncoderHandle)
        {
            this.mLeftEncoderHandle = mLeftEncoderHandle;
        }

        public int getmRightEncoderHandle()
        {
            return mRightEncoderHandle;
        }

        public void setmRightEncoderHandle(int mRightEncoderHandle)
        {
            this.mRightEncoderHandle = mRightEncoderHandle;
        }

        public int getmGyroHandle()
        {
            return mGyroHandle;
        }

        public void setmGyroHandle(int mGyroHandle)
        {
            this.mGyroHandle = mGyroHandle;
        }

        public double getmTurnKp()
        {
            return mTurnKp;
        }

        public void setmTurnKp(double mTurnKp)
        {
            this.mTurnKp = mTurnKp;
        }

    }

}