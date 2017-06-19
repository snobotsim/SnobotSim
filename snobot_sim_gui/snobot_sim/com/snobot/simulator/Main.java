package com.snobot.simulator;

import com.snobot.simulator.jni.SnobotSimulatorJni;

public class Main
{
    public static void main(String[] args)
    {
        printVersions();
        
        try
        {
            Simulator simulator = new Simulator();
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
    
    private static void printVersions()
    {
        System.out.println("Versions:");
        System.out.println("SnobotSim HAL: " + SnobotSimGuiVersion.Version);
        System.out.println("SnobotSim GUI: " + SnobotSimulatorJni.getVersion());
    }
}
