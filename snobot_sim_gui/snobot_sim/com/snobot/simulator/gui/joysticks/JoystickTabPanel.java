package com.snobot.simulator.gui.joysticks;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.Map.Entry;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.snobot.simulator.gui.joysticks.sub_panels.RawJoystickPanel;
import com.snobot.simulator.gui.joysticks.sub_panels.WrappedJoystickPanel;
import com.snobot.simulator.gui.joysticks.sub_panels.XboxPanel;
import com.snobot.simulator.joysticks.IMockJoystick;
import com.snobot.simulator.joysticks.JoystickDiscoverer;
import com.snobot.simulator.joysticks.JoystickFactory;
import com.snobot.simulator.joysticks.joystick_specializations.NullJoystick;

import net.java.games.input.Controller;
import net.java.games.input.Controller.Type;

public class JoystickTabPanel extends JPanel
{
    private static final Logger sLOGGER = Logger.getLogger(JoystickTabPanel.class);

    private RawJoystickPanel mRawPanel;

    private JComboBox<String> mSelectInterperetTypeBox;
    private WrappedJoystickPanel mWrappedPanel;
    private XboxPanel mXboxPanel;

    private Controller mController;
    private String mJoystickName;

    public JoystickTabPanel(String aJoystickName, Controller aController, Class<?> aDefaultSpecialization) throws IOException
    {
        mController = aController;
        mJoystickName = aJoystickName;

        initComponents();

        if (JoystickDiscoverer.sAVAILABLE_SPECIALIZATIONS.containsKey(aDefaultSpecialization))
        {
            mSelectInterperetTypeBox.setSelectedItem(JoystickDiscoverer.sAVAILABLE_SPECIALIZATIONS.get(aDefaultSpecialization));
            handleWrapperSelected(mSelectInterperetTypeBox.getSelectedItem().toString());
        }
        else
        {
            handleWrapperSelected(mSelectInterperetTypeBox.getItemAt(0));
        }
    }

    private void initComponents() throws IOException
    {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFocusable(false);

        mSelectInterperetTypeBox = new JComboBox<>();
        mSelectInterperetTypeBox.setFocusable(false);

        // Raw Panel
        mRawPanel = new RawJoystickPanel(mController);
        tabbedPane.add("Raw", mRawPanel);

        // Wrapped Panel
        mWrappedPanel = new WrappedJoystickPanel();
        tabbedPane.add("Wrapped", mWrappedPanel);

        // XBOX Panel
        mXboxPanel = new XboxPanel();
        tabbedPane.add("As XBOX", mXboxPanel);

        // Main View
        setLayout(new BorderLayout());
        add(mSelectInterperetTypeBox, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);

        if (mController.getType() == Type.KEYBOARD)
        {
            mSelectInterperetTypeBox.addItem("Keyboard");
        }
        else
        {
            for (String name : JoystickDiscoverer.sAVAILABLE_SPECIALIZATIONS.values())
            {
                mSelectInterperetTypeBox.addItem(name);
            }
        }

        mSelectInterperetTypeBox.addItemListener(new ItemListener()
        {

            @Override
            public void itemStateChanged(ItemEvent e)
            {
                if (e.getStateChange() == ItemEvent.SELECTED)
                {
                    handleWrapperSelected(e.getItem().toString());
                }
            }
        });
    }

    private void handleWrapperSelected(String aType)
    {
        IMockJoystick wrappedJoystick = null;

        // Assuming values are unique as well as keys
        for (Entry<Class<? extends IMockJoystick>, String> pair : JoystickDiscoverer.sAVAILABLE_SPECIALIZATIONS.entrySet())
        {
            if (pair.getValue().equals(aType))
            {
                try
                {
                    JoystickFactory.get().setSpecialization(mJoystickName, pair.getKey());
                    wrappedJoystick = pair.getKey().getDeclaredConstructor(Controller.class).newInstance(mController);
                }
                catch (Exception e)
                {
                    sLOGGER.log(Level.ERROR, e);
                }
                break;
            }
        }

        if (wrappedJoystick == null)
        {
            wrappedJoystick = new NullJoystick();
        }

        mWrappedPanel.setJoystick(wrappedJoystick);
        mXboxPanel.setJoystick(wrappedJoystick);
    }

    public void update()
    {
        mRawPanel.updateDisplay();
        mWrappedPanel.updateDisplay();
        mXboxPanel.updateDisplay();
    }

}
