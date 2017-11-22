package com.snobot.simulator.jni.standard_components;

import java.nio.ByteBuffer;
import java.util.Collection;
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
    private static final ISpiSimulatorFactory sSPI_FACTORY = new DefaultSpiSimulatorFactory();

    public static native void setSpiAccumulatorValue(int aHandle, long aValue);

    public static native void registerSpiCallback(String functionName);

    public static native void reset();

    public static void registerSpiCallback()
    {
        registerSpiCallback("spiCallback");
    }

    public static void spiCallback(String callbackType, int port, HalCallbackValue halValue)
    {
        if ("Initialized".equals(callbackType))
        {
            ISpiWrapper wrapper = sSPI_FACTORY.createSpiWrapper(port);
            SensorActuatorRegistry.get().register(wrapper, port);
        }
        else if ("Transaction".equals(callbackType))
        {
            SensorActuatorRegistry.get().getSpiWrappers().get(port).handleTransaction();
        }
        else if ("ResetAccumulator".equals(callbackType))
        {
            SensorActuatorRegistry.get().getSpiWrappers().get(port).resetAccumulator();
        }
        else
        {
            sLOGGER.log(Level.ERROR, "Unknown SPI callback " + callbackType + " - " + halValue);
        }
    }

    public static void spiCallback(String callbackType, int port, ByteBuffer buffer)
    {
        if ("Read".equals(callbackType))
        {
            SensorActuatorRegistry.get().getSpiWrappers().get(port).handleRead(buffer);
        }
        else if ("Write".equals(callbackType))
        {
            SensorActuatorRegistry.get().getSpiWrappers().get(port).handleWrite(buffer);
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
