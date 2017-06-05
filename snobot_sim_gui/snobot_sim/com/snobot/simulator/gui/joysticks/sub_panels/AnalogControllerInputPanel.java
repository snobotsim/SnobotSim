package com.snobot.simulator.gui.joysticks.sub_panels;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class AnalogControllerInputPanel extends JPanel
{
    private JSlider mSlider;
    private JLabel mLabel;

    public AnalogControllerInputPanel(int aIndex)
    {
        mSlider = new JSlider(-127, 127);
        mSlider.setEnabled(false);
        mLabel = new JLabel("" + aIndex);

        add(mLabel);
        add(mSlider);
    }

    public void setValue(double aValue)
    {
        mSlider.setValue((int) aValue);
    }
}