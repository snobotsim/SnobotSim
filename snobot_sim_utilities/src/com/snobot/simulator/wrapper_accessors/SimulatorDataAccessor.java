package com.snobot.simulator.wrapper_accessors;

import com.snobot.simulator.DcMotorModelConfig;

public interface SimulatorDataAccessor
{
    enum SnobotLogLevel
    {
        DEBUG, INFO, WARNING, ERROR
    }

    void setLogLevel(SnobotLogLevel logLevel);

    String getNativeBuildVersion();

    void reset();

    boolean connectTankDriveSimulator(int leftEncHandle, int rightEncHandle, int gyroHandle, double turnKp);

    DcMotorModelConfig createMotor(String selectedMotor, int numMotors, double gearReduction, double efficiency);

    DcMotorModelConfig createMotor(String motorType);

    boolean setSpeedControllerModel_Simple(int aScHandle, double maxSpeed);

    boolean setSpeedControllerModel_Static(int mHandle, DcMotorModelConfig motorConfig, double load);

    boolean setSpeedControllerModel_Static(int aScHandle, DcMotorModelConfig motorConfig, double load, double conversionFactor);

    boolean setSpeedControllerModel_Gravitational(int aScHandle, DcMotorModelConfig motorConfig, double load);

    boolean setSpeedControllerModel_Rotational(int aScHandle, DcMotorModelConfig motorConfig, double armCenterOfMass, double armMass);

    void setDisabled(boolean b);

    void setAutonomous(boolean b);

    double getMatchTime();

    void waitForProgramToStart();

    /**
     * Updates the simulator components and speed controllers, moving them
     * forward aUpdateTime seconds in time. This function does not delay any
     * time
     * 
     * @param aUpdatePeriod
     *            The time to move the components forward
     */
    void updateSimulatorComponents(double aUpdatePeriod);

    /**
     * Simulates waiting for a DS packet to come in. Actually waits the amount
     * of time. Uses default tactical timing of 20ms/50hz
     * 
     * @param aUpdatePeriod
     *            The time, in seconds, to pause before notifying the DS it has
     *            received data
     */
    default void waitForNextUpdateLoop()
    {
        waitForNextUpdateLoop(.002);
    }

    /**
     * Simulates waiting for a DS packet to come in. Actually waits the amount
     * of time
     * 
     * @param aUpdatePeriod
     *            The time, in seconds, to pause before notifying the DS it has
     *            received data
     */
    void waitForNextUpdateLoop(double aUpdatePeriod);

    void setJoystickInformation(int i, float[] axisValues, short[] povValues, int buttonCount, int buttonMask);

}
