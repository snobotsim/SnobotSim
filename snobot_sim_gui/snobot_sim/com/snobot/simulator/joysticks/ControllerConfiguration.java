package com.snobot.simulator.joysticks;

import net.java.games.input.Controller;

public class ControllerConfiguration
{
    public Controller mController;
    public Class<? extends IMockJoystick> mSpecialization;

    public ControllerConfiguration(Controller controller, Class<? extends IMockJoystick> specializationClass)
    {
        mController = controller;
        mSpecialization = specializationClass;
    }
}