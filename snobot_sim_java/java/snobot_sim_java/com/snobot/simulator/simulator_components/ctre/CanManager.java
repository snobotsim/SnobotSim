package com.snobot.simulator.simulator_components.ctre;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class CanManager
{
    private static final Logger sLOGGER = Logger.getLogger(CanManager.class);
    private static int STREAM_CTR = 1;

    private final Map<Integer, ICanDeviceManager> mDeviceManagerMap;
    private final Map<Integer, ICanDeviceManager> mStreamSessionMap;

    public CanManager()
    {
        mStreamSessionMap = new HashMap<>();

        mDeviceManagerMap = new HashMap<>();

        TalonSrxDeviceManager talonManager = new TalonSrxDeviceManager();
        for (int id : TalonSrxDeviceManager.getSupportedMessageIds())
        {
            mDeviceManagerMap.put(id, talonManager);
        }

        PigeonDeviceManager pigeonManager = new PigeonDeviceManager();
        for (int id : PigeonDeviceManager.getSupportedMessageIds())
        {
            mDeviceManagerMap.put(id, pigeonManager);
        }
    }

    public void handleSendMessage(String aCallback, int aCanMessageId, int aCanPort, ByteBuffer aData, int aDataSize)
    {
        ICanDeviceManager deviceManager = mDeviceManagerMap.get(aCanMessageId);
        if (deviceManager != null)
        {
            deviceManager.handleSend(aCanMessageId, aCanPort, aData, aDataSize);
        }
        else
        {
            sLOGGER.log(Level.ERROR, "Unknown device manager for " + aCanMessageId);
        }
    }

    public int handleReceiveMessage(String aCallback, int aCanMessageId, int aCanPort, ByteBuffer aData)
    {
        // Clear the incoming vector
        byte[] debug = new byte[8];
        aData.put(debug);
        aData.rewind();

        ICanDeviceManager deviceManager = mDeviceManagerMap.get(aCanMessageId);
        if (deviceManager != null)
        {
            return deviceManager.handleReceive(aCanMessageId, aCanPort, aData);
        }
        else
        {
            sLOGGER.log(Level.ERROR, "Unknown device manager for " + aCanMessageId);
        }

        return 0;
    }

    public int handleOpenStream(String callbackType, int aMessageId, int aMessageIdMask, int aMaxMessages)
    {
        ICanDeviceManager deviceManager = mDeviceManagerMap.get(aMessageId);
        if (deviceManager != null)
        {
            int streamSession = STREAM_CTR++;
            mStreamSessionMap.put(streamSession, deviceManager);
            return streamSession;
        }
        else
        {
            sLOGGER.log(Level.ERROR, "Unknown device manager for " + aMessageId);
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
