package com.snobot.simulator.jni.standard_components;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.jni.HalCallbackValue;
import com.snobot.simulator.simulator_components.gyro.AnalogGyroWrapper;

public class AnalogGyroCallbackJni
{
    private static final Logger sLOGGER = Logger.getLogger(AnalogGyroCallbackJni.class);

    public static native void setAnalogGyroAngle(int aHandle, double aAngle);

    public static native void registerAnalogGyroCallback(String functionName);

    public static native void reset();

    public static void registerAnalogGyroCallback()
    {
        registerAnalogGyroCallback("analogGyroCallback");
    }

    public static void analogGyroCallback(String callbackType, int port, HalCallbackValue halValue)
    {
        if ("Initialized".equals(callbackType))
        {
            AnalogGyroWrapper wrapper = new AnalogGyroWrapper(port, "Analog Gyro");
            SensorActuatorRegistry.get().register(wrapper, port);
        }
        else if ("Angle".equals(callbackType))
        {
            SensorActuatorRegistry.get().getGyros().get(port).setAngle(halValue.mDouble);
        }
        else
        {
            sLOGGER.log(Level.ERROR, "Unknown AnalogGyro callback " + callbackType + " - " + halValue);
        }
    }
}
