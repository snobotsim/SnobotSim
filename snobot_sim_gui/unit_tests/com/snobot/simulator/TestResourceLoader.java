package com.snobot.simulator;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.test.utilities.BaseSimulatorTest;

public class TestResourceLoader extends BaseSimulatorTest
{

    @Test
    public void testLoadMissingResource()
    {
        Assert.assertFalse(JniLibraryResourceLoader.loadLibrary("DoesNotExist"));
    }

    @Test
    public void testLoadLibraryTwice()
    {
        Assert.assertTrue(JniLibraryResourceLoader.loadLibrary("wpiHal"));
        Assert.assertTrue(JniLibraryResourceLoader.loadLibrary("wpiHal"));
    }

    @Test
    public void testCopyResource() throws IOException
    {
        String resourceFile = "/com/snobot/simulator/config/default_properties.properties";
        Assert.assertTrue(JniLibraryResourceLoader.copyResourceFromJar(resourceFile, new File("test_output/deleteOnCopy.properties")));
        Assert.assertTrue(JniLibraryResourceLoader.copyResourceFromJar(resourceFile, new File("test_output/saveOnCopy.properties"), false));
    }
}
