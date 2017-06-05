package com.kauailabs.navx.frc;

import com.snobot.simulator.jni.module_wrapper.AccelerometerWrapperJni;
import com.snobot.simulator.jni.module_wrapper.GyroWrapperJni;

import edu.wpi.first.wpilibj.SPI.Port;

public class AHRS
{
    protected int mYawHandle;
    protected int mPitchHandle;
    protected int mRollHandle;

    protected int mXAccel;
    protected int mYAccel;
    protected int mZAccel;

    public AHRS(Port aPort)
    {
        int baseHandle = aPort.value * 10;

        mYawHandle = baseHandle;
        mPitchHandle = baseHandle + 1;
        mRollHandle = baseHandle + 2;

        GyroWrapperJni.register(mYawHandle, "NavX Yaw");
        GyroWrapperJni.register(mPitchHandle, "NavX Pitch");
        GyroWrapperJni.register(mRollHandle, "NavX Roll");

        AccelerometerWrapperJni.register(mXAccel, "NavX X");
        AccelerometerWrapperJni.register(mYAccel, "NavX Y");
        AccelerometerWrapperJni.register(mZAccel, "NavX Z");
    }

    public double getYaw()
    {
        return GyroWrapperJni.getAngle(mYawHandle);
    }

    public void reset()
    {
        GyroWrapperJni.reset(mYawHandle);
        GyroWrapperJni.reset(mPitchHandle);
        GyroWrapperJni.reset(mRollHandle);
    }

}
