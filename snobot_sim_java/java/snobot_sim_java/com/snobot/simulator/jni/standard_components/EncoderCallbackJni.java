package com.snobot.simulator.jni.standard_components;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.jni.HalCallbackValue;
import com.snobot.simulator.module_wrapper.EncoderWrapper;
import com.snobot.simulator.module_wrapper.EncoderWrapper.DistanceSetterHelper;

public class EncoderCallbackJni
{
    private static final Logger sLOGGER = Logger.getLogger(EncoderCallbackJni.class);

    public static native void setEncoderDistance(int aHandle, double aAngle);

    public static native void registerEncoderCallback(String functionName);

    public static native void reset();

    public static void registerEncoderCallback()
    {
        registerEncoderCallback("encoderCallback");
    }

    public static void encoderCallback(String callbackType, int port, HalCallbackValue halValue)
    {
        if ("Initialized".equals(callbackType))
        {
            SensorActuatorRegistry.get().register(new EncoderWrapper(port, new DistanceSetterHelper()
            {

                @Override
                public void setDistance(double aDistance)
                {
                    setEncoderDistance(port, aDistance);
                }
            }), port);
        }
        else if ("Count".equals(callbackType))
        {
            sLOGGER.log(Level.DEBUG, "Ignoring count feedback");
        }
        else if ("Reset".equals(callbackType))
        {
            SensorActuatorRegistry.get().getEncoders().get(port).reset();
        }
        else
        {
            sLOGGER.log(Level.ERROR, "Unknown Encoder callback " + callbackType + " - " + halValue);
        }
    }
}
