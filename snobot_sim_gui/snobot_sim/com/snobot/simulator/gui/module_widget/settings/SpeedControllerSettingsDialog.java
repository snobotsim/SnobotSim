package com.snobot.simulator.gui.module_widget.settings;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import com.snobot.simulator.DcMotorModelConfig;
import com.snobot.simulator.jni.module_wrapper.SpeedControllerWrapperJni;
import com.snobot.simulator.jni.module_wrapper.SpeedControllerWrapperJni.MotorSimType;

public class SpeedControllerSettingsDialog extends SimpleSettingsDialog
{
    public SpeedControllerSettingsDialog(String aTitle, int aKey, String aName)
    {
        super(aTitle, aKey, aName);

        DcMotorModelConfig modelConfig = SpeedControllerWrapperJni.getMotorConfig(aKey);

        JPanel nullSimPanel = new JPanel();
        JPanel simpleSimPanel = new SimpleSimConfigPanel();
        JPanel staticLoadSimPanel = new StaticLoadSimConfigPanel(modelConfig);
        JPanel rotationalLoadSimPanel = new RotationalLoadSimConfigPanel(modelConfig);
        JPanel gravitationalLoadSimPanel = new GravitationalLoadSimConfigPanel(modelConfig);

        CardLayout simOptionsLayout = new CardLayout();
        JPanel simOptionsPanel = new JPanel(simOptionsLayout);
        simOptionsPanel.add(nullSimPanel, MotorSimType.None.toString());
        simOptionsPanel.add(simpleSimPanel, MotorSimType.Simple.toString());
        simOptionsPanel.add(staticLoadSimPanel, MotorSimType.StaticLoad.toString());
        simOptionsPanel.add(rotationalLoadSimPanel, MotorSimType.RotationalLoad.toString());
        simOptionsPanel.add(gravitationalLoadSimPanel, MotorSimType.GravitationalLoad.toString());

        String comboBoxItems[] = new String[MotorSimType.values().length];
        for (int i = 0; i < MotorSimType.values().length; ++i)
        {
            comboBoxItems[i] = MotorSimType.values()[i].toString();
        }

        JComboBox<String> simModeSelector = new JComboBox<>(comboBoxItems);
        JPanel simDisplayPanel = new JPanel(new BorderLayout());
        simDisplayPanel.add(simModeSelector, BorderLayout.NORTH);
        simDisplayPanel.add(simOptionsPanel, BorderLayout.CENTER);

        getContentPane().add(simDisplayPanel, BorderLayout.EAST);

        simModeSelector.addItemListener(new ItemListener()
        {

            @Override
            public void itemStateChanged(ItemEvent e)
            {
                simOptionsLayout.show(simOptionsPanel, (String) e.getItem());
            }
        });

        MotorSimType mode = SpeedControllerWrapperJni.getMotorSimType(mHandle);
        simModeSelector.setSelectedItem(mode.toString());
    }
}

class SimpleSimConfigPanel extends JPanel
{
    public SimpleSimConfigPanel()
    {
        add(new JLabel("Simple sim!"));
    }

}

class StaticLoadSimConfigPanel extends JPanel
{
    private MotorSimPanel mMotorSimPanel;

    public StaticLoadSimConfigPanel(DcMotorModelConfig aConfig)
    {
        mMotorSimPanel = new MotorSimPanel(aConfig);
        add(mMotorSimPanel);
    }
}

class RotationalLoadSimConfigPanel extends JPanel
{
    private MotorSimPanel mMotorSimPanel;

    public RotationalLoadSimConfigPanel(DcMotorModelConfig aConfig)
    {
        mMotorSimPanel = new MotorSimPanel(aConfig);
        add(mMotorSimPanel);
    }
}

class GravitationalLoadSimConfigPanel extends JPanel
{
    private MotorSimPanel mMotorSimPanel;

    public GravitationalLoadSimConfigPanel(DcMotorModelConfig aConfig)
    {
        mMotorSimPanel = new MotorSimPanel(aConfig);
        add(mMotorSimPanel);
    }
}

class MotorSimPanel extends JPanel
{
    private JComboBox<String> mMotorSelectionBox;
    private JSpinner mNumMotors;
    private JTextField mGearReduction;
    private JTextField mGearboxEfficiency;

    public MotorSimPanel(DcMotorModelConfig aConfig)
    {
        mNumMotors = new JSpinner();
        mGearReduction = new JTextField();
        mGearboxEfficiency = new JTextField();

        mMotorSelectionBox = new JComboBox<>();
        mMotorSelectionBox.addItem("Some Motor");
        mMotorSelectionBox.addItem("CIM");
        mMotorSelectionBox.addItem("Other Motor");

        if (aConfig != null)
        {
            mMotorSelectionBox.setSelectedItem(aConfig.mMotorType);
            mNumMotors.setValue(aConfig.mNumMotors);
            mGearReduction.setText("" + aConfig.mGearReduction);
            mGearboxEfficiency.setText("" + aConfig.mGearboxEfficiency);
        }

        add(mMotorSelectionBox);
        add(mNumMotors);
        add(mGearReduction);
        add(mGearboxEfficiency);

    }
}
