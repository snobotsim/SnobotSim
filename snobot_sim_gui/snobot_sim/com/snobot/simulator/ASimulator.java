package com.snobot.simulator;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.snobot.simulator.config.SimulatorConfigReader;
import com.snobot.simulator.robot_container.IRobotClassContainer;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;

/**
 * Base class for a custom simulator.
 * 
 * @author PJ
 *
 */
public class ASimulator implements ISimulatorUpdater
{
    private static final Logger sLOGGER = Logger.getLogger(ASimulator.class);
    private static final Object sUPDATE_MUTEX = new Object();

    private static final double sMOTOR_UPDATE_FREQUENCY = .02;

    private final SimulatorConfigReader mConfigReader;
    private boolean mRunning;

    protected ASimulator()
    {
        mConfigReader = new SimulatorConfigReader();
    }

    public boolean loadConfig(String aConfigFile)
    {
        boolean success = mConfigReader.loadConfig(aConfigFile);

        return success;
    }


    @Override
    public void update()
    {

    }

    @Override
    public void setRobot(IRobotClassContainer aRobot)
    {
        mRunning = true;
        mUpdateMotorsThread.start();
    }

    protected final Thread mUpdateMotorsThread = new Thread(new Runnable()
    {

        @Override
        public void run()
        {
            while (mRunning)
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
                    sLOGGER.log(Level.ERROR, e);
                }
            }
        }
    }, "MotorUpdater");

    public void shutdown()
    {
        mRunning = false;
        try
        {
            mUpdateMotorsThread.join();
        }
        catch (InterruptedException e)
        {
            sLOGGER.log(Level.ERROR, e);
        }
    }

    public void createSimulatorComponents()
    {
        mConfigReader.setupSimulatorComponents();
    }
}
