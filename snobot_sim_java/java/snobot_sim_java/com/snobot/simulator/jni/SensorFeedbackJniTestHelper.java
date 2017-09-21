package com.snobot.simulator.jni;

import java.nio.ByteBuffer;

public class SensorFeedbackJniTestHelper
{

    public static native void commandCan(int messageId, ByteBuffer buffer, int size);
}