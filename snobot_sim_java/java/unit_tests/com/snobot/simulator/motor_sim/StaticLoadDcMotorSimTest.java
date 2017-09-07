package com.snobot.simulator.motor_sim;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.snobot.simulator.motor_sim.motor_factory.MakeTransmission;
import com.snobot.simulator.motor_sim.motor_factory.PublishedMotorFactory;
import com.snobot.simulator.motor_sim.motor_factory.VexMotorFactory;

public class StaticLoadDcMotorSimTest
{
    private DcMotorModel getSingle775WithTransmission(int numMotors, double effiecency)
    {
        return MakeTransmission.makeTransmission(PublishedMotorFactory.makeRS775(), numMotors, 10.0, effiecency);
    }

    @Test
    public void testRS775_6VSmallLoad()
    {
        StaticLoadDcMotorSim rs775 = new StaticLoadDcMotorSim(getSingle775WithTransmission(1, 1.0), .01);

        // Apply a positive voltage and small load.
        System.out.println("Voltage=6V, Load=.01 kg*m^2");
        for (int i = 0; i < 1000; ++i)
        {
            rs775.setVoltagePercentage(.5);
            rs775.update(0.01);

            if (i % 100 == 0)
            {
                System.out.print("Time: " + 0.01 * i);
                System.out.print(", Position: " + rs775.getPosition());
                System.out.print(", Velocity: " + rs775.getVelocity());
                System.out.println(", Current: " + rs775.getCurrent());
            }
        }
        // We expect negligible final current, and a final velocity of ~68.04
        // rad/sec.
        assertEquals(rs775.getCurrent(), 0.0, 1E-3);
        assertEquals(rs775.getVelocity(), 68.06, 1E-2);

    }

    @Test
    public void testRS775_12VSmallLoad()
    {
        StaticLoadDcMotorSim rs775 = new StaticLoadDcMotorSim(getSingle775WithTransmission(1, 1.0), .01);

        // Apply a larger voltage.
        System.out.println("Voltage=12V, Load=.01 kg*m^2");
        for (int i = 0; i < 1000; ++i)
        {
            rs775.setVoltagePercentage(1);
            rs775.update(0.01);

            if (i % 100 == 0)
            {
                System.out.print("Time: " + 0.01 * i);
                System.out.print(", Position: " + rs775.getPosition());
                System.out.print(", Velocity: " + rs775.getVelocity());
                System.out.println(", Current: " + rs775.getCurrent());
            }
        }

        // We expect negligible final current, and a final velocity of ~2 *
        // 68.04 rad/sec.
        assertEquals(rs775.getCurrent(), 0.0, 1E-3);
        assertEquals(rs775.getVelocity(), 68.04 * 2, 1E-1);

    }

    @Test
    public void testRS775_12VLargeLoad()
    {
        StaticLoadDcMotorSim rs775 = new StaticLoadDcMotorSim(getSingle775WithTransmission(1, 1.0), 1.0);

        System.out.println("Voltage=12V, Load=1.0 kg*m^2");
        for (int i = 0; i < 1000; ++i)
        {
            rs775.setVoltagePercentage(1);
            rs775.update(0.01);

            if (i % 100 == 0)
            {
                System.out.print("Time: " + 0.01 * i);
                System.out.print(", Position: " + rs775.getPosition());
                System.out.print(", Velocity: " + rs775.getVelocity());
                System.out.println(", Current: " + rs775.getCurrent());
            }
        }

        // This is slower, so 1000 iterations isn't enough to get to steady
        // state
        assertEquals(rs775.getCurrent(), 48.758, 1E-3);
        assertEquals(rs775.getVelocity(), 59.59, 1E-1);
    }

    @Test
    public void testDoubleRS775_100Efficiency_12VLargeLoad()
    {
        StaticLoadDcMotorSim rs775 = new StaticLoadDcMotorSim(getSingle775WithTransmission(2, 1.0), 1.0);

        System.out.println("(2 motors) Voltage=12V, Load=1.0 kg*m^2");
        for (int i = 0; i < 1000; ++i)
        {
            rs775.setVoltagePercentage(1);
            rs775.update(0.01);

            if (i % 100 == 0)
            {
                System.out.print("Time: " + 0.01 * i);
                System.out.print(", Position: " + rs775.getPosition());
                System.out.print(", Velocity: " + rs775.getVelocity());
                System.out.println(", Current: " + rs775.getCurrent());
            }
        }

        // We expect the two motor version to move faster than the single motor
        // version.
        assertEquals(rs775.getCurrent(), 17.378, 1E-3);
        assertEquals(rs775.getVelocity(), 122.517, 1E-1);
    }
    @Test
    public void testDoubleRS775_80Efficiency_12VLargeLoad()
    {
        StaticLoadDcMotorSim rs775 = new StaticLoadDcMotorSim(getSingle775WithTransmission(2, .8), 1.0);

        // Make it less efficient.
        System.out.println("(2 motors, 80% efficient) Voltage=12V, Load=1.0 kg*m^2");
        for (int i = 0; i < 1000; ++i)
        {
            rs775.setVoltagePercentage(1);
            rs775.update(0.01);

            if (i % 100 == 0)
            {
                System.out.print("Time: " + 0.01 * i);
                System.out.print(", Position: " + rs775.getPosition());
                System.out.print(", Velocity: " + rs775.getVelocity());
                System.out.println(", Current: " + rs775.getCurrent());
            }
        }

        assertEquals(rs775.getCurrent(), 27.540, 1E-3);
        assertEquals(rs775.getVelocity(), 114.545, 1E-1);
        // We expect the less efficient version to be slower.
        // assert (rs775.getVelocity() + EPS < final_velocity);
        // assert (rs775.getPosition() + EPS < final_position);
    }

    @Test
    public void testRS775_Neg12VSmallLoad()
    {
        StaticLoadDcMotorSim rs775 = new StaticLoadDcMotorSim(getSingle775WithTransmission(1, 1.0), 1.0);

        // Go in reverse.
        System.out.println("Voltage=-12V, Load=1.0 kg*m^2");
        for (int i = 0; i < 1000; ++i)
        {
            rs775.setVoltagePercentage(-1);
            rs775.update(0.01);

            if (i % 100 == 0)
            {
                System.out.print("Time: " + 0.01 * i);
                System.out.print(", Position: " + rs775.getPosition());
                System.out.print(", Velocity: " + rs775.getVelocity());
                System.out.println(", Current: " + rs775.getCurrent());
            }
        }
        assertEquals(rs775.getCurrent(), 48.758, 1E-3);
        assertEquals(rs775.getVelocity(), -59.590, 1E-1);

    }
}
