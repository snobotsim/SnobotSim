package com.snobot.simulator.config;

import java.io.File;
import java.io.FileReader;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

@SuppressWarnings("unchecked")
public class SimulatorConfigReader
{
    private static final Logger sLOGGER = Logger.getLogger(SimulatorConfigReader.class);

    private SimulatorConfig mConfig;

    public SimulatorConfigReader()
    {
        mConfig = null;
    }

    public boolean loadConfig(String aConfigFile)
    {
        if (aConfigFile == null)
        {
            sLOGGER.log(Level.WARN, "Config file not set, won't hook anything up");
            return true;
        }

        boolean success = false;

        try
        {
            File file = new File(aConfigFile);
            sLOGGER.log(Level.INFO, "Loading " + file.getAbsolutePath());
            Yaml yaml = new Yaml();
            mConfig = (SimulatorConfig) yaml.load(new FileReader(file));

            success = true;
        }
        catch (Exception e)
        {
            sLOGGER.log(Level.ERROR, e);
        }

        return success;
    }

    public SimulatorConfig getConfig()
    {
        return mConfig;
    }
}
