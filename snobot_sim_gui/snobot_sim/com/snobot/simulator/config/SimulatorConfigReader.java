package com.snobot.simulator.config;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import com.snobot.simulator.motor_sim.DcMotorModelConfig;
import com.snobot.simulator.motor_sim.GravityLoadMotorSimulationConfig;
import com.snobot.simulator.motor_sim.RotationalLoadMotorSimulationConfig;
import com.snobot.simulator.motor_sim.SimpleMotorSimulationConfig;
import com.snobot.simulator.motor_sim.StaticLoadMotorSimulationConfig;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;

@SuppressWarnings("unchecked")
public class SimulatorConfigReader
{

    public boolean loadConfig(String aConfigFile)
    {
        if (aConfigFile == null)
        {
            Logger.getLogger(SimulatorConfigReader.class).log(Level.WARN, "Config file not set, won't hook anything up");
            return true;
        }

        boolean success = false;

        try
        {
            File file = new File(aConfigFile);
            Logger.getLogger(SimulatorConfigReader.class).log(Level.INFO, "Loading " + file.getAbsolutePath());
            Yaml yaml = new Yaml();
            parseConfig(yaml.load(new FileReader(file)));

            success = true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return success;
    }

    protected void parseConfig(Object aConfig)
    {
        Map<String, Object> config = (Map<String, Object>) aConfig;

        if (config.containsKey("speed_controllers"))
        {
            parseSpeedControllers((List<Map<String, Object>>) config.get("speed_controllers"));
        }

        if (config.containsKey("quad_encoders"))
        {
            parseEncoders((List<Map<String, Object>>) config.get("quad_encoders"));
        }

        if (config.containsKey("relays"))
        {
            parseRelays((List<Map<String, Object>>) config.get("relays"));
        }

        if (config.containsKey("solenoids"))
        {
            parseSolenoids((List<Map<String, Object>>) config.get("solenoids"));
        }

        if (config.containsKey("digital"))
        {
            parseDigital((List<Map<String, Object>>) config.get("digital"));
        }

        if (config.containsKey("tank_drives"))
        {
            parseTankDriveConfig((List<Map<String, Object>>) config.get("tank_drives"));
        }

    }

    protected void parseSolenoids(List<Map<String, Object>> aSolenoids)
    {
        for (Map<String, Object> solenoidConfig : aSolenoids)
        {
            int handle = getIntHandle(solenoidConfig.get("handle"));
            if (solenoidConfig.containsKey("name"))
            {
                DataAccessorFactory.getInstance().getSolenoidAccessor().setName(handle, solenoidConfig.get("name").toString());
            }
        }
    }

    protected void parseDigital(List<Map<String, Object>> aDigital)
    {
        for (Map<String, Object> digitalConfig : aDigital)
        {
            int handle = getIntHandle(digitalConfig.get("handle"));
            if (digitalConfig.containsKey("name"))
            {
                DataAccessorFactory.getInstance().getDigitalAccessor().setName(handle, digitalConfig.get("name").toString());
            }
        }
    }

    protected void parseRelays(List<Map<String, Object>> aRelays)
    {
        for (Map<String, Object> relayConfig : aRelays)
        {
            int handle = getIntHandle(relayConfig.get("handle"));
            if (relayConfig.containsKey("name"))
            {
                DataAccessorFactory.getInstance().getRelayAccessor().setName(handle, relayConfig.get("name").toString());
            }
        }
    }

    protected void parseSpeedControllers(List<Map<String, Object>> aSpeedControllers)
    {
        for (Map<String, Object> scConfig : aSpeedControllers)
        {
            int handle = getIntHandle(scConfig.get("handle"));
            if (scConfig.containsKey("name"))
            {
                DataAccessorFactory.getInstance().getSpeedControllerAccessor().setName(handle, scConfig.get("name").toString());
            }

            if (scConfig.containsKey("motor_sim"))
            {
                parseMotorSimConfig(handle, (Map<String, Object>) scConfig.get("motor_sim"));
            }
        }
    }

    protected void parseEncoders(List<Map<String, Object>> aEncoders)
    {
        for (Map<String, Object> encConfig : aEncoders)
        {
            int handle = getIntHandle(encConfig.get("handle"));

            if (encConfig.containsKey("name"))
            {
                DataAccessorFactory.getInstance().getEncoderAccessor().setName(handle, encConfig.get("name").toString());
            }

            if (encConfig.containsKey("speed_controller_handle"))
            {
                int speedControllerHandle = getIntHandle(encConfig.get("speed_controller_handle"));
                DataAccessorFactory.getInstance().getEncoderAccessor().connectSpeedController(handle, speedControllerHandle);
            }
        }
    }

    protected void parseMotorSimConfig(int aScHandle, Map<String, Object> motorSimConfig)
    {
        String type = motorSimConfig.get("type").toString();
        switch (type)
        {
        case "Simple":
            loadMotorSimSimple(aScHandle, motorSimConfig);
            break;
        case "StaticLoad":
            loadMotorSimStaticLoad(aScHandle, motorSimConfig);
            break;
        case "GravityLoad":
            loadMotorSimGravityLoad(aScHandle, motorSimConfig);
            break;
        case "RotationalLoad":
            loadMotorSimRotationalLoad(aScHandle, motorSimConfig);
            break;
        default:
            System.err.println("Unknown motor sim type: " + type);
        }
    }

    protected void parseTankDriveConfig(List<Map<String, Object>> aTankDriveConfig)
    {
        for (Map<String, Object> tankDriveConfig : aTankDriveConfig)
        {
            int gyroHandle = getIntHandle(tankDriveConfig.get("gyro_handle"));
            double turnKp = ((Number) tankDriveConfig.get("turn_kp")).doubleValue();

            int leftEncHandle = getIntHandle(tankDriveConfig.get("left_encoder_handle"));
            int rightEncHandle = getIntHandle(tankDriveConfig.get("right_encoder_handle"));

            DataAccessorFactory.getInstance().getSimulatorDataAccessor().connectTankDriveSimulator(leftEncHandle, rightEncHandle, gyroHandle, turnKp);
        }
    }

    protected void loadMotorSimSimple(int aScHandle, Map<String, Object> motorSimConfig)
    {
        double maxSpeed = ((Number) motorSimConfig.get("max_speed")).doubleValue();
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().setSpeedControllerModel_Simple(aScHandle,
                new SimpleMotorSimulationConfig(maxSpeed));
    }

    protected void loadMotorSimStaticLoad(int aScHandle, Map<String, Object> motorSimConfig)
    {
        double load = ((Number) motorSimConfig.get("load")).doubleValue();
        DcMotorModelConfig motorConfig = createDcMotorModel((Map<String, Object>) motorSimConfig.get("motor_model"));
        double conversionFactor = 1;

        if (motorSimConfig.containsKey("conversion_factor"))
        {
            conversionFactor = ((Number) motorSimConfig.get("conversion_factor")).doubleValue();
        }
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().setSpeedControllerModel_Static(aScHandle, motorConfig,
                new StaticLoadMotorSimulationConfig(load, conversionFactor));
    }

    protected void loadMotorSimGravityLoad(int aScHandle, Map<String, Object> motorSimConfig)
    {
        double load = ((Number) motorSimConfig.get("load")).doubleValue();
        DcMotorModelConfig motorConfig = createDcMotorModel((Map<String, Object>) motorSimConfig.get("motor_model"));

        DataAccessorFactory.getInstance().getSimulatorDataAccessor().setSpeedControllerModel_Gravitational(aScHandle, motorConfig,
                new GravityLoadMotorSimulationConfig(load));
    }

    protected void loadMotorSimRotationalLoad(int aScHandle, Map<String, Object> motorSimConfig)
    {
        double armCenterOfMass = ((Number) motorSimConfig.get("arm_center_of_mass")).doubleValue();
        double armMass = ((Number) motorSimConfig.get("arm_mass")).doubleValue();
        DcMotorModelConfig motorConfig = createDcMotorModel((Map<String, Object>) motorSimConfig.get("motor_model"));

        DataAccessorFactory.getInstance().getSimulatorDataAccessor().setSpeedControllerModel_Rotational(aScHandle, motorConfig, 
                new RotationalLoadMotorSimulationConfig(armCenterOfMass, armMass));
    }

    protected DcMotorModelConfig createDcMotorModel(Map<String, Object> modelConfig)
    {
        DcMotorModelConfig output = null;

        String motorType = modelConfig.get("motor_type").toString();

        if (modelConfig.containsKey("transmission"))
        {
            Map<String, Object> transmissionConfig = (Map<String, Object>) modelConfig.get("transmission");
            int numMotors = (Integer) transmissionConfig.get("num_motors");
            double gearRatio = ((Number) transmissionConfig.get("gear_reduction")).doubleValue();
            double gearboxEfficiency = ((Number) transmissionConfig.get("efficiency")).doubleValue();
            output = DataAccessorFactory.getInstance().getSimulatorDataAccessor().createMotor(motorType, numMotors, gearRatio, gearboxEfficiency);
        }
        else
        {
            output = DataAccessorFactory.getInstance().getSimulatorDataAccessor().createMotor(motorType);
        }

        if (modelConfig.containsKey("inverted"))
        {
            output.setInverted((Boolean) modelConfig.get("inverted"));
        }

        if (modelConfig.containsKey("has_brake"))
        {
            output.setHasBrake((Boolean) modelConfig.get("has_brake"));
        }

        return output;
    }

    protected int getIntHandle(Object aHandleObject)
    {
        int output = 0;

        try
        {
            output = (Integer) aHandleObject;
        }
        catch (ClassCastException ex)
        {
            String fullName = aHandleObject.toString();

            int lastDot = fullName.lastIndexOf(".");
            String className = fullName.substring(0, lastDot);
            String fieldName = fullName.substring(lastDot + 1);

            try
            {
                Class<?> myClass = Class.forName(className);
                Field myField = myClass.getDeclaredField(fieldName);
                output = myField.getInt(null);
            }
            catch (ClassNotFoundException | NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)
            {
                e.printStackTrace();
            }

        }

        return output;
    }
}
