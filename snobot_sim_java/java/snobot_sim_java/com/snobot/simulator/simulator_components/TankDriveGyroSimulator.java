package com.snobot.simulator.simulator_components;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.module_wrapper.EncoderWrapper;
import com.snobot.simulator.simulator_components.gyro.GyroWrapper;

public class TankDriveGyroSimulator implements ISimulatorUpdater
{

    private EncoderWrapper mLeftEncoder;
    private EncoderWrapper mRightEncoder;
    private GyroWrapper mGyroWrapper;

    private double mAngle; // degrees
    private boolean mIsSetup;

    private boolean mIsLeftReversed;
    private boolean mIsRightReversed;

    private double mKP;

    public TankDriveGyroSimulator(EncoderWrapper aLeftEncoder, EncoderWrapper aRightEncoder, GyroWrapper aGyroWrapper)
    {
        mLeftEncoder = aLeftEncoder;
        mRightEncoder = aRightEncoder;
        mGyroWrapper = aGyroWrapper;

        mIsSetup = mLeftEncoder != null && mRightEncoder != null && mGyroWrapper != null;

        // TODO I have no idea what this black magic number means
        // mKP = 22.0 / 12.0;
        mKP = 110.0 / 12.0;

        if (!mIsSetup)
        {
            System.err.println("Can't simulate gyro, some inputs are null");
        }

        SensorActuatorRegistry.get().register(this);
    }

    public void setTurnKp(double aKp)
    {
        mKP = aKp;
    }

    public void setIsReverse(boolean isLeftReversed, boolean isRightReversed)
    {
        mIsLeftReversed = isLeftReversed;
        mIsRightReversed = isRightReversed;
    }

    @Override
    public void update()
    {

        if (mIsSetup)
        {

            double rightDist = mRightEncoder.getPosition();
            double leftDist = mLeftEncoder.getPosition();

            if (mIsLeftReversed == true)
            {
                leftDist *= -1;
            }
            if (mIsRightReversed == true)
            {
                rightDist *= -1;
            }

            mAngle = (leftDist - rightDist) / (Math.PI * mKP) * (180.0);

            mGyroWrapper.setAngle(mAngle);
            Logger.getLogger(TankDriveGyroSimulator.class).log(Level.TRACE,
                    "SIMULATOR : angle=" + mAngle + ", right=" + rightDist + ", left=" + leftDist);
        }
    }

    public boolean isSetup()
    {
        return mIsSetup;
    }
}