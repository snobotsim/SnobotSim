/*
 *  Software License Agreement
 *
 * Copyright (C) Cross The Road Electronics.  All rights
 * reserved.
 * 
 * Cross The Road Electronics (CTRE) licenses to you the right to 
 * use, publish, and distribute copies of CRF (Cross The Road) firmware files (*.crf) and Software
 * API Libraries ONLY when in use with Cross The Road Electronics hardware products.
 * 
 * THE SOFTWARE AND DOCUMENTATION ARE PROVIDED "AS IS" WITHOUT
 * WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING WITHOUT
 * LIMITATION, ANY WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE AND NON-INFRINGEMENT. IN NO EVENT SHALL
 * CROSS THE ROAD ELECTRONICS BE LIABLE FOR ANY INCIDENTAL, SPECIAL, 
 * INDIRECT OR CONSEQUENTIAL DAMAGES, LOST PROFITS OR LOST DATA, COST OF
 * PROCUREMENT OF SUBSTITUTE GOODS, TECHNOLOGY OR SERVICES, ANY CLAIMS
 * BY THIRD PARTIES (INCLUDING BUT NOT LIMITED TO ANY DEFENSE
 * THEREOF), ANY CLAIMS FOR INDEMNITY OR CONTRIBUTION, OR OTHER
 * SIMILAR COSTS, WHETHER ASSERTED ON THE BASIS OF CONTRACT, TORT
 * (INCLUDING NEGLIGENCE), BREACH OF WARRANTY, OR OTHERWISE
 */
package com.ctre.phoenix.Sensors;
import com.ctre.phoenix.MotorControl.*;

@SuppressWarnings("MethodName")
public class PigeonImuJNI extends CTREJNIWrapper {
  
	public static native long JNI_new_PigeonImu_Talon(int talonID);
  
	public static native long JNI_new_PigeonImu(int deviceNumber);
  
	public static native void JNI_ConfigSetParameter(long handle, int paramEnum, double paramValue);
	  
	public static native void JNI_SetStatusFrameRateMs(long handle, int statusFrame, int periodMs);
	  
	public static native void JNI_SetYaw(long handle, double angleDeg);
	
	public static native void JNI_AddYaw(long handle, double angleDeg);
	
	public static native void JNI_SetYawToCompass(long handle);
	
	public static native void JNI_SetFusedHeading(long handle, double angleDeg);
	
	public static native void JNI_AddFusedHeading(long handle, double angleDeg);
	
	public static native void JNI_SetFusedHeadingToCompass(long handle);
	
	public static native void JNI_SetAccumZAngle(long handle, double angleDeg);
	
	public static native void JNI_EnableTemperatureCompensation(long handle, int bTempCompEnable);
	
	public static native void JNI_SetCompassDeclination(long handle, double angleDegOffset);
	
	public static native void JNI_SetCompassAngle(long handle, double angleDeg);
	
	public static native void JNI_EnterCalibrationMode(long handle, int calMode);
	  
	public static native void JNI_GetGeneralStatus(long handle, Object pigeonImu, Object generalStatus);
	
	public static native double[] JNI_Get6dQuaternion(long handle);
	
	public static native double[] JNI_GetYawPitchRoll(long handle);
	
	public static native double[] JNI_GetAccumGyro(long handle);
	
	public static native double JNI_GetAbsoluteCompassHeading(long handle);
	
	public static native double JNI_GetCompassHeading(long handle);
	
	public static native double JNI_GetCompassFieldStrength(long handle);
	
	public static native double JNI_GetTemp(long handle);
	
	public static native int JNI_GetUpTime(long handle);
	
	public static native short[] JNI_GetRawMagnetometer(long handle);
	
	public static native short[] JNI_GetBiasedMagnetometer(long handle);
	
	public static native short[] JNI_GetBiasedAccelerometer(long handle);
	
	public static native double[] JNI_GetRawGyro(long handle);
	
	public static native double[] JNI_GetAccelerometerAngles(long handle);
	
	public static native double JNI_GetFusedHeading(long handle);
	  
	public static native double JNI_GetFusedHeading(long handle, Object pigeonImu, Object status);
	  
	public static native int JNI_GetState(long handle);
	
	public static native int JNI_GetResetCount(long handle);
	
	public static native int JNI_GetResetFlags(long handle);
	
	public static native int JNI_GetFirmVers(long handle);
	  
	public static native int GetLastError(long handle);
	  
	public static native boolean JNI_HasResetOccured(long handle);
}