package com.snobot.simulator.simulator_components.ctre;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class CtreManager
{
    private static final Logger sLOGGER = Logger.getLogger(CtreManager.class);
    private static int STREAM_CTR = 1;

    private final Map<Integer, ICanDeviceManager> mDeviceManagerMap;
    private final Map<Integer, ICanDeviceManager> mStreamSessionMap;

    public CtreManager()
    {
        mStreamSessionMap = new HashMap<>();

        mDeviceManagerMap = new HashMap<>();

        CtreTalonSrxDeviceManager talonManager = new CtreTalonSrxDeviceManager();
        for (int id : CtreTalonSrxDeviceManager.getSupportedMessageIds())
        {
            mDeviceManagerMap.put(id, talonManager);
        }

        CtrePigeonImuDeviceManager pigeonManager = new CtrePigeonImuDeviceManager();
        for (int id : CtrePigeonImuDeviceManager.getSupportedMessageIds())
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
            sLOGGER.log(Level.ERROR, String.format("Unknown device manager for message 0x%08X", aCanMessageId));
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
            sLOGGER.log(Level.ERROR, String.format("Unknown device manager for message 0x%08X", aCanMessageId));
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
            sLOGGER.log(Level.ERROR, String.format("Unknown device manager for message 0x%08X", aMessageId));
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
            return deviceManager.readStreamSession(messages, messagesToRead);
        }
        return 0;
    }
}
