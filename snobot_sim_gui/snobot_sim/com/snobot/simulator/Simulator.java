package com.snobot.simulator;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import javax.swing.JFrame;

import com.snobot.simulator.gui.SimulatorFrame;
import com.snobot.simulator.joysticks.IMockJoystick;
import com.snobot.simulator.joysticks.JoystickFactory;
import com.snobot.simulator.robot_container.CppRobotContainer;
import com.snobot.simulator.robot_container.IRobotClassContainer;
import com.snobot.simulator.robot_container.JavaRobotContainer;
import com.snobot.simulator.robot_container.PythonRobotContainer;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.simulator.wrapper_accessors.SimulatorDataAccessor.SnobotLogLevel;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Simulator
{
    private final String USER_CONFIG_DIRECTORY;
    private final String PROPERTIES_FILE;

    private String mSimulatorClassName; // The name of the class that represents the simulator
    private String mSimulatorConfig;

    private IRobotClassContainer mRobot; // The robot code to run
    private ASimulator mSimulator; // The robot code to run

    protected Thread mRobotThread;
    protected Thread mSimulatorThread;

    public Simulator(SnobotLogLevel aLogLevel, File aPluginDirectory, String aUserConfigDir) throws Exception
    {
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().setLogLevel(aLogLevel);

        PluginSniffer sniffer = new PluginSniffer();
        sniffer.loadPlugins(aPluginDirectory);

        USER_CONFIG_DIRECTORY = aUserConfigDir;
        PROPERTIES_FILE = USER_CONFIG_DIRECTORY + "simulator_config.properties";

        File config_dir = new File(aUserConfigDir);
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
                        new File(PROPERTIES_FILE), false))
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

            NetworkTable.setPersistentFilename(USER_CONFIG_DIRECTORY + robotClassName + ".preferences.ini");
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
        loadConfig(PROPERTIES_FILE);

        sendJoystickUpdate();
        createSimulator();
        createRobot();

        mRobotThread = new Thread(createRobotThread(), "RobotThread");
        mSimulatorThread = new Thread(createSimulationThread(), "SimulatorThread");

        mSimulatorThread.start();
        mRobotThread.start();
    }

    protected void setFrameVisible(SimulatorFrame frame)
    {
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void sendJoystickUpdate()
    {
        IMockJoystick[] joysticks = JoystickFactory.get().getAll();
        for (int i = 0; i < joysticks.length; ++i)
        {
            IMockJoystick joystick = joysticks[i];
            DataAccessorFactory.getInstance().getSimulatorDataAccessor().setJoystickInformation(i, joystick.getAxisValues(), joystick.getPovValues(),
                    joystick.getButtonCount(),
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
                mSimulator.loadConfig(mSimulatorConfig);
                System.out.println("Created simulator : " + mSimulatorClassName);
            }
            else
            {
                mSimulator = new ASimulator();
                mSimulator.loadConfig(mSimulatorConfig);
                System.out.println("Created default simulator");
            }

        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessError | IllegalAccessException e)
        {
            System.err.println("Could not find simulator class " + mSimulatorClassName);
            exitWithError();
        }
    }

    private Runnable createSimulationThread()
    {
        return new Runnable()
        {

            @Override
            public void run()
            {
                try
                {
                    DataAccessorFactory.getInstance().getSimulatorDataAccessor().waitForProgramToStart();

                    if (mSimulator != null)
                    {
                        mSimulator.createSimulatorComponents();
                        mSimulator.setRobot(mRobot);
                    }

                    SimulatorFrame frame = new SimulatorFrame(mSimulatorConfig);
                    setFrameVisible(frame);

                    while (true)
                    {
                        DataAccessorFactory.getInstance().getSimulatorDataAccessor().waitForNextUpdateLoop();

                        mSimulator.update();
                        frame.updateLoop();
                        sendJoystickUpdate();
                    }
                }
                catch (Throwable e)
                {
                    System.err.println("Encountered fatal error, will exit.  Error: " + e.getMessage());
                    e.printStackTrace();
                    exitWithError();
                }
            }
        };
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
                    exitWithError();
                }
                catch (Exception e)
                {
                    System.out.println("Unexpected exception, shutting down simulator");
                    e.printStackTrace();
                    exitWithError();
                }
            }
        };
    }

    protected void stop()
    {
        mRobotThread = null;
        mSimulatorThread = null;
    }

    protected void exitWithError()
    {
        stop();
        System.exit(-1);
    }

    public ASimulator getSimulator()
    {
        return mSimulator;
    }
}
