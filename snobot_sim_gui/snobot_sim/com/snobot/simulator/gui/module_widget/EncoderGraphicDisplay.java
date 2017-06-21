package com.snobot.simulator.gui.module_widget;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.snobot.simulator.jni.module_wrapper.EncoderWrapperJni;
import com.snobot.simulator.jni.module_wrapper.SpeedControllerWrapperJni;

public class EncoderGraphicDisplay extends BaseWidgetDisplay<Integer, EncoderWrapperDisplay>
{

    public EncoderGraphicDisplay(Collection<Integer> aKeys, String aString)
    {
        super(aKeys);
        setBorder(new TitledBorder(aString));
    }

    @Override
    public void update()
    {
        for (Entry<Integer, EncoderWrapperDisplay> pair : mWidgetMap.entrySet())
        {
            int key = pair.getKey();
            boolean isConnected = EncoderWrapperJni.isHookedUp(key);
            double raw = EncoderWrapperJni.getRaw(key);
            double distance = EncoderWrapperJni.getDistance(key);

            pair.getValue().updateDisplay(isConnected, raw, distance);
        }
    }

    @Override
    protected EncoderWrapperDisplay createWidget(Integer pair)
    {
        return new EncoderWrapperDisplay();
    }

    @Override
    protected JDialog createSettingsDialog(Integer aKey)
    {
        JDialog dialog = new JDialog();

        class SpeedControllerOption
        {
            int mHandle;
            String mName;

            public SpeedControllerOption(int aHandle, String aName)
            {
                mHandle = aHandle;
                mName = aName;
            }

            @Override
            public String toString()
            {
                return mName + "(" + mHandle + ")";
            }
        }

        JComboBox<SpeedControllerOption> speedControllerSelector = new JComboBox<>();

        List<Integer> speedControllers = IntStream.of(SpeedControllerWrapperJni.getPortList()).boxed().collect(Collectors.toList());
        for (int handle : speedControllers)
        {
            speedControllerSelector.addItem(new SpeedControllerOption(handle, SpeedControllerWrapperJni.getName(handle)));
        }

        dialog.setTitle("Encoder " + aKey + " Settings");
        dialog.getContentPane().add(new JTextField(getName(aKey)));
        dialog.getContentPane().add(speedControllerSelector);
        dialog.pack();

        return dialog;
    }

    @Override
    protected String getName(Integer aKey)
    {
        return EncoderWrapperJni.getName(aKey);
    }
}

class EncoderWrapperDisplay extends JPanel
{

    private JTextField mRawField;
    private JTextField mDistanceField;

    public EncoderWrapperDisplay()
    {
        mRawField = new JTextField(6);
        mDistanceField = new JTextField(6);
        add(mRawField);
        add(mDistanceField);
    }

    public void updateDisplay(boolean aHasConnection, double aRaw, double aDistance)
    {
        if (aHasConnection)
        {
            DecimalFormat df = new DecimalFormat("#.###");
            mRawField.setText("" + aRaw);
            mDistanceField.setText(df.format(aDistance));
        }
        else
        {
            mRawField.setText("Not Hooked Up");
            mDistanceField.setText("Not Hooked Up");
        }
    }
}