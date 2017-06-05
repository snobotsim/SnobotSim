package com.snobot.simulator.gui.module_widget;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Map.Entry;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.snobot.simulator.gui.module_widget.settings.SimpleSettingsDialog;
import com.snobot.simulator.jni.module_wrapper.DigitalSourceWrapperJni;

public class DigitalSourceGraphicDisplay extends BaseWidgetDisplay<Integer, DigitalSourceWrapperDisplay>
{

    public DigitalSourceGraphicDisplay(Collection<Integer> aKeys)
    {
        super(aKeys);
        setBorder(new TitledBorder("Digital IO"));
    }

    @Override
    public void update()
    {
        for (Entry<Integer, DigitalSourceWrapperDisplay> pair : mWidgetMap.entrySet())
        {
            boolean value = DigitalSourceWrapperJni.getState(pair.getKey());
            pair.getValue().updateDisplay(value);
        }
    }

    @Override
    protected DigitalSourceWrapperDisplay createWidget(Integer aKey)
    {
        if (DigitalSourceWrapperJni.getWantsHidden(aKey))
        {
            return null;
        }
        return new DigitalSourceWrapperDisplay();
    }

    @Override
    protected JDialog createSettingsDialog(Integer aKey)
    {
        SimpleSettingsDialog dialog = new SimpleSettingsDialog("Digital " + aKey + " Settings", aKey, getName(aKey));
        dialog.addSubmitListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                DigitalSourceWrapperJni.setName(aKey, dialog.getComponentName());
                mLabelMap.get(aKey).setText(dialog.getComponentName());
            }
        });
        dialog.pack();

        return dialog;
    }

    @Override
    protected String getName(Integer aKey)
    {
        return DigitalSourceWrapperJni.getName(aKey);
    }
}

class DigitalSourceWrapperDisplay extends JPanel
{
    private static final int sDOT_SIZE = 30;

    private boolean mState;

    public DigitalSourceWrapperDisplay()
    {
        setPreferredSize(new Dimension(sDOT_SIZE, sDOT_SIZE));
    }

    public void updateDisplay(boolean aValue)
    {
        mState = aValue;
    }

    @Override
    public void paint(Graphics g)
    {
        g.clearRect(0, 0, getWidth(), getHeight());
        g.setColor(mState ? Color.green : Color.red);
        g.fillOval(0, 0, sDOT_SIZE, sDOT_SIZE);
    }
}