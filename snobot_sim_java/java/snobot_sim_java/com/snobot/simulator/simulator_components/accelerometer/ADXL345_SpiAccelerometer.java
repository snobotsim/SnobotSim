package com.snobot.simulator.simulator_components.accelerometer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.snobot.simulator.jni.SensorFeedbackJni;
import com.snobot.simulator.simulator_components.ISpiWrapper;

public class ADXL345_SpiAccelerometer implements ISpiWrapper
{
    private static final Logger sLOGGER = Logger.getLogger(ADXL345_SpiAccelerometer.class);
    private static double sLSB = 0.00390625;

    protected final ThreeAxisAccelerometer mDataContainer;
    protected final int mNativePort;

    public ADXL345_SpiAccelerometer(int aPort)
    {
        mDataContainer = new ThreeAxisAccelerometer(100 + aPort * 3, "ADXL345 SPI Accel ");
        mNativePort = aPort;
    }

    private void populateRead(int lastWrittenAddress)
    {
        ByteBuffer buffer = ByteBuffer.allocateDirect(10);
        buffer.put((byte) 0);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        boolean includeAll = lastWrittenAddress == 0x02;

        if (lastWrittenAddress >= 0x2 && lastWrittenAddress <= 0x6)
        {
            if (lastWrittenAddress == 0x02)
            {
                short value = (short) (mDataContainer.getX() / sLSB);
                buffer.putShort(value);
            }

            if (lastWrittenAddress == 0x04 || includeAll)
            {
                short value = (short) (mDataContainer.getY() / sLSB);
                buffer.putShort(value);
            }

            if (lastWrittenAddress == 0x06 || includeAll)
            {
                short value = (short) (mDataContainer.getZ() / sLSB);
                buffer.putShort(value);
            }

            SensorFeedbackJni.setSpiValueForRead(mNativePort, buffer, buffer.capacity());
        }
        else
        {
            sLOGGER.log(Level.ERROR, "Unsupported write address " + lastWrittenAddress);
        }
    }

    @Override
    public void handleRead()
    {

    }

    @Override
    public void handleWrite()
    {

    }

    @Override
    public void handleTransaction()
    {
        ByteBuffer buffer = ByteBuffer.allocateDirect(10);
        SensorFeedbackJni.getSpiLastTransaction(mNativePort, buffer, buffer.capacity());

        int lastWrittenAddress = buffer.get() & 0xF;
        populateRead(lastWrittenAddress);
    }

    @Override
    public void resetAccumulator()
    {

    }

}
