package com.snobot.simulator.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.snobot.simulator.gui.module_widget.AccelerometerOutputDisplay;
import com.snobot.simulator.gui.module_widget.AnalogOutputDisplay;
import com.snobot.simulator.gui.module_widget.BaseWidgetDisplay;
import com.snobot.simulator.gui.module_widget.DigitalSourceGraphicDisplay;
import com.snobot.simulator.gui.module_widget.EncoderGraphicDisplay;
import com.snobot.simulator.gui.module_widget.GyroGraphicDisplay;
import com.snobot.simulator.gui.module_widget.RelayGraphicDisplay;
import com.snobot.simulator.gui.module_widget.SolenoidGraphicDisplay;
import com.snobot.simulator.gui.module_widget.SpeedControllerGraphicDisplay;
import com.snobot.simulator.jni.module_wrapper.AccelerometerWrapperJni;
import com.snobot.simulator.jni.module_wrapper.AnalogSourceWrapperJni;
import com.snobot.simulator.jni.module_wrapper.DigitalSourceWrapperJni;
import com.snobot.simulator.jni.module_wrapper.EncoderWrapperJni;
import com.snobot.simulator.jni.module_wrapper.GyroWrapperJni;
import com.snobot.simulator.jni.module_wrapper.RelayWrapperJni;
import com.snobot.simulator.jni.module_wrapper.SolenoidWrapperJni;
import com.snobot.simulator.jni.module_wrapper.SpeedControllerWrapperJni;

public class GraphicalSensorDisplayPanel extends JPanel
{
    public GraphicalSensorDisplayPanel()
    {
        create();
    }

    private List<BaseWidgetDisplay<?, ?>> mDisplayPanels;

    public void create()
    {
        mDisplayPanels = new ArrayList<>();

        mDisplayPanels.add(new SpeedControllerGraphicDisplay(IntStream.of(SpeedControllerWrapperJni.getPortList()).boxed().collect(Collectors.toList())));
        mDisplayPanels.add(new SolenoidGraphicDisplay(IntStream.of(SolenoidWrapperJni.getPortList()).boxed().collect(Collectors.toList())));
        mDisplayPanels.add(new DigitalSourceGraphicDisplay(IntStream.of(DigitalSourceWrapperJni.getPortList()).boxed().collect(Collectors.toList())));
        mDisplayPanels.add(new RelayGraphicDisplay(IntStream.of(RelayWrapperJni.getPortList()).boxed().collect(Collectors.toList())));
        mDisplayPanels.add(new AnalogOutputDisplay(IntStream.of(AnalogSourceWrapperJni.getPortList()).boxed().collect(Collectors.toList())));
        mDisplayPanels.add(new EncoderGraphicDisplay(IntStream.of(EncoderWrapperJni.getPortList()).boxed().collect(Collectors.toList()), "Encoders"));
        mDisplayPanels.add(new GyroGraphicDisplay(IntStream.of(GyroWrapperJni.getPortList()).boxed().collect(Collectors.toList())));
        mDisplayPanels.add(new AccelerometerOutputDisplay(IntStream.of(AccelerometerWrapperJni.getPortList()).boxed().collect(Collectors.toList())));

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        for(BaseWidgetDisplay<?, ?> panel : mDisplayPanels)
        {
            if(!panel.isEmpty())
            {
                add(panel);
            }
        }
    }

    public void update()
    {
        for(BaseWidgetDisplay<?, ?> panel : mDisplayPanels)
        {
            panel.update();
        }

        repaint();
    }

    public void showSettingsButtons(boolean aShow)
    {
        for(BaseWidgetDisplay<?, ?> panel : mDisplayPanels)
        {
            panel.showSettingsButtons(aShow);
        }
    }
}
