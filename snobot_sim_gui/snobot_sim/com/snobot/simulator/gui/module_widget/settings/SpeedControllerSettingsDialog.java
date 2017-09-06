package com.snobot.simulator.gui.module_widget.settings;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.snobot.simulator.DcMotorModelConfig;
import com.snobot.simulator.jni.SimulationConnectorJni;
import com.snobot.simulator.jni.module_wrapper.SpeedControllerWrapperJni;
import com.snobot.simulator.jni.module_wrapper.SpeedControllerWrapperJni.MotorSimType;

public class SpeedControllerSettingsDialog extends SimpleSettingsDialog
{
    protected final JPanel mSimOptionsPanel;
    protected final JComboBox<MotorSimType> mSimModeSelector;

    public SpeedControllerSettingsDialog(String aTitle, int aKey, String aName)
    {
        super(aTitle, aKey, aName);

        JPanel nullSimPanel = new NullSimConfigPanel(mHandle);
        JPanel simpleSimPanel = new SimpleSimConfigPanel(mHandle);
        JPanel staticLoadSimPanel = new StaticLoadSimConfigPanel(mHandle);
        JPanel rotationalLoadSimPanel = new RotationalLoadSimConfigPanel(mHandle);
        JPanel gravitationalLoadSimPanel = new GravitationalLoadSimConfigPanel(mHandle);

        CardLayout simOptionsLayout = new CardLayout();
        mSimOptionsPanel = new JPanel(simOptionsLayout);
        mSimOptionsPanel.add(nullSimPanel, MotorSimType.None.toString());
        mSimOptionsPanel.add(simpleSimPanel, MotorSimType.Simple.toString());
        mSimOptionsPanel.add(staticLoadSimPanel, MotorSimType.StaticLoad.toString());
        mSimOptionsPanel.add(rotationalLoadSimPanel, MotorSimType.RotationalLoad.toString());
        mSimOptionsPanel.add(gravitationalLoadSimPanel, MotorSimType.GravitationalLoad.toString());

        mSimModeSelector = new JComboBox<>(MotorSimType.values());
        JPanel typeSelectionPanel = new JPanel();
        typeSelectionPanel.add(new JLabel("Simulation Type"));
        typeSelectionPanel.add(mSimModeSelector);

        JPanel simDisplayPanel = new JPanel(new BorderLayout());
        simDisplayPanel.add(typeSelectionPanel, BorderLayout.NORTH);
        simDisplayPanel.add(mSimOptionsPanel, BorderLayout.CENTER);

        getContentPane().add(simDisplayPanel, BorderLayout.CENTER);

        mSimModeSelector.addItemListener(new ItemListener()
        {

            @Override
            public void itemStateChanged(ItemEvent e)
            {
                simOptionsLayout.show(mSimOptionsPanel, e.getItem().toString());
            }
        });

        MotorSimType mode = SpeedControllerWrapperJni.getMotorSimType(mHandle);
        mSimModeSelector.setSelectedItem(mode);
    }

    public void setVisible(boolean aVisible)
    {
        MotorSimType mode = SpeedControllerWrapperJni.getMotorSimType(mHandle);
        mSimModeSelector.setSelectedItem(mode);
        super.setVisible(aVisible);
    }

    @Override
    protected void onSubmit()
    {
        for (Component comp : mSimOptionsPanel.getComponents())
        {
            if (comp.isVisible() == true)
            {
                SubmitableMotorSimulator motorSimulatorPanel = (SubmitableMotorSimulator) comp;
                motorSimulatorPanel.submit();
            }
        }
    }
}

interface SubmitableMotorSimulator
{
    public void submit();
}

class NullSimConfigPanel extends JPanel implements SubmitableMotorSimulator
{
    protected int mHandle;

    public NullSimConfigPanel(int aHandle)
    {
        this.mHandle = aHandle;
        add(new JLabel("Null simulator.  Position will not change"));
    }

    @Override
    public void submit()
    {
        SimulationConnectorJni.setSpeedControllerModel_Simple(mHandle, 0);
    }

}

class SimpleSimConfigPanel extends JPanel implements SubmitableMotorSimulator
{
    protected int mHandle;
    protected JTextField mMaxSpeedField;

    public SimpleSimConfigPanel(int aHandle)
    {
        this.mHandle = aHandle;

        mMaxSpeedField = new JTextField(3);
        add(new JLabel("Max Speed"));
        add(mMaxSpeedField);
    }

    @Override
    public void submit()
    {
        double maxSpeed = Double.parseDouble(mMaxSpeedField.getText());
        SimulationConnectorJni.setSpeedControllerModel_Simple(mHandle, maxSpeed);
    }

    @Override
    public void setVisible(boolean aVisible)
    {
        if (aVisible)
        {
            mMaxSpeedField.setText("" + SpeedControllerWrapperJni.getMotorSimSimpleModelConfig(mHandle));
        }

        super.setVisible(aVisible);
    }

}

abstract class MotorSimWithModelPanel extends JPanel implements SubmitableMotorSimulator
{
    protected DcMotorConfigPanel mMotorSimPanel;
    protected int mHandle;

    protected MotorSimWithModelPanel(int aHandle)
    {
        setLayout(new BorderLayout());
        mHandle = aHandle;
        mMotorSimPanel = new DcMotorConfigPanel();
        add(mMotorSimPanel, BorderLayout.CENTER);
    }

    @Override
    public void setVisible(boolean aVisible)
    {
        if (aVisible)
        {
            DcMotorModelConfig modelConfig = SpeedControllerWrapperJni.getMotorConfig(mHandle);
            mMotorSimPanel.setModelConfig(modelConfig);
            updateSimulatorParams();
        }

        super.setVisible(aVisible);
    }

    protected abstract void updateSimulatorParams();
}

class StaticLoadSimConfigPanel extends MotorSimWithModelPanel
{
    protected JTextField mLoadField;

    public StaticLoadSimConfigPanel(int aKey)
    {
        super(aKey);

        mLoadField = new JTextField(5);

        JPanel simPanel = new JPanel();
        simPanel.setBorder(new TitledBorder("Simulator Parameters"));
        simPanel.add(new JLabel("Load"));
        simPanel.add(mLoadField);

        add(simPanel, BorderLayout.NORTH);
    }

    @Override
    public void submit()
    {
        double load = Double.parseDouble(mLoadField.getText());
        SimulationConnectorJni.setSpeedControllerModel_Static(mHandle, mMotorSimPanel.getMotorConfig(), load);
    }

    @Override
    protected void updateSimulatorParams()
    {
        mLoadField.setText("" + SpeedControllerWrapperJni.getMotorSimStaticModelConfig(mHandle));
    }
}

class RotationalLoadSimConfigPanel extends MotorSimWithModelPanel
{
    public RotationalLoadSimConfigPanel(int aKey)
    {
        super(aKey);
    }

    @Override
    public void submit()
    {
        SimulationConnectorJni.setSpeedControllerModel_Rotational(mHandle, mMotorSimPanel.getMotorConfig(), 1, 1);
    }

    @Override
    protected void updateSimulatorParams()
    {

    }
}

class GravitationalLoadSimConfigPanel extends MotorSimWithModelPanel
{
    protected JTextField mLoadField;

    public GravitationalLoadSimConfigPanel(int aKey)
    {
        super(aKey);

        mLoadField = new JTextField(5);

        JPanel simPanel = new JPanel();
        simPanel.setBorder(new TitledBorder("Simulator Parameters"));
        simPanel.add(new JLabel("Load"));
        simPanel.add(mLoadField);

        add(simPanel, BorderLayout.NORTH);
    }

    @Override
    public void submit()
    {
        double load = Double.parseDouble(mLoadField.getText());
        SimulationConnectorJni.setSpeedControllerModel_Gravitational(mHandle, mMotorSimPanel.getMotorConfig(), load);
    }

    @Override
    protected void updateSimulatorParams()
    {
        mLoadField.setText("" + SpeedControllerWrapperJni.getMotorSimGravitationalModelConfig(mHandle));
    }
}
