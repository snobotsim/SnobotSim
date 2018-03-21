package com.snobot.simulator.gui.joysticks;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JDialog;
import javax.swing.JTabbedPane;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.snobot.simulator.joysticks.ControllerConfiguration;
import com.snobot.simulator.joysticks.JoystickFactory;

public class JoystickManagerDialog extends JDialog
{
    private static final Logger sLOGGER = LogManager.getLogger(JoystickManagerDialog.class);
    private static final int sUPDATE_TIME = 20;

    private Map<String, JoystickTabPanel> mJoystickPanels;
    private SelectionPanel mSelectionPanel;
    private boolean mIsOpen;

    public JoystickManagerDialog()
    {
        setTitle("Joystick Manager");
        mIsOpen = false;

        addWindowListener(mCloseListener);

        initComponents();
    }

    private void initComponents()
    {
        // GraphicsDevice gd =
        // GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        // int width = gd.getDisplayMode().getWidth();
        // int height = gd.getDisplayMode().getHeight();
        // setSize(width - 100, height - 100);
        setSize(1000, 600);

        mJoystickPanels = new HashMap<>();

        setLayout(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFocusable(false);

        JoystickFactory joystickFactory = JoystickFactory.get();

        Map<String, ControllerConfiguration> goodControllers = joystickFactory.getControllerConfiguration();

        for (Entry<String, ControllerConfiguration> pair : goodControllers.entrySet())
        {
            try
            {
                String controllerName = pair.getKey();
                ControllerConfiguration config = pair.getValue();

                JoystickTabPanel panel = new JoystickTabPanel(controllerName, config.mController, config.mSpecialization);
                mJoystickPanels.put(controllerName, panel);
                tabbedPane.add(controllerName, panel);
            }
            catch (IOException e)
            {
                sLOGGER.log(Level.ERROR, e);
            }
        }

        mSelectionPanel = new SelectionPanel(goodControllers.keySet(), joystickFactory.getAll());

        add(mSelectionPanel, BorderLayout.WEST);
        add(tabbedPane, BorderLayout.CENTER);
    }

    public void setVisible(boolean aVisible)
    {
        if (aVisible && !mIsOpen)
        {
            mIsOpen = true;

            Thread t = new Thread(new Runnable()
            {

                @Override
                public void run()
                {
                    while (mIsOpen)
                    {
                        for (JoystickTabPanel panel : mJoystickPanels.values())
                        {
                            panel.update();
                        }

                        try
                        {
                            Thread.sleep(sUPDATE_TIME);
                        }
                        catch (InterruptedException aEvent)
                        {
                            sLOGGER.log(Level.ERROR, aEvent);
                        }
                    }
                }
            });
            t.setName("Joystick Updater");
            t.start();
        }
        super.setVisible(aVisible);
    }

    public void close()
    {
        mIsOpen = false;
    }

    private WindowListener mCloseListener = new WindowAdapter()
    {
        @Override
        public void windowClosing(WindowEvent aEvent)
        {
            close();
        }
    };
}
