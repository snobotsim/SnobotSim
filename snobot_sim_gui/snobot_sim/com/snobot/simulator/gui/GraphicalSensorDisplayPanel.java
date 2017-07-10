package com.snobot.simulator.gui;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.snobot.simulator.gui.module_widget.AnalogOutputDisplay;
import com.snobot.simulator.gui.module_widget.DigitalSourceGraphicDisplay;
import com.snobot.simulator.gui.module_widget.EncoderGraphicDisplay;
import com.snobot.simulator.gui.module_widget.GyroGraphicDisplay;
import com.snobot.simulator.gui.module_widget.RelayGraphicDisplay;
import com.snobot.simulator.gui.module_widget.SolenoidGraphicDisplay;
import com.snobot.simulator.gui.module_widget.SpeedControllerGraphicDisplay;
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

    private SpeedControllerGraphicDisplay mSpeedControllerPanel;
    private SolenoidGraphicDisplay mSolenoidPanel;
    private DigitalSourceGraphicDisplay mDigitalSourcePanel;
    private RelayGraphicDisplay mRelayPanel;
    private AnalogOutputDisplay mAnalogPanel;
    private EncoderGraphicDisplay mEncoderPanel;
    private GyroGraphicDisplay mGyroPanel;

    public void create()
    {
        List<Integer> speedControllers = IntStream.of(SpeedControllerWrapperJni.getPortList()).boxed().collect(Collectors.toList());
        List<Integer> digitalSource = IntStream.of(DigitalSourceWrapperJni.getPortList()).boxed().collect(Collectors.toList());
        List<Integer> relaySource = IntStream.of(RelayWrapperJni.getPortList()).boxed().collect(Collectors.toList());
        List<Integer> analogSource = IntStream.of(AnalogSourceWrapperJni.getPortList()).boxed().collect(Collectors.toList());
        List<Integer> encoders = IntStream.of(EncoderWrapperJni.getPortList()).boxed().collect(Collectors.toList());
        List<Integer> solenoids = IntStream.of(SolenoidWrapperJni.getPortList()).boxed().collect(Collectors.toList());
        List<Integer> gyros = IntStream.of(GyroWrapperJni.getPortList()).boxed().collect(Collectors.toList());

        mSpeedControllerPanel = new SpeedControllerGraphicDisplay(speedControllers);
        mSolenoidPanel = new SolenoidGraphicDisplay(solenoids);
        mDigitalSourcePanel = new DigitalSourceGraphicDisplay(digitalSource);
        mRelayPanel = new RelayGraphicDisplay(relaySource);
        mAnalogPanel = new AnalogOutputDisplay(analogSource);
        mEncoderPanel = new EncoderGraphicDisplay(encoders, "Encoders");
        mGyroPanel = new GyroGraphicDisplay(gyros);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        if (!mSpeedControllerPanel.isEmpty())
        {
            add(mSpeedControllerPanel);
        }
        if (!mSolenoidPanel.isEmpty())
        {
            add(mSolenoidPanel);
        }
        if (!mDigitalSourcePanel.isEmpty())
        {
            add(mDigitalSourcePanel);
        }
        if (!mRelayPanel.isEmpty())
        {
            add(mRelayPanel);
        }
        if (!mAnalogPanel.isEmpty())
        {
            add(mAnalogPanel);
        }
        if (!mEncoderPanel.isEmpty())
        {
            add(mEncoderPanel);
        }
        if(!mGyroPanel.isEmpty())
        {
        	add(mGyroPanel);
        }
    }

    public void update()
    {
        mSpeedControllerPanel.update();
        mSolenoidPanel.update();
        mDigitalSourcePanel.update();
        mRelayPanel.update();
        mAnalogPanel.update();
        mEncoderPanel.update();
        mGyroPanel.update();

        repaint();
    }

    public void showSettingsButtons(boolean aShow)
    {

        mSpeedControllerPanel.showSettingsButtons(aShow);
        mSolenoidPanel.showSettingsButtons(aShow);
        mDigitalSourcePanel.showSettingsButtons(aShow);
        mRelayPanel.showSettingsButtons(aShow);
        mAnalogPanel.showSettingsButtons(aShow);
        mEncoderPanel.showSettingsButtons(aShow);
        mGyroPanel.showSettingsButtons(aShow);
    }
}
