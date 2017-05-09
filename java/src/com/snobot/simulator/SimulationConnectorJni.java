
package com.snobot.simulator;

public class SimulationConnectorJni {
    
    public static native void updateLoop();

    public static native void setSpeedControllerModel_Simple(
            int aSpeedControllerHandle, 
            double aMaxSpeed);

    public static void setSpeedControllerModel_Static(
            int aSpeedControllerHandle, 
            DcMotorModelConfigJni aConfig,
            double aLoad)
    {
    	setSpeedControllerModel_Static(aSpeedControllerHandle, aConfig, aLoad, 1.0);
    }

    public static native void setSpeedControllerModel_Static(
            int aSpeedControllerHandle, 
            DcMotorModelConfigJni aConfig,
            double aLoad,
            double aConversionFactor);

    public static native void setSpeedControllerModel_Gravitational(
            int aSpeedControllerHandle, 
            DcMotorModelConfigJni aConfig,
            double aLoad);

    public static void setSpeedControllerModel_Rotational(
            int aSpeedControllerHandle, 
            DcMotorModelConfigJni aConfig,
            double aArmCenterOfMass,
            double aArmMass)
    {
    	setSpeedControllerModel_Rotational(aSpeedControllerHandle, aConfig, aArmCenterOfMass, aArmMass, 0, 0);
    }

    public static native void setSpeedControllerModel_Rotational(
            int aSpeedControllerHandle, 
            DcMotorModelConfigJni aConfig,
            double aArmCenterOfMass,
            double aArmMass,
            double aConstantAssistTorque,
            double aOverCenterAssistTorque);

    public static int connectEncoderAndSpeedController(
            int aEncoderHandleA, 
            int aEncoderHandleB, 
            int aSpeedControllerHandle)
    {
    	int handle = (aEncoderHandleA << 8) + aEncoderHandleB;
    	
    	connectEncoderAndSpeedController(handle, aSpeedControllerHandle);
    	
    	return handle;
    }
    
    public static native void connectEncoderAndSpeedController(
            int aEncoderHandle, 
            int aSpeedControllerHandle);

    public static void connectTankDriveSimulator(
            int aLeftEncoderHandleA, int aLeftEncoderHandleB, 
            int aRightEncoderHandleA, int aRightEncoderHandleB, 
            int aGyroHandle, 
            double aTurnKp)
    {
    	int leftHandle = (aLeftEncoderHandleA << 8) + aLeftEncoderHandleB;
    	int rightHandle = (aRightEncoderHandleA << 8) + aRightEncoderHandleB;
    	
    	connectTankDriveSimulator(leftHandle, rightHandle, aGyroHandle, aTurnKp);
    }

    public static native void connectTankDriveSimulator(
            int aLeftEncoderHandle, 
            int aRightEncoderHandle, 
            int aGyroHandle, 
            double aTurnKp);
}
