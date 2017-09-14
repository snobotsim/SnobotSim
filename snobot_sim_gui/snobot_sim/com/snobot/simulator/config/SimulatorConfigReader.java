package com.snobot.simulator.config;

import java.io.File;
import java.io.FileReader;

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
            System.out.println("*******************************************");
            System.out.println("Config file not set, won't hook anything up");
            System.out.println("*******************************************");
            return true;
        }

        boolean success = false;

        try
        {
            File file = new File(aConfigFile);
            System.out.println("Loading " + file.getAbsolutePath());
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
