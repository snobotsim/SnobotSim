package com.snobot.simulator.example_robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;

public class ExampleRobot extends IterativeRobot
{
	public Joystick mJoystick;
	public SpeedController mLeftDrive;
	public SpeedController mRightDrive;
	public Solenoid mSolenoid;
	
	@Override
	public void robotInit()
	{
		mJoystick = new Joystick(0);
		mLeftDrive = new VictorSP(0);
		mRightDrive = new VictorSP(1);
		mSolenoid = new Solenoid(0);
	}

	@Override
	public void teleopPeriodic()
	{
		mLeftDrive.set(mJoystick.getRawAxis(0));
		mRightDrive.set(-mJoystick.getRawAxis(0));
		
		mSolenoid.set(mJoystick.getRawButton(1));
	}

}
