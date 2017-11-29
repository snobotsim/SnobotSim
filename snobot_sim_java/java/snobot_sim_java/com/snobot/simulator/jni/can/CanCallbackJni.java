package com.snobot.simulator.jni.can;

import java.nio.ByteBuffer;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.snobot.simulator.jni.HalCallbackValue;
import com.snobot.simulator.simulator_components.ctre.CanManager;

public class CanCallbackJni
{
    private static final Logger sLOGGER = Logger.getLogger(CanCallbackJni.class);

    public static final CanManager sCAN_MANAGER = new CanManager();

    public static native void registerCanCallback(String functionName);

    public static native void reset();

    public static void registerCanCallback()
    {
        registerCanCallback("canCallback");
    }

    public static void canCallback(String callbackType, int dummy, HalCallbackValue halValue)
    {
        sLOGGER.log(Level.ERROR, "Shouldn't be called directly " + callbackType + " - " + halValue);
    }

    public static void canCallback(String callbackType, int dummy, int aMessageId, ByteBuffer aData, int aDataSize, int aPeriodMs)
    {
        int canMessageId = aMessageId & 0xFFFFFFC0;
        int canPort = aMessageId & 0x3F;

        sCAN_MANAGER.handleSendMessage(callbackType, canMessageId, canPort, aData, aDataSize);
    }

    public static int canCallback(String callbackType, int dummy, int aMessageId, int messageIDMask, ByteBuffer aData, int aTimestamp)
    {
        int canMessageId = aMessageId & 0xFFFFFFC0;
        int canPort = aMessageId & 0x3F;

        return sCAN_MANAGER.handleReceiveMessage(callbackType, canMessageId, canPort, aData);
    }

    public static int canCallback(String callbackType, int dummy, int messageId, int messageIdMask, int maxMessages)
    {
        return sCAN_MANAGER.handleOpenStream(callbackType, messageId, messageIdMask, maxMessages);
    }

    public static void canCallback(String callbackType, int dummy, int sessionHandle)
    {
        sCAN_MANAGER.handleCloseStream(callbackType, sessionHandle);
    }

    public static int canCallback(String callbackType, int port, int sessionHandle, ByteBuffer[] messages, int messagesToRead)
    {
        return sCAN_MANAGER.handleReadStream(callbackType, sessionHandle, messages, messagesToRead);
    }

    public static void canCallback(String callbackType, int port, float percentVbus, int busOffCount, int txFullCount, int recvErroRCount,
            int transmitCount)
    {
        sLOGGER.log(Level.ERROR, "Unsupported CanCallback (getstatus) ");
    }
}
