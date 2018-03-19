package com.snobot.simulator;

import java.io.File;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.simulator.wrapper_accessors.SimulatorDataAccessor.SnobotLogLevel;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.util.WPILibVersion;

public class Main
{
    private static final Logger sLOGGER = Logger.getLogger(Main.class);

    private static final File DEFAULT_PLUGIN_DIR = new File("user_libs");
    private static final String sUSER_CONFIG_DIR = "simulator_config/";

    private static final PrintStream VERSION_PRINTER = System.out;

    public static void main(String[] aArgs)
    {
        DefaultDataAccessorFactory.initalize();

        Collection<String> argList = Arrays.asList(aArgs);

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
            Simulator simulator = new Simulator(parseLogLevel(argList), DEFAULT_PLUGIN_DIR, sUSER_CONFIG_DIR);
            simulator.startSimulation();
        }
        catch (ClassNotFoundException e)
        {
            sLOGGER.log(Level.FATAL, "Class not found exception.  You either have an error in your properties file, "
                    + "or the project is not set up to be able to find the robot project you are attempting to create"
                    +  "\nerror: " + e, e);

            System.exit(-1);
        }
        catch (UnsatisfiedLinkError e)
        {
            sLOGGER.log(Level.FATAL, "Unsatisfied link error.  This likely means that there is a native "
                    + "call in WpiLib or the NetworkTables libraries.  Please tell PJ so he can mock it out.\n\nError Message: " + e, e);

            System.exit(-1);
        }
        catch (Exception e)
        {
            sLOGGER.log(Level.ERROR, e);
            System.exit(1);
        }
    }

    private static SnobotLogLevel parseLogLevel(Collection<String> aArgList)
    {
        int logLevel = 0;

        for (String arg : aArgList)
        {
            if (arg.startsWith("log_level="))
            {
                logLevel = Integer.parseInt(arg.substring("log_level=".length()));
            }
        }

        return SnobotLogLevel.values()[logLevel];
    }

    private static void printVersions()
    {
        VERSION_PRINTER.println("Versions:");
        VERSION_PRINTER.println("Wpilib Java    : " + WPILibVersion.Version);
        VERSION_PRINTER.println("SnobotSim HAL  : " + DataAccessorFactory.getInstance().getSimulatorDataAccessor().getNativeBuildVersion());
        VERSION_PRINTER.println("SnobotSim GUI  : " + SnobotSimGuiVersion.Version);
        VERSION_PRINTER.println("SnobotSim Type : " + DataAccessorFactory.getInstance().getAccessorType());
    }

    private static void discoverRobots()
    {
        try
        {
            RobotBase.initializeHardwareConfiguration();
            PluginSniffer sniffer = new PluginSniffer();
            sniffer.loadPlugins(DEFAULT_PLUGIN_DIR);
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

            VERSION_PRINTER.println(output.toString());
        }
        catch (Exception e)
        {
            sLOGGER.log(Level.ERROR, e);
            System.exit(-1);
        }
    }
}
