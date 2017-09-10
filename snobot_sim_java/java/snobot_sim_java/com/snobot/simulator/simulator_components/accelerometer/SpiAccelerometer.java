package com.snobot.simulator.simulator_components.accelerometer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.snobot.simulator.jni.SensorFeedbackJni;
import com.snobot.simulator.simulator_components.ISpiWrapper;

public class SpiAccelerometer implements ISpiWrapper
{
    private static double sLSB = 0.00390625;

    protected final ThreeAxisAccelerometer mDataContainer;
    protected final int mNativePort;

    public SpiAccelerometer(int aPort)
    {
        mDataContainer = new ThreeAxisAccelerometer(aPort, "SPI Accel ");
        mNativePort = aPort;
    }

    @Override
    public void handleRead()
    {
        ByteBuffer lastWriteValue = ByteBuffer.allocateDirect(4);
        SensorFeedbackJni.getSpiLastWrite(mNativePort, lastWriteValue, 4);
        lastWriteValue.rewind();
        int lastWrittenAddress = lastWriteValue.get() & 0xF;
        ByteBuffer buffer = ByteBuffer.allocateDirect(3);
        buffer.put((byte) 0);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        if (lastWrittenAddress == 0x02)
        {
            short value = (short) (mDataContainer.getX() / sLSB);
            buffer.putShort(value);
        }

        if (lastWrittenAddress == 0x04)
        {
            short value = (short) (mDataContainer.getY() / sLSB);
            buffer.putShort(value);
        }

        if (lastWrittenAddress == 0x06)
        {
            short value = (short) (mDataContainer.getZ() / sLSB);
            buffer.putShort(value);
        }
        SensorFeedbackJni.setSpiValueForRead(mNativePort, buffer, buffer.capacity());

        System.out.println("Reading..." + lastWrittenAddress);
    }

}
