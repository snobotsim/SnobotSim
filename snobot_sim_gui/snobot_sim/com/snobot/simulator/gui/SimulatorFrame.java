package com.snobot.simulator.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.snobot.simulator.config.SimulatorConfigWriter;
import com.snobot.simulator.gui.joysticks.JoystickManagerDialog;
import com.snobot.simulator.jni.RobotStateSingletonJni;

public class SimulatorFrame extends JFrame
{

    private GraphicalSensorDisplayPanel mBasicPanel;
    private EnablePanel mEnablePanel;
    private String mSimulatorConfigFile;

    public SimulatorFrame(String aSimulatorConfigFile)
    {
        initComponenents();

        mSimulatorConfigFile = aSimulatorConfigFile;
    }

    public void updateLoop()
    {
        mBasicPanel.update();
        mEnablePanel.setTime(RobotStateSingletonJni.getMatchTime());
    }

    private void initComponenents()
    {
        mBasicPanel = new GraphicalSensorDisplayPanel();
        mEnablePanel = new EnablePanel();

        mEnablePanel.addStateChangedListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                RobotStateSingletonJni.setDisabled(!mEnablePanel.isEnabled());
                RobotStateSingletonJni.setAutonomous(mEnablePanel.isAuton());
            }
        });

        JButton configureJoystickBtn = new JButton("Configure Joysticks");
        configureJoystickBtn.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                showJoystickDialog();
            }
        });

        JButton changeSettingsBtn = new JButton("Change Settings");
        JButton saveSettingsBtn = new JButton("Save Settings");

        changeSettingsBtn.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                changeSettingsBtn.setVisible(false);
                saveSettingsBtn.setVisible(true);

                showSettingsOptions(true);
            }
        });

        saveSettingsBtn.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                changeSettingsBtn.setVisible(true);
                saveSettingsBtn.setVisible(false);

                saveSettings();
            }
        });
        saveSettingsBtn.setVisible(false);

        JPanel settingsPanel = new JPanel(new BorderLayout());
        settingsPanel.add(changeSettingsBtn, BorderLayout.NORTH);
        settingsPanel.add(saveSettingsBtn, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(configureJoystickBtn, BorderLayout.NORTH);
        buttonPanel.add(settingsPanel, BorderLayout.SOUTH);
        
        JScrollPane scrollPane = new JScrollPane(mBasicPanel);

        add(scrollPane, BorderLayout.CENTER);
        add(mEnablePanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        RobotStateSingletonJni.setDisabled(false);
        RobotStateSingletonJni.setAutonomous(false);

        mEnablePanel.setRobotEnabled(true);
        RobotStateSingletonJni.setDisabled(false);
    }

    private void saveSettings()
    {
        SimulatorConfigWriter writer = new SimulatorConfigWriter();
        
        String dumpFile = null;
        
        if(mSimulatorConfigFile == null)
        {
            JFileChooser fc = new JFileChooser(".");
            int result = fc.showSaveDialog(this);
            if(result == JFileChooser.APPROVE_OPTION)
            {
                dumpFile = fc.getSelectedFile().toString();
            }
        }
        else
        {
            dumpFile = mSimulatorConfigFile;
        }
        
        if(dumpFile != null)
        {
            System.out.println("Saving to '" + dumpFile + "'");
            if (mSimulatorConfigFile == null)
            {
                String message = "<html>This does not update the simulator file, you must add this line to the simulator config file:<br><br><br>"
                        + "simulator_config: " + dumpFile + "<html>";
                JOptionPane.showMessageDialog(null, message, "Warning", JOptionPane.INFORMATION_MESSAGE);
                mSimulatorConfigFile = dumpFile;
            }

            writer.writeConfig(dumpFile);
        }
        else
        {
            System.err.println("User cancelled save!");
        }
        
        showSettingsOptions(false);
    }

    private void showJoystickDialog()
    {
        JoystickManagerDialog dialog = new JoystickManagerDialog();
        dialog.setModal(true);
        dialog.setVisible(true);
    }

    private void showSettingsOptions(boolean aShow)
    {
        mBasicPanel.showSettingsButtons(aShow);
        pack();
    }
}
