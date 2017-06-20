
package com.snobot.simulator.jni;

import com.snobot.simulator.DcMotorModelConfig;

public class SimulationConnectorJni extends BaseSimulatorJni
{
    
    public static native void updateLoop();

    public static native void setSpeedControllerModel_Simple(
            int aSpeedControllerHandle, 
            double aMaxSpeed);

    public static void setSpeedControllerModel_Static(
            int aSpeedControllerHandle, 
            DcMotorModelConfig aConfig,
            double aLoad)
    {
    	setSpeedControllerModel_Static(aSpeedControllerHandle, aConfig, aLoad, 1.0);
    }

    public static native void setSpeedControllerModel_Static(
            int aSpeedControllerHandle, 
            DcMotorModelConfig aConfig,
            double aLoad,
            double aConversionFactor);

    public static native void setSpeedControllerModel_Gravitational(
            int aSpeedControllerHandle, 
            DcMotorModelConfig aConfig,
            double aLoad);

    public static void setSpeedControllerModel_Rotational(
            int aSpeedControllerHandle, 
            DcMotorModelConfig aConfig,
            double aArmCenterOfMass,
            double aArmMass)
    {
    	setSpeedControllerModel_Rotational(aSpeedControllerHandle, aConfig, aArmCenterOfMass, aArmMass, 0, 0);
    }

    public static native void setSpeedControllerModel_Rotational(
            int aSpeedControllerHandle, 
            DcMotorModelConfig aConfig,
            double aArmCenterOfMass,
            double aArmMass,
            double aConstantAssistTorque,
            double aOverCenterAssistTorque);
    
    public static native void connectEncoderAndSpeedController(
            int aEncoderHandle, 
            int aSpeedControllerHandle);

    public static native void connectTankDriveSimulator(
            int aLeftEncoderHandle, 
            int aRightEncoderHandle, 
            int aGyroHandle, 
            double aTurnKp);
}
