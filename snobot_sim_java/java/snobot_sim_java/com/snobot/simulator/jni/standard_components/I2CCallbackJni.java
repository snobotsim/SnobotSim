package com.snobot.simulator.jni.standard_components;

import java.nio.ByteBuffer;
import java.util.Collection;
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
    private static final II2cSimulatorFactory sI2C_FACTORY = new DefaultI2CSimulatorFactory();

    public static native void registerI2CCallback(String functionName);

    public static native void reset();

    public static void registerI2CCallback()
    {
        registerI2CCallback("i2cCallback");
    }

    public static void i2cCallback(String callbackType, int port, ByteBuffer buffer)
    {
        sLOGGER.log(Level.ERROR, "Unknown I2C callback " + callbackType + " - " + buffer.capacity());
    }

    public static void i2cCallback(String callbackType, int port, HalCallbackValue halValue)
    {
        if ("Initialized".equals(callbackType))
        {
            II2CWrapper wrapper = sI2C_FACTORY.createI2CWrapper(port);
            SensorActuatorRegistry.get().register(wrapper, port);
        }
        else if ("Read".equals(callbackType))
        {
            SensorActuatorRegistry.get().getI2CWrapperss().get(port).handleRead();
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
