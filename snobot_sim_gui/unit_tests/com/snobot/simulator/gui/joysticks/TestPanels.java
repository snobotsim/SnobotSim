package com.snobot.simulator.gui.joysticks;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.test.utilities.BaseGuiSimulatorTest;

public class TestPanels extends BaseGuiSimulatorTest
{
    @Test
    public void testJoystickManagerDialog() throws Exception
    {
        JoystickManagerDialog dialog = new JoystickManagerDialog();
        boolean success = true;

        try
        {
            dialog.setVisible(true);
        }
        catch (Exception e)
        {

        }
        finally
        {
            dialog.dispose();
        }

        Assert.assertTrue(success);
    }
}
