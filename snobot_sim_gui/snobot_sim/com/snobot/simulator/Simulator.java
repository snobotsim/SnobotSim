package com.snobot.simulator;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import javax.swing.JFrame;

import com.snobot.simulator.gui.SimulatorFrame;
import com.snobot.simulator.jni.JoystickJni;
import com.snobot.simulator.jni.RobotStateSingletonJni;
import com.snobot.simulator.jni.SimulationConnectorJni;
import com.snobot.simulator.jni.SnobotSimulatorJni;
import com.snobot.simulator.joysticks.IMockJoystick;
import com.snobot.simulator.joysticks.JoystickFactory;
import com.snobot.simulator.robot_container.CppRobotContainer;
import com.snobot.simulator.robot_container.IRobotClassContainer;
import com.snobot.simulator.robot_container.JavaRobotContainer;
import com.snobot.simulator.robot_container.PythonRobotContainer;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Simulator
{
    private static final String sUSER_CONFIG_DIR = "user_config/";
    private static final String sPROPERTIES_FILE = sUSER_CONFIG_DIR + "simulator_config.properties";

    private String mSimulatorClassName; // The name of the class that represents the simulator
    private String mSimulatorConfig;

    private IRobotClassContainer mRobot; // The robot code to run
    private ASimulator mSimulator; // The robot code to run

    public Simulator(int aLogLevel) throws Exception
    {
        SnobotSimulatorJni.initializeLogging(aLogLevel);

        PluginSniffer sniffer = new PluginSniffer();
        sniffer.loadPlugins();

        File config_dir = new File(sUSER_CONFIG_DIR);
        if (!Files.exists(config_dir.toPath()))
        {
            config_dir.mkdir();
        }
    }

    private void loadConfig(String aFile)
    {

        try
        {
            if (!Files.exists(Paths.get(aFile)))
            {
                System.err.println("Could not read properties file, will use defaults and will overwrite the file if it exists");

                if (!JniLibraryResourceLoader.copyResourceFromJar("/com/snobot/simulator/config/default_properties.properties",
                        new File(sPROPERTIES_FILE), false))
            	{
            		throw new RuntimeException("Could not copy properties file!  Have to exit!");
            	}
            }

            Properties p = new Properties();
            p.load(new FileInputStream(new File(aFile)));

            String robotClassName = p.getProperty("robot_class");
            mSimulatorClassName = p.getProperty("simulator_class");
            mSimulatorConfig = p.getProperty("simulator_config");

            String robotType = p.getProperty("robot_type");
            if (robotType == null || robotType.equals("java"))
            {
                mRobot = new JavaRobotContainer(robotClassName);
            }
            else if (robotType.equals("cpp"))
            {
                mRobot = new CppRobotContainer(robotClassName);
            }
            else if (robotType.equals("python"))
            {
                mRobot = new PythonRobotContainer(robotClassName);
            }
            else
            {
                throw new RuntimeException("Unsuppored robot type " + robotType);
            }

            NetworkTable.setPersistentFilename(sUSER_CONFIG_DIR + robotClassName + ".preferences.ini");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println("Could not read properties file");
        }
    }

    private void createRobot() throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException,
            SecurityException, IllegalArgumentException, InvocationTargetException
    {
        System.out.println("*************************************************************");
        System.out.println("*                    Starting Robot Code                    *");
        System.out.println("*************************************************************");

        mRobot.constructRobot();
    }

    public void startSimulation()
            throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException,
            IllegalArgumentException, InvocationTargetException
    {
        loadConfig(sPROPERTIES_FILE);

        sendJoystickUpdate();
        createSimulator();
        createRobot();

        Thread robotThread = new Thread(createRobotThread(), "RobotThread");


        Thread t = new Thread(new Runnable()
        {

            @Override
            public void run()
            {
            	try
            	{
	                RobotStateSingletonJni.waitForProgramToStart();
	
	                if (mSimulator != null)
	                {
	                    mSimulator.createSimulatorComponents(mSimulatorConfig);
	                    mSimulator.setRobot(mRobot);
	                    System.out.println("Created simulator : " + mSimulatorClassName);
	                }
	
	                SimulatorFrame frame = new SimulatorFrame();
	                frame.pack();
	                frame.setVisible(true);
	                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	                frame.addWindowListener(new WindowAdapter()
	                {
	                    /**
	                     * Invoked when a window has been closed.
	                     */
	                    public void windowClosing(WindowEvent e)
	                    {
	                        // SnobotSimulatorJni.shutdown();
	                    }
	                });
	
	                while (true)
	                {
	                    RobotStateSingletonJni.waitForNextUpdateLoop();
	 
	                    mSimulator.update();
	                    frame.updateLoop();
	                    sendJoystickUpdate();
	
	                    SimulationConnectorJni.updateLoop();
	                }
	            }
                catch(Throwable e)
                {
                	System.err.println("Encountered fatal error, will exit.  Error: " + e.getMessage());
                	e.printStackTrace();
                	System.exit(1);
                }
            }
        }, "SimulatorThread");
        t.start();
        robotThread.start();
    }

    private void sendJoystickUpdate()
    {

        IMockJoystick[] joysticks = JoystickFactory.get().getAll();
        for (int i = 0; i < joysticks.length; ++i)
        {
            IMockJoystick joystick = joysticks[i];
            JoystickJni.setJoystickInformation(i, joystick.getAxisValues(), joystick.getPovValues(), joystick.getButtonCount(),
                    joystick.getButtonMask());
        }
    }

    private void createSimulator()
    {
        try
        {
            if (mSimulatorClassName != null && !mSimulatorClassName.isEmpty())
            {
                mSimulator = (ASimulator) Class.forName(mSimulatorClassName).newInstance();
            }
            else
            {
                mSimulator = new ASimulator();
            }

        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessError | IllegalAccessException e)
        {
            throw new RuntimeException("Could not find simulator class " + mSimulatorClassName);
        }
    }

    private Runnable createRobotThread()
    {
        return new Runnable()
        {

            @Override
            public void run()
            {

                try
                {
                    mRobot.startCompetition();
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
                    System.out.println("Unexpected exception, shutting down simulator");
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
        };
    }
}
