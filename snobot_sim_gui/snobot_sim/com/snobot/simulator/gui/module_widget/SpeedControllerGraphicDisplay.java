package com.snobot.simulator.gui.module_widget;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Map.Entry;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.snobot.simulator.gui.Util;
import com.snobot.simulator.gui.module_widget.settings.SimpleSettingsDialog;
import com.snobot.simulator.jni.module_wrapper.SpeedControllerWrapperJni;

public class SpeedControllerGraphicDisplay extends BaseWidgetDisplay<Integer, MotorDisplay>
{

    public SpeedControllerGraphicDisplay(Collection<Integer> aKeys)
    {
        super(aKeys);
        setBorder(new TitledBorder("Speed Controllers"));
    }

    @Override
    public void update()
    {
        for (Entry<Integer, MotorDisplay> pair : mWidgetMap.entrySet())
        {
            double value = SpeedControllerWrapperJni.getVoltagePercentage(pair.getKey());
            pair.getValue().updateDisplay(value);
        }
    }

    @Override
    protected MotorDisplay createWidget(Integer aKey)
    {
        return new MotorDisplay();
    }

    @Override
    protected JDialog createSettingsDialog(Integer aKey)
    {
        SimpleSettingsDialog dialog = new SimpleSettingsDialog("Speed Controller " + aKey + " Settings", aKey, getName(aKey));
        dialog.addSubmitListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                SpeedControllerWrapperJni.setName(aKey, dialog.getComponentName());
                mLabelMap.get(aKey).setText(dialog.getComponentName());
            }
        });
        dialog.pack();

        return dialog;
    }

    @Override
    protected String getName(Integer aKey)
    {
        return SpeedControllerWrapperJni.getName(aKey);
    }
}

class MotorDisplay extends JPanel
{
    private static final int sDOT_SIZE = 30;

    private double mMotorSpeed;

    public MotorDisplay()
    {
        setPreferredSize(new Dimension(sDOT_SIZE, sDOT_SIZE));
    }

    public void updateDisplay(double aValue)
    {
        mMotorSpeed = aValue;
    }

    @Override
    public void paint(Graphics g)
    {
        g.clearRect(0, 0, getWidth(), getHeight());
        g.setColor(Util.getMotorColor(mMotorSpeed));
        g.fillOval(0, 0, sDOT_SIZE, sDOT_SIZE);
    }
}