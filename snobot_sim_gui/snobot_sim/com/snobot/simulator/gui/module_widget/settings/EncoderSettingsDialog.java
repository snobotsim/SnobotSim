package com.snobot.simulator.gui.module_widget.settings;

import java.awt.BorderLayout;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.snobot.simulator.jni.module_wrapper.EncoderWrapperJni;
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

        mSpeedControllerSelection = new JComboBox<>();
        mSpeedControllerSelection.addItem(new SpeedControllerOption(-1, "None"));

        List<Integer> speedControllers = IntStream.of(SpeedControllerWrapperJni.getPortList()).boxed().collect(Collectors.toList());
        for (int handle : speedControllers)
        {
            SpeedControllerOption option = new SpeedControllerOption(handle, SpeedControllerWrapperJni.getName(handle));
            mSpeedControllerSelection.addItem(option);
        }

        selectAttachedControllerAndRefreshNames();

        JPanel scSelectionPanel = new JPanel();
        scSelectionPanel.add(new JLabel("Connected SC"));
        scSelectionPanel.add(mSpeedControllerSelection);
        getContentPane().add(scSelectionPanel, BorderLayout.CENTER);
    }

    @Override
    public void setVisible(boolean aVisible)
    {
        selectAttachedControllerAndRefreshNames();
        super.setVisible(aVisible);
    }

    private void selectAttachedControllerAndRefreshNames()
    {
        int connectedSc = -1;
        if (EncoderWrapperJni.isHookedUp(mHandle))
        {
            connectedSc = EncoderWrapperJni.getHookedUpId(mHandle);
        }

        for (int i = 0; i < mSpeedControllerSelection.getItemCount(); ++i)
        {
            SpeedControllerOption option = mSpeedControllerSelection.getItemAt(i);

            if (option.mHandle == connectedSc)
            {
                mSpeedControllerSelection.setSelectedIndex(i);
            }
            if (option.mHandle != -1)
            {
                option.mName = SpeedControllerWrapperJni.getName(option.mHandle);
            }
        }
    }

    @Override
    protected void onSubmit()
    {
        SpeedControllerOption option = (SpeedControllerOption) mSpeedControllerSelection.getSelectedItem();
        int scId = option == null ? -1 : option.mHandle;

        EncoderWrapperJni.connectSpeedController(mHandle, scId);
    }

}
