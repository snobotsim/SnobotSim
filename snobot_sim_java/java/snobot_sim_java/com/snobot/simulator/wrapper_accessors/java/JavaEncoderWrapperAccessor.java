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
    public boolean connectSpeedController(int aEncoderHandle, int aSpeedControllerHandle)
    {
        boolean success = false;

        EncoderWrapper encoder = SensorActuatorRegistry.get().getEncoders().get(aEncoderHandle);
        PwmWrapper speedController = SensorActuatorRegistry.get().getSpeedControllers().get(aSpeedControllerHandle);
        if (encoder != null && speedController != null)
        {
            speedController.setFeedbackSensor(encoder);
            success = true;
        }
        else
        {
            System.err.println("Could not conenct SC to ENC... " + aEncoderHandle + ", " + aSpeedControllerHandle);
        }

        return success;
    }

    private PwmWrapper getConnectedPwm(int aPort)
    {
        EncoderWrapper encWrapper = getValue(aPort);
        for (PwmWrapper pwmWrapper : SensorActuatorRegistry.get().getSpeedControllers().values())
        {
            if (pwmWrapper.getFeedbackSensor().equals(encWrapper))
            {
                return pwmWrapper;
            }
        }
        return null;
    }

    @Override
    public boolean isHookedUp(int aPort)
    {
        return getConnectedPwm(aPort) != null;
    }

    @Override
    public int getHookedUpId(int aPort)
    {
        PwmWrapper wrapper = getConnectedPwm(aPort);
        if (wrapper != null)
        {
            return wrapper.getHandle();
        }
        return -1;
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
