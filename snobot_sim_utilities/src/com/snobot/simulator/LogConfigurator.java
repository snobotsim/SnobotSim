package com.snobot.simulator;

import java.io.File;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LogConfigurator
{

    public static void loadLog4jConfig()
    {
        String logFile = new File("log4j.properties").getAbsolutePath();
        if (new File(logFile).exists())
        {
            PropertyConfigurator.configure(logFile);
            Logger.getLogger(LogConfigurator.class).log(Level.INFO, "Loading overriden properties");
        }
        else
        {
            PropertyConfigurator.configure(LogConfigurator.class.getClassLoader().getResource("com/snobot/simulator/log4j.properties"));
        }
    }
}
