
package com.snobot.simulator;

public class SimulationConnectorJni {
    
    class DcMotorModelConfig
    {
        
    }

    public static native void updateLoop();

    public static native void setSpeedControllerSimpleModel(
            int aSpeedControllerHandle, 
            double aMaxSpeed);

    public static native void setSpeedControllerModel(
            int aSpeedControllerHandle, 
            String aModelType, 
            DcMotorModelConfig aConfig);

    public static native int connectEncoderAndSpeedController(
            int aEncoderHandleA, 
            int aEncoderHandleB, 
            int aSpeedControllerHandle);

    public static native void connectEncoderAndSpeedController(
            int aEncoderHandle, 
            int aSpeedControllerHandle);

    public static native void connectTankDriveSimulator(
            int aLeftEncoderHandleA, int aLeftEncoderHandleB, 
            int aRightEncoderHandleA, int aRightEncoderHandleB, 
            int aGyroHandle, 
            double aTurnKp);

    public static native void connectTankDriveSimulator(
            int aLeftEncoderHandle, 
            int aRightEncoderHandle, 
            int aGyroHandle, 
            double aTurnKp);
}
