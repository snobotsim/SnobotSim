package com.snobot.simulator.simulator_components.navx;

import java.nio.ByteBuffer;

import com.snobot.simulator.jni.SensorFeedbackJni;
import com.snobot.simulator.simulator_components.ISpiWrapper;
import com.snobot.simulator.simulator_components.gyro.GyroWrapper;
import com.snobot.simulator.simulator_components.gyro.GyroWrapper.AngleSetterHelper;

public class SpiNavxSimulator extends NavxSimulator implements ISpiWrapper
{
    protected final int mSpiPort;
    protected byte mLastAddress;

    private static final GyroWrapper.AngleSetterHelper NULL_ANGLE_SETTER = new AngleSetterHelper()
    {

        @Override
        public void updateAngle(double aAngle)
        {
            // Nothing to do, handled by read/write transactions
        }
    };

    public SpiNavxSimulator(int aPort)
    {
        super(aPort, 
                new GyroWrapper("NavX Yaw", NULL_ANGLE_SETTER), 
                new GyroWrapper("NavX Pitch", NULL_ANGLE_SETTER),
                new GyroWrapper("NavX Roll", NULL_ANGLE_SETTER));

        this.mSpiPort = aPort;
        mLastAddress = -1;
    }

    static final int CRC7_POLY = 0x0091;

    private static byte getCRC(byte[] buffer, int length)
    {
        int i, j, crc = 0;

        for (i = 0; i < length; i++)
        {
            crc ^= (int) (0x00ff & buffer[i]);
            for (j = 0; j < 8; j++)
            {
                if ((crc & 0x0001) != 0)
                {
                    crc ^= CRC7_POLY;
                }
                crc >>= 1;
            }
        }
        return (byte) crc;
    }

    @Override
    public void handleWrite()
    {
        ByteBuffer lastWriteValue = ByteBuffer.allocateDirect(4);
        SensorFeedbackJni.getSpiLastWrite(mSpiPort, lastWriteValue, 4);
        lastWriteValue.rewind();
        mLastAddress = lastWriteValue.get();
        mLastAddress = 4;
        // System.out.println(Integer.toHexString(lastWriteValue.getInt(0)));

    }

    @Override
    public void handleRead()
    {
        ByteBuffer temp = null;

        // System.out.println(Integer.toHexString(lastWriteValue.getInt(0)));
        System.out.println(Integer.toHexString(mLastAddress));
        if (mLastAddress == 0x00)
        {
            temp = ByteBuffer.allocate(18);
            putConfig(temp);
            temp.put(17, getCRC(temp.array(), 17));
        }
        else if (mLastAddress == 0x04)
        {
            temp = ByteBuffer.allocate(85 + 1 - mLastAddress);
            putData(temp, mLastAddress);
        }
        else
        {
            System.err.println("Unknown last write address " + mLastAddress);
        }

        if (temp != null)
        {
            temp.put(temp.capacity() - 1, getCRC(temp.array(), temp.capacity() - 1));

            // System.out.println("Sending " + Arrays.toString(temp.array()));
            ByteBuffer toSend = ByteBuffer.allocateDirect(temp.capacity());
            temp.rewind();
            toSend.put(temp);
            toSend.rewind();
            SensorFeedbackJni.setSpiValueForRead(mSpiPort, toSend, temp.capacity());
        }
    }
}
