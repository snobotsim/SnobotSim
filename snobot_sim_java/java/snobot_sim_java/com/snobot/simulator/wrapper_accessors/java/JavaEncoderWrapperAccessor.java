package com.snobot.simulator.wrapper_accessors.java;

import java.util.Map;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.module_wrapper.EncoderWrapper;
import com.snobot.simulator.module_wrapper.PwmWrapper;
import com.snobot.simulator.wrapper_accessors.EncoderWrapperAccessor;

public class JavaEncoderWrapperAccessor extends BaseWrapperAccessor<EncoderWrapper> implements EncoderWrapperAccessor
{
    @Override
    protected Map<Integer, EncoderWrapper> getMap()
    {
        return SensorActuatorRegistry.get().getEncoders();
    }

    @Override
    public int getHandle(int aPortA, int aPortB)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void connectSpeedController(int aEncoderHandle, int aSpeedControllerHandle)
    {
        EncoderWrapper encoder = SensorActuatorRegistry.get().getEncoders().get(aEncoderHandle);
        PwmWrapper speedController = SensorActuatorRegistry.get().getSpeedControllers().get(aSpeedControllerHandle);
        if (encoder != null && speedController != null)
        {
            speedController.setFeedbackSensor(encoder);
        }
        else
        {
            System.err.println("Could not conenct SC to ENC... " + aEncoderHandle + ", " + aSpeedControllerHandle);
        }
    }

    @Override
    public boolean isHookedUp(int aPort)
    {
        // return getValue(aPort).isHookedUp();
        return false;
    }

    @Override
    public int getHookedUpId(int aPort)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDistancePerTick(int aPort, double aDistancePerTick)
    {
        getValue(aPort).setDistancePerTick(aDistancePerTick);
    }

    @Override
    public double getDistancePerTick(int aPort)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getRaw(int aPort)
    {
        return getValue(aPort).getRaw();
    }

    @Override
    public double getDistance(int aPort)
    {
        return getValue(aPort).getPosition();
    }
}
