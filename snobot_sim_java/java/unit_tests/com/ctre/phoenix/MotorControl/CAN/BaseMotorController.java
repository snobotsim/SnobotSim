package com.ctre.phoenix.MotorControl.CAN;
import com.ctre.phoenix.MotorControl.CAN.MotControllerJNI;
import com.ctre.phoenix.MotorControl.IMotorController;
import com.ctre.phoenix.MotorControl.ControlMode;
import com.ctre.phoenix.MotorControl.Faults;
import com.ctre.phoenix.MotorControl.ControlFrame;
import com.ctre.phoenix.MotorControl.VelocityMeasPeriod;
import com.ctre.phoenix.MotorControl.WpilibSpeedController;
import com.ctre.phoenix.MotorControl.NeutralMode;
import com.ctre.phoenix.MotorControl.LimitSwitchSource;
import com.ctre.phoenix.MotorControl.RemoteLimitSwitchSource;
import com.ctre.phoenix.MotorControl.LimitSwitchNormal;
import com.ctre.phoenix.MotorControl.FeedbackDevice;
import com.ctre.phoenix.MotorControl.StatusFrame;
import com.ctre.phoenix.MotorControl.StatusFrameEnhanced;
import com.ctre.phoenix.MotorControl.StickyFaults;
import com.ctre.phoenix.MotorControl.RemoteFeedbackDevice;
import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.ErrorCode;
/* WPILIB */
import edu.wpi.first.wpilibj.SpeedController;

public abstract class BaseMotorController implements com.ctre.phoenix.MotorControl.IMotorController {
	
	private ControlMode m_controlMode = ControlMode.PercentOutput;
	private ControlMode m_sendMode = ControlMode.PercentOutput;
	
	private int _arbId = 0;
	private boolean _invert = false;
	
	protected long m_handle;
	
	private WpilibSpeedController _wpilibSpeedController;
	/**
	 * Constructor for motor controllers.
	 */
	public BaseMotorController(int arbId)
	{
		m_handle = MotControllerJNI.Create(arbId);
		_arbId = arbId;
		_wpilibSpeedController = new WpilibSpeedController(this);
	}

	protected long getHandle() {
		return m_handle;
	}

	/**
	 * Returns the Device ID
	 *
	 * @return Device number.
	 */
	public int getDeviceID() {
		return MotControllerJNI.GetDeviceNumber(m_handle);
	}

	//------ Set output routines. ----------//
	/**
	 * Puts motor controller into PercentOutput mode.
	 * @param value Percent output [-1,+1]
	 */
	void set(float value) {
		set(ControlMode.PercentOutput, value, 0);
	}
	/**
	 * Sets the appropriate output on the talon, depending on the mode.
	 *
	 * In PercentOutput, the output is between -1.0 and 1.0, with 0.0 as stopped.
	 * In Voltage mode, output value is in volts.
	 * In Current mode, output value is in amperes.
	 * In Speed mode, output value is in position change / 100ms.
	 * In Position mode, output value is in encoder ticks or an analog value,
	 *   depending on the sensor.
	 * In Follower mode, the output value is the integer device ID of the talon to
	 * duplicate.
	 *
	 * @param outputValue The setpoint value, as described above.
	 * @see #SelectProfileSlot to choose between the two sets of gains.
	 */
	public void set(ControlMode mode, double outputValue)
	{
		set(mode, outputValue, 0);
	}
	
	public void set(ControlMode mode, double demand0, double demand1)
	{
		m_controlMode = mode;
		m_sendMode = mode;
		int work;
		
		switch(m_controlMode)
		{
			case PercentOutput:
			case TimedPercentOutput:
				MotControllerJNI.SetDemand(m_handle, m_sendMode.value,(int)(1023 * demand0), 0);
				break;
			case Follower:
				/* did caller specify device ID */
				if ( (0 <= demand0) && (demand0 <= 62)) { // [0,62]
					work = getBaseID();
					work >>= 16;
					work <<= 8;
					work |= ((int)demand0) & 0xFF;
				} else {
					work = (int)demand0;
				}
				MotControllerJNI.SetDemand(m_handle, m_sendMode.value, work, 0);
				break;
			case Velocity:
			case Position:
			case MotionMagic:
			case MotionMagicArc:
			case MotionProfile:
				MotControllerJNI.SetDemand(m_handle, m_sendMode.value, (int)demand0, 0);
				break;
			case Current:
				MotControllerJNI.SetDemand(m_handle, m_sendMode.value,(int)(1000. * demand0), 0); /* milliamps */
				break;
			case Disabled:
				/* fall thru...*/
			default:
				MotControllerJNI.SetDemand(m_handle, m_sendMode.value, 0, 0);
				break;
		}
		
	}
	
	public void neutralOutput()
	{
		set(ControlMode.Disabled, 0);
	}
	
  /**
   * Sets the mode of operation during neutral throttle output.
   *
   * @param neutralMode The desired mode of operation when the Controller output throttle is neutral (ie brake/coast)
   **/
	public void setNeutralMode(NeutralMode neutralMode)
	{
		MotControllerJNI.SetNeutralMode(m_handle, neutralMode.value);
	}
  /**
   * Sets the phase of the sensor.  Use when controller forward/reverse output doesn't 
correlate to appropriate forward/reverse reading of sensor.
   *
   * @param PhaseSensor Indicates whether to invert the phase of the sensor.
   **/
	public void setSensorPhase(boolean PhaseSensor)
	{
		MotControllerJNI.SetSensorPhase(m_handle, PhaseSensor);
	}
  /**
   * Inverts the output of the motor controller.  LEDs, sensor phase,
and limit switches will also be inverted to match the new 
forward/reverse directions.
   *
   * @param invert Invert state to set.
   **/
	public void setInverted(boolean invert)
	{
		_invert = invert; /* cache for getter */
		MotControllerJNI.SetInverted(m_handle, invert);
	}
	
	public boolean getInverted()
	{
		return _invert;
	}
  /**
   * Configures the open-loop ramp rate of throttle output.
   *
   * @param secondsFromNeutralToFull Minimum desired time to go from neutral to full throttle.  A value of '0' will disable the ramp.
   * @param timeoutMs                Timeout value in ms.  Function will generate error if config is not successful within timeout.
   * @return                         Error Code generated by function.  0 indicates no error.   */
	public ErrorCode configOpenloopRamp(double secondsFromNeutralToFull, int timeoutMs)
	{
		int retval = MotControllerJNI.ConfigOpenLoopRamp(m_handle, (float) secondsFromNeutralToFull, timeoutMs);
		return ErrorCode.valueOf(retval);
	}
  /**
   * Configures the closed-loop ramp rate of throttle output.
   *
   * @param secondsFromNeutralToFull Minimum desired time to go from neutral to full throttle.  A value of '0' will disable the ramp.
   * @param timeoutMs                Timeout value in ms.  @see #ConfigOpenLoopRamp
   * @return                          Error Code generated by function.  0 indicates no error.   */
	public ErrorCode configClosedloopRamp(double secondsFromNeutralToFull, int timeoutMs)
	{
		int retval = MotControllerJNI.ConfigClosedLoopRamp(m_handle, (float) secondsFromNeutralToFull, timeoutMs);
		return ErrorCode.valueOf(retval);
	}
  /**
   * Configures the forward peak output percentage.
   *
   * @param percentOut Desired peak output percentage.
   * @param timeoutMs  Timeout value in ms.  @see #ConfigOpenLoopRamp
   * @return            Error Code generated by function.  0 indicates no error.   */
	public ErrorCode configPeakOutputForward(double percentOut, int timeoutMs)
	{
		int retval = MotControllerJNI.ConfigPeakOutputForward(m_handle, (float) percentOut, timeoutMs);
		return ErrorCode.valueOf(retval);
	}
  /**
   * Configures the reverse peak output percentage.
   *
   * @param percentOut Desired peak output percentage.
   * @param timeoutMs  Timeout value in ms.  @see #ConfigOpenLoopRamp
   * @return            Error Code generated by function.  0 indicates no error.   */
	public ErrorCode configPeakOutputReverse(double percentOut, int timeoutMs)
	{
		int retval = MotControllerJNI.ConfigPeakOutputReverse(m_handle, (float) percentOut, timeoutMs);
		return ErrorCode.valueOf(retval);
	}
  /**
   * Configures the forward nominal output percentage.
   *
   * @param percentOut Nominal (minimum) percent output.
   * @param timeoutMs  Timeout value in ms.  @see #ConfigOpenLoopRamp
   * @return            Error Code generated by function.  0 indicates no error.   */
	public ErrorCode configNominalOutputForward(double percentOut, int timeoutMs)
	{
		int retval = MotControllerJNI.ConfigNominalOutputForward(m_handle, (float) percentOut, timeoutMs);
		return ErrorCode.valueOf(retval);
	}
  /**
   * Configures the reverse nominal output percentage.
   *
   * @param percentOut Nominal (minimum) percent output.
   * @param timeoutMs  Timeout value in ms.  @see #ConfigOpenLoopRamp
   * @return            Error Code generated by function.  0 indicates no error.   */
	public ErrorCode configNominalOutputReverse(double percentOut, int timeoutMs)
	{
		int retval = MotControllerJNI.ConfigNominalOutputReverse(m_handle, (float) percentOut, timeoutMs);
		return ErrorCode.valueOf(retval);
	}
  /**
   * Configures the output deadband percentage.
   *
   * @param percentDeadband Desired deadband percentage.  Minimum is 0.1%, Maximum is 12.5%.
   * @param timeoutMs       Timeout value in ms.  @see #ConfigOpenLoopRamp
   * @return                 Error Code generated by function.  0 indicates no error.   */
	public ErrorCode configNeutralDeadband(double percentDeadband, int timeoutMs)
	{
		int retval = MotControllerJNI.ConfigNeutralDeadband(m_handle, (float) percentDeadband, timeoutMs);
		return ErrorCode.valueOf(retval);
	}
  /**
   * Configures the Voltage Compensation saturation voltage.
   *
   * @param voltage    TO-DO: Comment me!
   * @param timeoutMs Timeout value in ms.  @see #ConfigOpenLoopRamp
   * @return           Error Code generated by function.  0 indicates no error.   */
	public ErrorCode configVoltageCompSaturation(double voltage, int timeoutMs)
	{
		int retval = MotControllerJNI.ConfigVoltageCompSaturation(m_handle, (float) voltage, timeoutMs);
		return ErrorCode.valueOf(retval);
	}
  /**
   * Configures the voltage measurement filter.
   *
   * @param filterWindowSamples Number of samples in the rolling average of voltage measurement.
   * @param timeoutMs           Timeout value in ms.  @see #ConfigOpenLoopRamp
   * @return                     Error Code generated by function.  0 indicates no error.   */
	public ErrorCode configVoltageMeasurementFilter(int filterWindowSamples, int timeoutMs)
	{
		int retval = MotControllerJNI.ConfigVoltageMeasurementFilter(m_handle, filterWindowSamples, timeoutMs);
		return ErrorCode.valueOf(retval);
	}
  /**
   * Enables voltage compensation.  If enabled, voltage compensation works in all control modes.
   *
   * @param enable Enable state of voltage compensation.
   **/
	public void enableVoltageCompensation(boolean enable)
	{
		MotControllerJNI.EnableVoltageCompensation(m_handle, enable);
	}
  /**
   * Gets the bus voltage seen by the motor controller.
   *
   * @return        The bus voltage value (in volts).   */
	public double getBusVoltage()
	{
		return MotControllerJNI.GetBusVoltage(m_handle);
	}
  /**
   * Gets the output percentage of the motor controller.
   *
   * @return              Output of the motor controller (in percent).   */
	public double getMotorOutputPercent()
	{
		return MotControllerJNI.GetMotorOutputPercent(m_handle);
	}
	
	public double getMotorOutputVoltage()
	{
		double v = getBusVoltage();
		double p = getMotorOutputPercent();
		return v * p;
	}
	
  /**
   * Gets the output current of the motor controller.
   *
   * @return        The output current (in amps).   */
	public double getOutputCurrent()
	{
		return MotControllerJNI.GetOutputCurrent(m_handle);
	}
  /**
   * Gets the temperature of the motor controller.
   *
   * @return            Temperature of the motor controller (in °C)   */
	public double getTemperature()
	{
		return MotControllerJNI.GetTemperature(m_handle);
	}

	/**
	   * Select the remote feedback device for the motor controller.
	   *
	   * @param feedbackDevice Remote Feedback Device to select.
	   * @param timeoutMs      Timeout value in ms.  @see #ConfigOpenLoopRamp
	   * @return                Error Code generated by function.  0 indicates no error.   */
	public ErrorCode configSelectedFeedbackSensor(RemoteFeedbackDevice feedbackDevice, int timeoutMs)
	{
		int e1 = MotControllerJNI.ConfigRemoteFeedbackFilter(m_handle, feedbackDevice._arbId, feedbackDevice._peripheralIndex, feedbackDevice._reserved, timeoutMs);
		int e2 = MotControllerJNI.ConfigSelectedFeedbackSensor(m_handle, FeedbackDevice.RemoteSensor.value, timeoutMs);
		
		if(e1 == 0)
			return ErrorCode.valueOf(e2);
		return ErrorCode.valueOf(e1);
	}
	
  /**
   * Select the feedback device for the motor controller.
   *
   * @param feedbackDevice Feedback Device to select.
   * @param timeoutMs      Timeout value in ms.  @see #ConfigOpenLoopRamp
   * @return                Error Code generated by function.  0 indicates no error.   */
	public ErrorCode configSelectedFeedbackSensor(FeedbackDevice feedbackDevice, int timeoutMs)
	{
		int retval = MotControllerJNI.ConfigSelectedFeedbackSensor(m_handle, feedbackDevice.value, timeoutMs);
		return ErrorCode.valueOf(retval);
	}
  /**
   * Get the selected sensor position.
   *
   * @return      Position of selected sensor (in Raw Sensor Units).   */
	public int getSelectedSensorPosition()
	{
		return MotControllerJNI.GetSelectedSensorPosition(m_handle);
	}

	/**
	 * TODO - Document this
	 */
	public int getSelectedSensorVelocity() {
		return MotControllerJNI.GetSelectedSensorVelocity(m_handle);
	}
	
  /**
   * Get the selected sensor velocity.
   *
   * @return      Velocity of selected sensor (in Raw Sensor Units per 100 ms).   */
	public int getSelectedSensorVelocity(int param)
	{
		return MotControllerJNI.GetSelectedSensorVelocity(m_handle);
	}
  /**
   * Sets the sensor position to the given value.
   *
   * @param sensorPos Position to set for the selected sensor (in Raw Sensor Units).
   * @param timeoutMs Timeout value in ms.  @see #ConfigOpenLoopRamp
   * @return           Error Code generated by function.  0 indicates no error.   */
	public ErrorCode setSelectedSensorPosition(int sensorPos, int timeoutMs)
	{
		int retval = MotControllerJNI.SetSelectedSensorPosition(m_handle, sensorPos, timeoutMs);
		return ErrorCode.valueOf(retval);
	}
  /**
   * Sets the period of the given control frame.
   *
   * @param frame    Frame whose period is to be changed.
   * @param periodMs Period in ms for the given frame.
   * @return          Error Code generated by function.  0 indicates no error.   */
	public ErrorCode setControlFramePeriod(ControlFrame frame, int periodMs)
	{
		int retval = MotControllerJNI.SetControlFramePeriod(m_handle, frame.value, periodMs);
		return ErrorCode.valueOf(retval);
	}
	protected ErrorCode setStatusFramePeriod(int frameValue, int periodMs, int timeoutMs)
	{
		int retval = MotControllerJNI.SetStatusFramePeriod(m_handle, frameValue, periodMs, timeoutMs);
		return ErrorCode.valueOf(retval);
	}
	
  /**
   * Sets the period of the given status frame.
   *
   * @param frame     Frame whose period is to be changed.
   * @param periodMs  Period in ms for the given frame.
   * @param timeoutMs Timeout value in ms.  @see #ConfigOpenLoopRamp
   * @return           Error Code generated by function.  0 indicates no error.   */
	public ErrorCode setStatusFramePeriod(StatusFrame frame, int periodMs, int timeoutMs)
	{
		return setStatusFramePeriod(frame.value, periodMs, timeoutMs);
	}
  /**
   * Gets the period of the given status frame.
   *
   * @param frame     Frame to get the period of.
   * @param timeoutMs Timeout value in ms.  @see #ConfigOpenLoopRamp
   * @return          Period of the given status frame.   */
	public int getStatusFramePeriod(StatusFrame frame, int timeoutMs)
	{
		return MotControllerJNI.GetStatusFramePeriod(m_handle, frame.value, timeoutMs);
	}

	/**
	 * Gets the period of the given status frame.
	 *
	 * @param frame
	 *            Frame to get the period of.
	 * @param timeoutMs
	 *            Timeout value in ms. @see #ConfigOpenLoopRamp
	 * @return Period of the given status frame.
	 */
	public int getStatusFramePeriod(StatusFrameEnhanced frame, int timeoutMs) {
		return MotControllerJNI.GetStatusFramePeriod(m_handle, frame.value, timeoutMs);
	}
  /**
   * Sets the period over which velocity measurements are taken.
   *
   * @param period    Desired period for the velocity measurement. @see #VelocityMeasPeriod
   * @param timeoutMs Timeout value in ms.  @see #ConfigOpenLoopRamp
   * @return           Error Code generated by function.  0 indicates no error.   */
	public ErrorCode configVelocityMeasurementPeriod(VelocityMeasPeriod period, int timeoutMs)
	{
		int retval = MotControllerJNI.ConfigVelocityMeasurementPeriod(m_handle, period.value, timeoutMs);
		return ErrorCode.valueOf(retval);
	}
  /**
   * Sets the number of velocity samples used in the rolling average velocity measurement.
   *
   * @param windowSize Number of samples in the rolling average of velocity measurement.
   * @param timeoutMs  Timeout value in ms.  @see #ConfigOpenLoopRamp
   * @return            Error Code generated by function.  0 indicates no error.   */
	public ErrorCode configVelocityMeasurementWindow(int windowSize, int timeoutMs)
	{
		int retval = MotControllerJNI.ConfigVelocityMeasurementWindow(m_handle, windowSize, timeoutMs);
		return ErrorCode.valueOf(retval);
	}

    //------ remote limit switch ----------//
	protected ErrorCode configForwardLimitSwitchSource(int typeValue, int normalOpenOrCloseValue, int deviceID, int timeoutMs)
	{
		int retval = MotControllerJNI.ConfigForwardLimitSwitchSource(m_handle, typeValue, normalOpenOrCloseValue, deviceID, timeoutMs);
		return ErrorCode.valueOf(retval);
	}
	protected ErrorCode configReverseLimitSwitchSource(int typeValue, int normalOpenOrCloseValue, int deviceID, int timeoutMs)
	{
		int retval = MotControllerJNI.ConfigReverseLimitSwitchSource(m_handle, typeValue, normalOpenOrCloseValue, deviceID, timeoutMs);
		return ErrorCode.valueOf(retval);
	}
	
	/**
	 * Configures the forward limit switch for a remote source.
	 *
	 * @param type
	 *            Remote limit switch source. @see #LimitSwitchSource
	 * @param normalOpenOrClose
	 *            Setting for normally open or normally closed.
	 * @param deviceID
	 *            Device ID of remote source.
	 * @param timeoutMs
	 *            Timeout value in ms. @see #ConfigOpenLoopRamp
	 * @return Error Code generated by function. 0 indicates no error.
	 */
	public ErrorCode configForwardLimitSwitchSource(RemoteLimitSwitchSource type, LimitSwitchNormal normalOpenOrClose, int deviceID, int timeoutMs) {
		return configForwardLimitSwitchSource(type.value, normalOpenOrClose.value, deviceID, timeoutMs);
	}

  /**
   * Configures the reverse limit switch for a remote source.
   *
   * @param type              Remote limit switch source. @see #LimitSwitchSource
   * @param normalOpenOrClose Setting for normally open or normally closed.
   * @param deviceID          Device ID of remote source.
   * @param timeoutMs         Timeout value in ms.  @see #ConfigOpenLoopRamp
   * @return                   Error Code generated by function.  0 indicates no error.   */
	public ErrorCode configReverseLimitSwitchSource(RemoteLimitSwitchSource type, LimitSwitchNormal normalOpenOrClose, int deviceID, int timeoutMs)
	{
		int retval = MotControllerJNI.ConfigReverseLimitSwitchSource(m_handle, type.value, normalOpenOrClose.value, deviceID, timeoutMs);
		return ErrorCode.valueOf(retval);
	}
	
	public ErrorCode configForwardLimitSwitchSource(LimitSwitchSource type, LimitSwitchNormal normalOpenOrClose, int timeoutMs) {
		return configForwardLimitSwitchSource(type.value, normalOpenOrClose.value, 0x00000000, timeoutMs);
	}
	
  /**
   * Sets the enable state for limit switches.
   *
   * @param enable Enable state for limit switches.
   **/
	public void enableLimitSwitches(boolean enable)
	{
		MotControllerJNI.EnableLimitSwitches(m_handle, enable);
	}
  /**
   * Configures the forward soft limit.
   *
   * @param forwardSensorLimit Forward Sensor Position Limit (in Raw Sensor Units).
   * @param timeoutMs          Timeout value in ms.  @see #ConfigOpenLoopRamp
   * @return                    Error Code generated by function.  0 indicates no error.   */
	public ErrorCode configForwardSoftLimit(int forwardSensorLimit, int timeoutMs)
	{
		int retval = MotControllerJNI.ConfigForwardSoftLimit(m_handle, forwardSensorLimit, timeoutMs);
		return ErrorCode.valueOf(retval);
	}
  /**
   * Configures the reverse soft limit.
   *
   * @param reverseSensorLimit Reverse Sensor Position Limit (in Raw Sensor Units).
   * @param timeoutMs          Timeout value in ms.  @see #ConfigOpenLoopRamp
   * @return                    Error Code generated by function.  0 indicates no error.   */
	public ErrorCode configReverseSoftLimit(int reverseSensorLimit, int timeoutMs)
	{
		int retval = MotControllerJNI.ConfigReverseSoftLimit(m_handle, reverseSensorLimit, timeoutMs);
		return ErrorCode.valueOf(retval);
	}
  /**
   * Sets the enable state for soft limit switches.
   *
   * @param enable Enable state for soft limit switches.
   **/
	public void enableSoftLimits(boolean enable)
	{
		MotControllerJNI.EnableSoftLimits(m_handle, enable);
	}
  /**
   * Sets the 'P' constant in the given parameter slot.
   *
   * @param slotIdx   Parameter slot for the constant.
   * @param value     Value of the P constant.
   * @param timeoutMs Timeout value in ms.  @see #ConfigOpenLoopRamp
   * @return           Error Code generated by function.  0 indicates no error.   */
	public ErrorCode config_kP(int slotIdx, double value, int timeoutMs)
	{
		int retval = MotControllerJNI.Config_kP(m_handle, slotIdx, (float) value, timeoutMs);
		return ErrorCode.valueOf(retval);
	}
  /**
   * Sets the 'I' constant in the given parameter slot.
   *
   * @param slotIdx   Parameter slot for the constant.
   * @param value     Value of the I constant.
   * @param timeoutMs Timeout value in ms.  @see #ConfigOpenLoopRamp
   * @return           Error Code generated by function.  0 indicates no error.   */
	public ErrorCode config_kI(int slotIdx, double value, int timeoutMs)
	{
		int retval = MotControllerJNI.Config_kI(m_handle, slotIdx, (float) value, timeoutMs);
		return ErrorCode.valueOf(retval);
	}
  /**
   * Sets the 'D' constant in the given parameter slot.
   *
   * @param slotIdx   Parameter slot for the constant.
   * @param value     Value of the D constant.
   * @param timeoutMs Timeout value in ms.  @see #ConfigOpenLoopRamp
   * @return           Error Code generated by function.  0 indicates no error.   */
	public ErrorCode config_kD(int slotIdx, double value, int timeoutMs)
	{
		int retval = MotControllerJNI.Config_kD(m_handle, slotIdx, (float) value, timeoutMs);
		return ErrorCode.valueOf(retval);
	}
  /**
   * Sets the 'F' constant in the given parameter slot.
   *
   * @param slotIdx   Parameter slot for the constant.
   * @param value     Value of the F constant.
   * @param timeoutMs Timeout value in ms.  @see #ConfigOpenLoopRamp
   * @return           Error Code generated by function.  0 indicates no error.   */
	public ErrorCode config_kF(int slotIdx, double value, int timeoutMs)
	{
		int retval = MotControllerJNI.Config_kF(m_handle, slotIdx, (float) value, timeoutMs);
		return ErrorCode.valueOf(retval);
	}
  /**
   * Sets the Integral Zone constant in the given parameter slot.
   *
   * @param slotIdx   Parameter slot for the constant.
   * @param izone     Value of the Integral Zone constant.
   * @param timeoutMs Timeout value in ms.  @see #ConfigOpenLoopRamp
   * @return           Error Code generated by function.  0 indicates no error.   */
	public ErrorCode config_IntegralZone(int slotIdx, int izone, int timeoutMs)
	{
		int retval = MotControllerJNI.Config_IntegralZone(m_handle, slotIdx, (float) izone, timeoutMs);
		return ErrorCode.valueOf(retval);
	}
  /**
   * Sets the allowable closed-loop error in the given parameter slot.
   *
   * @param slotIdx                  Parameter slot for the constant.
   * @param allowableClosedLoopError Value of the allowable closed-loop error.
   * @param timeoutMs                Timeout value in ms.  @see #ConfigOpenLoopRamp
   * @return                          Error Code generated by function.  0 indicates no error.   */
	public ErrorCode configAllowableClosedloopError(int slotIdx, int allowableClosedLoopError, int timeoutMs)
	{
		int retval = MotControllerJNI.ConfigAllowableClosedloopError(m_handle, slotIdx, allowableClosedLoopError, timeoutMs);
		return ErrorCode.valueOf(retval);
	}
  /**
   * Sets the maximum integral accumulator in the given parameter slot.
   *
   * @param slotIdx   Parameter slot for the constant.
   * @param iaccum    Value of the maximum integral accumulator.
   * @param timeoutMs Timeout value in ms.  @see #ConfigOpenLoopRamp
   * @return           Error Code generated by function.  0 indicates no error.   */
	public ErrorCode configMaxIntegralAccumulator(int slotIdx, double iaccum, int timeoutMs)
	{
		int retval =  MotControllerJNI.ConfigMaxIntegralAccumulator(m_handle, slotIdx, (float) iaccum, timeoutMs);
		return ErrorCode.valueOf(retval);
	}
  /**
   * Sets the integral accumulator.
   *
   * @param iaccum    Value to set for the integral accumulator.
   * @param timeoutMs Timeout value in ms.  @see #ConfigOpenLoopRamp
   * @return           Error Code generated by function.  0 indicates no error.   */
	public ErrorCode setIntegralAccumulator(double iaccum, int timeoutMs)
	{
		int retval =  MotControllerJNI.SetIntegralAccumulator(m_handle, (float) iaccum, timeoutMs);
		return ErrorCode.valueOf(retval);
	}
  /**
   * Gets the closed-loop error.
   *
   * @param slotIdx         Parameter slot of the constant.
   * @return                Closed-loop error value.   */
	public int getClosedLoopError()
	{
		return MotControllerJNI.GetClosedLoopError(m_handle, 0); /* todo: remove slotIdx */
	}
  /**
   * Gets the iaccum value.
   *
   * @return        Integral accumulator value.   */
	public double getIntegralAccumulator() {
		return MotControllerJNI.GetIntegralAccumulator(m_handle, 0); /* todo: remove slotIdx */
	}

	public double getErrorDerivative() {
		return MotControllerJNI.GetErrorDerivative(m_handle, 0); /* todo: remove slotIdx */
	}

  /**
   * Gets the derivative of the closed-loop error.
   *
   * @param slotIdx Parameter slot of the constant.
   * @return        The error derivative value.   */
	public double getErrorDerivative(int slotIdx)
	{
		return MotControllerJNI.GetErrorDerivative(m_handle, slotIdx);
	}
  /**
   * Selects which profile slot to use for closed-loop control.
   *
   * @param slotIdx Profile slot to select.
   **/
	public void selectProfileSlot(int slotIdx)
	{
		MotControllerJNI.SelectProfileSlot(m_handle, slotIdx);
	}
  /**
   * Sets the Motion Magic Cruise Velocity.
   *
   * @param sensorUnitsPer100ms Motion Magic Cruise Velocity (in Raw Sensor Units per 100 ms).
   * @param timeoutMs           Timeout value in ms.  @see #ConfigOpenLoopRamp
   * @return                     Error Code generated by function.  0 indicates no error.   */
	public ErrorCode configMotionCruiseVelocity(int sensorUnitsPer100ms, int timeoutMs)
	{
		int retval =  MotControllerJNI.ConfigMotionCruiseVelocity(m_handle, sensorUnitsPer100ms, timeoutMs);
		return ErrorCode.valueOf(retval);
	}
  /**
   * Sets the Motion Magic Acceleration.
   *
   * @param sensorUnitsPer100msPerSec Motion Magic Acceleration (in Raw Sensor Units per 100 ms per second).
   * @param timeoutMs                 Timeout value in ms.  @see #ConfigOpenLoopRamp
   * @return                           Error Code generated by function.  0 indicates no error.   */
	public ErrorCode configMotionAcceleration(int sensorUnitsPer100msPerSec, int timeoutMs)
	{
		int retval =  MotControllerJNI.ConfigMotionAcceleration(m_handle, sensorUnitsPer100msPerSec, timeoutMs);
		return ErrorCode.valueOf(retval);
	}
	// ------ error ----------//
  /**
   * Gets the last error generated by this object.
   *
   * @return  Last Error Code generated by a function.   */
	public ErrorCode getLastError()
	{
		int retval = MotControllerJNI.GetLastError(m_handle);
		return ErrorCode.valueOf(retval);
	}
	
	// ------ Faults ----------//
	public ErrorCode getFaults(Faults toFill) 
	{
		return ErrorCode.NotImplemented; // TODO
	}

	public ErrorCode getStickyFaults(StickyFaults toFill) 
	{
		return ErrorCode.NotImplemented;// TODO
	}

	public ErrorCode clearStickyFaults(int timeoutMs)
	{
		return ErrorCode.NotImplemented;// TODO
	}
	
  /**
   * Gets the firmware version of the device.
   *
   * @return  Firmware version of device.   */
	public int getFirmwareVersion()
	{
		return MotControllerJNI.GetFirmwareVersion(m_handle);
	}
  /**
   * Returns true if the device has reset.
   *
   * @return  Has a Device Reset Occurred?   */
	public boolean hasResetOccurred()
	{
		return MotControllerJNI.HasResetOccurred(m_handle);
	}
  /**
   * Sets the value of a custom parameter.
   *
   * @param newValue   Value for custom parameter.
   * @param paramIndex Index of custom parameter.
   * @param timeoutMs  Timeout value in ms.  @see #ConfigOpenLoopRamp
   * @return            Error Code generated by function.  0 indicates no error.   */
	public ErrorCode configSetCustomParam(int newValue, int paramIndex, int timeoutMs)
	{
		int retval =  MotControllerJNI.ConfigSetCustomParam(m_handle, newValue, paramIndex, timeoutMs);
		return ErrorCode.valueOf(retval);
	}
  /**
   * Gets the value of a custom parameter.
   *
   * @param paramIndex Index of custom parameter.
   * @param timoutMs   Timeout value in ms.  @see #ConfigOpenLoopRamp
   * @return           Value of the custom param.  */
	public ErrorCode configGetCustomParam(int paramIndex, int timoutMs)
	{
		int retval = MotControllerJNI.ConfigGetCustomParam(m_handle, paramIndex, timoutMs);
		return ErrorCode.valueOf(retval);
	}
  /**
   * Sets a parameter.
   *
   * @param param     Parameter enumeration.
   * @param value     Value of parameter.
   * @param subValue  Subvalue for parameter.  Maximum value of 255.
   * @param ordinal   Ordinal of parameter.
   * @param timeoutMs Timeout value in ms.  @see #ConfigOpenLoopRamp
   * @return           Error Code generated by function.  0 indicates no error.   */
	public ErrorCode configSetParameter(ParamEnum param, double value, int subValue, int ordinal, int timeoutMs)
	{
		int retval =  MotControllerJNI.ConfigSetParameter(m_handle, param.value, (float) value, subValue, ordinal, timeoutMs);
		return ErrorCode.valueOf(retval);
	}
  /**
   * Gets a parameter.
   *
   * @param param     Parameter enumeration.
   * @param ordinal   Ordinal of parameter.
   * @param timeoutMs Timeout value in ms.  @see #ConfigOpenLoopRamp
   * @return          Value of parameter.  */
	public double configGetParameter(ParamEnum param, int ordinal, int timeoutMs)
	{
		return MotControllerJNI.ConfigGetParameter(m_handle, param.value, ordinal, timeoutMs);
	}
	
	
	public int getBaseID()
	{
		return _arbId;
	}
	
	
	public void follow(IMotorController masterToFollow)
	{
		int id32 = masterToFollow.getBaseID();
		int id24 = id32;
		id24 >>= 16;
		id24 = (short)id24;
		id24 <<= 8;
		id24 |= (id32 & 0xFF);
		set(ControlMode.Follower, id24);
	}
	public void valueUpdated()
	{
		//MT
	}
	// ----- WPILIB ------//
	public SpeedController getWPILIB_SpeedController()
	{
		return _wpilibSpeedController;
	}
}