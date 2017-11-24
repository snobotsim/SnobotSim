/*
 * ?Software License Agreement
 *
 *?Copyright (C) Cross The Road Electronics.? All rights
 *?reserved.
 *?
 *?Cross The Road Electronics (CTRE) licenses to you the right to?
 *?use, publish, and distribute copies of CRF (Cross The Road) firmware files (*.crf) and Software
 * API Libraries ONLY when in use with Cross The Road Electronics hardware products.
 *?
 *?THE SOFTWARE AND DOCUMENTATION ARE PROVIDED "AS IS" WITHOUT
 *?WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING WITHOUT
 *?LIMITATION, ANY WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 *?PARTICULAR PURPOSE, TITLE AND NON-INFRINGEMENT. IN NO EVENT SHALL
 *?CROSS THE ROAD ELECTRONICS BE LIABLE FOR ANY INCIDENTAL, SPECIAL,?
 *?INDIRECT OR CONSEQUENTIAL DAMAGES, LOST PROFITS OR LOST DATA, COST OF
 *?PROCUREMENT OF SUBSTITUTE GOODS, TECHNOLOGY OR SERVICES, ANY CLAIMS
 *?BY THIRD PARTIES (INCLUDING BUT NOT LIMITED TO ANY DEFENSE
 *?THEREOF), ANY CLAIMS FOR INDEMNITY OR CONTRIBUTION, OR OTHER
 *?SIMILAR COSTS, WHETHER ASSERTED ON THE BASIS OF CONTRACT, TORT
 *?(INCLUDING NEGLIGENCE), BREACH OF WARRANTY, OR OTHERWISE
 */
package com.ctre;


import java.nio.ByteBuffer;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.can.CANJNI;

/** 
 * Pigeon IMU Class.
 * Class supports communicating over CANbus and over ribbon-cable (CAN Talon SRX).
 */
public class PigeonImu extends CtreCanMap
{
	/** Data object for holding fusion information. */
	public static class FusionStatus {
		public double heading;
		public boolean bIsValid;
		public boolean bIsFusing;
		public String description;
		/**
		 * Same as GetLastError()
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
		 * Same as GetLastError()
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
	/** firmware state reported over CAN */
	private enum MotionDriverState
	{
		Init0 (0),
		WaitForPowerOff (1),
		ConfigAg (2),
		SelfTestAg (3),
		StartDMP (4),
		ConfigCompass_0 (5),
		ConfigCompass_1 (6),
		ConfigCompass_2 (7),
		ConfigCompass_3 (8),
		ConfigCompass_4 (9),
		ConfigCompass_5 (10),
		SelfTestCompass (11),
		WaitForGyroStable (12),
		AdditionalAccelAdjust (13),
		Idle (14),
		Calibration (15),
		LedInstrum (16),
		Error (31);
		private int value; private MotionDriverState(int value) { this.value = value; } 

		public static MotionDriverState getEnum(int value) {
			for (MotionDriverState e : MotionDriverState.values()) {
				if (e.value == value) {
					return e;
				}
			}
			return Error;
		}
	};
	/** sub command for the various Set param enums */
	private enum TareType {
		SetValue (0x00),
		AddOffset (0x01),
		MatchCompass (0x02),
		SetOffset (0xFF);
		private int value; private TareType(int value) { this.value = value; } 
	};
	/** data storage for reset signals */
	private class ResetStats {
		public int resetCount;
		public int resetFlags;
		public int firmVers;
		public boolean hasReset;
	};
	ResetStats _resetStats = new ResetStats();

	/** Portion of the arbID for all status and control frames. */
	private int _deviceId;
	private int _lastError = 0;
	private long  _cache;
	//private int _len;
	private short [] _cache_words = new short[4];
	private double [] _cache_prec = new double[4];
	private RxEvent _rxe = new RxEvent();

	/** overall threshold for when frame data is too old */
	private final int EXPECTED_RESPONSE_TIMEOUT_MS = (200);

	/** CAN frame defines */
	//private static final int RAW_STATUS_2 = 0x00040C40;
	private static final int RAW_STATUS_4 = 0x00040CC0;
	//private static final int RAW_STATUS_6 = 0x00040D40;

	private static final int BIASED_STATUS_2 = 0x00041C40;
	private static final int BIASED_STATUS_4 = 0x00041CC0;
	private static final int BIASED_STATUS_6 = 0x00041D40;

	private static final int COND_STATUS_1 = 0x00042000;
	private static final int COND_STATUS_2 = 0x00042040;
	private static final int COND_STATUS_3 = 0x00042080;
	//private static final int COND_STATUS_4 = 0x000420c0;
	private static final int COND_STATUS_5 = 0x00042100;
	private static final int COND_STATUS_6 = 0x00042140;
	//private static final int COND_STATUS_7 = 0x00042180;
	//private static final int COND_STATUS_8 = 0x000421c0;
	private static final int COND_STATUS_9 = 0x00042200;
	private static final int COND_STATUS_10 = 0x00042240;
	private static final int COND_STATUS_11 = 0x00042280;
	

	private static final int CONTROL_1 = 0x00042800;

	//private static final int PARAM_REQUEST = 0x00042C00;
	//private static final int PARAM_RESPONSE = 0x00042C40;
	private static final int PARAM_SET = 0x00042C80;

	//private static final int kParamArbIdValue = PARAM_RESPONSE;
	//private static final int kParamArbIdMask = 0xFFFFFFFF;


	/**
	 * Create a Pigeon object that communicates with Pigeon on CAN Bus.
	 * @param deviceNumber CAN Device Id of Pigeon [0,62]
	 */
	public PigeonImu(int deviceNumber)
	{
		_deviceId = 0x15000000 | (int)deviceNumber;

		SendCAN(CONTROL_1 | _deviceId, 0x00000000, 0, 100);
	}

	/**
	 * Create a Pigeon object that communciates with Pigeon through the Gadgeteer ribbon
	 * @param talonSrx cable connected to a Talon on CAN Bus.
	 */
	public PigeonImu(CANTalon talonSrx)
	{
		_deviceId = (int)0x02000000 | talonSrx.getDeviceID();
	
		SendCAN(CONTROL_1 | _deviceId, 0x00000000, 0, 100);
	}

	//----------------------- Control Param routines -----------------------//
	/**
	 * General setter to allow for the use of future features, without having to update API.
	 * @param paramEnum Parameter to set
	 * @param paramValue Parameter value
	 * @return nonzero error code if set fails.
	 */
	public int ConfigSetParameter(ParamEnum paramEnum, double paramValue)
	{
		int frame;
		/* param specific encoders can be placed here */
		frame = (int) paramValue;
		frame <<= 8;
		frame |= (paramEnum.value) & 0xFF;
		int status = SendCAN(PARAM_SET | _deviceId, frame, 5, 0);
		return status;
	}
	private int ConfigSetParameter(ParamEnum paramEnum, TareType tareType, double angleDeg)
	{
		final double deg_per_canunit = 0.015625f;
		int deg_3B = ((int)(angleDeg / deg_per_canunit));
		long paramValue;
		paramValue = (long)deg_3B & 0xFFFFFF;
		paramValue <<= 8;
		paramValue |= (tareType.value) & 0xFF;
		return ConfigSetParameter(paramEnum, (double)paramValue);
	}
	public void SetStatusFrameRateMs(StatusFrameRate stateFrameRate, int periodMs) {
		/* bounds check the period */
		if (periodMs < 1)
			periodMs = 1;
		else if (periodMs > 255)
			periodMs = 255;
		/* bounds frame */
		if(stateFrameRate.value > 255)
			return;
		/* save the unsigned pieces */
		int period = (int)periodMs;
		int idx = (stateFrameRate.value & 0xFF);
		/* assemble */
		int paramValue = period;
		paramValue <<= 8;
		paramValue |= idx;
		/* send the set request */
		ConfigSetParameter(ParamEnum.StatusFrameRate, paramValue);
	}
	public int SetYaw(double angleDeg)
	{
		int errCode = ConfigSetParameter(ParamEnum.YawOffset, TareType.SetValue, angleDeg);
		return HandleError(errCode);
	}
	/**
	 * Atomically add to the Yaw register.
	 */
	public int AddYaw(double angleDeg)
	{
		int errCode = ConfigSetParameter(ParamEnum.YawOffset, TareType.AddOffset, angleDeg);
		return HandleError(errCode);
	}
	public int SetYawToCompass()
	{
		int errCode = ConfigSetParameter(ParamEnum.YawOffset, TareType.MatchCompass, 0);
		return HandleError(errCode);
	}
	public int SetFusedHeading(double angleDeg)
	{
		int errCode = ConfigSetParameter(ParamEnum.FusedHeadingOffset, TareType.SetValue, angleDeg);
		return HandleError(errCode);
	}
	/**
	 * Atomically add to the Fused Heading register.
	 */
	public int AddFusedHeading(double angleDeg)
	{
		int errCode = ConfigSetParameter(ParamEnum.FusedHeadingOffset, TareType.AddOffset, angleDeg);
		return HandleError(errCode);
	}
	public int SetFusedHeadingToCompass()
	{
		int errCode = ConfigSetParameter(ParamEnum.FusedHeadingOffset, TareType.MatchCompass, 0);
		return HandleError(errCode);
	}
	public int SetAccumZAngle(double angleDeg)
	{
		int errCode = ConfigSetParameter(ParamEnum.AccumZ, TareType.SetValue, angleDeg);
		return HandleError(errCode);
	}
	/**
	 * Enable/Disable Temp compensation.  Pigeon defaults with this on at boot.
	 * @param tempCompEnable
	 * @return nonzero for error, zero for success.
	 */
	public int EnableTemperatureCompensation(boolean bTempCompEnable)
	{
		int errCode = ConfigSetParameter(ParamEnum.TempCompDisable, bTempCompEnable ? 0 : 1);
		return HandleError(errCode);
	}
	/**
	 * Set the declination for compass.
	 * Declination is the difference between Earth Magnetic north, and the geographic "True North".
	 */
	int SetCompassDeclination(double angleDegOffset)
	{
		int errCode = ConfigSetParameter(ParamEnum.CompassOffset, TareType.SetOffset, 0);
		return HandleError(errCode);
	}
	/**
	 * Sets the compass angle.
	 * Although compass is absolute [0,360) degrees, the continuous compass
	 * register holds the wrap-arounds.
	 */
	int SetCompassAngle(double angleDeg)
	{
		int errCode = ConfigSetParameter(ParamEnum.CompassOffset, TareType.SetValue, 0);
		return HandleError(errCode);
	}
	//----------------------- Calibration routines -----------------------//
	public int EnterCalibrationMode(CalibrationMode calMode)
	{
		long frame;
		frame = calMode.value & 0xFF;
		frame <<= 8;
		frame |= ((int)ParamEnum.EnterCalibration.value) & 0xFF;
		int status = SendCAN(PARAM_SET | _deviceId, frame, 5, 0);
		return status;
	}
	/**
	 * Get the status of the current (or previousley complete) calibration.
	 * @param statusToFill
	 */
	public int GetGeneralStatus(GeneralStatus statusToFill)
	{
		int errCode= ReceiveCAN(COND_STATUS_1);
	
		int b3 = (int)(_cache >> 0x18) & 0xFF;
		int b5 = (int)(_cache >> 0x28) & 0xFF;

		int iCurrMode = (b5 >> 4) & 0xF;
		CalibrationMode currentMode = CalibrationMode.getEnum(iCurrMode);

		/* shift up bottom nibble, and back down with sign-extension */
		int calibrationErr = (int)b5 & 0xF;
		calibrationErr <<= (32 - 4);
		calibrationErr >>= (32 - 4);

		int noMotionBiasCount = (int) (_cache >> 0x24) & 0xF;
		int tempCompensationCount = (int) (_cache >> 0x20) & 0xF;
		int upTimSec = (int) (_cache >> 0x38) & 0xFF;

		if (statusToFill != null) {
			statusToFill.currentMode = currentMode;
			statusToFill.calibrationError = calibrationErr;
			statusToFill.bCalIsBooting = ((b3 & 1) == 1);
			statusToFill.state = GetState(errCode, _cache);
			statusToFill.tempC = GetTemp(_cache);
			statusToFill.noMotionBiasCount = noMotionBiasCount;
			statusToFill.tempCompensationCount = tempCompensationCount;
			statusToFill.upTimeSec = upTimSec;
			statusToFill.lastError = errCode;

			/* build description string */
			if (errCode != 0) { // same as NoComm
				statusToFill.description = "Status frame was not received, check wired connections and web-based config.";
			} else if(statusToFill.bCalIsBooting) {
				statusToFill.description = "Pigeon is boot-caling to properly bias accel and gyro.  Do not move Pigeon.  When finished biasing, calibration mode will start.";
			} else if(statusToFill.state == PigeonState.UserCalibration) {
				/* mode specific descriptions */
				switch(currentMode) {
					case BootTareGyroAccel:
						statusToFill.description = "Boot-Calibration: Gyro and Accelerometer are being biased.";
						break;
					case Temperature:
						statusToFill.description = "Temperature-Calibration: Pigeon is collecting temp data and will finish when temp range is reached.  " +
						"Do not moved Pigeon.";
						break;
					case Magnetometer12Pt:
						statusToFill.description = "Magnetometer Level 1 calibration: Orient the Pigeon PCB in the 12 positions documented in the User's Manual.";
						break;
					case Magnetometer360:
						statusToFill.description = "Magnetometer Level 2 calibration: Spin robot slowly in 360' fashion.  ";
						break;
					case Accelerometer:
						statusToFill.description = "Accelerometer Calibration: Pigeon PCB must be placed on a level source.  Follow User's Guide for how to level surfacee.  ";
						break;
					case Unknown:
						statusToFill.description = "Unknown Calibration mode: " + iCurrMode;
						break;
				}
			} else if (statusToFill.state == PigeonState.Ready){
				/* definitely not doing anything cal-related.  So just instrument the motion driver state */
				statusToFill.description = "Pigeon is running normally.  Last CAL error code was ";
				statusToFill.description += calibrationErr;
				statusToFill.description += ".";
			} else if (statusToFill.state == PigeonState.Initializing){
				/* definitely not doing anything cal-related.  So just instrument the motion driver state */
				statusToFill.description = "Pigeon is boot-caling to properly bias accel and gyro.  Do not move Pigeon.";
			} else {
				statusToFill.description = "Not enough data to determine status.";
			}
		}
		return HandleError(errCode);
	}
	//----------------------- General Error status  -----------------------//
	public int GetLastError()
	{
		return _lastError;
	}
	private String GetErrorMessage(int ctrCode)
	{
		CTR_Code code = CTR_Code.getEnum(ctrCode);
		switch(code) {
			case CTR_OKAY:return "CTRE CAN Receive Timeout";
			case CTR_RxTimeout: return "CTRE CAN Receive Timeout";
			case CTR_TxTimeout: return "CTRE CAN Transmit Timeout";
			case CTR_InvalidParamValue: return "CTRE CAN Invalid Parameter";
			case CTR_UnexpectedArbId: return "CTRE Unexpected Arbitration ID (CAN Node ID)";
			case CTR_TxFailed: return "CTRE CAN Transmit Error";
			case CTR_SigNotUpdated:return "CTRE CAN Signal Not Updated";
			case CTR_BufferFull:return "CTRE CAN Buffer Full";
			default:
			case CTR_UnknownError:return "CTRE CAN Unknown Error Value:" + ctrCode;
		}
	}
	private int HandleError(int errorCode)
	{
		/* error handler */
		if (errorCode != 0) {
			/* what should we do here? */
			DriverStation.reportError(GetErrorMessage(errorCode),true);
		}
		/* mirror last status */
		_lastError = errorCode;
		return _lastError;
	}

	//----------------------- General Signal decoders  -----------------------//
	private int ReceiveCAN(int arbId)
	{
		return ReceiveCAN(arbId, true);
	}
	private int ReceiveCAN(int arbId, boolean allowStale)
	{
		int retval = super.GetRx(arbId | _deviceId, EXPECTED_RESPONSE_TIMEOUT_MS, _rxe, allowStale);
		/* always pass up the data */
		_cache = _rxe._data;
		//_len = _rxe._len;
		return retval;
	}
	int SendCAN(int arbId, long data, int dataSize, int periodMs)
	{
		int retval = 0; /* no means of getting error info for now */
		
        ByteBuffer trustedBuffer = ByteBuffer.allocateDirect(dataSize);

		switch (dataSize) {
			default:
			case 8: trustedBuffer.put(7, (byte) (data >> 0x38));	/* fall through */
			case 7: trustedBuffer.put(6, (byte) (data >> 0x30));	/* fall through */
			case 6: trustedBuffer.put(5, (byte) (data >> 0x28));	/* fall through */
			case 5: trustedBuffer.put(4, (byte) (data >> 0x20));	/* fall through */
			case 4: trustedBuffer.put(3, (byte) (data >> 0x18));	/* fall through */
			case 3: trustedBuffer.put(2, (byte) (data >> 0x10));	/* fall through */
			case 2: trustedBuffer.put(1, (byte) (data >> 0x08));	/* fall through */
			case 1: trustedBuffer.put(0, (byte) (data >> 0x00));	break;
			case 0: /* nothing to do */ break;
		}
        byte[] bytes = new byte[trustedBuffer.capacity()];
        trustedBuffer.get(bytes);
        CANJNI.FRCNetCommCANSessionMuxSendMessage(arbId, bytes, periodMs);
        
        return retval;
    }
	/**
	 * Decode two 16bit params.
	 */
	private int GetTwoParam16(int arbId, short [] words)
	{
		int errCode = ReceiveCAN(arbId);
		/* always give caller the latest */
		if (words.length >= 2) {
			words[0] = (short) ((_cache) & 0xFF);
			words[0] <<= 8;
			words[0] |= ((_cache >> 0x08) & 0xFF);

			words[1] = (short) ((_cache >> 0x10) & 0xFF);
			words[1] <<= 8;
			words[1] |= ((_cache >> 0x18) & 0xFF);
		}
		return errCode;
	}
	private int GetThreeParam16(int arbId, short [] words)
	{
		int errCode = ReceiveCAN(arbId);

		if (words.length >= 3) {
			words[0] = (short)(_cache & 0xFF);
			words[0] <<= 8;
			words[0] |= (short)((_cache >> 0x08) & 0xFF);

			words[1] = (short)((_cache >> 0x10) & 0xFF);
			words[1] <<= 8;
			words[1] |= (short)((_cache >> 0x18) & 0xFF);

			words[2] = (short)((_cache >> 0x20) & 0xFF);
			words[2] <<= 8;
			words[2] |= (short)((_cache >> 0x28) & 0xFF);
		}

		return errCode;
	}
	
	private int GetThreeParam16(int arbId, double [] signals, double scalar)
	{
		short word_p1;
		short word_p2;
		short word_p3;
		int errCode = ReceiveCAN(arbId);

		word_p1 = (short) (_cache & 0xFF);
		word_p1 <<= 8;
		word_p1 |= (short) ((_cache >> 0x08) & 0xFF);

		word_p2 = (short) ((_cache >> 0x10) & 0xFF);
		word_p2 <<= 8;
		word_p2 |= (short) ((_cache >> 0x18) & 0xFF);

		word_p3 = (short) ((_cache >> 0x20) & 0xFF);
		word_p3 <<= 8;
		word_p3 |= (short) ((_cache >> 0x28) & 0xFF);

		if (signals.length >= 3) {
			signals[0] = word_p1 * scalar;
			signals[1] = word_p2 * scalar;
			signals[2] = word_p3 * scalar;
		}

		return errCode;
	}

	private int GetThreeBoundedAngles(int arbId, double [] boundedAngles)
	{
		return  GetThreeParam16(arbId, boundedAngles, 360f / 32768f);
	}
	private int GetFourParam16(int arbId, double [] params, double scalar)
	{
		short p0,p1,p2,p3;
		int errCode = ReceiveCAN(arbId);

		p0 = (short) (_cache & 0xFF);
		p0 <<= 8;
		p0 |= (short) ((_cache >> 0x08) & 0xFF);

		p1 = (short) ((_cache >> 0x10) & 0xFF);
		p1 <<= 8;
		p1 |= (short) ((_cache >> 0x18) & 0xFF);

		p2 = (short) ((_cache >> 0x20) & 0xFF);
		p2 <<= 8;
		p2 |= (short) ((_cache >> 0x28) & 0xFF);

		p3 = (short) ((_cache >> 0x30) & 0xFF);
		p3 <<= 8;
		p3 |= (short) ((_cache >> 0x38) & 0xFF);

		/* always give caller the latest */
		if (params.length >= 4) {
			params[0] = p0 * scalar;
			params[1] = p1 * scalar;
			params[2] = p2 * scalar;
			params[3] = p3 * scalar;
		}

		return errCode;
	}

	private int GetThreeParam20(int arbId, double [] params, double scalar)
	{
		int p1, p2, p3;

		int errCode = ReceiveCAN(arbId);

		int p1_h8 = (int)_cache;
		int p1_m8 = (int)(_cache >> 8);
		int p1_l4 = (int)(_cache >> 20);

		int p2_h4 = (int)(_cache >> 16);
		int p2_m8 = (int)(_cache >> 24);
		int p2_l8 = (int)(_cache >> 32);

		int p3_h8 = (int)(_cache >> 40);
		int p3_m8 = (int)(_cache >> 48);
		int p3_l4 = (int)(_cache >> 60);

		p1_l4 &= 0xF;
		p2_h4 &= 0xF;
		p3_l4 &= 0xF;

		p1_h8 &= 0xFF;
		p1_m8 &= 0xFF;
		p2_m8 &= 0xFF;
		p2_l8 &= 0xFF;
		p3_h8 &= 0xFF;
		p3_m8 &= 0xFF;

		p1 = p1_h8;
		p1 <<= 8;
		p1 |= p1_m8;
		p1 <<= 4;
		p1 |= p1_l4;
		p1 <<= (32 - 20);
		p1 >>= (32 - 20);

		p2 = p2_h4;
		p2 <<= 8;
		p2 |= p2_m8;
		p2 <<= 8;
		p2 |= p2_l8;
		p2 <<= (32 - 20);
		p2 >>= (32 - 20);

		p3 = p3_h8;
		p3 <<= 8;
		p3 |= p3_m8;
		p3 <<= 4;
		p3 |= p3_l4;
		p3 <<= (32 - 20);
		p3 >>= (32 - 20);

		if(params.length >= 3) {
			params[0] = p1 * scalar;
			params[1] = p2 * scalar;
			params[2] = p3 * scalar;
		}

        // System.out.println("Getting");
        // System.out.println(String.format("%016X", _cache));
        // System.out.println(p1 + ", " + String.format("%016X", p1));
        // System.out.println(p2 + ", " + String.format("%016X", p2));
        // System.out.println(p3 + ", " + String.format("%016X", p3));
        // System.out.println(params[0]);
        // System.out.println(params[1]);
        // System.out.println(params[2]);
		return errCode;
	}
	//----------------------- Strongly typed Signal decoders  -----------------------//
	public int Get6dQuaternion(double [] wxyz)
	{
		int errCode = GetFourParam16(COND_STATUS_10, wxyz, 1.0 / 16384);
		return HandleError(errCode);
	}
	public int GetYawPitchRoll(double [] ypr)
	{
		int errCode = GetThreeParam20(COND_STATUS_9, ypr, (360. / 8192.));
		return HandleError(errCode);
	}
	public int GetAccumGyro(double xyz_deg[])
	{
		int errCode = GetThreeParam20(COND_STATUS_11, xyz_deg, (360. / 8192.));
		return HandleError(errCode);
	}
	/**
	 *  @return compass heading [0,360) degrees.
	 */
	public double GetAbsoluteCompassHeading()
	{
		int raw;
		double retval;
		int errCode = ReceiveCAN(COND_STATUS_2);
	
		raw = (int) ((_cache >> 0x30) & 0xFF);
		raw <<= 8;
		raw |= ((_cache >> 0x38) & 0xFF);
		
		/* throw out the rotation count */
		raw &= 0x1FFF;
	
		retval = raw * (360. / 8192.);
	
		HandleError(errCode);
		return retval;
	}
	/**
	 *  @return continuous compass heading [-23040, 23040) degrees.
	 *  Use SetCompassHeading to modify the wrap-around portion.
	 */
	public double GetCompassHeading()
	{
		int raw;
		double retval;
		int errCode = ReceiveCAN(COND_STATUS_2);
		int  h4 =  (int) ((_cache >> 0x28) & 0xF);
		int  m8 =  (int) ((_cache >> 0x30) & 0xFF);
		int  l8 =  (int) ((_cache >> 0x38) & 0xFF);
	
		raw = h4;
		raw <<= 8;
		raw |= m8;
		raw <<= 8;
		raw |= l8;

		/* sign extend */
		raw <<= (32 - 20);
		raw >>= (32 - 20);
	
		retval = raw * (360. / 8192.);
	
		HandleError(errCode);
		return retval;
	}
	/**
	 * @return field strength in Microteslas (uT).
	 */
	public double GetCompassFieldStrength()
	{
		double magnitudeMicroTeslas;

		int errCode = GetTwoParam16(COND_STATUS_2, _cache_words);
		magnitudeMicroTeslas = _cache_words[1] * (0.15);
		HandleError(errCode);
		return magnitudeMicroTeslas;
	}

	private double GetTemp(long statusFrame)
	{
		int H = (int) ((statusFrame >> 0) & 0xFF);
		int L = (int) ((statusFrame >> 8) & 0xFF);
		int raw = 0;
		raw |= H;
		raw <<= 8;
		raw |= L;
		double tempC = raw * (1.0f / 256.0f);
		return tempC;
	}
	public double GetTemp()
	{
		int errCode = ReceiveCAN(COND_STATUS_1);
		double tempC = GetTemp(_cache);
		HandleError(errCode);
		return tempC;
	}
	private PigeonState GetState(int errCode, long statusFrame)
	{
		PigeonState retval = PigeonState.NoComm;

		if(errCode != 0){
			/* bad frame */
		} else {
			/* good frame */
			byte b2 = (byte)(statusFrame >> 0x10);
		
			MotionDriverState mds = MotionDriverState.getEnum(b2 & 0x1f);
			switch (mds) {
				case Error:
				case Init0:
				case WaitForPowerOff:
				case ConfigAg:
				case SelfTestAg:
				case StartDMP:
				case ConfigCompass_0:
				case ConfigCompass_1:
				case ConfigCompass_2:
				case ConfigCompass_3:
				case ConfigCompass_4:
				case ConfigCompass_5:
				case SelfTestCompass:
				case WaitForGyroStable:
				case AdditionalAccelAdjust:
					retval = PigeonState.Initializing;
					break;
				case Idle:
					retval = PigeonState.Ready;
					break;
				case Calibration:
				case LedInstrum:
					retval = PigeonState.UserCalibration;
					break;
				default:
					retval = PigeonState.Initializing;
					break;
			}
		}
		return retval;
	}
	public PigeonState GetState()
	{
		int errCode = ReceiveCAN(COND_STATUS_1);
		PigeonState retval = GetState(errCode, _cache);
		HandleError(errCode);
		return retval;
	}
	/// <summary>
	/// How long has Pigeon been running
	/// </summary>
	/// <param name="timeSec"></param>
	/// <returns></returns>
	public int GetUpTime()
	{
		/* repoll status frame */
		int errCode = ReceiveCAN(COND_STATUS_1);
		int timeSec = (int) ((_cache >> 56) & 0xFF);
		HandleError(errCode);
		return timeSec;
	}

	public int GetRawMagnetometer(short [] rm_xyz)
	{
		int errCode = GetThreeParam16(RAW_STATUS_4, rm_xyz);
		return HandleError(errCode);
	}
	public int GetBiasedMagnetometer(short [] bm_xyz)
	{
		int errCode = GetThreeParam16(BIASED_STATUS_4, bm_xyz);
		return HandleError(errCode);
	}
	public int GetBiasedAccelerometer(short [] ba_xyz)
	{
		int errCode = GetThreeParam16(BIASED_STATUS_6, ba_xyz);
		return HandleError(errCode);
	}
	public int GetRawGyro(double [] xyz_dps)
	{
		int errCode = GetThreeParam16(BIASED_STATUS_2, xyz_dps, 1.0f / 16.4f);
		return HandleError(errCode);
	}
	
	public int GetAccelerometerAngles(double [] tiltAngles)
	{
		int errCode = GetThreeBoundedAngles(COND_STATUS_3, tiltAngles);
		return HandleError(errCode);
	}
	/**
	 * @param status 	object reference to fill with fusion status flags.  
	 *					Caller may pass null if flags are not needed.
	 * @return fused heading in degrees.
	 */
	public double GetFusedHeading(FusionStatus status)
	{
		boolean bIsFusing, bIsValid;
		double fusedHeading;

		int errCode = GetThreeParam20(COND_STATUS_6, _cache_prec, 360. / 8192.);
		fusedHeading = _cache_prec[0];
		byte b2 = (byte)(_cache >> 16);
		
		String description;

		if (errCode != 0) {
			bIsFusing = false;
			bIsValid = false;
			description = "Could not receive status frame.  Check wiring and web-config.";
		} else {
			int flags = (b2) & 7;
			if (flags == 7) {
				bIsFusing = true;
			} else {
				bIsFusing = false;
			}
	
			if ((b2 & 0x8) == 0) {
				bIsValid = false;
			} else {
				bIsValid = true;
			}

			if(bIsValid == false) {
				description = "Fused Heading is not valid.";
			}else if(bIsFusing == false){
				description = "Fused Heading is valid.";
			} else {
				description = "Fused Heading is valid and is fusing compass.";
			}
		}
				
		if (status != null) {
			/* fill caller's struct */
			status.heading = fusedHeading;
			status.bIsFusing = bIsFusing;
			status.bIsValid = bIsValid;
			status.description = description;
			status.lastError = errCode;
		}

		HandleError(errCode);
		return fusedHeading;
	}
	//----------------------- Startup/Reset status -----------------------//
	/**
	 * Polls status5 frame, which is only transmitted on motor controller boot.
	 * @return error code.
	 */
	private int GetStartupStatus()
	{
		int errCode = ReceiveCAN(COND_STATUS_5, false);
		if (errCode == 0) {
			int H,L;
			int raw;
			/* frame has been received, therefore motor contorller has reset at least once */
			_resetStats.hasReset = true;
			/* reset count */
			H = (int) ((_cache) & 0xFF);
			L = (int) ((_cache >> 0x08) & 0xFF);
			raw = H<<8 | L;
			_resetStats.resetCount = (int)raw;
			/* reset flags */
			H = (int) ((_cache >> 0x10) & 0xFF);
			L = (int) ((_cache >> 0x18) & 0xFF);
			raw = H << 8 | L;
			_resetStats.resetFlags = (int)raw;
			/* firmVers */
			H = (int) ((_cache >> 0x20) & 0xFF);
			L = (int) ((_cache >> 0x28) & 0xFF);
			raw = H << 8 | L;
			_resetStats.firmVers = (int)raw;
		}
		return errCode;
	}
	public int GetResetCount()
	{
		/* repoll status frame */
		int errCode = GetStartupStatus();
		int retval = _resetStats.resetCount;
		HandleError(errCode);
		return retval;
	}
	public int GetResetFlags()
	{
		/* repoll status frame */
		int errCode = GetStartupStatus();
		int retval = _resetStats.resetFlags;
		HandleError(errCode);
		return retval;
	}
	/**
	 * @return param holds the version of the Talon.  Talon must be powered cycled at least once.
	 */
	public int GetFirmVers()
	{
		/* repoll status frame */
		int errCode = GetStartupStatus();
		int retval  = _resetStats.firmVers;
		HandleError(errCode);
		return retval;
	}
	/**
	 * @return true iff a reset has occured since last call.
	 */
	public boolean HasResetOccured()
	{
		/* repoll status frame, ignore return since hasReset is explicitly tracked */
		GetStartupStatus();
		/* get-then-clear reset flag */
		boolean retval = _resetStats.hasReset;
		_resetStats.hasReset = false;
		return retval;
	}

}