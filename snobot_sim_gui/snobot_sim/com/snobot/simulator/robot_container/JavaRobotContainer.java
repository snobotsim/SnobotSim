package com.snobot.simulator.robot_container;

import com.snobot.simulator.JniLibraryResourceLoader;

import edu.wpi.first.wpilibj.RobotBase;

public class JavaRobotContainer implements IRobotClassContainer
{
    private String mRobotClassName;
    private RobotBase mRobot;

    public JavaRobotContainer(String aRobotClassName)
    {
        mRobotClassName = aRobotClassName;
    }

    @Override
    public void constructRobot() throws InstantiationException, IllegalAccessException, ClassNotFoundException
    {
        JniLibraryResourceLoader.loadLibrary("wpilibJavaJNI");

        RobotBase.initializeHardwareConfiguration();
        mRobot = (RobotBase) Class.forName(mRobotClassName).newInstance();
    }

    @Override
    public void startCompetition()
    {
        mRobot.startCompetition();
    }

    public RobotBase getJavaRobot()
    {
        return mRobot;
    }
}
