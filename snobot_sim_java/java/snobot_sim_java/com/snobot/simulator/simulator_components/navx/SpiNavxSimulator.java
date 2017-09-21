package com.snobot.simulator.simulator_components.navx;

import java.nio.ByteBuffer;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.snobot.simulator.jni.SensorFeedbackJni;
import com.snobot.simulator.simulator_components.ISpiWrapper;

public class SpiNavxSimulator extends NavxSimulator implements ISpiWrapper
{
    private static final Logger sLOGGER = Logger.getLogger(SpiNavxSimulator.class);
    public static final int SPI_NAVX_OFFSET = 200;

    public SpiNavxSimulator(int aSpiPort)
    {
        super(aSpiPort, SPI_NAVX_OFFSET);
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
    public void handleRead()
    {

        ByteBuffer lastWriteValue = ByteBuffer.allocateDirect(4);
        SensorFeedbackJni.getSpiLastWrite(mNativePort, lastWriteValue, 4);
        lastWriteValue.rewind();
        int lastWrittenAddress = lastWriteValue.get();

        ByteBuffer withoutCrc = null;

        if (lastWrittenAddress == 0x00)
        {
            withoutCrc = createConfigBuffer();
        }
        else if (lastWrittenAddress == 0x04)
        {
            withoutCrc = createDataBuffer(lastWrittenAddress);
        }
        else
        {
            sLOGGER.log(Level.ERROR, "Unknown last write address " + lastWrittenAddress);
        }

        if (withoutCrc != null)
        {
            byte[] raw_bytes = new byte[withoutCrc.capacity() + 1];
            withoutCrc.get(raw_bytes, 0, withoutCrc.capacity());

            byte crc = getCRC(raw_bytes, withoutCrc.capacity());
            raw_bytes[raw_bytes.length - 1] = crc;

            ByteBuffer toSend = ByteBuffer.allocateDirect(withoutCrc.capacity() + 1);
            toSend.put(raw_bytes);
            toSend.rewind();
            SensorFeedbackJni.setSpiValueForRead(mNativePort, toSend, withoutCrc.capacity() + 1);
        }
    }

    @Override
    public void handleWrite()
    {

    }

    @Override
    public void handleTransaction()
    {

    }
}
