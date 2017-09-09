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

#pragma once

#ifndef CTR_EXCLUDE_WPILIB_CLASSES
#include "CANTalon.h"
#include "CtreCanMap.h"
#include "ErrorBase.h"

/** 
 * Pigeon IMU Class.
 * Class supports communicating over CANbus and over ribbon-cable (CAN Talon SRX).
 */
class PigeonImu : 	public CtreCanMap,
					public frc::ErrorBase {
public:
	/** Data object for holding fusion information. */
	struct FusionStatus {
		double heading;
		bool bIsValid;
		bool bIsFusing;
		std::string description;
		/**
		 * Same as GetLastError()
		 */
		int lastError;
	};
	/** Various calibration modes supported by Pigeon. */
	enum CalibrationMode {
		BootTareGyroAccel = 0,
		Temperature = 1,
		Magnetometer12Pt = 2,
		Magnetometer360 = 3,
		Accelerometer = 5,
	};
	/** Overall state of the Pigeon. */
	enum PigeonState {
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
	struct GeneralStatus {
		/**
		 * The current state of the motion driver.  This reflects if the sensor signals are accurate.
		 * Most calibration modes will force Pigeon to reinit the motion driver.
		 */
		PigeonImu::PigeonState state;
		/**
		 * The currently applied calibration mode if state is in UserCalibration or if bCalIsBooting is true.
		 * Otherwise it holds the last selected calibration mode (when calibrationError was updated).
		 */
		PigeonImu::CalibrationMode currentMode;
		/**
		 * The error code for the last calibration mode.
		 * Zero represents a successful cal (with solid green LEDs at end of cal)
		 * and nonzero is a failed calibration (with solid red LEDs at end of cal).
		 * Different calibration
		 */
		int calibrationError;
		/**
		 * After caller requests a calibration mode, pigeon will perform a boot-cal before
		 * entering the requested mode.  During this period, this flag is set to true.
		 */
		bool bCalIsBooting;
		/**
		 * general string description of current status
		 */
		std::string description;
		/**
		 * Temperature in Celsius
		 */
		double tempC;
		/**
		 * Number of seconds Pigeon has been up (since boot).
		 * This register is reset on power boot or processor reset.
		 * Register is capped at 255 seconds with no wrap around.
		 */
		int upTimeSec;
		/**
		 * Number of times the Pigeon has automatically rebiased the gyro.
		 * This counter overflows from 15 -> 0 with no cap.
		 */
		int noMotionBiasCount;
		/**
		 * Number of times the Pigeon has temperature compensated the various signals.
		 * This counter overflows from 15 -> 0 with no cap.
		 */
		int tempCompensationCount;
		/**
		 * Same as GetLastError()
		 */
		int lastError;
	};
	/** General Parameter Enums */
	enum ParamEnum {
		ParamEnum_YawOffset = 160,
		ParamEnum_CompassOffset = 161,
		ParamEnum_BetaGain = 162,
		ParamEnum_Reserved163 = 163,
		ParamEnum_GyroNoMotionCal = 164,
		ParamEnum_EnterCalibration = 165,
		ParamEnum_FusedHeadingOffset = 166,
		ParamEnum_StatusFrameRate	= 169,
		ParamEnum_AccumZ	= 170,
		ParamEnum_TempCompDisable	= 171,
	};

	/** Enumerated type for status frame types. */
	enum StatusFrameRate {
		StatusFrameRate_CondStatus_1_General = 2,
		StatusFrameRate_CondStatus_9_SixDeg_YPR = 3,
		StatusFrameRate_CondStatus_6_SensorFusion = 4,
		StatusFrameRate_CondStatus_11_GyroAccum = 5,
		StatusFrameRate_CondStatus_2_GeneralCompass = 11,
		StatusFrameRate_CondStatus_3_GeneralAccel = 12,
		StatusFrameRate_CondStatus_10_SixDeg_Quat = 14,
		StatusFrameRate_RawStatus_4_Mag = 6,
		StatusFrameRate_BiasedStatus_2_Gyro = 8,
		StatusFrameRate_BiasedStatus_4_Mag = 9,
		StatusFrameRate_BiasedStatus_6_Accel = 10,
	};

	PigeonImu(int deviceNumber);
	PigeonImu(CANTalon * talonSrx);

	/**
	 * General setter to allow for the use of future features, without having to update API.
	 * @param paramEnum Parameter to set
	 * @param paramValue Parameter value
	 * @return nonzero error code if set fails.
	 */
	int ConfigSetParameter(ParamEnum paramEnum, double paramValue);

	void SetStatusFrameRateMs(StatusFrameRate statusFrameRate, int periodMs);

	int SetYaw(double angleDeg);
	int AddYaw(double angleDeg);
	int SetYawToCompass();

	int SetFusedHeading(double angleDeg);
	int AddFusedHeading(double angleDeg);
	int SetFusedHeadingToCompass();
	int SetAccumZAngle(double angleDeg);
	int EnableTemperatureCompensation(bool bTempCompEnable);

	int SetCompassDeclination(double angleDegOffset);
	int SetCompassAngle(double angleDeg);

	int EnterCalibrationMode(CalibrationMode calMode);
	int GetGeneralStatus(PigeonImu::GeneralStatus & genStatusToFill);
	int GetLastError();
	int Get6dQuaternion(double wxyz[4]);
	int GetYawPitchRoll(double ypr[3]);
	int GetAccumGyro(double xyz_deg[3]);
	double GetAbsoluteCompassHeading();
	double GetCompassHeading();
	double GetCompassFieldStrength();
	double GetTemp();
	PigeonState GetState();
	uint32_t GetUpTime();
	int GetRawMagnetometer(int16_t rm_xyz[3]);

	int GetBiasedMagnetometer(int16_t bm_xyz[3]);
	int GetBiasedAccelerometer(int16_t ba_xyz[3]);
	int GetRawGyro(double xyz_dps[3]);
	int GetAccelerometerAngles(double tiltAngles[3]);

	double GetFusedHeading(FusionStatus & status);
	double GetFusedHeading();
	uint32_t GetResetCount();
	uint32_t GetResetFlags();
	uint32_t GetFirmVers();

	bool HasResetOccured();

	static std::string ToString(PigeonImu::PigeonState state);
	static std::string ToString(CalibrationMode cm);
private:
	/** firmware state reported over CAN */
	enum MotionDriverState {
		Init0 = 0,
		WaitForPowerOff = 1,
		ConfigAg = 2,
		SelfTestAg = 3,
		StartDMP = 4,
		ConfigCompass_0 = 5,
		ConfigCompass_1 = 6,
		ConfigCompass_2 = 7,
		ConfigCompass_3 = 8,
		ConfigCompass_4 = 9,
		ConfigCompass_5 = 10,
		SelfTestCompass = 11,
		WaitForGyroStable = 12,
		AdditionalAccelAdjust = 13,
		Idle = 14,
		Calibration = 15,
		LedInstrum = 16,
		Error = 31,
	};
	/** sub command for the various Set param enums */
	enum TareType {
		SetValue = 0x00, AddOffset = 0x01, MatchCompass = 0x02, SetOffset = 0xFF,
	};
	/** data storage for reset signals */
	struct ResetStats {
		int32_t resetCount;
		int32_t resetFlags;
		int32_t firmVers;
		bool hasReset;
	};
	ResetStats _resetStats = { 0 };

	enum UsageFlags{
	Default = 0x00000000, 
	ConnectCAN = 0x00000001,
	ConnectTalonSRX = 0x00000002,
	
	GetCompass = 0x08000000,
	GetYPR = 0x10000000,
	GetFused = 0x20000000,
	GetAccel = 0x40000000,
	TempComp = 0x80000000,
    };
	
	/** Portion of the arbID for all status and control frames. */
	uint32_t _deviceId;
	uint32_t _deviceNumber;
	uint32_t _usageHist = 0;
	int32_t _lastError = 0;
	uint64_t _cache;
	uint32_t _len;

	/** overall threshold for when frame data is too old */
	const uint32_t EXPECTED_RESPONSE_TIMEOUT_MS = (200);

	const uint32_t RAW_STATUS_2 = 0x00040C40;
	const uint32_t RAW_STATUS_4 = 0x00040CC0;
	const uint32_t RAW_STATUS_6 = 0x00040D40;

	const uint32_t BIASED_STATUS_2 = 0x00041C40;
	const uint32_t BIASED_STATUS_4 = 0x00041CC0;
	const uint32_t BIASED_STATUS_6 = 0x00041D40;

	const uint32_t COND_STATUS_1 = 0x00042000;
	const uint32_t COND_STATUS_2 = 0x00042040;
	const uint32_t COND_STATUS_3 = 0x00042080;
	const uint32_t COND_STATUS_4 = 0x000420c0;
	const uint32_t COND_STATUS_5 = 0x00042100;
	const uint32_t COND_STATUS_6 = 0x00042140;
	const uint32_t COND_STATUS_7 = 0x00042180;
	const uint32_t COND_STATUS_8 = 0x000421c0;
	const uint32_t COND_STATUS_9 = 0x00042200;
	const uint32_t COND_STATUS_10 = 0x00042240;
	const uint32_t COND_STATUS_11 = 0x00042280;

	const uint32_t CONTROL_1 = 0x00042800;

	const uint32_t PARAM_REQUEST = 0x00042C00;
	const uint32_t PARAM_RESPONSE = 0x00042C40;
	const uint32_t PARAM_SET = 0x00042C80;

	int ConfigSetParameter(ParamEnum paramEnum, TareType tareType, double angleDeg);
	int HandleError(int errorCode);
	int ReceiveCAN(int arbId);
	int ReceiveCAN(int arbId, bool allowStale);
	int SendCAN(int arbId, const uint64_t & data, int dataSize, int periodMs);
	int GetTwoParam16(int arbId, short words[2]);
	int GetThreeParam16(int arbId, short words[3]);
	int GetThreeParam16(int arbId, double signals[3], double scalar);
	int GetThreeFloatAngles(int arbId, double signals[3], double scalar);
	int GetThreeBoundedAngles(int arbId, double boundedAngles[3]);
	int GetFourParam16(int arbId, double params[4], double scalar);
	int GetThreeParam20(int arbId, double param[3], double scalar);
	int GetStartupStatus();

	PigeonImu::PigeonState GetState(int errCode, const uint64_t & statusFrame);
	double GetTemp(const uint64_t & statusFrame);
	
	void ApplyUsageStats(UsageFlags Usage);
};
#endif // CTR_EXCLUDE_WPILIB_CLASSES
