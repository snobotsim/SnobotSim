// package com.snobot.simulator.motor_sim;
//
// import static org.junit.Assert.assertEquals;
//
// import org.junit.Test;
//
// import com.snobot.simulator.DcMotorModelConfig;
// import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
// import com.snobot.test.utilities.BaseSimulatorTest;
//
// import edu.wpi.first.wpilibj.SpeedController;
// import edu.wpi.first.wpilibj.Talon;
//
// public class StaticLoadDcMotorSimTest2 extends BaseSimulatorTest
// {
//
// @Test
// public void testRS775_6VSmallLoad()
// {
//
// SpeedController rs775 = new Talon(0);
// DcMotorModelConfig motorConfig =
// DataAccessorFactory.getInstance().getSimulatorDataAccessor().createMotor("rs775");
// DataAccessorFactory.getInstance().getSimulatorDataAccessor().setSpeedControllerModel_Gravitational(0,
// motorConfig, .01);
// DataAccessorFactory.getInstance().getSimulatorDataAccessor().setUpdateRate(0.01);
//
// System.out.println("Voltage=6V, Load=.01 kg*m^2");
// for (int i = 0; i < 1000; ++i)
// {
// rs775.set(.5);
// DataAccessorFactory.getInstance().getSimulatorDataAccessor().updateLoop();
//
// if (i % 100 == 0)
// {
// System.out.print("Time: " + 0.01 * i);
// System.out.print(", Position: " +
// DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPosition(0));
// System.out.print(", Velocity: " +
// DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVelocity(0));
// System.out.println(", Current: " +
// DataAccessorFactory.getInstance().getSpeedControllerAccessor().getCurrent(0));
// }
// }
// // We expect negligible final current, and a final velocity of ~68.04
// // rad/sec.
// assertEquals(DataAccessorFactory.getInstance().getSpeedControllerAccessor().getCurrent(0),
// 0.0, 1E-3);
// assertEquals(DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVelocity(0),
// 68.06, 1E-2);
//
// }
//
// @Test
// public void testRS775_12VSmallLoad()
// {
// SpeedController rs775 = new Talon(0);
// DcMotorModelConfig motorConfig =
// DataAccessorFactory.getInstance().getSimulatorDataAccessor().createMotor("rs775");
// DataAccessorFactory.getInstance().getSimulatorDataAccessor().setSpeedControllerModel_Gravitational(0,
// motorConfig, .01);
// DataAccessorFactory.getInstance().getSimulatorDataAccessor().setUpdateRate(0.01);
//
// // Apply a larger voltage.
// System.out.println("Voltage=12V, Load=.01 kg*m^2");
// for (int i = 0; i < 1000; ++i)
// {
// rs775.set(1);
// DataAccessorFactory.getInstance().getSimulatorDataAccessor().updateLoop();
//
// if (i % 100 == 0)
// {
// System.out.print("Time: " + 0.01 * i);
// System.out.print(", Position: " +
// DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPosition(0));
// System.out.print(", Velocity: " +
// DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVelocity(0));
// System.out.println(", Current: " +
// DataAccessorFactory.getInstance().getSpeedControllerAccessor().getCurrent(0));
// }
// }
//
// // We expect negligible final current, and a final velocity of ~2 *
// // 68.04 rad/sec.
// assertEquals(DataAccessorFactory.getInstance().getSpeedControllerAccessor().getCurrent(0),
// 0.0, 1E-3);
// assertEquals(DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVelocity(0),
// 68.04 * 2, 1E-1);
//
// }
//
// @Test
// public void testRS775_12VLargeLoad()
// {
// SpeedController rs775 = new Talon(0);
// DcMotorModelConfig motorConfig =
// DataAccessorFactory.getInstance().getSimulatorDataAccessor().createMotor("rs775");
// DataAccessorFactory.getInstance().getSimulatorDataAccessor().setSpeedControllerModel_Static(0,
// motorConfig, .01);
// DataAccessorFactory.getInstance().getSimulatorDataAccessor().setUpdateRate(0.01);
//
// System.out.println("Voltage=12V, Load=1.0 kg*m^2");
// for (int i = 0; i < 1000; ++i)
// {
// rs775.set(1);
// DataAccessorFactory.getInstance().getSimulatorDataAccessor().updateLoop();
//
// if (i % 100 == 0)
// {
// System.out.print("Time: " + 0.01 * i);
// System.out.print(", Position: " +
// DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPosition(0));
// System.out.print(", Velocity: " +
// DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVelocity(0));
// System.out.println(", Current: " +
// DataAccessorFactory.getInstance().getSpeedControllerAccessor().getCurrent(0));
// }
// }
//
// // This is slower, so 1000 iterations isn't enough to get to steady
// // state
// assertEquals(DataAccessorFactory.getInstance().getSpeedControllerAccessor().getCurrent(0),
// 48.758, 1E-3);
// assertEquals(DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVelocity(0),
// 59.59, 1E-1);
// }
//
// @Test
// public void testDoubleRS775_100Efficiency_12VLargeLoad()
// {
// SpeedController rs775 = new Talon(0);
// DcMotorModelConfig motorConfig =
// DataAccessorFactory.getInstance().getSimulatorDataAccessor().createMotor("rs775",
// 2, 1, 1);
// DataAccessorFactory.getInstance().getSimulatorDataAccessor().setSpeedControllerModel_Gravitational(0,
// motorConfig, .01);
// DataAccessorFactory.getInstance().getSimulatorDataAccessor().setUpdateRate(0.01);
//
// System.out.println("(2 motors) Voltage=12V, Load=1.0 kg*m^2");
// for (int i = 0; i < 1000; ++i)
// {
// rs775.set(1);
// DataAccessorFactory.getInstance().getSimulatorDataAccessor().updateLoop();
//
// if (i % 100 == 0)
// {
// System.out.print("Time: " + 0.01 * i);
// System.out.print(", Position: " +
// DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPosition(0));
// System.out.print(", Velocity: " +
// DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVelocity(0));
// System.out.println(", Current: " +
// DataAccessorFactory.getInstance().getSpeedControllerAccessor().getCurrent(0));
// }
// }
//
// // We expect the two motor version to move faster than the single motor
// // version.
// assertEquals(DataAccessorFactory.getInstance().getSpeedControllerAccessor().getCurrent(0),
// 17.378, 1E-3);
// assertEquals(DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVelocity(0),
// 122.517, 1E-1);
// }
// @Test
// public void testDoubleRS775_80Efficiency_12VLargeLoad()
// {
// SpeedController rs775 = new Talon(0);
// DcMotorModelConfig motorConfig =
// DataAccessorFactory.getInstance().getSimulatorDataAccessor().createMotor("rs775",
// 2, 1, .8);
// DataAccessorFactory.getInstance().getSimulatorDataAccessor().setSpeedControllerModel_Gravitational(0,
// motorConfig, .01);
// DataAccessorFactory.getInstance().getSimulatorDataAccessor().setUpdateRate(0.01);
//
// // Make it less efficient.
// System.out.println("(2 motors, 80% efficient) Voltage=12V, Load=1.0 kg*m^2");
// for (int i = 0; i < 1000; ++i)
// {
// rs775.set(1);
// DataAccessorFactory.getInstance().getSimulatorDataAccessor().updateLoop();
//
// if (i % 100 == 0)
// {
// System.out.print("Time: " + 0.01 * i);
// System.out.print(", Position: " +
// DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPosition(0));
// System.out.print(", Velocity: " +
// DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVelocity(0));
// System.out.println(", Current: " +
// DataAccessorFactory.getInstance().getSpeedControllerAccessor().getCurrent(0));
// }
// }
//
// assertEquals(DataAccessorFactory.getInstance().getSpeedControllerAccessor().getCurrent(0),
// 27.540, 1E-3);
// assertEquals(DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVelocity(0),
// 114.545, 1E-1);
// // We expect the less efficient version to be slower.
// // assert (rs775.getVelocity() + EPS < final_velocity);
// // assert
// //
// (DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPosition(0)
// // + EPS < final_position);
// }
//
// @Test
// public void testRS775_Neg12VSmallLoad()
// {
// SpeedController rs775 = new Talon(0);
// DcMotorModelConfig motorConfig =
// DataAccessorFactory.getInstance().getSimulatorDataAccessor().createMotor("rs775",
// 2, 1, 1);
// DataAccessorFactory.getInstance().getSimulatorDataAccessor().setSpeedControllerModel_Gravitational(0,
// motorConfig, .01);
// DataAccessorFactory.getInstance().getSimulatorDataAccessor().setUpdateRate(0.01);
//
// // Go in reverse.
// System.out.println("Voltage=-12V, Load=1.0 kg*m^2");
// for (int i = 0; i < 1000; ++i)
// {
// rs775.set(-1);
// DataAccessorFactory.getInstance().getSimulatorDataAccessor().updateLoop();
//
// if (i % 100 == 0)
// {
// System.out.print("Time: " + 0.01 * i);
// System.out.print(", Position: " +
// DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPosition(0));
// System.out.print(", Velocity: " +
// DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVelocity(0));
// System.out.println(", Current: " +
// DataAccessorFactory.getInstance().getSpeedControllerAccessor().getCurrent(0));
// }
// }
// assertEquals(DataAccessorFactory.getInstance().getSpeedControllerAccessor().getCurrent(0),
// 48.758, 1E-3);
// assertEquals(DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVelocity(0),
// -59.590, 1E-1);
//
// }
// }
