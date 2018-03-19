package com.snobot.simulator.joysticks.joystick_specializations;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.simulator.joysticks.IMockJoystick;
import com.snobot.test.utilities.MockController;

import net.java.games.input.Component.Identifier;

public class TestKeyboardJoystick
{
    @Test
    public void testKeyboardJoystick() throws Exception
    {
        MockController controller = new MockController();
        KeyboardJoystick joystick = new KeyboardJoystick(controller);

        short[] povValues = joystick.getPovValues();
        Assert.assertEquals(-1, povValues[0]);

        testAxis(joystick, 0, 0, 1, 1, 0, 0);

        // Test Axis
        reset(controller);
        controller.setValueForComponent(Identifier.Key.W, 1);
        testAxis(joystick, 0, -1, 1, 1, 0, 0);

        reset(controller);
        controller.setValueForComponent(Identifier.Key.S, 1);
        testAxis(joystick, 0, 1, 1, 1, 0, 0);

        reset(controller);
        controller.setValueForComponent(Identifier.Key.A, 1);
        testAxis(joystick, -1, 0, 1, 1, 0, 0);

        reset(controller);
        controller.setValueForComponent(Identifier.Key.D, 1);
        testAxis(joystick, 1, 0, 1, 1, 0, 0);

        reset(controller);
        controller.setValueForComponent(Identifier.Key.I, 1);
        testAxis(joystick, 0, 0, 1, 1, 0, -1);

        reset(controller);
        controller.setValueForComponent(Identifier.Key.K, 1);
        testAxis(joystick, 0, 0, 1, 1, 0, 1);

        reset(controller);
        controller.setValueForComponent(Identifier.Key.J, 1);
        testAxis(joystick, 0, 0, 1, 1, -1, 0);

        reset(controller);
        controller.setValueForComponent(Identifier.Key.L, 1);
        testAxis(joystick, 0, 0, 1, 1, 1, 0);

        reset(controller);
        controller.setValueForComponent(Identifier.Key.C, 1);
        testAxis(joystick, 0, 0, -1, 1, 0, 0);

        reset(controller);
        controller.setValueForComponent(Identifier.Key.N, 1);
        testAxis(joystick, 0, 0, 1, -1, 0, 0);

        // Set POV
        reset(controller);
        testPovs(joystick, -1);

        reset(controller);
        controller.setValueForComponent(Identifier.Key.UP, 1);
        testPovs(joystick, 0);

        reset(controller);
        controller.setValueForComponent(Identifier.Key.UP, 1);
        controller.setValueForComponent(Identifier.Key.RIGHT, 1);
        testPovs(joystick, 45);

        reset(controller);
        controller.setValueForComponent(Identifier.Key.RIGHT, 1);
        testPovs(joystick, 90);

        reset(controller);
        controller.setValueForComponent(Identifier.Key.RIGHT, 1);
        controller.setValueForComponent(Identifier.Key.DOWN, 1);
        testPovs(joystick, 135);

        reset(controller);
        controller.setValueForComponent(Identifier.Key.DOWN, 1);
        testPovs(joystick, 180);

        reset(controller);
        controller.setValueForComponent(Identifier.Key.DOWN, 1);
        controller.setValueForComponent(Identifier.Key.LEFT, 1);
        testPovs(joystick, 225);

        reset(controller);
        controller.setValueForComponent(Identifier.Key.LEFT, 1);
        testPovs(joystick, 270);

        reset(controller);
        controller.setValueForComponent(Identifier.Key.UP, 1);
        controller.setValueForComponent(Identifier.Key.LEFT, 1);
        testPovs(joystick, -45);
    }

    private void reset(MockController controller)
    {
        controller.setValueForComponent(Identifier.Key.W, 0);
        controller.setValueForComponent(Identifier.Key.A, 0);
        controller.setValueForComponent(Identifier.Key.S, 0);
        controller.setValueForComponent(Identifier.Key.D, 0);

        controller.setValueForComponent(Identifier.Key.I, 0);
        controller.setValueForComponent(Identifier.Key.J, 0);
        controller.setValueForComponent(Identifier.Key.K, 0);
        controller.setValueForComponent(Identifier.Key.L, 0);

        controller.setValueForComponent(Identifier.Key.C, 0);
        controller.setValueForComponent(Identifier.Key.N, 0);

        controller.setValueForComponent(Identifier.Key.UP, 0);
        controller.setValueForComponent(Identifier.Key.DOWN, 0);
        controller.setValueForComponent(Identifier.Key.LEFT, 0);
        controller.setValueForComponent(Identifier.Key.RIGHT, 0);
    }

    private void testAxis(IMockJoystick joystick, double a0, double a1, double a2, double a3, double a4, double a5)
    {
        float[] axisValues = joystick.getAxisValues();

        Assert.assertEquals(a0, axisValues[0], .0001);
        Assert.assertEquals(a1, axisValues[1], .0001);
        Assert.assertEquals(a2, axisValues[2], .0001);
        Assert.assertEquals(a3, axisValues[3], .0001);
        Assert.assertEquals(a4, axisValues[4], .0001);
        Assert.assertEquals(a5, axisValues[5], .0001);
    }

    private void testPovs(IMockJoystick joystick, int pov0)
    {
        joystick.getController().poll();
        short[] povValues = joystick.getPovValues();

        Assert.assertEquals(pov0, povValues[0]);
    }
}
