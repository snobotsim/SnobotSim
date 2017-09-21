package com.snobot.simulator.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.snobot.simulator.motor_sim.IMotorSimulatorConfig;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.simulator.wrapper_accessors.EncoderWrapperAccessor;
import com.snobot.simulator.wrapper_accessors.IBasicSensorActuatorWrapperAccessor;
import com.snobot.simulator.wrapper_accessors.SimulatorDataAccessor;
import com.snobot.simulator.wrapper_accessors.SpeedControllerWrapperAccessor;
import com.snobot.simulator.wrapper_accessors.SpeedControllerWrapperAccessor.MotorSimType;

public class SimulatorConfigWriter
{
    private static final Logger sLOGGER = Logger.getLogger(SimulatorConfigWriter.class);

    public boolean writeConfig(String aOutFile)
    {
        boolean success = false;

        try
        {
            File file = new File(aOutFile);
            sLOGGER.log(Level.INFO, "Writing to " + file.getAbsolutePath());

            Object output = dumpConfig();

            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

            Yaml yaml = new Yaml(options);
            yaml.dump(output, new FileWriter(file));

            success = true;
        }
        catch (IOException e)
        {
            sLOGGER.log(Level.ERROR, e);
        }

        return success;
    }

    protected SimulatorConfig dumpConfig()
    {
        // Map<String, Object> output = new LinkedHashMap<>();

        SimulatorConfig output = new SimulatorConfig();

        dumpBasicConfig(DataAccessorFactory.getInstance().getAccelerometerAccessor(), output.getmAccelerometers());
        dumpBasicConfig(DataAccessorFactory.getInstance().getAnalogAccessor(), output.getmAnalogIO());
        dumpBasicConfig(DataAccessorFactory.getInstance().getDigitalAccessor(), output.getmDigitalIO());
        dumpBasicConfig(DataAccessorFactory.getInstance().getGyroAccessor(), output.getmGyros());
        dumpBasicConfig(DataAccessorFactory.getInstance().getRelayAccessor(), output.getmRelays());
        dumpBasicConfig(DataAccessorFactory.getInstance().getSolenoidAccessor(), output.getmSolenoids());

        dumpEncoderConfig(DataAccessorFactory.getInstance().getEncoderAccessor(), output.getmEncoders());
        dumpPwmConfig(DataAccessorFactory.getInstance().getSpeedControllerAccessor(), output.getmPwm());
        dumpSimulatorComponents(DataAccessorFactory.getInstance().getSimulatorDataAccessor(), output.getmSimulatorComponents());

        output.setmDefaultI2CWrappers(DataAccessorFactory.getInstance().getSimulatorDataAccessor().getDefaultI2CWrappers());
        output.setmDefaultSpiWrappers(DataAccessorFactory.getInstance().getSimulatorDataAccessor().getDefaultSpiWrappers());

        return output;
    }

    protected void dumpBasicConfig(IBasicSensorActuatorWrapperAccessor accessor, List<BasicModuleConfig> outputList)
    {
        for (int portHandle : accessor.getPortList())
        {
            BasicModuleConfig config = new BasicModuleConfig(portHandle, accessor.getName(portHandle));
            outputList.add(config);
        }
    }

    protected void dumpEncoderConfig(EncoderWrapperAccessor accessor, List<EncoderConfig> outputList)
    {
        for (int portHandle : accessor.getPortList())
        {
            EncoderConfig config = new EncoderConfig(portHandle, accessor.getName(portHandle), accessor.getHookedUpId(portHandle));
            outputList.add(config);
        }
    }

    protected void dumpPwmConfig(SpeedControllerWrapperAccessor accessor, List<PwmConfig> outputList)
    {
        for (int portHandle : accessor.getPortList())
        {
            PwmConfig config = new PwmConfig(portHandle, accessor.getName(portHandle), getMotorSimConfig(accessor, portHandle));
            outputList.add(config);
        }
    }

    protected void dumpSimulatorComponents(SimulatorDataAccessor accessor, List<Object> outputList)
    {
        for (Object config : accessor.getSimulatorComponentConfigs())
        {
            outputList.add(config);
        }
    }

    protected IMotorSimulatorConfig getMotorSimConfig(SpeedControllerWrapperAccessor accessor, int aPortHandle)
    {
        MotorSimType simType = accessor.getMotorSimType(aPortHandle);
        switch (simType)
        {
        case Simple:
            return accessor.getMotorSimSimpleModelConfig(aPortHandle);
        case StaticLoad:
            return accessor.getMotorSimStaticModelConfig(aPortHandle);
        case GravitationalLoad:
            return accessor.getMotorSimGravitationalModelConfig(aPortHandle);
        case RotationalLoad:
            return accessor.getMotorSimRotationalModelConfig(aPortHandle);

        case None:
        default:
            break;
        }
        return null;
    }

    // protected void dumpSpeedControllers(Map<String, Object> aOutMap)
    // {
    // List<Integer> speedControllers =
    // DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList();
    //
    // if (!speedControllers.isEmpty())
    // {
    // List<Object> listConfig = new ArrayList<>();
    // for (int handle : speedControllers)
    // {
    // Map<String, Object> singleConfig = new LinkedHashMap<>();
    // singleConfig.put("name",
    // DataAccessorFactory.getInstance().getSpeedControllerAccessor().getName(handle));
    // singleConfig.put("handle", handle);
    //
    // listConfig.add(singleConfig);
    // dumpMotorSim(singleConfig, handle);
    // }
    // aOutMap.put("speed_controllers", listConfig);
    // }
    // }
    //
    // private void dumpMotorSim(Map<String, Object> aScConfig, int aHandle)
    // {
    // MotorSimType simType =
    // DataAccessorFactory.getInstance().getSpeedControllerAccessor().getMotorSimType(aHandle);
    // switch (simType)
    // {
    // case Simple:
    // dumpMotorSimSimple(aScConfig, aHandle);
    // break;
    // case StaticLoad:
    // dumpMotorSimStaticLoad(aScConfig, aHandle);
    // break;
    // case GravitationalLoad:
    // dumpMotorSimGravitationalLoad(aScConfig, aHandle);
    // break;
    // case RotationalLoad:
    // dumpMotorSimRotationalLoad(aScConfig, aHandle);
    // break;
    //
    // case None:
    // default:
    // break;
    //
    // }
    // }
    //
    // private void dumpMotorSimSimple(Map<String, Object> aScConfig, int
    // aHandle)
    // {
    // Map<String, Object> motorSim = new LinkedHashMap<>();
    // motorSim.put("type", "Simple");
    // motorSim.put("max_speed",
    // DataAccessorFactory.getInstance().getSpeedControllerAccessor().getMotorSimSimpleModelConfig(aHandle));
    //
    // aScConfig.put("motor_sim", motorSim);
    // }
    //
    // private void dumpMotorSimStaticLoad(Map<String, Object> aScConfig, int
    // aHandle)
    // {
    // Map<String, Object> motorSim = new LinkedHashMap<>();
    // motorSim.put("type", "StaticLoad");
    // motorSim.put("load",
    // DataAccessorFactory.getInstance().getSpeedControllerAccessor().getMotorSimStaticModelConfig(aHandle));
    // motorSim.put("conversion_factor", 1);
    //
    // DcMotorModelConfig modelConfig =
    // DataAccessorFactory.getInstance().getSpeedControllerAccessor().getMotorConfig(aHandle);
    // if (modelConfig != null)
    // {
    // motorSim.put("motor_model", dumpDcMotorModelConfig(modelConfig));
    // }
    //
    // aScConfig.put("motor_sim", motorSim);
    // }
    //
    // private void dumpMotorSimGravitationalLoad(Map<String, Object> aScConfig,
    // int aHandle)
    // {
    //
    // }
    //
    // private void dumpMotorSimRotationalLoad(Map<String, Object> aScConfig,
    // int aHandle)
    // {
    //
    // }
    //
    // private Map<String, Object> dumpDcMotorModelConfig(DcMotorModelConfig
    // aConfig)
    // {
    // Map<String, Object> output = new LinkedHashMap<>();
    //
    // output.put("motor_type", aConfig.mFactoryParams.mMotorType);
    // output.put("inverted", aConfig.mInverted);
    // output.put("has_brake", aConfig.mHasBrake);
    //
    // Map<String, Object> transmissionConfig = new LinkedHashMap<>();
    // transmissionConfig.put("num_motors", aConfig.mFactoryParams.mNumMotors);
    // transmissionConfig.put("gear_reduction",
    // aConfig.mFactoryParams.mGearReduction);
    // transmissionConfig.put("efficiency",
    // aConfig.mFactoryParams.mGearboxEfficiency);
    // output.put("transmission", transmissionConfig);
    //
    // return output;
    // }
}
