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

    void updateLoop();

    void waitForNextUpdateLoop();

    void setJoystickInformation(int i, float[] axisValues, short[] povValues, int buttonCount, int buttonMask);

    void setUpdateRate(double aUpdatePeriod);

}
