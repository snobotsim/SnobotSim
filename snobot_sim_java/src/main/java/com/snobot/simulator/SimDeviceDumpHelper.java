package com.snobot.simulator;

import java.lang.reflect.Field;

import edu.wpi.first.hal.HALValue;
import edu.wpi.first.hal.sim.mockdata.SimDeviceDataJNI;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.wpi.first.hal.sim.SimDeviceSim;
import edu.wpi.first.hal.sim.mockdata.SimDeviceDataJNI.SimDeviceInfo;

public final class SimDeviceDumpHelper
{
    private static final Logger sLOGGER = LogManager.getLogger(SimDeviceDumpHelper.class);

    private SimDeviceDumpHelper()
    {
        // Nothing to do
    }

    private static String getName(Class<?> aClazz, Object aDeviceName)
    {
        try
        {
            Field privateStringField = aClazz.getDeclaredField("name");

            privateStringField.setAccessible(true);

            return (String) privateStringField.get(aDeviceName);
        }
        catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex)
        {
            throw new RuntimeException(ex); // NOPMD
        }
    }

    @SuppressWarnings("PMD.ConsecutiveLiteralAppends")
    public static void dumpSimDevices()
    {
        StringBuilder builder = new StringBuilder(200);
        builder.append("***************************************************\nDumping devices:\n");
        for (SimDeviceDataJNI.SimDeviceInfo deviceSimName : SimDeviceSim.enumerateDevices(""))
        {
            builder.append("Got a device: \n");

            String deviceName = getName(SimDeviceDataJNI.SimDeviceInfo.class, deviceSimName);
            builder.append("  ").append(deviceName).append('\n');

            SimDeviceSim simDeviceSime = new SimDeviceSim(deviceName); // NOPMD
            for (SimDeviceDataJNI.SimValueInfo valueName : simDeviceSime.enumerateValues())
            {
                String sim = getName(SimDeviceDataJNI.SimValueInfo.class, valueName);
                Object value = null;
                switch (simDeviceSime.getValue(sim).getValue().getType())
                {
                case HALValue.kBoolean:
                    value = simDeviceSime.getValue(sim).getValue().getBoolean();
                    break;
                case HALValue.kInt:
                case HALValue.kDouble:
                    value = simDeviceSime.getValue(sim).getValue().getDouble();
                    break;
                case HALValue.kLong:
                    value = simDeviceSime.getValue(sim).getValue().getLong();
                    break;
                case HALValue.kUnassigned:
                    break;
                default:
                    throw new RuntimeException();
                }
                builder.append("    ").append(sim).append('=').append(simDeviceSime.getValue(sim).getValue().getType()).append('{').append(value)
                    .append('}')
                    .append('\n');
            }
        }
        builder.append("***************************************************\n");
        System.out.println(builder); // NOPMD
    }
}
