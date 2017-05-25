package com.ctre;

public class CTREJNIWrapper
{
    public CTREJNIWrapper()
    {
    }

    public static native int getPortWithModule(byte paramByte1, byte paramByte2);

    public static native int getPort(byte paramByte);

    static boolean libraryLoaded = false;

    static
    {
        if (!libraryLoaded)
        {
            try
            {
                System.loadLibrary("CTRLibDriver");
            }
            catch (UnsatisfiedLinkError e)
            {
                e.printStackTrace();
                System.exit(1);
            }
            libraryLoaded = true;
        }
      }
    }
