package com.snobot.simulator.example_robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Example robot used in the event that no robot to simulate is specified. Does
 * a simple simulation by hooking up a few commonly used components
 * 
 * @author PJ
 *
 */
public class ExampleRobot extends IterativeRobot
{
	public Joystick mJoystick;
    public Solenoid mSolenoid;
	public SpeedController mLeftDrive;
	public SpeedController mRightDrive;
    public Encoder mLeftDriveEncoder;
    public Encoder mRightDriveEncoder;

    public Timer mAutoTimer;
	
	@Override
	public void robotInit()
	{
		mJoystick = new Joystick(0);

        mSolenoid = new Solenoid(0);
		mLeftDrive = new VictorSP(0);
		mRightDrive = new VictorSP(1);
        mLeftDriveEncoder = new Encoder(0, 1);
        mRightDriveEncoder = new Encoder(2, 3);

        mLeftDriveEncoder.setDistancePerPulse(.01);
        mRightDriveEncoder.setDistancePerPulse(.01);

        mAutoTimer = new Timer();
	}

    @Override
    public void autonomousInit()
    {
        mLeftDriveEncoder.reset();
        mRightDriveEncoder.reset();
        mAutoTimer.start();
    }

    @Override
    public void autonomousPeriodic()
    {
        if (mAutoTimer.get() < 2)
        {
            mLeftDrive.set(1);
            mRightDrive.set(-1);
        }
        else
        {
            mLeftDrive.set(0);
            mRightDrive.set(0);
        }
    }

	@Override
	public void teleopPeriodic()
	{
		mLeftDrive.set(mJoystick.getRawAxis(0));
		mRightDrive.set(-mJoystick.getRawAxis(0));
		
		mSolenoid.set(mJoystick.getRawButton(1));

        SmartDashboard.putNumber("Left Enc", mLeftDriveEncoder.getDistance());
        SmartDashboard.putNumber("Right Enc", mRightDriveEncoder.getDistance());
	}

}
