package com.snobot.simulator.jni.standard_components;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.jni.HalCallbackValue;
import com.snobot.simulator.simulator_components.ISpiWrapper;
import com.snobot.simulator.simulator_components.components_factory.DefaultSpiSimulatorFactory;
import com.snobot.simulator.simulator_components.components_factory.ISpiSimulatorFactory;

public final class SpiCallbackJni
{
    private static final Logger sLOGGER = Logger.getLogger(SpiCallbackJni.class);
    private static ISpiSimulatorFactory sSPI_FACTORY = new DefaultSpiSimulatorFactory();
    private static final Map<Integer, ISpiWrapper> sCUSTOM_WRAPPERS = new HashMap<>();

    private SpiCallbackJni()
    {

    }

    public static void setSpiFactory(ISpiSimulatorFactory aFactory)
    {
        sSPI_FACTORY = aFactory;
    }

    public static native void resetNative();

    public static void reset()
    {
        sCUSTOM_WRAPPERS.clear();
        resetNative();
    }

    public static native void registerSpiCallback(String aFunctionName);

    public static void registerSpiCallback()
    {
        registerSpiCallback("spiCallback");
    }

    public static native void registerSpiReadWriteCallback(int aPort);

    public static void registerSpiReadWriteCallback(int aPort, ISpiWrapper aWrapper)
    {
        registerSpiReadWriteCallback(aPort);
        sCUSTOM_WRAPPERS.put(aPort, aWrapper);
    }

    public static void spiCallback(String aCallbackType, int aPort, HalCallbackValue aHalValue)
    {
        if ("Initialized".equals(aCallbackType))
        {
            ISpiWrapper wrapper = sSPI_FACTORY.createSpiWrapper(aPort);
            SensorActuatorRegistry.get().register(wrapper, aPort);
        }
        else
        {
            sLOGGER.log(Level.ERROR, "Unknown SPI callback " + aCallbackType + " - " + aHalValue);
        }
    }

    public static void spiCallback(String aCallbackType, int aPort, ByteBuffer aBuffer)
    {
        if (sCUSTOM_WRAPPERS.containsKey(aPort))
        {
            ISpiWrapper wrapper = sCUSTOM_WRAPPERS.get(aPort);
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
                sLOGGER.log(Level.ERROR, "Unknown SPI callback " + aCallbackType);
            }

        }
        else
        {
            sLOGGER.log(Level.ERROR, "Calling read/write for unregistered wrapper " + aPort);
        }
    }


    public static void setDefaultWrapper(int aPort, String aType)
    {
        sSPI_FACTORY.setDefaultWrapper(aPort, aType);
    }

    public static Collection<String> getAvailableTypes()
    {
        return sSPI_FACTORY.getAvailableTypes();
    }

    public static Map<Integer, String> getDefaults()
    {
        return sSPI_FACTORY.getDefaults();
    }
}
