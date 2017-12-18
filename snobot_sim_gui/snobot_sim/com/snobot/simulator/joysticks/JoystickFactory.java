package com.snobot.simulator.joysticks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.snobot.simulator.joysticks.joystick_specializations.NullJoystick;

import edu.wpi.first.wpilibj.DriverStation;
import net.java.games.input.Controller;

public class JoystickFactory
{
    public static final String sJOYSTICK_CONFIG_FILE = "simulator_config/joystick_config.properties";

    private static final Logger sLOGGER = Logger.getLogger(JoystickFactory.class);

    private static final JoystickFactory sINSTANCE = new JoystickFactory();
    private static final String sKEY = "Joystick_";

    public static JoystickFactory get()
    {
        return sINSTANCE;
    }

    private IMockJoystick[] mJoystickMap;
    private Map<String, ControllerConfiguration> mControllerConfig;

    private JoystickFactory()
    {
        mControllerConfig = new HashMap<>();

        mJoystickMap = new IMockJoystick[DriverStation.kJoystickPorts];
        for (int i = 0; i < DriverStation.kJoystickPorts; ++i)
        {
            mJoystickMap[i] = new NullJoystick();
        }

        mControllerConfig = JoystickDiscoverer.rediscoverJoysticks();
        loadSticks();
    }

    public Map<String, ControllerConfiguration> getControllerConfiguration()
    {
        return mControllerConfig;
    }

    private void writeJoystickFile()
    {
        try
        {
            Properties p = new Properties();
            for (int i = 0; i < DriverStation.kJoystickPorts; ++i)
            {
                String joystickName = mJoystickMap[i].getName();

                ControllerConfiguration config = mControllerConfig.get(joystickName);
                String specializationName = config == null ? null : config.mSpecialization.getName();

                p.put(sKEY + i, joystickName + "---" + specializationName);
            }

            FileOutputStream stream = new FileOutputStream(sJOYSTICK_CONFIG_FILE);

            p.store(stream, "");
            stream.close();

            sLOGGER.log(Level.INFO,
                    "Wrote joystick config file to " + new File(sJOYSTICK_CONFIG_FILE).getAbsolutePath());
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void loadSticks()
    {
        if (!Files.exists(Paths.get(sJOYSTICK_CONFIG_FILE)))
        {
            writeJoystickFile();
            return;
        }

        try
        {
            InputStream input_stream = new FileInputStream(sJOYSTICK_CONFIG_FILE);
            Properties properties = new Properties();
            properties.load(input_stream);
            input_stream.close();

            for (Entry<Object, Object> i : properties.entrySet())
            {
                int number = Integer.parseInt(i.getKey().toString().substring(sKEY.length()));

                String config = i.getValue().toString();
                String[] parts = config.split("---");

                String joystickName = parts[0];
                String specialization = parts[1];

                if (!specialization.equals("null"))
                {
                    setSpecialization(joystickName, (Class<? extends IMockJoystick>) Class.forName(specialization), false);
                }
                setJoysticks(number, joystickName, false);
            }
        }
        catch (IOException | ClassNotFoundException ex)
        {
            ex.printStackTrace();
        }
    }

    public void setSpecialization(String aName, Class<? extends IMockJoystick> aClass)
    {
        setSpecialization(aName, aClass, true);
    }

    public void setSpecialization(String aName, Class<? extends IMockJoystick> aClass, boolean aAutoSave)
    {
        if (mControllerConfig.containsKey(aName))
        {
            mControllerConfig.get(aName).mSpecialization = aClass;

            if (aAutoSave)
            {
                writeJoystickFile();
            }
        }

    }

    public IMockJoystick[] getAll()
    {
        return mJoystickMap;
    }

    public IMockJoystick get(int aJoystickIndex)
    {
        return mJoystickMap[aJoystickIndex];
    }

    public void setJoysticks(int aJoystickIndex, String aControllerName)
    {
        setJoysticks(aJoystickIndex, aControllerName, true);
    }

    public void setJoysticks(int aJoystickIndex, String aControllerName, boolean aAutoSave)
    {
        IMockJoystick joystick = null;

        if (aControllerName.equals(NullJoystick.sNAME))
        {
            joystick = new NullJoystick();
        }
        else if (mControllerConfig.containsKey(aControllerName))
        {
            ControllerConfiguration config = mControllerConfig.get(aControllerName);

            try
            {
                joystick = config.mSpecialization.getDeclaredConstructor(Controller.class, String.class).newInstance(config.mController,
                        aControllerName);
            }
            catch (Exception e)
            {
                sLOGGER.log(Level.ERROR, e);
                joystick = new NullJoystick();
            }
        }

        else
        {
            joystick = new NullJoystick();
            sLOGGER.log(Level.ERROR, "Unknown joystick name : " + aControllerName);
        }

        mJoystickMap[aJoystickIndex] = joystick;

        if (aAutoSave)
        {
            writeJoystickFile();
        }
    }

    public void registerJoystickCreated(int aPort)
    {
        if (mJoystickMap[aPort] instanceof NullJoystick)
        {
            tryToReplaceGamepad(aPort);
        }
    }

    private IMockJoystick tryToReplaceGamepad(int aJoystickIndex)
    {
        IMockJoystick output = null;

        for (Entry<String, ControllerConfiguration> pair : mControllerConfig.entrySet())
        {
            ControllerConfiguration configuration = pair.getValue();

            if (configuration.mController == null)
            {
                continue;
            }

            boolean foundController = false;

            for (IMockJoystick mockJoystick : mJoystickMap)
            {
                if (mockJoystick.getController() != null && mockJoystick.getController().equals(configuration.mController))
                {
                    foundController = true;
                    break;
                }
            }

            if (!foundController)
            {
                sLOGGER.log(Level.DEBUG, "Replacing null joystick with '" + pair.getKey() + "'");
                setJoysticks(aJoystickIndex, pair.getKey());
                break;
            }
        }

        return output;
    }

}
