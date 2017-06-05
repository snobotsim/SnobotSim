package com.snobot.simulator.gui.joysticks.sub_panels;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DigitalControllerInputPanel extends JPanel
{
    private JCheckBox mCheckbox;
    private JLabel mLabel;

    public DigitalControllerInputPanel(int aIndex)
    {
        mCheckbox = new JCheckBox();
        mCheckbox.setEnabled(false);

        mLabel = new JLabel("" + aIndex);

        add(mLabel);
        add(mCheckbox);
    }

    public void setValue(boolean aValue)
    {
        mCheckbox.setSelected(aValue);
    }
}