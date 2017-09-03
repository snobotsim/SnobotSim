package simulator.com.snobot.simulator.robot_tests;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

public class MockRobot extends IterativeRobot
{

    private Encoder mRightEncoder;
    private Encoder mLeftEncoder;
    private SpeedController mRightMotor;
    private SpeedController mLeftMotor;

    public MockRobot()
    {
        mRightEncoder = new Encoder(0, 1);
        mLeftEncoder = new Encoder(2, 3);

        mRightMotor = new Talon(0);
        mLeftMotor = new Talon(1);
    }
}
