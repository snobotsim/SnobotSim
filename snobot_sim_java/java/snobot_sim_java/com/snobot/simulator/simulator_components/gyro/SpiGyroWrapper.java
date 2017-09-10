package com.snobot.simulator.simulator_components.gyro;

import java.nio.ByteBuffer;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.jni.SensorFeedbackJni;
import com.snobot.simulator.simulator_components.ISpiWrapper;

public class SpiGyroWrapper extends GyroWrapper implements ISpiWrapper
{
    public static final int SPI_GYRO_OFFSET = 100;

    protected final int mSpiPort;

    public SpiGyroWrapper(int aSpiPort)
    {
        super("SPI Gyro " + aSpiPort, new AngleSetterHelper()
        {
            // constructor
            {
            }

            @Override
            public void updateAngle(double aAngle)
            {
                long accumValue = (long) (aAngle / 0.0125 / 0.001);
                SensorFeedbackJni.setSpiAccumulatorValue(aSpiPort, accumValue);
            }
        });

        SensorActuatorRegistry.get().register((GyroWrapper) this, aSpiPort + SPI_GYRO_OFFSET);

        mSpiPort = aSpiPort;
    }

    @Override
    public void handleRead()
    {
        ByteBuffer buf = ByteBuffer.allocateDirect(4);
        buf.putInt(0xe00a4000);
        SensorFeedbackJni.setSpiValueForRead(mSpiPort, buf, 4);
    }
}
