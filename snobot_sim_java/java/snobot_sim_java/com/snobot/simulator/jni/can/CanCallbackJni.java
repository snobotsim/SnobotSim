package com.snobot.simulator.jni.can;

import java.nio.ByteBuffer;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.snobot.simulator.jni.HalCallbackValue;
import com.snobot.simulator.simulator_components.ctre.CtreManager;

public class CanCallbackJni
{
    private static final Logger sLOGGER = Logger.getLogger(CanCallbackJni.class);

    public static final CtreManager sCAN_MANAGER = new CtreManager();

    public static native void registerCanCallback(String functionName);

    public static native void reset();

    public static void canCallback(String callbackType, int port, HalCallbackValue halValue)
    {
        sLOGGER.log(Level.ERROR, "Unsupported");
    }

    public static void registerCanCallback()
    {
        registerCanCallback("canCallback");
    }

    public static void canCallbackMotorController(String aName, int aPort, ByteBuffer aData)
    {
        sCAN_MANAGER.handleMotorControllerMessage(aName, aPort, aData);
    }

    public static void canCallbackPigeon(String aName, int aPort, ByteBuffer aData)
    {
        sCAN_MANAGER.handlePigeonMessage(aName, aPort, aData);
    }

}
