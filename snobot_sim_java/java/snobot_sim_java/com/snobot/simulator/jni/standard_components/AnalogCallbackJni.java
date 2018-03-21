package com.snobot.simulator.jni.standard_components;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.jni.HalCallbackValue;
import com.snobot.simulator.module_wrapper.AnalogWrapper;
import com.snobot.simulator.module_wrapper.AnalogWrapper.VoltageSetterHelper;

public final class AnalogCallbackJni
{
    private static final Logger sLOGGER = LogManager.getLogger(AnalogCallbackJni.class);

    private AnalogCallbackJni()
    {

    }

    public static native void setAnalogVoltage(int aHandle, double aVoltage);

    public static native void registerAnalogCallback(String aFunctionName);

    public static void registerAnalogCallback()
    {
        registerAnalogCallback("analogCallback");
    }

    public static native void reset();

    public static void analogCallback(String aCallbackType, int aPort, HalCallbackValue aHalValue)
    {
        if ("Initialized".equals(aCallbackType))
        {
            SensorActuatorRegistry.get().register(new AnalogWrapper(aPort, new VoltageSetterHelper()
            {

                @Override
                public void setVoltage(double aVoltage)
                {
                    setAnalogVoltage(aPort, aVoltage);
                }
            }), aPort);
        }
        else
        {
            sLOGGER.log(Level.ERROR, "Unknown Analog callback " + aCallbackType + " - " + aHalValue);
        }
    }
}
