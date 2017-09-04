package com.snobot.simulator;

import org.junit.Assert;
import org.junit.Test;

public class TestPluginSniffer
{

    @Test
    public void testPluginSnifferWithNoPlugins() throws Exception
    {
        PluginSniffer sniffer = new PluginSniffer();
        sniffer.loadPlugins();

        Assert.assertEquals(0, sniffer.getCppRobots().size());
        Assert.assertEquals(0, sniffer.getJavaRobots().size());
    }
}
