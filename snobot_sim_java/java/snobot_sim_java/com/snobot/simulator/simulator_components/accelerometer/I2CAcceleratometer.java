package com.snobot.simulator.simulator_components.accelerometer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.snobot.simulator.jni.SensorFeedbackJni;
import com.snobot.simulator.simulator_components.II2CWrapper;

public class I2CAcceleratometer implements II2CWrapper
{
    private static double sLSB = 0.00390625;

    protected final ThreeAxisAccelerometer mDataContainer;
    protected final int mNativePort;

    public I2CAcceleratometer(int aPort)
    {
        mDataContainer = new ThreeAxisAccelerometer(aPort, "I2C Accel ");
        mNativePort = aPort;
    }

    @Override
    public void handleRead()
    {
        ByteBuffer lastWriteValue = ByteBuffer.allocateDirect(4);
        SensorFeedbackJni.getI2CLastWrite(mNativePort, lastWriteValue, 4);
        lastWriteValue.rewind();
        int lastWrittenAddress = lastWriteValue.get();

        ByteBuffer buffer = ByteBuffer.allocateDirect(2);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        if (lastWrittenAddress == 0x32)
        {
            short value = (short) (mDataContainer.getX() / sLSB);
            buffer.putShort(value);
        }

        if (lastWrittenAddress == 0x34)
        {
            short value = (short) (mDataContainer.getY() / sLSB);
            buffer.putShort(value);
        }

        if (lastWrittenAddress == 0x36)
        {
            short value = (short) (mDataContainer.getZ() / sLSB);
            buffer.putShort(value);
        }

        SensorFeedbackJni.setI2CValueForRead(mNativePort, buffer, buffer.capacity());
    }

}
