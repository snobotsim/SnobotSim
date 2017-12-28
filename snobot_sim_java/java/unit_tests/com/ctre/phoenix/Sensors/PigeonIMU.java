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

import com.ctre.phoenix.MotorControl.CAN.TalonSRX;

/** 
 * Pigeon IMU Class.
 * Class supports communicating over CANbus and over ribbon-cable (CAN Talon SRX).
 */
public class PigeonIMU
{
	private long m_handle;
	/** Data object for holding fusion information. */
	public static class FusionStatus {
		public double heading;
		public boolean bIsValid;
		public boolean bIsFusing;
		public String description;
		/**
		 * Same as getLastError()
		 */
		public int lastError;
	}
	/** Various calibration modes supported by Pigeon. */
	public enum CalibrationMode	{
		BootTareGyroAccel(0),
		Temperature(1),
		Magnetometer12Pt(2),
		Magnetometer360(3),
		Accelerometer(5),
		Unknown(-1);
		private int value; private CalibrationMode(int value) { this.value = value; } 

		public static CalibrationMode getEnum(int value) {
			for (CalibrationMode e : CalibrationMode.values()) {
				if (e.value == value) {
					return e;
				}
			}
			return Unknown;
		}
	};
	/** Overall state of the Pigeon. */
	public enum PigeonState {
		NoComm,
		Initializing,
		Ready,
		UserCalibration,
	};
	/**
	 * Data object for status on current calibration and general status.
	 *
	 * Pigeon has many calibration modes supported for a variety of uses.
	 * The modes generally collects and saves persistently information that makes
	 * the Pigeon signals more accurate.  This includes collecting temperature, gyro, accelerometer,
	 * and compass information.
	 *
	 * For FRC use-cases, typically compass and temperature calibration is not required.
	 *
	 * Additionally when motion driver software in the Pigeon boots, it will perform a fast boot calibration
	 * to initially bias gyro and setup accelerometer.
	 *
	 * These modes can be enabled with the EnterCalibration mode.
	 *
	 * When a calibration mode is entered, caller can expect...
	 *
	 *  - PigeonState to reset to Initializing and bCalIsBooting is set to true.  Pigeon LEDs will blink the boot pattern.
	 *  	This is similar to the normal boot cal, however it can an additional ~30 seconds since calibration generally
	 *  	requires more information.
	 *  	currentMode will reflect the user's selected calibration mode.
	 *
	 *  - PigeonState will eventually settle to UserCalibration and Pigeon LEDs will show cal specific blink patterns.
	 *  	bCalIsBooting is now false.
	 *
	 *  - Follow the instructions in the Pigeon User Manual to meet the calibration specific requirements.
	 * 		When finished calibrationError will update with the result.
	 * 		Pigeon will solid-fill LEDs with red (for failure) or green (for success) for ~5 seconds.
	 * 		Pigeon then perform boot-cal to cleanly apply the newly saved calibration data.
	 */
	public static class GeneralStatus {
		/**
		 * The current state of the motion driver.  This reflects if the sensor signals are accurate.
		 * Most calibration modes will force Pigeon to reinit the motion driver.
		 */
		public PigeonState state;
		/**
		 * The currently applied calibration mode if state is in UserCalibration or if bCalIsBooting is true.
		 * Otherwise it holds the last selected calibration mode (when calibrationError was updated).
		 */
		public CalibrationMode currentMode;
		/**
		 * The error code for the last calibration mode.
		 * Zero represents a successful cal (with solid green LEDs at end of cal)
		 * and nonzero is a failed calibration (with solid red LEDs at end of cal).
		 * Different calibration
		 */
		public int calibrationError;
		/**
		 * After caller requests a calibration mode, pigeon will perform a boot-cal before
		 * entering the requested mode.  During this period, this flag is set to true.
		 */
		public boolean bCalIsBooting;
		/**
		 * general string description of current status
		 */
		public String description;
		/**
		 * Temperature in Celsius
		 */
		public double tempC;
		/**
		 * Number of seconds Pigeon has been up (since boot).
		 * This register is reset on power boot or processor reset.
		 * Register is capped at 255 seconds with no wrap around.
		 */
		public int upTimeSec;
		/**
		 * Number of times the Pigeon has automatically rebiased the gyro.
		 * This counter overflows from 15 -> 0 with no cap.
		 */
		public int noMotionBiasCount;
		/**
		 * Number of times the Pigeon has temperature compensated the various signals.
		 * This counter overflows from 15 -> 0 with no cap.
		 */
		public int tempCompensationCount;
		/**
		 * Same as getLastError()
		 */
		public int lastError;
	};
	/** General Parameter Enums */
	public enum ParamEnum {
		YawOffset(160),
		CompassOffset(161),
		BetaGain(162),
		Reserved163(163),
		GyroNoMotionCal(164),
		EnterCalibration(165),
		FusedHeadingOffset(166),
		StatusFrameRate(169),
		AccumZ(170),
		TempCompDisable(171);
		private int value; private ParamEnum(int value) { this.value = value; } 
	};
	/**
	 * Enumerated types for frame rate ms.
	 */
	public enum StatusFrameRate {
		CondStatus_1_General (2),
		CondStatus_9_SixDeg_YPR (3),
		CondStatus_6_SensorFusion (4),
		CondStatus_11_GyroAccum (5),
		CondStatus_2_GeneralCompass (11),
		CondStatus_3_GeneralAccel (12),
		CondStatus_10_SixDeg_Quat (14),
		RawStatus_4_Mag (6),
		BiasedStatus_2_Gyro (8),
		BiasedStatus_4_Mag (9),
		BiasedStatus_6_Accel (10);

		public int value;

		public static StatusFrameRate valueOf(int value) {
			for (StatusFrameRate mode : values()) {
				if (mode.value == value) {
					return mode;
				}
			}
			return null;
		}

		StatusFrameRate(int value) {
			this.value = value;
		}
	}

	private int m_deviceNumber = 0;

	/**
	 * Create a Pigeon object that communicates with Pigeon on CAN Bus.
	 * @param deviceNumber CAN Device Id of Pigeon [0,62]
	 */
	public PigeonIMU(int deviceNumber)
	{
		m_handle = PigeonImuJNI.JNI_new_PigeonImu(deviceNumber);
		m_deviceNumber = deviceNumber;
	}

	/**
	 * Create a Pigeon object that communciates with Pigeon through the Gadgeteer ribbon
	 * @param talonSrx cable connected to a Talon on CAN Bus.
	 */
	public PigeonIMU(TalonSRX talonSrx)
	{
		m_deviceNumber = talonSrx.getDeviceID();
		m_handle = PigeonImuJNI.JNI_new_PigeonImu_Talon(m_deviceNumber);
	}

	//----------------------- Control Param routines -----------------------//
	/**
	 * General setter to allow for the use of future features, without having to update API.
	 * @param paramEnum Parameter to set
	 * @param paramValue Parameter value
	 */
	public void configSetParameter(ParamEnum paramEnum, double paramValue)
	{
		PigeonImuJNI.JNI_ConfigSetParameter(m_handle, paramEnum.value, paramValue);
	}
	public void setStatusFrameRateMs(StatusFrameRate stateFrameRate, int periodMs) {
		PigeonImuJNI.JNI_SetStatusFrameRateMs(m_handle, stateFrameRate.ordinal(), periodMs);
	}
	public void setYaw(double angleDeg)
	{
		PigeonImuJNI.JNI_SetYaw(m_handle, angleDeg);
	}
	/**
	 * Atomically add to the Yaw register.
	 */
	public void addYaw(double angleDeg)
	{
		PigeonImuJNI.JNI_AddYaw(m_handle, angleDeg);
	}
	public void setYawToCompass()
	{
		PigeonImuJNI.JNI_SetYawToCompass(m_handle);
	}
	public void setFusedHeading(double angleDeg)
	{
		PigeonImuJNI.JNI_SetFusedHeading(m_handle, angleDeg);
	}
	public void addFusedHeading(double angleDeg)
	{
		PigeonImuJNI.JNI_AddFusedHeading(m_handle, angleDeg);
	}
	public void setFusedHeadingToCompass()
	{
		PigeonImuJNI.JNI_SetFusedHeadingToCompass(m_handle);
	}
	public void setAccumZAngle(double angleDeg)
	{
		PigeonImuJNI.JNI_SetAccumZAngle(m_handle, angleDeg);
	}
	/**
	 * Enable/Disable Temp compensation.  Pigeon defaults with this on at boot.
	 */
	public void enableTemperatureCompensation(boolean bTempCompEnable)
	{
		PigeonImuJNI.JNI_EnableTemperatureCompensation(m_handle, bTempCompEnable ? 1 : 0);
	}
	/**
	 * Set the declination for compass.
	 * Declination is the difference between Earth Magnetic north, and the geographic "True North".
	 */
	public void setCompassDeclination(double angleDegOffset)
	{
		PigeonImuJNI.JNI_SetCompassDeclination(m_handle, angleDegOffset);
	}
	/**
	 * Sets the compass angle.
	 * Although compass is absolute [0,360) degrees, the continuous compass
	 * register holds the wrap-arounds.
	 */
	public void setCompassAngle(double angleDeg)
	{
		PigeonImuJNI.JNI_SetCompassAngle(m_handle, angleDeg);
	}
	//----------------------- Calibration routines -----------------------//
	public void enterCalibrationMode(CalibrationMode calMode)
	{
		PigeonImuJNI.JNI_EnterCalibrationMode(m_handle, calMode.value);
	}
	/**
	 * Get the status of the current (or previousley complete) calibration.
	 * @param generalStatus
	 */
	protected void setGeneralStatusFromJNI(GeneralStatus generalStatus,
                                           int pigeonState, int calMode, int calibrationError,
                                           boolean calBooting,
                                           double temp, int upTime,
										   int noMotionCount, int tempCompCount,
										   int lastError)
	{
		generalStatus.state = PigeonState.values()[pigeonState];
		generalStatus.currentMode = CalibrationMode.values()[calMode];
		generalStatus.calibrationError = calibrationError;
		generalStatus.bCalIsBooting = calBooting;
		generalStatus.tempC = temp;
		generalStatus.upTimeSec = upTime;
		generalStatus.noMotionBiasCount = noMotionCount;
		generalStatus.tempCompensationCount = tempCompCount;
		generalStatus.lastError = lastError;
		
		/* build description string */
		if (lastError != 0) { // same as NoComm
			generalStatus.description = "Status frame was not received, check wired connections and web-based config.";
		} else if(generalStatus.bCalIsBooting) {
			generalStatus.description = "Pigeon is boot-caling to properly bias accel and gyro.  Do not move Pigeon.  When finished biasing, calibration mode will start.";
		} else if(generalStatus.state == PigeonState.UserCalibration) {
			/* mode specific descriptions */
			switch(generalStatus.currentMode) {
				case BootTareGyroAccel:
					generalStatus.description = "Boot-Calibration: Gyro and Accelerometer are being biased.";
					break;
				case Temperature:
					generalStatus.description = "Temperature-Calibration: Pigeon is collecting temp data and will finish when temp range is reached. \n";
					generalStatus.description += "Do not moved Pigeon.";
					break;
				case Magnetometer12Pt:
					generalStatus.description = "Magnetometer Level 1 calibration: Orient the Pigeon PCB in the 12 positions documented in the User's Manual.";
					break;
				case Magnetometer360:
					generalStatus.description = "Magnetometer Level 2 calibration: Spin robot slowly in 360' fashion.  ";
					break;
				case Accelerometer:
					generalStatus.description = "Accelerometer Calibration: Pigeon PCB must be placed on a level source.  Follow User's Guide for how to level surfacee.  ";
					break;
				case Unknown:
					generalStatus.description = "Unknown status";
					break;
			}
		} else if (generalStatus.state == PigeonState.Ready){
			/* definitely not doing anything cal-related.  So just instrument the motion driver state */
			generalStatus.description = "Pigeon is running normally.  Last CAL error code was ";
			generalStatus.description += Integer.toString(calibrationError);
			generalStatus.description += ".";
		} else if (generalStatus.state == PigeonState.Initializing){
			/* definitely not doing anything cal-related.  So just instrument the motion driver state */
			generalStatus.description = "Pigeon is boot-caling to properly bias accel and gyro.  Do not move Pigeon.";
		} else {
			generalStatus.description = "Not enough data to determine status.";
		}
	}
	 
	public GeneralStatus getGeneralStatus()
	{
		GeneralStatus statusToFill = new GeneralStatus();
		PigeonImuJNI.JNI_GetGeneralStatus(m_handle, this, statusToFill);
		return statusToFill;
	}
	//----------------------- General Error status  -----------------------//
	public int getLastError()
	{
		return PigeonImuJNI.GetLastError(m_handle);
	}
	
	//----------------------- Strongly typed Signal decoders  -----------------------//
	public double[] get6dQuaternion()
	{
		double[] k = PigeonImuJNI.JNI_Get6dQuaternion(m_handle);
		return k;
	}
	public double[] getYawPitchRoll()
	{
		double[] k = PigeonImuJNI.JNI_GetYawPitchRoll(m_handle);
		return k;
	}
	public double[] getAccumGyro()
	{
		double[] k = PigeonImuJNI.JNI_GetAccumGyro(m_handle);
		return k;
	}
	/**
	 *  @return compass heading [0,360) degrees.
	 */
	public double getAbsoluteCompassHeading()
	{
		double k = PigeonImuJNI.JNI_GetAbsoluteCompassHeading(m_handle);
		return k;
	}
	/**
	 *  @return continuous compass heading [-23040, 23040) degrees.
	 *  Use SetCompassHeading to modify the wrap-around portion.
	 */
	public double getCompassHeading()
	{
		double k = PigeonImuJNI.JNI_GetCompassHeading(m_handle);
		return k;
	}
	/**
	 * @return field strength in Microteslas (uT).
	 */
	public double getCompassFieldStrength()
	{
		double k = PigeonImuJNI.JNI_GetCompassFieldStrength(m_handle);
		return k;
	}
	public double getTemp()
	{
		double k = PigeonImuJNI.JNI_GetTemp(m_handle);
		return k;
	}
	public PigeonState getState(int errCode, long statusFrame)
	{
		PigeonState k = PigeonState.values()[PigeonImuJNI.JNI_GetState(m_handle)];
		return k;
	}
	public PigeonState getState()
	{
		PigeonState retval = PigeonState.values()[PigeonImuJNI.JNI_GetState(m_handle)];
		return retval;
	}
	/// <summary>
	/// How long has Pigeon been running
	/// </summary>
	/// <param name="timeSec"></param>
	/// <returns></returns>
	public int getUpTime()
	{
		int k = PigeonImuJNI.JNI_GetUpTime(m_handle);
		return k;
	}

	public short[] getRawMagnetometer()
	{
		short[] k = PigeonImuJNI.JNI_GetRawMagnetometer(m_handle);
		return k;
	}
	public short[] getBiasedMagnetometer()
	{
		short[] k = PigeonImuJNI.JNI_GetBiasedMagnetometer(m_handle);
		return k;
	}
	public short[] getBiasedAccelerometer()
	{
		short[] k = PigeonImuJNI.JNI_GetBiasedAccelerometer(m_handle);
		return k;
	}
	public double[] getRawGyro()
	{
		double[] k = PigeonImuJNI.JNI_GetRawGyro(m_handle);
		return k;
	}
	
	public double[] getAccelerometerAngles()
	{
		double[] k = PigeonImuJNI.JNI_GetAccelerometerAngles(m_handle);
		return k;
	}
	/**
	 * @param status 	object reference to fill with fusion status flags.  
	 *					Caller may pass null if flags are not needed.
	 */
	protected void setFusedStatusFromJNI(FusionStatus status, 
	                                     double heading, boolean bIsValid, 
										 boolean bIsFusing, int lastError)
	{
		status.heading = heading;
		status.bIsValid = bIsValid;
		status.bIsFusing = bIsFusing;
		status.lastError = lastError;
		if(lastError != 0)
		{
			status.description = "Could not receive status frame.  Check wiring and web-config.";
		}
		else if(bIsValid == false)
		{
			status.description = "Fused Heading is not valid.";
		}
		else if(bIsFusing == false)
		{
			status.description = "Fused Heading is valid.";
		}
		else
		{
			status.description = "Fused Heading is valid and is fusing compass.";
		}
	}
	 
	public FusionStatus getFusedHeading()
	{
		FusionStatus status = new FusionStatus();
		PigeonImuJNI.JNI_GetFusedHeading(m_handle, this, status);
		return status;
	}
	//----------------------- Startup/Reset status -----------------------//
	/**
	 * Polls status5 frame, which is only transmitted on motor controller boot.
	 * @return error code.
	 */
	public int getResetCount()
	{
		int k = PigeonImuJNI.JNI_GetResetCount(m_handle);
		return k;
	}
	public int getResetFlags()
	{
		int k = PigeonImuJNI.JNI_GetResetFlags(m_handle);
		return k;
	}
	 /* @return param holds the version of the Talon.  Talon must be powered cycled at least once. */
	public int getFirmVers()
	{
		int k = PigeonImuJNI.JNI_GetFirmVers(m_handle);
		return k;
	}
	/**
	 * @return true iff a reset has occured since last call.
	 */
	public boolean hasResetOccured()
	{
		boolean k = PigeonImuJNI.JNI_HasResetOccured(m_handle);
		return k;
	}
}
