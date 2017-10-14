package com.snobot.simulator.simulator_components.ctre;

import java.nio.ByteBuffer;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.jni.SensorFeedbackJni;
import com.snobot.simulator.module_wrapper.ASensorWrapper;
import com.snobot.simulator.simulator_components.accelerometer.ThreeAxisAccelerometer;
import com.snobot.simulator.simulator_components.gyro.IGyroWrapper;

public class PigeonDeviceManager implements ICanDeviceManager
{
    private static final Logger sLOGGER = Logger.getLogger(PigeonDeviceManager.class);
    private static final int sSENSOR_OFFSET = 400;

    private int getBasePort(int aPort)
    {
        return sSENSOR_OFFSET + aPort * 3;
    }

    private class PigeonGyroWrapper extends ASensorWrapper implements IGyroWrapper
    {
        public PigeonGyroWrapper(String string)
        {
            super(string);
        }

        @Override
        public double getAngle()
        {
            return 0;
        }

        @Override
        public void setAngle(double aAngle)
        {
        }

    };

    @Override
    public void handleSend(int aMessageId)
    {
        int port = aMessageId & 0x3F;
        int messageId = aMessageId & 0xFFFFFFC0;

        if (messageId == 0x15042800)
        {
            int basePort = getBasePort(port);
            SensorActuatorRegistry.get().register(new PigeonGyroWrapper("Pigeon Pitch"), basePort + 0);
            SensorActuatorRegistry.get().register(new PigeonGyroWrapper("Pigeon Yaw"), basePort + 1);
            SensorActuatorRegistry.get().register(new PigeonGyroWrapper("Pigeon Roll"), basePort + 2);

            new ThreeAxisAccelerometer(basePort, "Pigeon");
        }
        else
        {
            sLOGGER.log(Level.WARN, String.format("Unknown send command %016X", messageId));
        }

        System.out.println("Send: " + aMessageId + ", " + port);
    }

    private ByteBuffer dumpAngles(int aPort, double aScaler, int aBytes)
    {
        Map<Integer, IGyroWrapper> allGyros = SensorActuatorRegistry.get().getGyros();
        ByteBuffer buffer = ByteBuffer.allocateDirect(16);
        // buffer.order(ByteOrder.LITTLE_ENDIAN);
        int basePort = getBasePort(aPort);

        System.out.println("\nSetting");
        int yaw = (int) (allGyros.get(basePort + 0).getAngle() * aScaler);
        int pitch = (int) (allGyros.get(basePort + 1).getAngle() * aScaler);
        int roll = (int) (allGyros.get(basePort + 2).getAngle() * aScaler);
        System.out.println(yaw + ", " + String.format("%016X", yaw));
        System.out.println(pitch + ", " + String.format("%016X", pitch));
        System.out.println(roll + ", " + String.format("%016X", roll));

        if (aBytes == 3)
        {
            buffer.putShort((short) 0);
        }
        buffer.putShort((short) yaw);
        buffer.putShort((short) pitch);
        buffer.putShort((short) roll);

        return buffer;
    }

    @Override
    public void handleReceive(int aMessageId)
    {
        int port = aMessageId & 0x3F;
        int messageId = aMessageId & 0xFFFFFFC0;

        if (messageId == 0x15041C40)
        {
            SensorFeedbackJni.setCanSetValueForRead(dumpAngles(port, 16.4, 2), 8);
        }
        else if (messageId == 0x15042140)
        {
            ByteBuffer buffer = ByteBuffer.allocateDirect(16);

            SensorFeedbackJni.setCanSetValueForRead(buffer, 8);
        }
        else
        {
            sLOGGER.log(Level.WARN, String.format("Unknown read command %016X", messageId));
        }
    }

    @Override
    public void openStreamSession(int aMessageId)
    {

    }

    @Override
    public void readStreamSession(int aMessageId)
    {

    }
}
