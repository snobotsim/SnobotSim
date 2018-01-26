package com.snobot.simulator.jni.standard_components;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.jni.HalCallbackValue;
import com.snobot.simulator.simulator_components.II2CWrapper;
import com.snobot.simulator.simulator_components.components_factory.DefaultI2CSimulatorFactory;
import com.snobot.simulator.simulator_components.components_factory.II2cSimulatorFactory;

public final class I2CCallbackJni
{
    private static final Logger sLOGGER = Logger.getLogger(I2CCallbackJni.class);
    private static II2cSimulatorFactory sI2C_FACTORY = new DefaultI2CSimulatorFactory();
    private static final Map<Integer, II2CWrapper> sCUSTOM_WRAPPERS = new HashMap<>();

    private I2CCallbackJni()
    {

    }

    public static native void resetNative();

    public static void reset()
    {
        sCUSTOM_WRAPPERS.clear();
        resetNative();
    }

    public static void setI2CFactory(II2cSimulatorFactory aFactory)
    {
        sI2C_FACTORY = aFactory;
    }

    public static native void registerI2CCallback(String aFunctionName);

    public static void registerI2CCallback()
    {
        registerI2CCallback("i2cCallback");
    }

    public static native void registerReadWriteCallbacks(int aPort);

    public static void registerReadWriteCallbacks(int aPort, II2CWrapper aWrapper)
    {
        registerReadWriteCallbacks(aPort);
        sCUSTOM_WRAPPERS.put(aPort, aWrapper);
    }

    public static void i2cCallback(String aCallbackType, int aPort, ByteBuffer aBuffer)
    {
        if (sCUSTOM_WRAPPERS.containsKey(aPort))
        {
            II2CWrapper wrapper = sCUSTOM_WRAPPERS.get(aPort);
            if ("Read".equals(aCallbackType))
            {
                wrapper.handleRead(aBuffer);
            }
            else if ("Write".equals(aCallbackType))
            {
                wrapper.handleWrite(aBuffer);
            }
            else
            {
                sLOGGER.log(Level.ERROR, "Unknown I2C callback " + aCallbackType);
            }

        }
        else
        {
            sLOGGER.log(Level.ERROR, "Calling read/write for unregistered wrapper " + aPort);
        }
    }

    public static void i2cCallback(String aCallbackType, int aPort, HalCallbackValue aHalValue)
    {
        if ("Initialized".equals(aCallbackType))
        {
            II2CWrapper wrapper = sI2C_FACTORY.createI2CWrapper(aPort);
            SensorActuatorRegistry.get().register(wrapper, aPort);
        }
        else
        {
            sLOGGER.log(Level.ERROR, "Unknown I2C callback " + aCallbackType + " - " + aHalValue);
        }
    }

    public static void setDefaultWrapper(int aPort, String aType)
    {
        sI2C_FACTORY.setDefaultWrapper(aPort, aType);
    }

    public static Collection<String> getAvailableTypes()
    {
        return sI2C_FACTORY.getAvailableTypes();
    }

    public static Map<Integer, String> getDefaults()
    {
        return sI2C_FACTORY.getDefaults();
    }
}
