package com.snobot.simulator.simulator_components;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.test.utilities.BaseSimulatorTest;


public class TestCompressorWrapper extends BaseSimulatorTest
{

    @Test
    public void testCompressorWrapper()
    {
        CompressorWrapper compressor = new CompressorWrapper();

        Assert.assertEquals(120, compressor.getAirPressure(), DOUBLE_EPSILON);

        compressor.solenoidFired(60);
        Assert.assertEquals(60, compressor.getAirPressure(), DOUBLE_EPSILON);

        for (int i = 0; i < 100; ++i)
        {
            compressor.update();
        }
        Assert.assertEquals(85, compressor.getAirPressure(), DOUBLE_EPSILON);

    }
}
