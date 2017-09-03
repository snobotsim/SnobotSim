package com.snobot.simulator.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.snobot.simulator.DcMotorModelConfig;
import com.snobot.simulator.jni.module_wrapper.DigitalSourceWrapperJni;
import com.snobot.simulator.jni.module_wrapper.EncoderWrapperJni;
import com.snobot.simulator.jni.module_wrapper.RelayWrapperJni;
import com.snobot.simulator.jni.module_wrapper.SolenoidWrapperJni;
import com.snobot.simulator.jni.module_wrapper.SpeedControllerWrapperJni;
import com.snobot.simulator.jni.module_wrapper.SpeedControllerWrapperJni.MotorSimType;

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
        List<Integer> digital = IntStream.of(DigitalSourceWrapperJni.getPortList()).boxed().collect(Collectors.toList());

        if (!digital.isEmpty())
        {
            List<Object> listConfig = new ArrayList<>();
            for (int handle : digital)
            {
                Map<String, Object> singleConfig = new LinkedHashMap<>();
                singleConfig.put("name", DigitalSourceWrapperJni.getName(handle));
                singleConfig.put("handle", handle);

                listConfig.add(singleConfig);
            }
            aOutMap.put("digital", listConfig);
        }
    }

    protected void dumpRelays(Map<String, Object> aOutMap)
    {
        List<Integer> relays = IntStream.of(RelayWrapperJni.getPortList()).boxed().collect(Collectors.toList());

        if (!relays.isEmpty())
        {
            List<Object> listConfig = new ArrayList<>();
            for (int handle : relays)
            {
                Map<String, Object> singleConfig = new LinkedHashMap<>();
                singleConfig.put("name", RelayWrapperJni.getName(handle));
                singleConfig.put("handle", handle);

                listConfig.add(singleConfig);
            }
            aOutMap.put("relays", listConfig);
        }
    }

    protected void dumpSolenoids(Map<String, Object> aOutMap)
    {
        List<Integer> solenoids = IntStream.of(SolenoidWrapperJni.getPortList()).boxed().collect(Collectors.toList());

        if (!solenoids.isEmpty())
        {
            List<Object> listConfig = new ArrayList<>();
            for (int handle : solenoids)
            {
                Map<String, Object> singleConfig = new LinkedHashMap<>();
                singleConfig.put("name", SolenoidWrapperJni.getName(handle));
                singleConfig.put("handle", handle);

                listConfig.add(singleConfig);
            }
            aOutMap.put("solenoids", listConfig);
        }
    }

    protected void dumpSpeedControllers(Map<String, Object> aOutMap)
    {
        List<Integer> speedControllers = IntStream.of(SpeedControllerWrapperJni.getPortList()).boxed().collect(Collectors.toList());

        if (!speedControllers.isEmpty())
        {
            List<Object> listConfig = new ArrayList<>();
            for (int handle : speedControllers)
            {
                Map<String, Object> singleConfig = new LinkedHashMap<>();
                singleConfig.put("name", SpeedControllerWrapperJni.getName(handle));
                singleConfig.put("handle", handle);

                listConfig.add(singleConfig);
                dumpMotorSim(singleConfig, handle);
            }
            aOutMap.put("speed_controllers", listConfig);
        }
    }

    protected void dumpEncoders(Map<String, Object> aOutMap)
    {
        List<Integer> encoders = IntStream.of(EncoderWrapperJni.getPortList()).boxed().collect(Collectors.toList());

        if (!encoders.isEmpty())
        {
            List<Object> listConfig = new ArrayList<>();
            for (int handle : encoders)
            {
                Map<String, Object> singleConfig = new LinkedHashMap<>();
                singleConfig.put("name", EncoderWrapperJni.getName(handle));
                singleConfig.put("single_handle", handle);

                if (EncoderWrapperJni.isHookedUp(handle))
                {
                    singleConfig.put("speed_controller_handle", EncoderWrapperJni.getHookedUpId(handle));
                }

                listConfig.add(singleConfig);
            }
            aOutMap.put("quad_encoders", listConfig);
        }
    }

    private void dumpMotorSim(Map<String, Object> aScConfig, int aHandle)
    {
        MotorSimType simType = SpeedControllerWrapperJni.getMotorSimType(aHandle);
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
        motorSim.put("max_speed", SpeedControllerWrapperJni.getMotorSimSimpleModelConfig(aHandle));

        aScConfig.put("motor_sim", motorSim);
    }

    private void dumpMotorSimStaticLoad(Map<String, Object> aScConfig, int aHandle)
    {
        Map<String, Object> motorSim = new LinkedHashMap<>();
        motorSim.put("type", "StaticLoad");
        motorSim.put("load", SpeedControllerWrapperJni.getMotorSimStaticModelConfig(aHandle));
        motorSim.put("conversion_factor", 1);

        DcMotorModelConfig modelConfig = SpeedControllerWrapperJni.getMotorConfig(aHandle);
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

        output.put("motor_type", aConfig.mMotorType);
        output.put("inverted", aConfig.mInverted);
        output.put("has_brake", aConfig.mHasBrake);

        Map<String, Object> transmissionConfig = new LinkedHashMap<>();
        transmissionConfig.put("num_motors", aConfig.mNumMotors);
        transmissionConfig.put("gear_reduction", aConfig.mGearReduction);
        transmissionConfig.put("efficiency", aConfig.mGearboxEfficiency);
        output.put("transmission", transmissionConfig);

        return output;
    }
}
