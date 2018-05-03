package com.snobot.test.utilities;

import javax.swing.JFrame;

import org.junit.After;
import org.junit.Before;

public class BaseGuiSimulatorTest extends BaseSimulatorTest
{
    private JFrame mFrame;

    protected JFrame getFrame()
    {
        if (mFrame == null)
        {
            mFrame = new JFrame("Test Frame");
        }

        return mFrame;
    }

    @Before
    public void setup()
    {
        super.setup();
    }

    @After
    public void cleanup()
    {
        super.cleanup();

        if (mFrame != null)
        {
            mFrame.dispose();
        }
    }

    protected void sleepForVisualDebugging()
    {
        sleepForVisualDebugging(1000);
    }

    protected void sleepForVisualDebugging(long aMilliseconds)
    {
        // try
        // {
        // Thread.sleep(milliseconds);
        // }
        // catch (Exception e)
        // {
        //
        // }
    }

}
