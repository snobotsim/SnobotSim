package com.snobot.simulator.jni.standard_components;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.jni.HalCallbackValue;
import com.snobot.simulator.module_wrapper.AnalogWrapper;
import com.snobot.simulator.module_wrapper.AnalogWrapper.VoltageSetterHelper;

public class AnalogCallbackJni
{
    private static final Logger sLOGGER = Logger.getLogger(AnalogCallbackJni.class);

    public static native void setAnalogVoltage(int aHandle, double aVoltage);

    public static native void registerAnalogCallback(String functionName);

    public static native void reset();

    public static void registerAnalogCallback()
    {
        registerAnalogCallback("analogCallback");
    }

    public static void analogCallback(String callbackType, int port, HalCallbackValue halValue)
    {
        if ("Initialized".equals(callbackType))
        {
            SensorActuatorRegistry.get().register(new AnalogWrapper(port, new VoltageSetterHelper()
            {

                @Override
                public void setVoltage(double aVoltage)
                {
                    setAnalogVoltage(port, aVoltage);
                }
            }), port);
        }
        else
        {
            sLOGGER.log(Level.ERROR, "Unknown Analog callback " + callbackType + " - " + halValue);
        }
    }
}
