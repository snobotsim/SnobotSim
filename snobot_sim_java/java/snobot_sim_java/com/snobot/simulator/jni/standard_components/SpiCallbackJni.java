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

public class SpiCallbackJni
{
    private static final Logger sLOGGER = Logger.getLogger(SpiCallbackJni.class);
    private static ISpiSimulatorFactory sSPI_FACTORY = new DefaultSpiSimulatorFactory();
    private static final Map<Integer, ISpiWrapper> sCUSTOM_WRAPPERS = new HashMap<>();

    public static void setSpiFactory(ISpiSimulatorFactory aFactory)
    {
        sSPI_FACTORY = aFactory;
    }

    public static native void registerSpiCallback(String functionName);

    public static native void registerSpiReadWriteCallback(int port);

    public static native void resetNative();

    public static void reset()
    {
        sCUSTOM_WRAPPERS.clear();
        resetNative();
    }

    public static void registerSpiCallback()
    {
        registerSpiCallback("spiCallback");
    }

    public static void registerSpiReadWriteCallback(int aPort, ISpiWrapper aWrapper)
    {
        registerSpiReadWriteCallback(aPort);
        sCUSTOM_WRAPPERS.put(aPort, aWrapper);
    }

    public static void spiCallback(String callbackType, int port, HalCallbackValue halValue)
    {
        if ("Initialized".equals(callbackType))
        {
            ISpiWrapper wrapper = sSPI_FACTORY.createSpiWrapper(port);
            SensorActuatorRegistry.get().register(wrapper, port);
        }
        else
        {
            sLOGGER.log(Level.ERROR, "Unknown SPI callback " + callbackType + " - " + halValue);
        }
    }

    public static void spiCallback(String callbackType, int port, ByteBuffer buffer)
    {
        if (sCUSTOM_WRAPPERS.containsKey(port))
        {
            ISpiWrapper wrapper = sCUSTOM_WRAPPERS.get(port);
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
                sLOGGER.log(Level.ERROR, "Unknown SPI callback " + callbackType);
            }

        }
        else
        {
            sLOGGER.log(Level.ERROR, "Calling read/write for unregistered wrapper " + port);
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
