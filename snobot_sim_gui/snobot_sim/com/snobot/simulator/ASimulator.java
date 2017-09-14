package com.snobot.simulator;

import java.util.List;
import java.util.Map.Entry;

import com.snobot.simulator.config.BasicModuleConfig;
import com.snobot.simulator.config.EncoderConfig;
import com.snobot.simulator.config.PwmConfig;
import com.snobot.simulator.config.SimulatorConfig;
import com.snobot.simulator.config.SimulatorConfigReader;
import com.snobot.simulator.motor_sim.DcMotorModelConfig;
import com.snobot.simulator.motor_sim.GravityLoadMotorSimulationConfig;
import com.snobot.simulator.motor_sim.IMotorSimulatorConfig;
import com.snobot.simulator.motor_sim.RotationalLoadMotorSimulationConfig;
import com.snobot.simulator.motor_sim.SimpleMotorSimulationConfig;
import com.snobot.simulator.motor_sim.StaticLoadMotorSimulationConfig;
import com.snobot.simulator.robot_container.IRobotClassContainer;
import com.snobot.simulator.simulator_components.TankDriveGyroSimulator;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.simulator.wrapper_accessors.EncoderWrapperAccessor;
import com.snobot.simulator.wrapper_accessors.IBasicSensorActuatorWrapperAccessor;
import com.snobot.simulator.wrapper_accessors.SimulatorDataAccessor;
import com.snobot.simulator.wrapper_accessors.SpeedControllerWrapperAccessor;

public class ASimulator implements ISimulatorUpdater
{
    private static final Object sUPDATE_MUTEX = new Object();

    private static final double sMOTOR_UPDATE_FREQUENCY = .02;

    private SimulatorConfig mConfig;

    protected ASimulator()
    {
        mConfig = null;
        updateMotorsThread.start();
    }

    public boolean loadConfig(String aConfigFile)
    {
        SimulatorConfigReader configReader = new SimulatorConfigReader();
        boolean success = configReader.loadConfig(aConfigFile);

        mConfig = configReader.getConfig();

        if (mConfig != null)
        {
            for (Entry<Integer, String> pair : mConfig.getmDefaultI2CWrappers().entrySet())
            {
                DataAccessorFactory.getInstance().getSimulatorDataAccessor().setDefaultI2CSimulator(pair.getKey(), pair.getValue());
            }
            for (Entry<Integer, String> pair : mConfig.getmDefaultSpiWrappers().entrySet())
            {
                DataAccessorFactory.getInstance().getSimulatorDataAccessor().setDefaultSpiSimulator(pair.getKey(), pair.getValue());
            }
        }

        return success;
    }

    protected void createSimulatorComponents()
    {
        if (mConfig != null)
        {
            createBasicSimulatorComponents(DataAccessorFactory.getInstance().getDigitalAccessor(), mConfig.getmDigitalIO());
            createBasicSimulatorComponents(DataAccessorFactory.getInstance().getAnalogAccessor(), mConfig.getmAnalogIO());
            createBasicSimulatorComponents(DataAccessorFactory.getInstance().getRelayAccessor(), mConfig.getmRelays());
            createBasicSimulatorComponents(DataAccessorFactory.getInstance().getSolenoidAccessor(), mConfig.getmSolenoids());
            createEncoders(DataAccessorFactory.getInstance().getEncoderAccessor(), mConfig.getmEncoders());
            createPwms(DataAccessorFactory.getInstance().getSpeedControllerAccessor(), mConfig.getmPwm());

            for (Object obj : mConfig.getmSimulatorComponents())
            {
                setupSimulatorComponents(obj);
            }
        }
    }

    protected void createBasicSimulatorComponents(IBasicSensorActuatorWrapperAccessor accessor, List<? extends BasicModuleConfig> inputList)
    {
        for (BasicModuleConfig config : inputList)
        {
            int handle = config.getmHandle();
            if (handle != -1 && config.getmName() != null)
            {
                accessor.setName(handle, config.getmName());
            }
        }
    }

    protected void createEncoders(EncoderWrapperAccessor accessor, List<EncoderConfig> inputList)
    {
        for (EncoderConfig config : inputList)
        {
            int handle = config.getmHandle();
            if(handle != -1)
            {
                if (config.getmName() != null)
                {
                    accessor.setName(handle, config.getmName());
                }
                if (config.getmConnectedSpeedControllerHandle() != -1)
                {
                    accessor.connectSpeedController(handle, config.getmConnectedSpeedControllerHandle());
                }
            }
        }
    }

    protected void createPwms(SpeedControllerWrapperAccessor accessor, List<PwmConfig> inputList)
    {
        for (PwmConfig config : inputList)
        {
            int handle = config.getmHandle();
            if (handle != -1)
            {
                if (config.getmName() != null)
                {
                    accessor.setName(handle, config.getmName());
                }

                setupMotorSimulator(config, handle);
            }
        }
    }

    protected void setupMotorSimulator(PwmConfig aPwmConfig, int aPwmHandle)
    {
        SimulatorDataAccessor simulatorAccessor = DataAccessorFactory.getInstance().getSimulatorDataAccessor();
        IMotorSimulatorConfig baseMotorConfig = aPwmConfig.getmMotorSimConfig();
        if (baseMotorConfig != null)
        {
            if (baseMotorConfig instanceof SimpleMotorSimulationConfig)
            {
                simulatorAccessor.setSpeedControllerModel_Simple(aPwmHandle, (SimpleMotorSimulationConfig) baseMotorConfig);
            }
            else if (baseMotorConfig instanceof StaticLoadMotorSimulationConfig)
            {
                DcMotorModelConfig motorConfig = simulatorAccessor.createMotor("");
                simulatorAccessor.setSpeedControllerModel_Static(aPwmHandle, motorConfig, (StaticLoadMotorSimulationConfig) baseMotorConfig);
            }
            else if (baseMotorConfig instanceof GravityLoadMotorSimulationConfig)
            {
                DcMotorModelConfig motorConfig = simulatorAccessor.createMotor("");
                simulatorAccessor.setSpeedControllerModel_Gravitational(aPwmHandle, motorConfig, (GravityLoadMotorSimulationConfig) baseMotorConfig);
            }
            else if (baseMotorConfig instanceof RotationalLoadMotorSimulationConfig)
            {
                DcMotorModelConfig motorConfig = simulatorAccessor.createMotor("");
                simulatorAccessor.setSpeedControllerModel_Rotational(aPwmHandle, motorConfig, (RotationalLoadMotorSimulationConfig) baseMotorConfig);
            }
        }
    }

    protected void setupSimulatorComponents(Object aConfig)
    {
        if(aConfig instanceof TankDriveGyroSimulator.TankDriveConfig)
        {
            TankDriveGyroSimulator.TankDriveConfig config = (TankDriveGyroSimulator.TankDriveConfig) aConfig;
            DataAccessorFactory.getInstance().getSimulatorDataAccessor().connectTankDriveSimulator(config.getmLeftEncoderHandle(),
                    config.getmRightEncoderHandle(), config.getmGyroHandle(), config.getmTurnKp());
        }
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
