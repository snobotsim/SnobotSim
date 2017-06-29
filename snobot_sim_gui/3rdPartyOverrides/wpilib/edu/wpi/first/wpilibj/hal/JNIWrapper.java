/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2016-2017. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.hal;

import com.snobot.simulator.JniLibraryResourceLoader;
import com.snobot.simulator.jni.BaseSimulatorJni;

//
// base class for all JNI wrappers
//
public class JNIWrapper
{
    static
    {
        BaseSimulatorJni.initilaize();
        JniLibraryResourceLoader.loadLibrary("wpilibJavaJNI");
    }

    public static native int getPortWithModule(byte module, byte channel);

    public static native int getPort(byte channel);
}
