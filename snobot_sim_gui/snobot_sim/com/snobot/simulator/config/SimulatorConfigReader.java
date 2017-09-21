package com.snobot.simulator.config;

import java.io.File;
import java.io.FileReader;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

@SuppressWarnings("unchecked")
public class SimulatorConfigReader
{
    private SimulatorConfig mConfig;

    public SimulatorConfigReader()
    {
        mConfig = null;
    }

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
            mConfig = (SimulatorConfig) yaml.load(new FileReader(file));

            success = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return success;
    }

    public SimulatorConfig getConfig()
    {
        return mConfig;
    }
}
