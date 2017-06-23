package com.snobot.simulator.gui.module_widget;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Collection;
import java.util.Map.Entry;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.snobot.simulator.gui.module_widget.settings.SimpleSettingsDialog;
import com.snobot.simulator.jni.module_wrapper.RelayWrapperJni;

import edu.wpi.first.wpilibj.Relay.Value;

public class RelayGraphicDisplay extends BaseWidgetDisplay<Integer, RelayDisplay>
{


    public RelayGraphicDisplay(Collection<Integer> aKeys)
    {
        super(aKeys);
        setBorder(new TitledBorder("Relay"));
    }

    @Override
    public void update()
    {
        for (Entry<Integer, RelayDisplay> pair : mWidgetMap.entrySet())
        {
            int key = pair.getKey();
            boolean forward = RelayWrapperJni.getFowardValue(key);
            boolean reverse = RelayWrapperJni.getReverseValue(key);

            pair.getValue().updateDisplay(convertValue(forward, reverse));
        }
    }

    private Value convertValue(boolean forwards, boolean reverse)
    {
        if (forwards && !reverse)
        {
            return Value.kForward;
        }
        else if (!forwards && reverse)
        {
            return Value.kReverse;
        }
        else if (forwards && reverse)
        {
            return Value.kOn;
        }
        else
        {
            return Value.kOff;
        }
    }

    @Override
    protected RelayDisplay createWidget(Integer aKey)
    {
        return new RelayDisplay();
    }

    @Override
    protected JDialog createSettingsDialog(Integer aKey)
    {
        SimpleSettingsDialog dialog = new SimpleSettingsDialog("Relay " + aKey + " Settings", aKey, getName(aKey))
        {

            @Override
            protected void onSubmit()
            {
                RelayWrapperJni.setName(aKey, getComponentName());
                mLabelMap.get(aKey).setText(getComponentName());
            }

        };

        dialog.pack();

        return dialog;
    }

    @Override
    protected String getName(Integer aKey)
    {
        return RelayWrapperJni.getName(aKey);
    }
}

class RelayDisplay extends JPanel
{
    private static final int sWIDTH = 80;
    private static final int sHEIGHT = 15;

    private Value mValue;

    public RelayDisplay()
    {
        setPreferredSize(new Dimension(sWIDTH, sHEIGHT));
        mValue = Value.kOff;
    }

    public void updateDisplay(Value value)
    {
        mValue = value;
    }

    @Override
    public void paint(Graphics g)
    {
        g.clearRect(0, 0, getWidth(), getHeight());
        g.drawRect(0, 0, getWidth(), getHeight());

        switch (mValue)
        {
        case kOff:
            break;
        case kOn:
            g.setColor(Color.green);
            g.fillRect(0, 0, sWIDTH / 2, sHEIGHT);

            g.setColor(Color.red);
            g.fillRect(sWIDTH / 2, 0, sWIDTH, sHEIGHT);
            break;
        case kForward:
            g.setColor(Color.green);
            g.fillRect(0, 0, sWIDTH, sHEIGHT);
            break;
        case kReverse:
            g.setColor(Color.red);
            g.fillRect(0, 0, sWIDTH, sHEIGHT);
            break;
        }
    }
}