package com.snobot.simulator.simulator_components.ctre;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class CtrePigeonImuDeviceManager implements ICanDeviceManager
{
    private static final Logger sLOGGER = Logger.getLogger(CtrePigeonImuDeviceManager.class);
    private static final int sSENSOR_OFFSET = 400;

    private final Map<Integer, CtrePigeonImuSim> mPigeonMap;

    public CtrePigeonImuDeviceManager()
    {
        mPigeonMap = new HashMap<>();
    }

    public static Collection<Integer> getSupportedMessageIds()
    {
        return Arrays.asList(
                // In Series
                0x15042800, 0x15042140, 0x15041C40,

                // In Talon
                0x2042800, 0x2041C40, 0x2042140);
    }

    @Override
    public void handleSend(int aCanMessageId, int aCanPort, ByteBuffer aData, int aDataSize)
    {
        if (aCanMessageId == 0x15042800 || aCanMessageId == 0x2042800)
        {
            sLOGGER.log(Level.INFO, "Creating Pigeon on port " + aCanPort);
            CtrePigeonImuSim sim = new CtrePigeonImuSim(sSENSOR_OFFSET + aCanPort * 3);
            mPigeonMap.put(aCanPort, sim);
        }
        else
        {
            sLOGGER.log(Level.WARN, String.format("Unknown send command %016X", aCanMessageId));
        }
    }

    private void dumpAngles(ByteBuffer aData, int aPort, double aScaler, int aBytes)
    {
        CtrePigeonImuSim sim = mPigeonMap.get(aPort);
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
    public int handleReceive(int aCanMessageId, int aCanPort, ByteBuffer aData)
    {
        if (aCanMessageId == 0x15041C40 || aCanMessageId == 0x2041C40)
        {
            dumpAngles(aData, aCanPort, 16.4, 2);
        }
        else if (aCanMessageId == 0x15042140 || aCanMessageId == 0x2042140)
        {

        }
        else
        {
            sLOGGER.log(Level.WARN, String.format("Unknown read command %016X", aCanMessageId));
        }

        return 8;
    }

    @Override
    public int readStreamSession(ByteBuffer[] messages, int messagesToRead)
    {
        sLOGGER.log(Level.WARN, "readStreamSession isn't supported");
        return 0;
    }
}
