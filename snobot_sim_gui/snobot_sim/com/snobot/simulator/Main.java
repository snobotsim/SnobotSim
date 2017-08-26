package com.snobot.simulator;

import java.util.Arrays;
import java.util.Collection;

import com.snobot.simulator.jni.SnobotSimulatorJni;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.util.WPILibVersion;

public class Main
{
    public static void main(String[] args)
    {
        
        Collection<String> argList = Arrays.asList(args);

        if (argList.contains("version"))
        {
            printVersions();
        }

        if (argList.contains("find_robots"))
        {
            discoverRobots();
            System.exit(0);
        }

        try
        {
            Simulator simulator = new Simulator(parseLogLevel(argList));
            simulator.startSimulation();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            System.err.println("\n\n\n\n");
            System.err.println("Class not found exception.  You either have an error in your properties file, " + 
                    "or the project is not set up to be able to find the robot project you are attempting to create" + 
                    "\nerror: " + e);

            System.exit(-1);
        }
        catch (UnsatisfiedLinkError e)
        {
            e.printStackTrace();
            System.err.println("\n\n\n\n");
            System.err.println("Unsatisfied link error.  This likely means that there is a native "
                    + "call in WpiLib or the NetworkTables libraries.  Please tell PJ so he can mock it out.\n\nError Message: " + e);

            System.exit(-1);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private static int parseLogLevel(Collection<String> argList)
    {
        int logLevel = 0;

        for (String arg : argList)
        {
            if (arg.startsWith("log_level="))
            {
                logLevel = Integer.parseInt(arg.substring("log_level=".length()));
            }
        }

        return logLevel;
    }

    private static void printVersions()
	{
        System.out.println("Versions:");
        System.out.println("Wpilib Java   : " + WPILibVersion.Version);
        //System.out.println("SnobotSim HAL : " + SnobotSimGuiVersion.Version);
        //System.out.println("SnobotSim GUI : " + SnobotSimulatorJni.getVersion());

    }

    private static void discoverRobots()
    {
        try
        {
            RobotBase.initializeHardwareConfiguration();
            PluginSniffer sniffer = new PluginSniffer();
            sniffer.loadPlugins();
            sniffer.findRobots();

            StringBuilder output = new StringBuilder();
            output.append("\n\n\n\n");
            output.append("# <--------------------------------------->\n");
            output.append("# <- Here is a config script you can use ->\n");
            output.append("# <--------------------------------------->\n");
            output.append("\n\n\n\n");

            if (!sniffer.getCppRobots().isEmpty())
            {
                output.append("################################\n");
                output.append("#          CPP Robots          #\n");
                output.append("################################\n");
                output.append("\n\n");

                for (Class<?> clazzName : sniffer.getCppRobots())
                {
                    output.append("# " + clazzName.getSimpleName() + "\n");
                    output.append("robot_class      : " + clazzName.getName() + "\n");
                    output.append("robot_type       : cpp\n");
                    output.append("simulator_class  :\n");
                    output.append("simulator_config : simulator_config/2016-TeamXXXX.yml\n");
                    output.append("\n\n");
                }
            }

            if (!sniffer.getJavaRobots().isEmpty())
            {
                output.append("################################\n");
                output.append("#          Java Robots         #\n");
                output.append("################################\n");

                for (Class<?> clazzName : sniffer.getJavaRobots())
                {
                    output.append("# " + clazzName.getSimpleName() + "\n");
                    output.append("robot_class     : " + clazzName.getName() + "\n");
                    output.append("robot_type      : java\n");
                    output.append("simulator_class :\n");
                    output.append("\n\n");
                }
            }

            System.out.println(output.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
