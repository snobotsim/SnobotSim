package com.snobot.simulator.jni.standard_components;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.jni.HalCallbackValue;
import com.snobot.simulator.module_wrapper.RelayWrapper;

public final class RelayCallbackJni
{
    private static final Logger sLOGGER = Logger.getLogger(RelayCallbackJni.class);

    private RelayCallbackJni()
    {

    }

    public static native void reset();

    public static native void registerRelayCallback(String aFunctionName);

    public static void registerRelayCallback()
    {
        registerRelayCallback("relayCallback");
    }

    public static void relayCallback(String aCallbackType, int aPort, HalCallbackValue aHalValue)
    {
        if ("InitializedForward".equals(aCallbackType))
        {
            SensorActuatorRegistry.get().register(new RelayWrapper(aPort), aPort);
        }
        else if ("InitializedReverse".equals(aCallbackType))
        { // NOPMD
            // Nothing to do, assume it was initialized in forwards call
        }
        else if ("Forward".equals(aCallbackType))
        {
            SensorActuatorRegistry.get().getRelays().get(aPort).setRelayForwards(aHalValue.mBoolean);
        }
        else if ("Reverse".equals(aCallbackType))
        {
            SensorActuatorRegistry.get().getRelays().get(aPort).setRelayReverse(aHalValue.mBoolean);
        }
        else
        {
            sLOGGER.log(Level.ERROR, "Unknown Relay callback " + aCallbackType + " - " + aHalValue);
        }
    }

}
