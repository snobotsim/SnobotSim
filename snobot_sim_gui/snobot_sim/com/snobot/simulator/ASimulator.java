package com.snobot.simulator;

import com.snobot.simulator.config.SimulatorConfigReader;
import com.snobot.simulator.robot_container.IRobotClassContainer;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;

public class ASimulator implements ISimulatorUpdater
{
    private static final Object sUPDATE_MUTEX = new Object();

    private static final double sMOTOR_UPDATE_FREQUENCY = .02;

    protected ASimulator()
    {
        updateMotorsThread.start();
    }

    protected void createSimulatorComponents(String aConfigFile)
    {
        new SimulatorConfigReader().loadConfig(aConfigFile);
    }

    @Override
    public void update()
    {

    }

    @Override
    public void setRobot(IRobotClassContainer aRobot)
    {

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
                    DataAccessorFactory.getInstance().getSimulatorDataAccessor().updateSimulatorComponents(sMOTOR_UPDATE_FREQUENCY);
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
