package com.snobot.simulator.module_wrapper;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.module_wrapper.interfaces.IEncoderWrapper;
import com.snobot.simulator.module_wrapper.interfaces.IPwmWrapper;

public class BaseEncoderWrapper extends ASensorWrapper implements IEncoderWrapper 
{
    private static final Logger sLOGGER = LogManager.getLogger(BaseEncoderWrapper.class);

    protected double mPosition;
    protected double mVelocity;

    public BaseEncoderWrapper(String aName) 
    {
        super(aName);
    }

    @Override
    public void setPosition(double aPosition) 
    {
        mPosition = aPosition;
    }

    @Override
    public void setVelocity(double aVelocity) 
    {
        mVelocity = aVelocity;
    }

    @Override
    public double getPosition() 
    {
        return mPosition;
    }

    @Override
    public double getVelocity() 
    {
        return mVelocity;
    }

    @Override
    public void reset() 
    {
        for (IPwmWrapper pwmWrapper : SensorActuatorRegistry.get().getSpeedControllers().values()) 
        {
            if (pwmWrapper.getFeedbackSensor().equals(this))
            {
                pwmWrapper.reset();
            }
        }

        mPosition = 0;
        mVelocity = 0;
    }

    
    private IPwmWrapper getConnectedPwm()
    {
        for (IPwmWrapper pwmWrapper : SensorActuatorRegistry.get().getSpeedControllers().values())
        {
            if (pwmWrapper.getFeedbackSensor().equals(this))
            {
                return pwmWrapper;
            }
        }
        return null;
    }

    @Override
    public boolean isHookedUp() 
    {
        return getConnectedPwm() != null;
    }

    @Override
    public int getHookedUpId()
    {
        IPwmWrapper wrapper = getConnectedPwm();
        if (wrapper != null)
        {
            return wrapper.getHandle();
        }
        return -1;
    }

    @Override
    public boolean connectSpeedController(int aSpeedControllerHandle) 
    {
        boolean success = false;

        IPwmWrapper speedController = SensorActuatorRegistry.get().getSpeedControllers().get(aSpeedControllerHandle);
        if (speedController == null)
        {
            sLOGGER.log(Level.ERROR, "Could not conenct SC to ENC... " + aSpeedControllerHandle);
        }
        else
        {
            speedController.setFeedbackSensor(this);
            success = true;
        }

        return success;
    }
}
