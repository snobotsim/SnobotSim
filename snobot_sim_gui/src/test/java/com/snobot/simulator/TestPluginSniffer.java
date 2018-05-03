package com.snobot.simulator;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

public class TestPluginSniffer
{
    @Test
    public void testPluginSnifferWithNoPlugins() throws Exception
    {
        PluginSniffer sniffer = new PluginSniffer();
        sniffer.loadPlugins(new File("test_files/plugins"));

        Assert.assertEquals(0, sniffer.getCppRobots().size());
        Assert.assertEquals(0, sniffer.getJavaRobots().size());
    }
}
