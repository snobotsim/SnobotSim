package com.snobot.simulator.gui.joysticks.sub_panels;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.snobot.simulator.joysticks.IMockJoystick;

public class WrappedJoystickPanel extends JPanel
{
    private IMockJoystick mJoystick;
    private JPanel digitalPanel;
    private JPanel analogPanel;
    private List<AnalogControllerInputPanel> mAnalogDisplays;
    private List<DigitalControllerInputPanel> mDigitalDisplays;

    public WrappedJoystickPanel()
    {
        this(null);
    }

    public WrappedJoystickPanel(IMockJoystick aJoystick)
    {
        initComponents();
        setBackground(Color.green);

        setJoystick(aJoystick);
    }

    public void setJoystick(IMockJoystick aJoystick)
    {
        mJoystick = aJoystick;
        analogPanel.removeAll();
        digitalPanel.removeAll();

        if (mJoystick != null)
        {
            mAnalogDisplays = new ArrayList<>();
            mDigitalDisplays = new ArrayList<>();

            for (int i = 0; i < mJoystick.getAxisCount(); ++i)
            {
                AnalogControllerInputPanel panel = new AnalogControllerInputPanel(i);
                mAnalogDisplays.add(panel);
                analogPanel.add(panel);
            }
            for (int i = 0; i < mJoystick.getButtonCount(); ++i)
            {
                DigitalControllerInputPanel panel = new DigitalControllerInputPanel(i);
                mDigitalDisplays.add(panel);
                digitalPanel.add(panel);
            }
        }

        repaint();
    }

    public void updateDisplay()
    {
        if (mJoystick != null)
        {
            float[] axisValues = mJoystick.getAxisValues();
            int buttonMask = mJoystick.getButtonMask();

            for (int i = 0; i < axisValues.length; ++i)
            {
                AnalogControllerInputPanel panel = mAnalogDisplays.get(i);
                panel.setValue(axisValues[i]);
            }
            for (int i = 0; i < mJoystick.getButtonCount(); ++i)
            {
                DigitalControllerInputPanel panel = mDigitalDisplays.get(i);
                boolean active = (buttonMask & (1 << i)) != 0;
                panel.setValue(active);
            }
        }
        else
        {
            System.err.println("Joystick is null");
        }
    }

    private void initComponents()
    {
        digitalPanel = new JPanel();
        analogPanel = new JPanel();

        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                        .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                .addComponent(analogPanel, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE).addComponent(digitalPanel,
                                        GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE))
                        .addGap(0)));
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup().addComponent(analogPanel, GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                        .addPreferredGap(ComponentPlacement.RELATED).addComponent(digitalPanel, GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                        .addGap(0)));
        setLayout(groupLayout);
    }

}
