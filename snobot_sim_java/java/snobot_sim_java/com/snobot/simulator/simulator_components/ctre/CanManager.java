package com.snobot.simulator.simulator_components.ctre;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.jni.SensorFeedbackJni;

public class CanManager
{
    private static final int sCAN_OFFSET = 100;

    public void handleIncomingMessage(String aCallbackType, int aMessageId)
    {
        int port = aMessageId & 0x3F;

        if ("SendMessage".equals(aCallbackType))
        {
            handleSend(aMessageId, port);
        }
        else if ("ReceiveMessage".equals(aCallbackType))
        {
            handleReceive(aMessageId, port);
        }
        else if ("OpenStreamSession".equals(aCallbackType))
        {
            // Nothing to do
        }
        else if ("ReadStreamSession".equals(aCallbackType))
        {
            // Handled elsewhere
        }
        else
        {
            System.err.println("Unknown CAN callback " + aCallbackType + " - Message ID: 0x" + Integer.toHexString(aMessageId));
        }
    }

    private void handleTx1(ByteBuffer aBuffer, int aPort)
    {

        byte command = aBuffer.get(5);
        if (command == 0x00)
        {
            CanTalonSpeedControllerSim wrapper = new CanTalonSpeedControllerSim(aPort);
            SensorActuatorRegistry.get().register(wrapper, aPort + sCAN_OFFSET);
            System.out.println("Creating " + aPort);
        }
        else if (command == 0x20)
        {
            handleSetDemandCommand(aBuffer, aPort);
        }
        else
        {
            System.err.println("Unknown command: " + command);
        }
    }

    private void handleSetParamCommand(ByteBuffer aBuffer, int aPort)
    {
        CanTalonSpeedControllerSim wrapper = getWrapperHelper(aPort);
        aBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byte commandType = aBuffer.get(0);

        int rawValue = aBuffer.getInt(1);
        double floatValue = rawValue * 0.0000002384185791015625;

        // P gain
        if (commandType == 1)
        {
            wrapper.setPGain(floatValue);
        }
        // I gain
        else if (commandType == 2)
        {
            wrapper.setIGain(floatValue);
        }
        // D gain
        else if (commandType == 3)
        {
            wrapper.setDGain(floatValue);
        }
        // F gain
        else if (commandType == 4)
        {
            wrapper.setFGain(floatValue);
        }
        else
        {
            System.err.println("Unknown SetParam command: " + commandType);
        }
    }

    private void handleParamRequest(ByteBuffer aBuffer, int aPort)
    {
        // System.out.println("Getting parameters...");

        CanTalonSpeedControllerSim wrapper = getWrapperHelper(aPort);
        aBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byte commandType = aBuffer.get(0);

        double floatValue = 0;

        // P gain
        if (commandType == 1)
        {
            floatValue = wrapper.getPidConstants().mP;
        }
        // I gain
        else if (commandType == 2)
        {
            floatValue = wrapper.getPidConstants().mI;
        }
        // D gain
        else if (commandType == 3)
        {
            floatValue = wrapper.getPidConstants().mD;
        }
        // F gain
        else if (commandType == 4)
        {
            floatValue = wrapper.getPidConstants().mF;
        }
        else
        {
            System.err.println("Unknown SetParam command: " + commandType);
        }
        int rawValue = (int) (floatValue / 0.0000002384185791015625);
        // System.out.println("Raw value..." + rawValue + ", " + floatValue);
        
        ByteBuffer outputBuffer = ByteBuffer.allocateDirect(40);
        outputBuffer.order(ByteOrder.LITTLE_ENDIAN);
        outputBuffer.putInt(0x2041840); // Message ID
        outputBuffer.putInt(0xDEADBEEF); // Timestamp
        outputBuffer.put(commandType);
        outputBuffer.putInt(rawValue);

        // unsigned ParamEnum:8;
        // unsigned ParamValueL:8;
        // unsigned ParamValueML:8;
        // unsigned ParamValueMH:8;
        // unsigned ParamValueH:8;

        SensorFeedbackJni.setCanSetValueForReadStream(outputBuffer, 1);

    }

    private void handleSetDemandCommand(ByteBuffer aBuffer, int aPort)
    {
        byte commandType = (byte) ((aBuffer.get(6) >> 4) & 0xF);
        CanTalonSpeedControllerSim wrapper = getWrapperHelper(aPort);
        short demand = aBuffer.getShort(3);
        // System.out.println(String.format(" Demand: %d", demand));

        if (commandType == 0x00)
        {
            double appliedVoltageDemand = demand / 1023.0;
            // System.out.println(" Setting by applied throttle.. " +
            // appliedVoltageDemand);
            wrapper.set(appliedVoltageDemand);
        }
        else if (commandType == (byte) 0x01)
        {
            System.out.println("  Setting by position.");
        }
        else if (commandType == (byte) 0x02)
        {
            System.out.println("  Setting by speed.");
        }
        else if (commandType == (byte) 0x03)
        {
            System.out.println("  Setting by current.");
        }
        else if (commandType == (byte) 0x04)
        {
            double voltageDemand = demand / 256.0;
            // System.out.println(" Setting by voltage. " + voltageDemand);
            wrapper.set(voltageDemand / 12.0);
        }
        else if (commandType == (byte) 0x05)
        {
            System.out.println("  Setting by FOLLOWER.");
        }
        else if (commandType == (byte) 0x06)
        {
            System.out.println("  Setting by Motion Profile.");
        }
        else if (commandType == (byte) 0x07)
        {
            System.out.println("  Setting by Motion Magic.");
        }
        else if (commandType == (byte) 0x0F)
        {
            // Nothing to do, but don't print error
        }
        else
        {
            System.err.println(String.format("Unknown set command type 0x%02X", commandType));
        }
    }

    private void populateStatus1(int aPort)
    {
        CanTalonSpeedControllerSim wrapper = getWrapperHelper(aPort);
        // System.out.println(" Getting STATUS1 " + wrapper.get());

        ByteBuffer buffer = ByteBuffer.allocateDirect(19);
        buffer.putShort(3, (short) (wrapper.get() * 1023));
        SensorFeedbackJni.setCanSetValueForRead(buffer, 8);
    }

    private void populateStatus3(int aPort)
    {
        CanTalonSpeedControllerSim wrapper = getWrapperHelper(aPort);
        int binnedPosition = (int) wrapper.getPosition();
        int binnedVelocity = (int) wrapper.getVelocity();

        ByteBuffer buffer = ByteBuffer.allocateDirect(19);
        // buffer.put(0, (byte) ((binnedPosition & 0xFF0000) >> 16));
        // buffer.put(1, (byte) ((binnedPosition & 0x00FF00) >> 8));
        // buffer.put(2, (byte) ((binnedPosition & 0x0000FF) >> 0));
        // buffer.put(3, (byte) ((binnedVelocity & 0xFF00) >> 8));
        // buffer.put(4, (byte) ((binnedVelocity & 0x00FF) >> 0));
        putNumber(buffer, binnedPosition, 3);
        buffer.putShort((short) binnedVelocity);

        SensorFeedbackJni.setCanSetValueForRead(buffer, 8);
    }

    private void populateStatus4(int aPort)
    {
        // System.out.println("POPULATE STATUS 4");
        double temperature = 30;
        double batteryVoltage = 12;
        CanTalonSpeedControllerSim wrapper = getWrapperHelper(aPort);
        int binnedPosition = (int) wrapper.getPosition();
        int binnedVelocity = (int) wrapper.getVelocity();

        ByteBuffer buffer = ByteBuffer.allocateDirect(19);
        // buffer.put(0, (byte) ((binnedPosition & 0xFF0000) >> 16));
        // buffer.put(1, (byte) ((binnedPosition & 0x00FF00) >> 8));
        // buffer.put(2, (byte) ((binnedPosition & 0x0000FF) >> 0));
        // buffer.put(3, (byte) ((binnedVelocity & 0xFF00) >> 8));
        // buffer.put(4, (byte) ((binnedVelocity & 0x00FF) >> 0));
        putNumber(buffer, binnedPosition, 3);
        buffer.putShort((short) binnedVelocity);
        buffer.put((byte) ((temperature + 50) / 0.6451612903));
        buffer.put((byte) ((batteryVoltage - 4) / 0.05));

        SensorFeedbackJni.setCanSetValueForRead(buffer, 8);
    }

    private void handleSend(int aMessageId, int aPort)
    {
        ByteBuffer buffer = ByteBuffer.allocateDirect(19);
        SensorFeedbackJni.getCanLastSentMessageData(buffer, 8);

        System.out.println(
                "@SendingMessage: MID: " + Integer.toHexString(aMessageId) + ", Data: (0x" + String.format("%016X", buffer.getLong(0)) + ")");

        int messageId = aMessageId & 0xFFFFFFC0;

        if (messageId == 0x02040000)
        {
            handleTx1(buffer, aPort);
        }
        else if (messageId == 0x02041880)
        {
            handleSetParamCommand(buffer, aPort);
        }
        else if (messageId == 0x02041800)
        {
            handleParamRequest(buffer, aPort);
        }
        else
        {
            unsupportedWrite(messageId);
        }
    }

    private void handleReceive(int aMessageId, int aPort)
    {
        System.out.println("@ReceiveMessage: MID: " + Integer.toHexString(aMessageId));

        int messageId = aMessageId & 0xFFFFFFC0;

        if (messageId == 0x02041400)
        {
            populateStatus1(aPort);
        }
        else if (messageId == 0x02041440)
        {
            unsupportedRead(2);
        }
        else if (messageId == 0x02041480)
        {
            populateStatus3(aPort);
        }
        else if (messageId == 0x020414C0)
        {
            populateStatus4(aPort);
        }
        else if (messageId == 0x02041500)
        {
            unsupportedRead(5);
        }
        else if (messageId == 0x02041540)
        {
            unsupportedRead(6);
        }
        else if (messageId == 0x02041580)
        {
            unsupportedRead(7);
        }
        else if (messageId == 0x020415C0)
        {
            unsupportedRead(8);
        }
        else if (messageId == 0x02041600)
        {
            unsupportedRead(9);
        }
        else
        {
            unsupportedRead(messageId);
        }
    }

    private void putNumber(ByteBuffer aOutput, int aNumber, int aBytes)
    {
        // buffer.put(0, (byte) ((binnedPosition & 0xFF0000) >> 16));
        // buffer.put(1, (byte) ((binnedPosition & 0x00FF00) >> 8));
        // buffer.put(2, (byte) ((binnedPosition & 0x0000FF) >> 0));

        for (int i = aBytes; i > 0; --i)
        {
            int shift = 8 * (i - 1);
            int mask = 0xFF << shift;
            aOutput.put((byte) ((aNumber & mask) >> shift));
        }
    }

    private void unsupportedRead(int aStatusType)
    {
        System.err.println("Status request " + aStatusType + " is not supported.");

        ByteBuffer buffer = ByteBuffer.allocateDirect(19);
        SensorFeedbackJni.setCanSetValueForRead(buffer, 8);
    }

    private void unsupportedWrite(int aStatusType)
    {
        System.err.println("TX Request " + aStatusType + " is not supported.");

        ByteBuffer buffer = ByteBuffer.allocateDirect(19);
        SensorFeedbackJni.setCanSetValueForRead(buffer, 8);
    }

    private CanTalonSpeedControllerSim getWrapperHelper(int aPort)
    {
        return (CanTalonSpeedControllerSim) SensorActuatorRegistry.get().getSpeedControllers().get(aPort + sCAN_OFFSET);
    }
}
