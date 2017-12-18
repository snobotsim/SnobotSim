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

public class I2CCallbackJni
{
    private static final Logger sLOGGER = Logger.getLogger(I2CCallbackJni.class);
    private static II2cSimulatorFactory sI2C_FACTORY = new DefaultI2CSimulatorFactory();
    private static final Map<Integer, II2CWrapper> sCUSTOM_WRAPPERS = new HashMap<>();

    public static native void registerI2CCallback(String functionName);

    public static native void registerReadWriteCallbacks(int port);

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

    public static void registerI2CCallback()
    {
        registerI2CCallback("i2cCallback");
    }

    public static void registerReadWriteCallbacks(int aPort, II2CWrapper aWrapper)
    {
        registerReadWriteCallbacks(aPort);
        sCUSTOM_WRAPPERS.put(aPort, aWrapper);
    }

    public static void i2cCallback(String callbackType, int port, ByteBuffer buffer)
    {
        if (sCUSTOM_WRAPPERS.containsKey(port))
        {
            II2CWrapper wrapper = sCUSTOM_WRAPPERS.get(port);
            if ("Read".equals(callbackType))
            {
                wrapper.handleRead(buffer);
            }
            else if ("Write".equals(callbackType))
            {
                wrapper.handleWrite(buffer);
            }
            else
            {
                sLOGGER.log(Level.ERROR, "Unknown I2C callback " + callbackType);
            }

        }
        else
        {
            sLOGGER.log(Level.ERROR, "Calling read/write for unregistered wrapper " + port);
        }
    }

    public static void i2cCallback(String callbackType, int port, HalCallbackValue halValue)
    {
        if ("Initialized".equals(callbackType))
        {
            II2CWrapper wrapper = sI2C_FACTORY.createI2CWrapper(port);
            SensorActuatorRegistry.get().register(wrapper, port);
        }
        else
        {
            sLOGGER.log(Level.ERROR, "Unknown I2C callback " + callbackType + " - " + halValue);
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
