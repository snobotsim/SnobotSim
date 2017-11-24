package com.snobot.simulator.simulator_components.ctre;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class PigeonDeviceManager implements ICanDeviceManager
{
    private static final Logger sLOGGER = Logger.getLogger(PigeonDeviceManager.class);
    private static final int sSENSOR_OFFSET = 400;

    private final Map<Integer, CanPigeonImuSim> mPigeonMap;

    public PigeonDeviceManager()
    {
        mPigeonMap = new HashMap<>();
    }

    @Override
    public void handleSend(int aMessageId, ByteBuffer aData, int aDataSize)
    {
        int port = aMessageId & 0x3F;
        int messageId = aMessageId & 0xFFFFFFC0;

        if (messageId == 0x15042800)
        {
            sLOGGER.log(Level.INFO, "Creating Pigeon on port " + port);
            CanPigeonImuSim sim = new CanPigeonImuSim(sSENSOR_OFFSET + port * 3);
            mPigeonMap.put(port, sim);
        }
        else
        {
            sLOGGER.log(Level.WARN, String.format("Unknown send command %016X", messageId));
        }
    }

    private void dumpAngles(ByteBuffer aData, int aPort, double aScaler, int aBytes)
    {
        CanPigeonImuSim sim = mPigeonMap.get(aPort);
        if (sim == null)
        {
            sLOGGER.log(Level.WARN, "Unknown pigeon " + aPort);
            return;
        }

        double yaw = sim.getYawWrapper().getAngle();
        double pitch = sim.getPitchWrapper().getAngle();
        double roll = sim.getRollWrapper().getAngle();

        int binnedYaw = (int) (yaw * aScaler);
        int binnedPitch = (int) (pitch * aScaler);
        int binnedRoll = (int) (roll * aScaler);

        sLOGGER.log(Level.DEBUG, 
                "Yaw: " + yaw + " - " + binnedYaw + ", " + String.format("%016X", binnedYaw) + ", " + 
                "Pitch: " + pitch + " - " + binnedPitch + ", " + String.format("%016X", binnedPitch) + ", " + 
                "Roll: " + roll + " - " + binnedRoll + ", " + String.format("%016X", binnedRoll));

        if (aBytes == 3)
        {
            aData.putShort((short) 0);
        }
        aData.putShort((short) binnedYaw);
        aData.putShort((short) binnedPitch);
        aData.putShort((short) binnedRoll);
    }

    @Override
    public int handleReceive(int aMessageId, ByteBuffer aData)
    {
        int port = aMessageId & 0x3F;
        int messageId = aMessageId & 0xFFFFFFC0;

        if (messageId == 0x15041C40)
        {
            dumpAngles(aData, port, 16.4, 2);
        }
        else if (messageId == 0x15042140)
        {

        }
        else
        {
            sLOGGER.log(Level.WARN, String.format("Unknown read command %016X", messageId));
        }

        return 8;
    }

    @Override
    public void readStreamSession(ByteBuffer[] messages, int messagesToRead)
    {
        System.out.println("readStreamSession");
    }
    @Override
    public int openStreamSession(int aMessageId)
    {
        System.out.println("Opening stream session");
        return 0;
    }
}
