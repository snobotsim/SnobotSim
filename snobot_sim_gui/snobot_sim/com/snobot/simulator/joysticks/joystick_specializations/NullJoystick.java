package com.snobot.simulator.joysticks.joystick_specializations;

import com.snobot.simulator.joysticks.IMockJoystick;

import net.java.games.input.Controller;

public class NullJoystick implements IMockJoystick
{
    public static final String sNAME = "Null Joystick";

    private static final int sNUM_BUTTONS = 10;
    private static final int sNUM_AXIS = 6;
    private static final int sNUM_POV = 1;

    private float[] mAxis;
    private short[] mPov;

    public NullJoystick()
    {
        mAxis = new float[sNUM_AXIS];
        mPov = new short[sNUM_POV];
        for (int i = 0; i < mPov.length; ++i)
        {
            mPov[i] = -1;
        }
    }

    @Override
    public int getAxisCount()
    {
        return sNUM_AXIS;
    }

    @Override
    public int getButtonCount()
    {
        return sNUM_BUTTONS;
    }

    @Override
    public void setRumble(short s)
    {
    }

    @Override
    public float[] getAxisValues()
    {
        return mAxis;
    }

    @Override
    public short[] getPovValues()
    {
        return mPov;
    }

    @Override
    public int getButtonMask()
    {
        return 0;
    }

    @Override
    public boolean getRawButton(int aIndex)
    {
        return false;
    }

    @Override
    public double getRawAxis(int aIndex)
    {
        return 0;
    }

    @Override
    public String getName()
    {
        return sNAME;
    }

    @Override
    public Controller getController()
    {
        return null;
    }

}
