package com.snobot.simulator.simulator_components.accelerometer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.snobot.simulator.jni.SensorFeedbackJni;
import com.snobot.simulator.simulator_components.ISpiWrapper;

public class ADXL362_SpiAccelerometer implements ISpiWrapper
{
    private static double sLSB = 0.001;

    protected final ThreeAxisAccelerometer mDataContainer;
    protected final int mNativePort;

    public ADXL362_SpiAccelerometer(int aPort)
    {
        mDataContainer = new ThreeAxisAccelerometer(aPort * 75, "SPI Accel ");
        mNativePort = aPort;
    }

    @Override
    public void handleRead()
    {
        ByteBuffer lastWriteValue = ByteBuffer.allocateDirect(4);
        SensorFeedbackJni.getSpiLastWrite(mNativePort, lastWriteValue, 4);
        lastWriteValue.rewind();
        int lastWrittenAddress = lastWriteValue.get(1) & 0xFF;
        ByteBuffer buffer = ByteBuffer.allocateDirect(10);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        if (lastWrittenAddress == 0x02)
        {
            buffer.putInt(0xF2F2F2);
        }
        else if (lastWrittenAddress >= 0x0E && lastWrittenAddress <= (0x0E + 4))
        {
            buffer.putShort((short) 0);
            boolean includeAll = lastWrittenAddress == 0x0E;

            if (lastWrittenAddress == 0x0E)
            {
                short value = (short) (mDataContainer.getX() / sLSB);
                buffer.putShort(value);
            }

            if (lastWrittenAddress == 0x10 || includeAll)
            {
                short value = (short) (mDataContainer.getY() / sLSB);
                buffer.putShort(value);
            }

            if (lastWrittenAddress == 0x12 || includeAll)
            {
                short value = (short) (mDataContainer.getZ() / sLSB);
                buffer.putShort(value);
            }
        }
        else
        {
            System.err.println("Unsupported write address " + lastWrittenAddress);
        }

        SensorFeedbackJni.setSpiValueForRead(mNativePort, buffer, buffer.capacity());
    }

}
