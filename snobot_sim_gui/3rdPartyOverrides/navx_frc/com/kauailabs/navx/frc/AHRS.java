package com.kauailabs.navx.frc;

import com.snobot.simulator.jni.module_wrapper.AccelerometerWrapperJni;
import com.snobot.simulator.jni.module_wrapper.GyroWrapperJni;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.SensorBase;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;

public class AHRS extends SensorBase implements PIDSource, LiveWindowSendable
{

    protected int mYawHandle;
    protected int mPitchHandle;
    protected int mRollHandle;

    protected int mXAccel;
    protected int mYAccel;
    protected int mZAccel;

    // Stuff from real code
    ITable m_table;
    PIDSourceType pid_source_type = PIDSourceType.kDisplacement;

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

    public double getPitch()
    {
        return GyroWrapperJni.getAngle(mPitchHandle);
    }

    public double getRoll()
    {
        return GyroWrapperJni.getAngle(mRollHandle);
    }

    public double getAngle()
    {
        return getYaw();
    }

    public double getRate()
    {
        System.err.println("AHRS getRate Unsupported");
        return 0;
    }

    public void reset()
    {
        GyroWrapperJni.reset(mYawHandle);
        GyroWrapperJni.reset(mPitchHandle);
        GyroWrapperJni.reset(mRollHandle);
    }

    /***********************************************************/
    /* PIDSource Interface Implementation */
    /***********************************************************/

    public PIDSourceType getPIDSourceType()
    {
        return pid_source_type;
    }

    public void setPIDSourceType(PIDSourceType type)
    {
        pid_source_type = type;
    }

    public double getCompassHeading()
    {
        System.err.println("Unsupported");
        return 0;
    }

    public void resetAngle()
    {
        System.err.println("Unsupported");
    }

    public void zeroYaw()
    {
        System.err.println("Unsupported");
    }

    /**
     * Returns the current yaw value reported by the sensor. This yaw value is
     * useful for implementing features including "auto rotate to a known
     * angle".
     * 
     * @return The current yaw angle in degrees (-180 to 180).
     */
    public double pidGet()
    {
        if (pid_source_type == PIDSourceType.kRate)
        {
            return getRate();
        }
        else
        {
            return getYaw();
        }
    }

    /***********************************************************/
    /* LiveWindowSendable Interface Implementation */
    /***********************************************************/

    public void updateTable()
    {
        if (m_table != null)
        {
            m_table.putNumber("Value", getYaw());
        }
    }

    public void startLiveWindowMode() {
    }

    public void stopLiveWindowMode() {
    }

    public void initTable(ITable itable) {
        m_table = itable;
        updateTable();
    }

    public ITable getTable() {
        return m_table;
    }

    public String getSmartDashboardType() {
        return "Gyro";
    }

}
