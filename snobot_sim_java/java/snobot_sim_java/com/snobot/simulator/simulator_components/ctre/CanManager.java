package com.snobot.simulator.simulator_components.ctre;

import java.nio.ByteBuffer;

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
            ByteBuffer buffer = ByteBuffer.allocateDirect(19);
            SensorFeedbackJni.getCanLastSentMessageData(buffer, 8);

            System.out.println(
                    "@SendingMessage: MID: " + Integer.toHexString(aMessageId) + ", Data: (0x" + String.format("%016X", buffer.getLong(0)) + ")");

            byte command = buffer.get(5);
            if (command == 0x00)
            {
                CanTalonSpeedControllerSim wrapper = new CanTalonSpeedControllerSim(port);
                SensorActuatorRegistry.get().register(wrapper, port + sCAN_OFFSET);
                System.out.println("Creating " + port);
            }
            else if (command == 0x20)
            {
                CanTalonSpeedControllerSim wrapper = getWrapperHelper(port);
                double appliedVoltageDemand = buffer.getShort(3) / 1023.0;
                wrapper.set(appliedVoltageDemand);
            }
            else
            {
                System.out.println("Unknown command: " + command);
            }

        }
        else if ("ReceiveMessage".equals(aCallbackType))
        {
            System.out.println("@ReceiveMessage: MID: " + Integer.toHexString(aMessageId));
            CanTalonSpeedControllerSim wrapper = getWrapperHelper(port);

            ByteBuffer buffer = ByteBuffer.allocateDirect(19);
            buffer.putShort(3, (short) (wrapper.get() * 1023));
            SensorFeedbackJni.setCanSetValueForRead(buffer, 8);

        }
        else
        {
            System.out.println("Unknown CAN callback " + aCallbackType + " - Message ID: 0x" + Integer.toHexString(aMessageId));
        }
    }

    private CanTalonSpeedControllerSim getWrapperHelper(int aPort)
    {
        return (CanTalonSpeedControllerSim) SensorActuatorRegistry.get().getSpeedControllers().get(aPort + sCAN_OFFSET);
    }

}
