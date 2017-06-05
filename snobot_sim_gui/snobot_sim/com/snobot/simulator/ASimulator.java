package com.snobot.simulator;

import java.util.ArrayList;
import java.util.List;

import com.snobot.simulator.jni.module_wrapper.SpeedControllerWrapperJni;
import com.snobot.simulator.robot_container.IRobotClassContainer;

public class ASimulator implements ISimulatorUpdater
{
    private static final Object sUPDATE_MUTEX = new Object();

    private static final double sMOTOR_UPDATE_FREQUENCY = .02;

    protected List<ISimulatorUpdater> mSimulatorComponenets;

    protected ASimulator()
    {
        mSimulatorComponenets = new ArrayList<>();
        updateMotorsThread.start();
    }

    protected void createSimulatorComponents(String aConfigFile)
    {
        new SimulatorConfigReader().loadConfig(aConfigFile);
    }

    @Override
    public void update()
    {
        synchronized (sUPDATE_MUTEX)
        {
            for (ISimulatorUpdater simulator : mSimulatorComponenets)
            {
                simulator.update();
            }
        }
    }

    @Override
    public void setRobot(IRobotClassContainer aRobot)
    {
        for (ISimulatorUpdater simulator : mSimulatorComponenets)
        {
            simulator.setRobot(aRobot);
        }
    }

    protected Thread updateMotorsThread = new Thread(new Runnable()
    {

        @Override
        public void run()
        {
            while (true)
            {
                synchronized (sUPDATE_MUTEX)
                {
                    SpeedControllerWrapperJni.updateAllSpeedControllers(sMOTOR_UPDATE_FREQUENCY);
                }

                try
                {
                    Thread.sleep((long) (sMOTOR_UPDATE_FREQUENCY * 1000));
                }
                catch (InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }, "MotorUpdater");
}
