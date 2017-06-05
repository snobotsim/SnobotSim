/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2016-2017. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.hal;

//
// base class for all JNI wrappers
//
public class JNIWrapper
{
    public static int getPortWithModule(byte module, byte channel)
    {
        return channel;
    }

    public static int getPort(byte channel)
    {
        return channel;
    }
}
