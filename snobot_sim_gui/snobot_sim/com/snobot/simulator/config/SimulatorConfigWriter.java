package com.snobot.simulator.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.snobot.simulator.DcMotorModelConfig;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.simulator.wrapper_accessors.SpeedControllerWrapperAccessor.MotorSimType;

public class SimulatorConfigWriter
{
    public boolean writeConfig(String aOutFile)
    {
        boolean success = false;

        try
        {
            File file = new File(aOutFile);
            System.out.println("Writing to " + file.getAbsolutePath());

            Map<String, Object> output = dumpConfig();

            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

            Yaml yaml = new Yaml(options);
            yaml.dump(output, new FileWriter(file));

            success = true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return success;
    }

    protected Map<String, Object> dumpConfig()
    {
        Map<String, Object> output = new LinkedHashMap<>();

        dumpSpeedControllers(output);
        dumpEncoders(output);
        dumpRelays(output);
        dumpSolenoids(output);
        dumpDigital(output);


        return output;
    }


    protected void dumpDigital(Map<String, Object> aOutMap)
    {
        List<Integer> digital = DataAccessorFactory.getInstance().getDigitalAccessor().getPortList();

        if (!digital.isEmpty())
        {
            List<Object> listConfig = new ArrayList<>();
            for (int handle : digital)
            {
                Map<String, Object> singleConfig = new LinkedHashMap<>();
                singleConfig.put("name", DataAccessorFactory.getInstance().getDigitalAccessor().getName(handle));
                singleConfig.put("handle", handle);

                listConfig.add(singleConfig);
            }
            aOutMap.put("digital", listConfig);
        }
    }

    protected void dumpRelays(Map<String, Object> aOutMap)
    {
        List<Integer> relays = DataAccessorFactory.getInstance().getRelayAccessor().getPortList();

        if (!relays.isEmpty())
        {
            List<Object> listConfig = new ArrayList<>();
            for (int handle : relays)
            {
                Map<String, Object> singleConfig = new LinkedHashMap<>();
                singleConfig.put("name", DataAccessorFactory.getInstance().getRelayAccessor().getName(handle));
                singleConfig.put("handle", handle);

                listConfig.add(singleConfig);
            }
            aOutMap.put("relays", listConfig);
        }
    }

    protected void dumpSolenoids(Map<String, Object> aOutMap)
    {
        List<Integer> solenoids = DataAccessorFactory.getInstance().getSolenoidAccessor().getPortList();

        if (!solenoids.isEmpty())
        {
            List<Object> listConfig = new ArrayList<>();
            for (int handle : solenoids)
            {
                Map<String, Object> singleConfig = new LinkedHashMap<>();
                singleConfig.put("name", DataAccessorFactory.getInstance().getSolenoidAccessor().getName(handle));
                singleConfig.put("handle", handle);

                listConfig.add(singleConfig);
            }
            aOutMap.put("solenoids", listConfig);
        }
    }

    protected void dumpSpeedControllers(Map<String, Object> aOutMap)
    {
        List<Integer> speedControllers = DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList();

        if (!speedControllers.isEmpty())
        {
            List<Object> listConfig = new ArrayList<>();
            for (int handle : speedControllers)
            {
                Map<String, Object> singleConfig = new LinkedHashMap<>();
                singleConfig.put("name", DataAccessorFactory.getInstance().getSpeedControllerAccessor().getName(handle));
                singleConfig.put("handle", handle);

                listConfig.add(singleConfig);
                dumpMotorSim(singleConfig, handle);
            }
            aOutMap.put("speed_controllers", listConfig);
        }
    }

    protected void dumpEncoders(Map<String, Object> aOutMap)
    {
        List<Integer> encoders = DataAccessorFactory.getInstance().getEncoderAccessor().getPortList();

        if (!encoders.isEmpty())
        {
            List<Object> listConfig = new ArrayList<>();
            for (int handle : encoders)
            {
                Map<String, Object> singleConfig = new LinkedHashMap<>();
                singleConfig.put("name", DataAccessorFactory.getInstance().getEncoderAccessor().getName(handle));
                singleConfig.put("single_handle", handle);

                if (DataAccessorFactory.getInstance().getEncoderAccessor().isHookedUp(handle))
                {
                    singleConfig.put("speed_controller_handle", DataAccessorFactory.getInstance().getEncoderAccessor().getHookedUpId(handle));
                }

                listConfig.add(singleConfig);
            }
            aOutMap.put("quad_encoders", listConfig);
        }
    }

    private void dumpMotorSim(Map<String, Object> aScConfig, int aHandle)
    {
        MotorSimType simType = DataAccessorFactory.getInstance().getSpeedControllerAccessor().getMotorSimType(aHandle);
        switch (simType)
        {
        case Simple:
            dumpMotorSimSimple(aScConfig, aHandle);
            break;
        case StaticLoad:
            dumpMotorSimStaticLoad(aScConfig, aHandle);
            break;
        case GravitationalLoad:
            dumpMotorSimGravitationalLoad(aScConfig, aHandle);
            break;
        case RotationalLoad:
            dumpMotorSimRotationalLoad(aScConfig, aHandle);
            break;

        case None:
        default:
            break;

        }
    }

    private void dumpMotorSimSimple(Map<String, Object> aScConfig, int aHandle)
    {
        Map<String, Object> motorSim = new LinkedHashMap<>();
        motorSim.put("type", "Simple");
        motorSim.put("max_speed", DataAccessorFactory.getInstance().getSpeedControllerAccessor().getMotorSimSimpleModelConfig(aHandle));

        aScConfig.put("motor_sim", motorSim);
    }

    private void dumpMotorSimStaticLoad(Map<String, Object> aScConfig, int aHandle)
    {
        Map<String, Object> motorSim = new LinkedHashMap<>();
        motorSim.put("type", "StaticLoad");
        motorSim.put("load", DataAccessorFactory.getInstance().getSpeedControllerAccessor().getMotorSimStaticModelConfig(aHandle));
        motorSim.put("conversion_factor", 1);

        DcMotorModelConfig modelConfig = DataAccessorFactory.getInstance().getSpeedControllerAccessor().getMotorConfig(aHandle);
        if (modelConfig != null)
        {
            motorSim.put("motor_model", dumpDcMotorModelConfig(modelConfig));
        }

        aScConfig.put("motor_sim", motorSim);
    }

    private void dumpMotorSimGravitationalLoad(Map<String, Object> aScConfig, int aHandle)
    {

    }

    private void dumpMotorSimRotationalLoad(Map<String, Object> aScConfig, int aHandle)
    {

    }

    private Map<String, Object> dumpDcMotorModelConfig(DcMotorModelConfig aConfig)
    {
        Map<String, Object> output = new LinkedHashMap<>();

        output.put("motor_type", aConfig.mFactoryParams.mMotorType);
        output.put("inverted", aConfig.mInverted);
        output.put("has_brake", aConfig.mHasBrake);

        Map<String, Object> transmissionConfig = new LinkedHashMap<>();
        transmissionConfig.put("num_motors", aConfig.mFactoryParams.mNumMotors);
        transmissionConfig.put("gear_reduction", aConfig.mFactoryParams.mGearReduction);
        transmissionConfig.put("efficiency", aConfig.mFactoryParams.mGearboxEfficiency);
        output.put("transmission", transmissionConfig);

        return output;
    }
}
