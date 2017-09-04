package com.snobot.test.utilities;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

public class MockRobot extends IterativeRobot
{

    private Encoder mRightEncoder;
    private Encoder mLeftEncoder;
    private SpeedController mRightMotor;
    private SpeedController mLeftMotor;
    private Relay mRelay;
    private Solenoid mSolenoid;
    private DigitalInput mDigitalIn;

    public MockRobot()
    {
        mRightEncoder = new Encoder(0, 1);
        mLeftEncoder = new Encoder(2, 3);

        mRightMotor = new Talon(0);
        mLeftMotor = new Talon(1);

        mRelay = new Relay(0);
        mSolenoid = new Solenoid(0);
        mDigitalIn = new DigitalInput(4);
    }
}
