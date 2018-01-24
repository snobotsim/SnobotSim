package com.snobot.simulator.jni.standard_components;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.jni.HalCallbackValue;
import com.snobot.simulator.module_wrapper.SolenoidWrapper;

public final class PcmCallbackJni
{
    private static final Logger sLOGGER = Logger.getLogger(PcmCallbackJni.class);

    private PcmCallbackJni()
    {

    }

    public static native void registerPcmCallback(String aFunctionName);

    public static void registerPcmCallback()
    {
        registerPcmCallback("pcmCallback");
    }

    public static native void reset();

    public static void pcmCallback(String aCallbackType, int aPort, HalCallbackValue aHalValue)
    {
        if ("SolenoidInitialized".equals(aCallbackType))
        {
            SensorActuatorRegistry.get().register(new SolenoidWrapper(aPort), aPort);
        }
        else if ("SolenoidOutput".equals(aCallbackType))
        {
            SensorActuatorRegistry.get().getSolenoids().get(aPort).set(aHalValue.mBoolean);
        }
        else
        {
            sLOGGER.log(Level.ERROR, "Unknown PCM callback " + aCallbackType + " - " + aHalValue);
        }
    }

}
