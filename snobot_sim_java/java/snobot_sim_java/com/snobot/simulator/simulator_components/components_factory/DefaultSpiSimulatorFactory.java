package com.snobot.simulator.simulator_components.components_factory;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.snobot.simulator.jni.adx_family.SpiI2CSimulatorJni;
import com.snobot.simulator.jni.navx.NavxSimulatorJni;
import com.snobot.simulator.simulator_components.ISpiWrapper;
import com.snobot.simulator.simulator_components.accelerometer.SpiI2CAccelerometer;
import com.snobot.simulator.simulator_components.gyro.SpiGyro;
import com.snobot.simulator.simulator_components.navx.NavxSimulatorWrapper;

public class DefaultSpiSimulatorFactory implements ISpiSimulatorFactory
{
    private static final Logger sLOGGER = Logger.getLogger(DefaultSpiSimulatorFactory.class);

    protected Map<Integer, String> mDefaults;

    public DefaultSpiSimulatorFactory()
    {
        mDefaults = new HashMap<>();
    }

    @Override
    public ISpiWrapper createSpiWrapper(int aPort)
    {
        ISpiWrapper output = null;

        if (mDefaults.containsKey(aPort))
        {
            output = createSimulator(aPort, mDefaults.get(aPort));
        }

        if (output == null)
        {
            sLOGGER.log(Level.ERROR, "Could not create simulator for SPI on port " + aPort);
        }

        return output;
    }

    @Override
    public void setDefaultWrapper(int aPort, String aType)
    {
        sLOGGER.log(Level.DEBUG, "Setting default on SPI port " + aPort + " to '" + aType + "'");
        mDefaults.put(aPort, aType);
    }

    public Collection<String> getAvailableTypes()
    {
        return Arrays.asList("NavX", "ADXRS450", "ADXL345", "ADXL362");
    }

    protected ISpiWrapper createSimulator(int aPort, String aType)
    {
        String fullType = "SPI " + aType;

        if ("NavX".equals(aType))
        {
            long nativePointer = NavxSimulatorJni.createNavx(fullType, aPort);
            return new NavxSimulatorWrapper(fullType, nativePointer, 200 + aPort * 3);
        }
        else if ("ADXRS450".equals(aType))
        {
            long nativePointer = SpiI2CSimulatorJni.createSpiGyro(fullType, aPort);
            return new SpiGyro(fullType, nativePointer, 100 + aPort);
        }
        else if ("ADXL345".equals(aType))
        {
            long nativePointer = SpiI2CSimulatorJni.createSpiI2cAccelerometer(fullType, aPort);
            return new SpiI2CAccelerometer(fullType, nativePointer, 100 + aPort * 3);
        }
        else if ("ADXL362".equals(aType))
        {
            long nativePointer = SpiI2CSimulatorJni.createSpiI2cAccelerometer(fullType, aPort);
            return new SpiI2CAccelerometer(fullType, nativePointer, 150 + aPort * 3);
        }

        return null;
    }

    @Override
    public Map<Integer, String> getDefaults()
    {
        return mDefaults;
    }

}
