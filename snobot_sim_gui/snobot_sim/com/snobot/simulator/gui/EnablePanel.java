package com.snobot.simulator.gui;

import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class EnablePanel extends JPanel
{
    private JCheckBox mEnableButton = new JCheckBox("Enabled");
    private JCheckBox mAutonButton = new JCheckBox("Autonomous");
    private JLabel mTimeLabel = new JLabel("Time: 000.00");

    public EnablePanel()
    {
        add(mEnableButton);
        add(mAutonButton);
        add(mTimeLabel);
    }

    public void addStateChangedListener(ActionListener aListener)
    {
        mEnableButton.addActionListener(aListener);
        mAutonButton.addActionListener(aListener);
    }

    @Override
    public boolean isEnabled()
    {
        return mEnableButton.isSelected();
    }

    public boolean isAuton()
    {
        return mAutonButton.isSelected();
    }

    public void setTime(double aTime)
    {
        DecimalFormat df = new DecimalFormat("000.00");
        mTimeLabel.setText("Time: " + df.format(aTime));
    }

    public void setRobotEnabled(boolean state)
    {
        mEnableButton.setSelected(state);
    }
}
