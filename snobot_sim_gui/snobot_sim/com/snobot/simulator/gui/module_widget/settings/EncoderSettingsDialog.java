package com.snobot.simulator.gui.module_widget.settings;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.JComboBox;

import com.snobot.simulator.jni.module_wrapper.SpeedControllerWrapperJni;

public class EncoderSettingsDialog extends SimpleSettingsDialog
{
    private class SpeedControllerOption
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

    protected JComboBox<SpeedControllerOption> mSpeedControllerSelection;

    public EncoderSettingsDialog(String aTitle, int aKey, String aName)
    {
        super(aTitle, aKey, aName);

    }

    // @Override
    // protected void initComponents()
    // {
    // super.initComponents();
    //
    // mSpeedControllerSelection = new JComboBox<>();
    // mContentPane.add(mSpeedControllerSelection, BorderLayout.CENTER);
    // }

    @Override
    public void setVisible(boolean aVisible)
    {
        mSpeedControllerSelection.removeAllItems();

        List<Integer> speedControllers = IntStream.of(SpeedControllerWrapperJni.getPortList()).boxed().collect(Collectors.toList());
        for (int handle : speedControllers)
        {
            mSpeedControllerSelection.addItem(new SpeedControllerOption(handle, SpeedControllerWrapperJni.getName(handle)));
        }

        super.setVisible(aVisible);
    }

    public int getSpeedControllerId()
    {
        SpeedControllerOption scId = (SpeedControllerOption) mSpeedControllerSelection.getSelectedItem();
        return scId == null ? -1 : scId.mHandle;
    }

}
