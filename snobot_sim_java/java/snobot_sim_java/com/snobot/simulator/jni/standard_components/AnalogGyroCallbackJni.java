package com.snobot.simulator.jni.standard_components;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.jni.HalCallbackValue;
import com.snobot.simulator.simulator_components.gyro.AnalogGyroWrapper;

public final class AnalogGyroCallbackJni
{
    private static final Logger sLOGGER = LogManager.getLogger(AnalogGyroCallbackJni.class);

    private AnalogGyroCallbackJni()
    {

    }

    public static native void setAnalogGyroAngle(int aHandle, double aAngle);

    public static native void registerAnalogGyroCallback(String aFunctionName);

    public static void registerAnalogGyroCallback()
    {
        registerAnalogGyroCallback("analogGyroCallback");
    }

    public static native void reset();

    public static void analogGyroCallback(String aCallbackType, int aPort, HalCallbackValue aHalValue)
    {
        if ("Initialized".equals(aCallbackType))
        {
            AnalogGyroWrapper wrapper = new AnalogGyroWrapper(aPort, "Analog Gyro");
            SensorActuatorRegistry.get().register(wrapper, aPort);
        }
        else if ("Angle".equals(aCallbackType))
        {
            SensorActuatorRegistry.get().getGyros().get(aPort).setAngle(aHalValue.mDouble);
        }
        else
        {
            sLOGGER.log(Level.ERROR, "Unknown AnalogGyro callback " + aCallbackType + " - " + aHalValue);
        }
    }
}
