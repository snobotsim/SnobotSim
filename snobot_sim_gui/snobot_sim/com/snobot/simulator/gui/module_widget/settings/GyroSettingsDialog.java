package com.snobot.simulator.gui.module_widget.settings;

import java.awt.BorderLayout;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.snobot.simulator.jni.module_wrapper.SpeedControllerWrapperJni;

public class GyroSettingsDialog extends SimpleSettingsDialog
{
    protected JComboBox<SpeedControllerOption> mSpeedControllerSelection;

    public GyroSettingsDialog(String aTitle, int aKey, String aName)
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

        JPanel scSelectionPanel = new JPanel();
        scSelectionPanel.add(new JLabel("Connected SC"));
        scSelectionPanel.add(mSpeedControllerSelection);
        getContentPane().add(scSelectionPanel, BorderLayout.CENTER);
    }

    @Override
    public void setVisible(boolean aVisible)
    {
        super.setVisible(aVisible);
    }

    @Override
    protected void onSubmit()
    {
        SpeedControllerOption option = (SpeedControllerOption) mSpeedControllerSelection.getSelectedItem();
        int scId = option == null ? -1 : option.mHandle;

        // EncoderWrapperJni.connectSpeedController(mHandle, scId);
    }

}
