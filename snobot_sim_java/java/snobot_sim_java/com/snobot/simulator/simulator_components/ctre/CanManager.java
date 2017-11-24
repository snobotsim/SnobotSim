package com.snobot.simulator.simulator_components.ctre;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class CanManager
{
    private static final Logger sLOGGER = Logger.getLogger(CanManager.class);

    private final Map<Integer, ICanDeviceManager> mDeviceManagers;
    private final Map<Integer, ICanDeviceManager> mStreamSessionMap;

    public CanManager()
    {
        mStreamSessionMap = new HashMap<>();
        mDeviceManagers = new HashMap<>();
        mDeviceManagers.put(0x02040000, new TalonSrxDeviceManager());
        mDeviceManagers.put(0x15040000, new PigeonDeviceManager());
    }

    private ICanDeviceManager getDeviceManager(int aMessageId)
    {
        int deviceId = aMessageId & 0xFFFF0000;

        if (!mDeviceManagers.containsKey(deviceId) && deviceId != 0)
        {
            sLOGGER.log(Level.ERROR, "Unknown device ID " + deviceId);
            return null;
        }

        ICanDeviceManager deviceManager = null;
        if (deviceId == 0)
        {
            deviceManager = mDeviceManagers.get(0x02040000);
        }
        else if (mDeviceManagers.containsKey(deviceId))
        {
            deviceManager = mDeviceManagers.get(deviceId);
        }
        else
        {
            sLOGGER.log(Level.ERROR, "Unknown device ID " + deviceId);
        }

        return deviceManager;
    }

    public void handleSendMessage(String aCallback, int aMessageId, ByteBuffer aData, int aDataSize)
    {
        ICanDeviceManager deviceManager = getDeviceManager(aMessageId);
        if (deviceManager != null)
        {
            deviceManager.handleSend(aMessageId, aData, aDataSize);
        }
    }

    public int handleReceiveMessage(String aCallback, int aMessageId, ByteBuffer aData)
    {
        ICanDeviceManager deviceManager = getDeviceManager(aMessageId);
        if (deviceManager != null)
        {
            return deviceManager.handleReceive(aMessageId, aData);
        }

        return 0;
    }

    public int handleOpenStream(String callbackType, int aMessageId, int aMessageIdMask, int aMaxMessages)
    {
        ICanDeviceManager deviceManager = getDeviceManager(aMessageId);
        if (deviceManager != null)
        {
            int streamSession = deviceManager.openStreamSession(aMessageId);
            mStreamSessionMap.put(streamSession, deviceManager);
            return streamSession;
        }

        return 0;
    }

    public void handleCloseStream(String callbackType, int sessionHandle)
    {
        if (!mStreamSessionMap.containsKey(sessionHandle))
        {
            sLOGGER.log(Level.ERROR, "Could not close stream " + sessionHandle);
        }
        mStreamSessionMap.remove(sessionHandle);
    }

    public int handleReadStream(String callbackType, int sessionHandle, ByteBuffer[] messages, int messagesToRead)
    {
        ICanDeviceManager deviceManager = mStreamSessionMap.get(sessionHandle);
        if (deviceManager != null)
        {
            deviceManager.readStreamSession(messages, messagesToRead);
        }
        return 1;
    }
}
