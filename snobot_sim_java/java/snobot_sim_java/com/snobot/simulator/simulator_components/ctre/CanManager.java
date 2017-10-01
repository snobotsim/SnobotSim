package com.snobot.simulator.simulator_components.ctre;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class CanManager
{
    private static final Logger sLOGGER = Logger.getLogger(CanManager.class);

    private final Map<Integer, ICanDeviceManager> mDeviceManagers;

    public CanManager()
    {
        mDeviceManagers = new HashMap<>();
        mDeviceManagers.put(0x02040000, new TalonSrxDeviceManager());
        mDeviceManagers.put(0x15040000, new PigeonDeviceManager());
    }

    public void handleIncomingMessage(String aCallbackType, int aMessageId)
    {
        int deviceId = aMessageId & 0xFFFF0000;

        if (!mDeviceManagers.containsKey(deviceId) && deviceId != 0)
        {
            sLOGGER.log(Level.ERROR, "Unknown device ID " + deviceId);
            return;
        }

        ICanDeviceManager deviceManager;
        if (deviceId == 0)
        {
            deviceManager = mDeviceManagers.get(0x02040000);
        }
        else
        {
            deviceManager = mDeviceManagers.get(deviceId);
        }

        if ("SendMessage".equals(aCallbackType))
        {
            deviceManager.handleSend(aMessageId);
        }
        else if ("ReceiveMessage".equals(aCallbackType))
        {
            deviceManager.handleReceive(aMessageId);
        }
        else if ("OpenStreamSession".equals(aCallbackType))
        {
            deviceManager.openStreamSession(aMessageId);
        }
        else if ("ReadStreamSession".equals(aCallbackType))
        {
            deviceManager.readStreamSession(aMessageId);
        }
        else
        {
            sLOGGER.log(Level.ERROR, "Unknown CAN callback " + aCallbackType + " - Message ID: 0x" + Integer.toHexString(aMessageId));
        }
    }
}
