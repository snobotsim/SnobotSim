/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2016. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ctre.phoenix.MotorControl;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.MotorSafety;
import edu.wpi.first.wpilibj.MotorSafetyHelper;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.hal.FRCNetComm.tResourceType;
import edu.wpi.first.wpilibj.hal.HAL;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;

import com.ctre.phoenix.*;
import com.ctre.phoenix.MotorControl.CAN.*;

public class SmartMotorController implements IMotorController, MotorSafety, PIDOutput, PIDSource, GadgeteerUartClient {
  private MotorSafetyHelper m_safetyHelper;
  private boolean m_isInverted = false;
  protected PIDSourceType m_pidSource = PIDSourceType.kDisplacement;
  /**
   * Number of adc engineering units per 0 to 3.3V sweep. This is necessary for scaling Analog
   * Position in rotations/RPM.
   */ 
  private static final int kNativeAdcUnitsPerRotation = 1024;
  /**
   * Number of pulse width engineering units per full rotation. This is necessary for scaling Pulse
   * Width Decoded Position in rotations/RPM.
   */
  private static final double kNativePwdUnitsPerRotation = 4096.0;
  /**
   * Number of minutes per 100ms unit.  Useful for scaling velocities measured by Talon's 100ms
   * timebase to rotations per minute.
   */
  private static final double kMinutesPer100msUnit = 1.0 / 600.0;

  public enum TalonControlMode {
    PercentVbus(0), Position(1), Speed(2), Current(3), Voltage(4), Follower(5), MotionProfile(6), MotionMagic(7),
    Disabled(15);

    @SuppressWarnings("MemberName")
    public final int value;

    @SuppressWarnings("JavadocMethod")
    public static TalonControlMode valueOf(int value) {
      for (TalonControlMode mode : values()) {
        if (mode.value == value) {
          return mode;
        }
      }

      return null;
    }

    TalonControlMode(int value) {
      this.value = value;
    }

//    @Override
    public boolean isPID() {
      return this == Current || this == Speed || this == Position;
    }

//    @Override
    public int getValue() {
      return value;
    }
  }

  private enum UsageFlags {
	Default(0x00000000), PercentVbus(0x00000001), Position(0x00000002), 
	Speed(0x00000004), Current(0x00000008), Voltage(0x00000010), 
	Follower(0x00000020), MotionProfile(0x00000040), MotionMagic(0x00000080), 
	
	VRampRate(0x00400000), CurrentLimit(0x00800000), 
	ZeroSensorI(0x01000000), ZeroSensorF(0x02000000), ZeroSensorR(0x04000000), 
	ForwardLimitSwitch(0x08000000), ReverseLimitSwitch(0x10000000), 
	ForwardSoftLimit(0x20000000), ReverseSoftLimit(0x40000000), MultiProfile(0x80000000);
	  
	  @SuppressWarnings("MemberName")
    public int value;

    @SuppressWarnings("JavadocMethod")
    public static UsageFlags valueOf(int value) {
      for (UsageFlags mode : values()) {
        if (mode.value == value) {
          return mode;
        }
      }
      return null;
    }

    UsageFlags(int value) {
      this.value = value;
    }
  }
  
  public enum FeedbackDevice {
    QuadEncoder(0), AnalogPot(2), AnalogEncoder(3), EncRising(4), EncFalling(5),
    CtreMagEncoder_Relative(6), CtreMagEncoder_Absolute(7), PulseWidth(8);

    @SuppressWarnings("MemberName")
    public int value;

    @SuppressWarnings("JavadocMethod")
    public static FeedbackDevice valueOf(int value) {
      for (FeedbackDevice mode : values()) {
        if (mode.value == value) {
          return mode;
        }
      }

      return null;
    }

    FeedbackDevice(int value) {
      this.value = value;
    }
  }

  /**
   * Depending on the sensor type, Talon can determine if sensor is plugged in ot not.
   */
  public enum FeedbackDeviceStatus {
    FeedbackStatusUnknown(0), FeedbackStatusPresent(1), FeedbackStatusNotPresent(2);

    @SuppressWarnings("MemberName")
    public int value;

    @SuppressWarnings("JavadocMethod")
    public static FeedbackDeviceStatus valueOf(int value) {
      for (FeedbackDeviceStatus mode : values()) {
        if (mode.value == value) {
          return mode;
        }
      }
      return null;
    }

    FeedbackDeviceStatus(int value) {
      this.value = value;
    }
  }

  /**
   * Enumerated types for frame rate ms.
   */
  public enum StatusFrameRate {
    General(0), Feedback(1), QuadEncoder(2), AnalogTempVbat(3), PulseWidth(4);

    @SuppressWarnings("MemberName")
    public int value;

    @SuppressWarnings("JavadocMethod")
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

  /**
   * Enumerated types for Motion Control Set Values. When in Motion Profile control mode, these
   * constants are paseed into set() to manipulate the motion profile executer. When changing modes,
   * be sure to read the value back using getMotionProfileStatus() to ensure changes in output take
   * effect before performing buffering actions. Disable will signal Talon to put motor output into
   * neutral drive. Talon will stop processing motion profile points.  This means the buffer is
   * effectively disconnected from the executer, allowing the robot to gracefully clear and push new
   * traj points.  isUnderrun will get cleared. The active trajectory is also cleared. Enable will
   * signal Talon to pop a trajectory point from it's buffer and process it. If the active
   * trajectory is empty, Talon will shift in the next point. If the active traj is empty, and so is
   * the buffer, the motor drive is neutral and isUnderrun is set.  When active traj times out, and
   * buffer has at least one point, Talon shifts in next one, and isUnderrun is cleared.  When
   * active traj times out, and buffer is empty, Talon keeps processing active traj and sets
   * IsUnderrun. Hold will signal Talon keep processing the active trajectory indefinitely. If the
   * active traj is cleared, Talon will neutral motor drive. Otherwise Talon will keep processing
   * the active traj but it will not shift in points from the buffer.  This means the buffer is
   * effectively disconnected from the executer, allowing the robot to gracefully clear and push new
   * traj points. isUnderrun is set if active traj is empty, otherwise it is cleared. isLast signal
   * is also cleared.
   *
   * <p>Typical workflow: set(Disable), Confirm Disable takes effect, clear buffer and push buffer
   * points, set(Enable) when enough points have been pushed to ensure no underruns, wait for MP to
   * finish or decide abort, If MP finished gracefully set(Hold) to hold position servo and
   * disconnect buffer, If MP is being aborted set(Disable) to neutral the motor and disconnect
   * buffer, Confirm mode takes effect, clear buffer and push buffer points, and rinse-repeat.
   */
  public enum SetValueMotionProfile {
    Disable(0), Enable(1), Hold(2);
    @SuppressWarnings("MemberName")
    public int value;

    @SuppressWarnings("JavadocMethod")
    public static SetValueMotionProfile valueOf(int value) {
      for (SetValueMotionProfile mode : values()) {
        if (mode.value == value) {
          return mode;
        }
      }
      return null;
    }

    SetValueMotionProfile(int value) {
      this.value = value;
    }
  }

  /**
   * Motion Profile Trajectory Point This is simply a data transer object.
   */
  @SuppressWarnings("MemberName")
  public static class TrajectoryPoint {
    public double position; //!< The position to servo to.
    public double velocity; //!< The velocity to feed-forward.
    /**
     * Time in milliseconds to process this point. Value should be between 1ms and 255ms.  If value
     * is zero then Talon will default to 1ms.  If value exceeds 255ms API will cap it.
     */
    public int timeDurMs;
    /**
     * Which slot to get PIDF gains. PID is used for position servo. F is used as the Kv constant
     * for velocity feed-forward. Typically this is hardcoded to the a particular slot, but you are
     * free gain schedule if need be.
     */
    public int profileSlotSelect;
    /**
     * Set to true to only perform the velocity feed-forward and not perform position servo. This is
     * useful when learning how the position servo changes the motor response.  The same could be
     * accomplish by clearing the PID gains, however this is synchronous the streaming, and doesn't
     * require restoing gains when finished.
     *
     * <p>Additionaly setting this basically gives you direct control of the motor output since
     * motor output = targetVelocity X Kv, where Kv is our Fgain. This means you can also scheduling
     * straight-throttle curves without relying on a sensor.
     */
    public boolean velocityOnly;
    /**
     * Set to true to signal Talon that this is the final point, so do not attempt to pop another
     * trajectory point from out of the Talon buffer. Instead continue processing this way point.
     * Typically the velocity member variable should be zero so that the motor doesn't spin
     * indefinitely.
     */
    public boolean isLastPoint;
    /**
     * Set to true to signal Talon to zero the selected sensor. When generating MPs, one simple
     * method is to make the first target position zero, and the final target position the target
     * distance from the current position. Then when you fire the MP, the current position gets set
     * to zero. If this is the intent, you can set zeroPos on the first trajectory point.
     *
     * <p>Otherwise you can leave this false for all points, and offset the positions of all
     * trajectory points so they are correct.
     */
    public boolean zeroPos;
  }

  /**
   * Motion Profile Status This is simply a data transer object.
   */
  @SuppressWarnings("MemberName")
  public static class MotionProfileStatus {
    /**
     * The available empty slots in the trajectory buffer.
     *
     * <p>The robot API holds a "top buffer" of trajectory points, so your applicaion can dump
     * several points at once.  The API will then stream them into the Talon's low-level buffer,
     * allowing the Talon to act on them.
     */
    public int topBufferRem;
    /**
     * The number of points in the top trajectory buffer.
     */
    public int topBufferCnt;
    /**
     * The number of points in the low level Talon buffer.
     */
    public int btmBufferCnt;
    /**
     * Set if isUnderrun ever gets set. Only is cleared by clearMotionProfileHasUnderrun() to ensure
     * robot logic can react or instrument it.
     *
     * @see #clearMotionProfileHasUnderrun()
     */
    public boolean hasUnderrun;
    /**
     * This is set if Talon needs to shift a point from its buffer into the active trajectory point
     * however the buffer is empty. This gets cleared automatically when is resolved.
     */
    public boolean isUnderrun;
    /**
     * True if the active trajectory point has not empty, false otherwise. The members in
     * activePoint are only valid if this signal is set.
     */
    public boolean activePointValid;
    /**
     * The number of points in the low level Talon buffer.
     */
    public TrajectoryPoint activePoint = new TrajectoryPoint();
    /**
     * The current output mode of the motion profile executer (disabled, enabled, or hold). When
     * changing the set() value in MP mode, it's important to check this signal to confirm the
     * change takes effect before interacting with the top buffer.
     */
    public SetValueMotionProfile outputEnable;
  }

  private long m_handle;
  private TalonControlMode m_controlMode;
  private static double kDelayForSolicitedSignals = 0.004;

  private int m_deviceNumber;
  private boolean m_controlEnabled;
  private boolean m_stopped = false;
  private int m_profile;
  private int m_usageHist = 0;

  double m_setPoint;
  /**
   * Encoder CPR, counts per rotations, also called codes per revoluion. Default value of zero means
   * the API behaves as it did during the 2015 season, each position unit is a single pulse and
   * there are four pulses per count (4X). Caller can use configEncoderCodesPerRev to set the
   * quadrature encoder CPR.
   */
  int m_codesPerRev;
  /**
   * Number of turns per rotation.  For example, a 10-turn pot spins ten full rotations from a wiper
   * voltage of zero to 3.3 volts.  Therefore knowing the number of turns a full voltage sweep
   * represents is necessary for calculating rotations and velocity. A default value of zero means
   * the API behaves as it did during the 2015 season, there are 1024 position units from zero to
   * 3.3V.
   */
  int m_numPotTurns;
  /**
   * Although the Talon handles feedback selection, caching the feedback selection is helpful at the
   * API level for scaling into rotations and RPM.
   */
  FeedbackDevice m_feedbackDevice;

  /**
   * Newer APIs don't rely on the native implementions.
   */
  private long _cache;
	
  /**
   * Constructor for the CAN::TalonSRX device.
   *
   * @param deviceNumber The CAN ID of the Talon SRX
   */
  public SmartMotorController(int deviceNumber) {
    m_deviceNumber = deviceNumber;
    m_handle = TalonSRXJNI.new_TalonSRX(deviceNumber);
    m_safetyHelper = new MotorSafetyHelper(this);
    m_controlEnabled = true;
    m_profile = 0;
    m_setPoint = 0;
    m_codesPerRev = 0;
    m_numPotTurns = 0;
    m_feedbackDevice = FeedbackDevice.QuadEncoder;
    setProfile(m_profile);
    applyControlMode(TalonControlMode.PercentVbus);
//    LiveWindow.addActuator("CAN::TalonSRX", m_deviceNumber, this);
  }

  /**
   * Constructor for the CAN::TalonSRX device.
   *
   * @param deviceNumber    The CAN ID of the Talon SRX
   * @param controlPeriodMs The period in ms to send the CAN control frame. Period is bounded to
   *                        [1ms,95ms].
   */
  public SmartMotorController(int deviceNumber, int controlPeriodMs) {
    m_deviceNumber = deviceNumber;
    /* bound period to be within [1 ms,95 ms] */
    m_handle = TalonSRXJNI.new_TalonSRX(deviceNumber, controlPeriodMs);
    m_safetyHelper = new MotorSafetyHelper(this);
    m_controlEnabled = true;
    m_profile = 0;
    m_setPoint = 0;
    m_codesPerRev = 0;
    m_numPotTurns = 0;
    m_feedbackDevice = FeedbackDevice.QuadEncoder;
    setProfile(m_profile);
    applyControlMode(TalonControlMode.PercentVbus);
//    LiveWindow.addActuator("CAN::TalonSRX", m_deviceNumber, this);
  }

  /**
   * Constructor for the CAN::TalonSRX device.
   *
   * @param deviceNumber    The CAN ID of the Talon SRX
   * @param controlPeriodMs The period in ms to send the CAN control frame. Period is bounded to
   *                        [1ms,95ms].
   * @param enablePeriodMs  The period in ms to send the enable control frame. Period is bounded to
   *                        [1ms,95ms]. This typically is not required to specify, however this
   *                        could be used to minimize the time between robot-enable and
   *                        talon-motor-drive.
   */
  public SmartMotorController(int deviceNumber, int controlPeriodMs, int enablePeriodMs) {
    m_deviceNumber = deviceNumber;
    m_handle = TalonSRXJNI.new_TalonSRX(deviceNumber, controlPeriodMs, enablePeriodMs);
    m_safetyHelper = new MotorSafetyHelper(this);
    m_controlEnabled = true;
    m_profile = 0;
    m_setPoint = 0;
    m_codesPerRev = 0;
    m_numPotTurns = 0;
    m_feedbackDevice = FeedbackDevice.QuadEncoder;
    setProfile(m_profile);
    applyControlMode(TalonControlMode.PercentVbus);
//    LiveWindow.addActuator("CAN::TalonSRX", m_deviceNumber, this);
  }

  @Override
  public void pidWrite(double output) {
    if (getControlMode() == TalonControlMode.PercentVbus) {
      set(output);
    } else {
      throw new IllegalStateException("PID only supported in PercentVbus mode");
    }
  }

  @Override
  public void setPIDSourceType(PIDSourceType pidSource) {
    m_pidSource = pidSource;
  }

  @Override
  public PIDSourceType getPIDSourceType() {
    return m_pidSource;
  }

  @Override
  public double pidGet() {
    return getPosition();
  }

  @SuppressWarnings("JavadocMethod")
  public void delete() {
    disable();
    if (m_handle != 0) {
      TalonSRXJNI.delete_TalonSRX(m_handle);
      m_handle = 0;
    }
  }

  /**
   * Sets the appropriate output on the talon, depending on the mode.
   *
   * <p>In PercentVbus, the output is between -1.0 and 1.0, with 0.0 as stopped. In Follower mode,
   * the output is the integer device ID of the talon to duplicate. In Voltage mode, outputValue is
   * in volts. In Current mode, outputValue is in amperes. In Speed mode, outputValue is in position
   * change / 10ms. In Position mode, outputValue is in encoder ticks or an analog value, depending
   * on the sensor.
   *
   * @param outputValue The setpoint value, as described above.
   */
  public void set(double outputValue) {
    /* feed safety helper since caller just updated our output */
    m_safetyHelper.feed();
    if (m_stopped) {
      enableControl();
      m_stopped = false;
    }
    if (m_controlEnabled) {
      m_setPoint = outputValue; /* cache set point for getSetpoint() */
      switch (m_controlMode) {
        case PercentVbus:
          TalonSRXJNI.Set(m_handle, m_isInverted ? -outputValue : outputValue);
          break;
        case Follower:
          TalonSRXJNI.SetDemand(m_handle, (int) outputValue);
          break;
        case Voltage:
          // Voltage is an 8.8 fixed point number.
          int volts = (int) ((m_isInverted ? -outputValue : outputValue) * 256);
          TalonSRXJNI.SetDemand(m_handle, volts);
          break;
        case Speed:
          TalonSRXJNI.SetDemand(m_handle, (int)(m_isInverted ? -outputValue : outputValue));
          break;
        case Position:
          TalonSRXJNI.SetDemand(m_handle, (int)outputValue);
          break;
        case Current:
          double milliamperes = (m_isInverted ? -outputValue : outputValue) * 1000.0; /* mA*/
          TalonSRXJNI.SetDemand(m_handle, (int) milliamperes);
          break;
        case MotionProfile:
          TalonSRXJNI.SetDemand(m_handle, (int) outputValue);
          break;
        case MotionMagic:
          TalonSRXJNI.SetDemand(m_handle, (int)outputValue);
          break;
        default:
          break;
      }
      TalonSRXJNI.SetModeSelect(m_handle, m_controlMode.value);
      com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Set");
    }
  }

  /**
   * Inverts the direction of the motor's rotation. Only works in PercentVbus mode.
   *
   * @param isInverted The state of inversion, true is inverted.
   */
  @Override
  public void setInverted(boolean isInverted) {
    m_isInverted = isInverted;
  }

  /**
   * Common interface for the inverting direction of a speed controller.
   *
   * @return isInverted The state of inversion, true is inverted.
   */
  @Override
  public boolean getInverted() {
    return m_isInverted;
  }

  /**
   * Resets the accumulated integral error and disables the controller.
   *
   * <p>The only difference between this and PIDController is that the PIDController
   * also resets the previous error for the D term, but the difference should have minimal effect as
   * it will only last one cycle.
   */
  public void reset() {
    disable();
    clearIAccum();
  }

  /**
   * Return true if Talon is enabled.
   *
   * @return true if the Talon is enabled and may be applying power to the motor
   */
  public boolean isEnabled() {
    return isControlEnabled();
  }

  /**
   * Returns the difference between the setpoint and the current position.
   *
   * @return The error in units corresponding to whichever mode we are in.
   * @see #set(double) set() for a detailed description of the various units.
   */
  public double getError() {
    return getClosedLoopError();
  }

  /**
   * Calls {@link #set(double)}.
   */
  public void setSetpoint(double setpoint) {
    set(setpoint);
  }

  /**
   * Flips the sign (multiplies by negative one) the sensor values going into the talon.
   *
   * <p>This only affects position and velocity closed loop control. Allows for situations where you
   * may have a sensor flipped and going in the wrong direction.
   *
   * @param flip True if sensor input should be flipped; False if not.
   */
  public void reverseSensor(boolean flip) {
    TalonSRXJNI.SetRevFeedbackSensor(m_handle, flip ? 1 : 0);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Reverse Sensor");
  }

  /**
   * Flips the sign (multiplies by negative one) the throttle values going into the motor on the
   * talon in closed loop modes.
   *
   * @param flip True if motor output should be flipped; False if not.
   */
  public void reverseOutput(boolean flip) {
    TalonSRXJNI.SetRevMotDuringCloseLoopEn(m_handle, flip ? 1 : 0);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Reverse Output");
  }

  /**
   * Gets the current status of the Talon (usually a sensor value).
   *
   * <p>In Current mode: returns output current. In Speed mode: returns current speed. In Position
   * mode: returns current sensor position. In PercentVbus and Follower modes: returns current
   * applied throttle.
   *
   * @return The current sensor value of the Talon.
   */
  public double get() {
	  double k;
    switch (m_controlMode) {
      case Voltage:
    	k = getOutputVoltage();
    	break;
      case Current:
        k = getOutputCurrent();
        break;
      case Speed:
        k = TalonSRXJNI.GetSensorVelocity(m_handle);
        break;
      case Position:
        k = TalonSRXJNI.GetSensorPosition(m_handle);
        break;
      case PercentVbus:
      default:
        k = (double) TalonSRXJNI.GetAppliedThrottle(m_handle) / 1023.0;
    }
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Get");
    return k;
  }

  /**
   * Get the current encoder position, regardless of whether it is the current feedback device.
   *
   * @return The current position of the encoder.
   */
  public int getEncPosition() {
    int k = TalonSRXJNI.GetEncPosition(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Get Enc Position");
    return k;
  }

  /**
   * Sets the value of the encoder position to the desired value.
   *
   * @param newPosition The desired position value to set.
   */
  public void setEncPosition(int newPosition) {
    setParameter(TalonSRXJNI.param_t.eEncPosition, newPosition);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Set Encoder Position");
  }

  /**
   * Get the current encoder velocity, regardless of whether it is the current feedback device.
   *
   * @return The current speed of the encoder.
   */
  public int getEncVelocity() {
    int k = TalonSRXJNI.GetEncVel(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Get Encoder Velocity");
    return k;
  }

  public int getPulseWidthPosition() {
    int k = TalonSRXJNI.GetPulseWidthPosition(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Get Pulse Width Position");
    return k;
  }

  public void setPulseWidthPosition(int newPosition) {
    setParameter(TalonSRXJNI.param_t.ePwdPosition, newPosition);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Set Pulse Width Position");
  }

  public int getPulseWidthVelocity() {
    int k = TalonSRXJNI.GetPulseWidthVelocity(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Get Pulse Width Velocity");
    return k;
  }

  public int getPulseWidthRiseToFallUs() {
    int k = TalonSRXJNI.GetPulseWidthRiseToFallUs(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Get Pulse Width Rise to Fall Us");
    return k;
  }

  public int getPulseWidthRiseToRiseUs() {
    int k = TalonSRXJNI.GetPulseWidthRiseToRiseUs(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Get Pulse Width Rise to Rise Us");
    return k;
  }

  /**
   * @param feedbackDevice which feedback sensor to check it if is connected.
   * @return status of caller's specified sensor type.
   */
  public FeedbackDeviceStatus isSensorPresent(FeedbackDevice feedbackDevice) {
    FeedbackDeviceStatus retval = FeedbackDeviceStatus.FeedbackStatusUnknown;
    /* detecting sensor health depends on which sensor caller cares about */
    switch (feedbackDevice) {
      case QuadEncoder:
      case AnalogPot:
      case AnalogEncoder:
      case EncRising:
      case EncFalling:
        /* no real good way to tell if these sensor
          are actually present so return status unknown. */
        break;
      case PulseWidth:
      case CtreMagEncoder_Relative:
      case CtreMagEncoder_Absolute:
        /* all of these require pulse width signal to be present. */
        if (TalonSRXJNI.IsPulseWidthSensorPresent(m_handle) == 0) {
          /* Talon not getting a signal */
          retval = FeedbackDeviceStatus.FeedbackStatusNotPresent;
        } else {
          /* getting good signal */
          retval = FeedbackDeviceStatus.FeedbackStatusPresent;
        }
        break;
      default:
        break;
    }
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Is Sensor Present");
    return retval;
  }

  /**
   * Get the number of of rising edges seen on the index pin.
   *
   * @return number of rising edges on idx pin.
   */
  public int getNumberOfQuadIdxRises() {
    int k = TalonSRXJNI.GetEncIndexRiseEvents(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Get Number of Quad Idx Rises");
    return k;
  }

  /**
   * @return IO level of QUADA pin.
   */
  public int getPinStateQuadA() {
    int k = TalonSRXJNI.GetQuadApin(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Get Quad A Pin");
    return k;
  }

  /**
   * @return IO level of QUADB pin.
   */
  public int getPinStateQuadB() {
    int k = TalonSRXJNI.GetQuadBpin(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Get Quad B Pin");
    return k;
  }

  /**
   * @return IO level of QUAD Index pin.
   */
  public int getPinStateQuadIdx() {
    int k = TalonSRXJNI.GetQuadIdxpin(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Get Quad Idx Pin");
    return k;
  }

  public void setAnalogPosition(int newPosition) {
    setParameter(TalonSRXJNI.param_t.eAinPosition, (double) newPosition);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Set Analog Position");
  }

  /**
   * Get the current analog in position, regardless of whether it is the current feedback device.
   *
   * <p>The bottom ten bits is the ADC (0 - 1023) on the analog pin of the Talon. The upper 14 bits
   * tracks the overflows and underflows (continuous sensor).
   *
   * @return The 24bit analog position.
   */
  public int getAnalogInPosition() {
    int k = TalonSRXJNI.GetAnalogInWithOv(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Get Analog Position");
    return k;
  }

  /**
   * Get the current analog in position, regardless of whether it is the current feedback device.
   *
   * @return The ADC (0 - 1023) on analog pin of the Talon.
   */
  public int getAnalogInRaw() {
    int k = getAnalogInPosition() & 0x3FF;
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Get Analog In Raw");
    return k;
  }

  /**
   * Get the current encoder velocity, regardless of whether it is the current feedback device.
   *
   * @return The current speed of the analog in device.
   */
  public int getAnalogInVelocity() {
    int k = TalonSRXJNI.GetAnalogInVel(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Get Analog Velocity");
    return k;
  }

  /**
   * Get the current difference between the setpoint and the sensor value.
   *
   * @return The error, in whatever units are appropriate.
   */
  public int getClosedLoopError() {
    /* retrieve the closed loop error in native units */
    int k = TalonSRXJNI.GetCloseLoopErr(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " getClosedLoopError");
    return k;
  }

  /**
   * Set the allowable closed loop error.
   *
   * @param allowableCloseLoopError allowable closed loop error for selected profile. mA for Curent
   *                                closed loop. Talon Native Units for position and velocity.
   */
  public void setAllowableClosedLoopErr(int allowableCloseLoopError) {
    if (m_profile == 0) {
      setParameter(TalonSRXJNI.param_t.eProfileParamSlot0_AllowableClosedLoopErr, (double)
          allowableCloseLoopError);
    } else {
      setParameter(TalonSRXJNI.param_t.eProfileParamSlot1_AllowableClosedLoopErr, (double)
          allowableCloseLoopError);
    }
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Set Allowable Closed Loop Error");
  }
  
  public boolean getLimitSwitchEn() {
	boolean k = (TalonSRXJNI.GetLimitSwitchEn(m_handle) == 1);  
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " getLimitSwitchEn");
    return k;
  }

  // Returns true if limit switch is closed. false if open.
  public boolean isFwdLimitSwitchClosed() {
    boolean k = (TalonSRXJNI.GetLimitSwitchClosedFor(m_handle) == 0);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " isFwdLimitSwitchClosed");
    return k;
  }

  // Returns true if limit switch is closed. false if open.
  public boolean isRevLimitSwitchClosed() {
    boolean k = (TalonSRXJNI.GetLimitSwitchClosedRev(m_handle) == 0);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " isRevLimitSwitchClosed");
    return k;
  }

  //Returns true if Index is set to Zero Sensor Position
  public boolean isZeroSensorPosOnIndexEnabled() {
	  boolean k = (TalonSRXJNI.GetClearPosOnIdx(m_handle) == 1);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " isZeroSensorPosOnIndexEnabled");
    return k;
  }
  
  //Returns true if Reverse Limit Switch is set to Zero Sensor Position
  public boolean isZeroSensorPosOnRevLimitEnabled() {
	  boolean k = (TalonSRXJNI.GetClearPosOnLimR(m_handle) == 1);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " isZeroSensorPosOnRevLimitEnabled");
    return k;
  }
  
  //Returns true if Reverse Limit Switch is set to Zero Sensor Position
  public boolean isZeroSensorPosOnFwdLimitEnabled() {
	  boolean k = (TalonSRXJNI.GetClearPosOnLimF(m_handle) == 1);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " isZeroSensorPosOnFwdLimitEnabled");
    return k;
  }
  
  // Returns true if break is enabled during neutral. false if coast.
  public boolean getBrakeEnableDuringNeutral() {
    boolean k = TalonSRXJNI.GetBrakeIsEnabled(m_handle) != 0;
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " getBrakeEnableDuringNeutral");
    return k;
  }

  /**
   * Configure how many codes per revolution are generated by your encoder.
   *
   * @param codesPerRev The number of counts per revolution.
   */
  public void configEncoderCodesPerRev(int codesPerRev) {
    /* first save the scalar so that all getters/setter work as the user expects */
    m_codesPerRev = codesPerRev;
    /* next send the scalar to the Talon over CAN.  This is so that the Talon can report
      it to whoever needs it, like the webdash.  Don't bother checking the return,
      this is only for instrumentation and is not necessary for Talon functionality. */
    setParameter(TalonSRXJNI.param_t.eNumberEncoderCPR, m_codesPerRev);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Config Encoder Codes per Rev");
  }

  /**
   * Configure the number of turns on the potentiometer.
   *
   * @param turns The number of turns of the potentiometer.
   */
  public void configPotentiometerTurns(int turns) {
    /* first save the scalar so that all getters/setter work as the user expects */
    m_numPotTurns = turns;
    /* next send the scalar to the Talon over CAN.  This is so that the Talon can report
      it to whoever needs it, like the webdash.  Don't bother checking the return,
      this is only for instrumentation and is not necessary for Talon functionality. */
    setParameter(TalonSRXJNI.param_t.eNumberPotTurns, m_numPotTurns);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Config Potentiometer Turns");
  }

  /**
   * Returns temperature of Talon, in degrees Celsius.
   */
  public double getTemperature() {
    double k = TalonSRXJNI.GetTemp(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " getTemperature");
	return k;
  }

  /**
   * Returns the current going through the Talon, in Amperes.
   */
  public double getOutputCurrent() {
    double k = TalonSRXJNI.GetCurrent(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " getOutputCurrent");
	return k;
  }

  /**
   * @return The voltage being output by the Talon, in Volts.
   */
  public double getOutputVoltage() {
    double k = getBusVoltage() * (double) TalonSRXJNI.GetAppliedThrottle(m_handle) / 1023.0;
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " getOutputVoltage");
	return k;
  }

  /**
   * @return The voltage at the battery terminals of the Talon, in Volts.
   */
  public double getBusVoltage() {
    double k = TalonSRXJNI.GetBatteryV(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " getBusVoltage");
    return k;
  }
  
  /**
   * When using analog sensors, 0 units corresponds to 0V, 1023 units corresponds to 3.3V When using
   * an analog encoder (wrapping around 1023 to 0 is possible) the units are still 3.3V per 1023
   * units. When using quadrature, each unit is a quadrature edge (4X) mode.
   *
   * @return The position of the sensor currently providing feedback.
   */
  public double getPosition() {
    double k = TalonSRXJNI.GetSensorPosition(m_handle);//scaleNativeUnitsToRotations(m_feedbackDevice, );
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " getPosition");
    return k;
  }

  public void setPosition(int pos) {
    TalonSRXJNI.SetSensorPosition(m_handle, pos);
  }

  /**
   * The speed units will be in the sensor's native ticks per 100ms.
   *
   * <p>For analog sensors, 3.3V corresponds to 1023 units. So a speed of 200 equates to ~0.645 dV
   * per 100ms or 6.451 dV per second. If this is an analog encoder, that likely means 1.9548
   * rotations per sec. For quadrature encoders, each unit corresponds a quadrature edge (4X). So a
   * 250 count encoder will produce 1000 edge events per rotation. An example speed of 200 would
   * then equate to 20% of a rotation per 100ms, or 10 rotations per second.
   *
   * @return The speed of the sensor currently providing feedback.
   */
  public double getSpeed() {
    double k = TalonSRXJNI.GetSensorVelocity(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " getSpeed");
    return k;
  }

  public TalonControlMode getControlMode() {
    TalonControlMode k = m_controlMode;
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " getControlMode");
    return k;
  }

  /**
   * Member of CANSpeedController Interface.
   * {@link #changeControlMode(TalonControlMode)} should be used instead.
   *
   */
  public void setControlMode(int mode)
  {
	  setControlMode(com.ctre.phoenix.MotorControl.ControlMode.SmartControlMode.values()[mode]);
  }
  
  public void setControlMode(com.ctre.phoenix.MotorControl.ControlMode.BasicControlMode mode) {
	  setControlMode(com.ctre.phoenix.MotorControl.ControlMode.Promote(mode));
  }
  
  @SuppressWarnings("JavadocMethod")
  public void setControlMode(com.ctre.phoenix.MotorControl.ControlMode.SmartControlMode mode) {
    TalonControlMode tcm = TalonControlMode.values()[mode.value];
    if (tcm != null) {
      changeControlMode(tcm);
    }
  }

  private void applyUsageStats(UsageFlags usage)
  {
	if((usage.value & m_usageHist) == 0)
	{  
		m_usageHist |= usage.value;

		HAL.report(tResourceType.kResourceType_CANTalonSRX, m_deviceNumber + 1,
			m_usageHist);
	}
  }
  
  private UsageFlags controlModeUsage(TalonControlMode mode)
  {
	  switch (mode)
	  {
		case PercentVbus:
			{return UsageFlags.PercentVbus; }
		case Position:
			{return UsageFlags.Position; }
		case Speed:
			{return UsageFlags.Speed; }
		case Current:
			{return UsageFlags.Current; }
		case Voltage:
			{return UsageFlags.Voltage; }
		case Follower:
			{return UsageFlags.Follower; }
		case MotionProfile:
			{return UsageFlags.MotionProfile; }
		case MotionMagic:
			{return UsageFlags.MotionMagic; }
		default:
			{return UsageFlags.Default; }
	  }
  }
  
  /**
   * Fixup the m_controlMode so set() serializes the correct demand value. Also fills the
   * modeSelecet in the control frame to disabled.
   *
   * @param controlMode Control mode to ultimately enter once user calls set().
   * @see #set
   */
  private void applyControlMode(TalonControlMode controlMode) {
    m_controlMode = controlMode;
    if (controlMode == TalonControlMode.Disabled) {
      m_controlEnabled = false;
    }
    // Disable until set() is called.
    TalonSRXJNI.SetModeSelect(m_handle, TalonControlMode.Disabled.value);

    applyUsageStats(controlModeUsage(m_controlMode));
  }

  @SuppressWarnings("JavadocMethod")
  public void changeControlMode(TalonControlMode controlMode) {
    if (m_controlMode == controlMode) {
      /* we already are in this mode, don't perform disable workaround */
    } else {
      applyControlMode(controlMode);
    }
  }

  @SuppressWarnings("JavadocMethod")
  public void setFeedbackDevice(FeedbackDevice device) {
    /* save the selection so that future setters/getters know which scalars to apply */
    m_feedbackDevice = device;
    /* pass feedback to actual CAN frame */
    TalonSRXJNI.SetFeedbackDeviceSelect(m_handle, device.value);
  }
  
  public int getFeedbackDeviceSelect() {
	  int k = TalonSRXJNI.GetFeedbackDeviceSelect(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " getFeedbackDeviceSelect");
    return k;
  }

  public void setStatusFrameRateMs(StatusFrameRate stateFrame, int periodMs) {
    TalonSRXJNI.SetStatusFrameRate(m_handle, stateFrame.value, periodMs);
  }

  public void enableControl() {
    changeControlMode(m_controlMode);
    m_controlEnabled = true;
  }

  public void enable() {
    enableControl();
  }

  public void disableControl() {
    TalonSRXJNI.SetModeSelect(m_handle, TalonControlMode.Disabled.value);
    m_controlEnabled = false;
  }

  public boolean isControlEnabled() {
    return m_controlEnabled;
  }

  /**
   * Get the current proportional constant.
   *
   * @return double proportional constant for current profile.
   */
  public double getP() {
    // if(!(m_controlMode.equals(ControlMode.Position) ||
    // m_controlMode.equals(ControlMode.Speed))) {
    // throw new
    // IllegalStateException("PID mode only applies in Position and Speed modes.");
    // }

    // Update the information that we have.
    if (m_profile == 0) {
      TalonSRXJNI.RequestParam(m_handle, TalonSRXJNI.param_t.eProfileParamSlot0_P.value);
    } else {
      TalonSRXJNI.RequestParam(m_handle, TalonSRXJNI.param_t.eProfileParamSlot1_P.value);
    }

    // Briefly wait for new values from the Talon.
    Timer.delay(kDelayForSolicitedSignals);

    double k = TalonSRXJNI.GetPgain(m_handle, m_profile);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  ", Get P");
    return k;
  }

  @SuppressWarnings("JavadocMethod")
  public double getI() {
    // if(!(m_controlMode.equals(ControlMode.Position) ||
    // m_controlMode.equals(ControlMode.Speed))) {
    // throw new
    // IllegalStateException("PID mode only applies in Position and Speed modes.");
    // }

    // Update the information that we have.
    if (m_profile == 0) {
      TalonSRXJNI.RequestParam(m_handle, TalonSRXJNI.param_t.eProfileParamSlot0_I.value);
    } else {
      TalonSRXJNI.RequestParam(m_handle, TalonSRXJNI.param_t.eProfileParamSlot1_I.value);
    }

    // Briefly wait for new values from the Talon.
    Timer.delay(kDelayForSolicitedSignals);

    double k = TalonSRXJNI.GetIgain(m_handle, m_profile);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  ", Get I");
	return k;
  }

  @SuppressWarnings("JavadocMethod")
  public double getD() {
    // if(!(m_controlMode.equals(ControlMode.Position) ||
    // m_controlMode.equals(ControlMode.Speed))) {
    // throw new
    // IllegalStateException("PID mode only applies in Position and Speed modes.");
    // }

    // Update the information that we have.
    if (m_profile == 0) {
      TalonSRXJNI.RequestParam(m_handle, TalonSRXJNI.param_t.eProfileParamSlot0_D.value);
    } else {
      TalonSRXJNI.RequestParam(m_handle, TalonSRXJNI.param_t.eProfileParamSlot1_D.value);
    }

    // Briefly wait for new values from the Talon.
    Timer.delay(kDelayForSolicitedSignals);

    double k = TalonSRXJNI.GetDgain(m_handle, m_profile);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  ", Get D");
	return k;
  }

  @SuppressWarnings("JavadocMethod")
  public double getF() {
    // if(!(m_controlMode.equals(ControlMode.Position) ||
    // m_controlMode.equals(ControlMode.Speed))) {
    // throw new
    // IllegalStateException("PID mode only applies in Position and Speed modes.");
    // }

    // Update the information that we have.
    if (m_profile == 0) {
      TalonSRXJNI.RequestParam(m_handle, TalonSRXJNI.param_t.eProfileParamSlot0_F.value);
    } else {
      TalonSRXJNI.RequestParam(m_handle, TalonSRXJNI.param_t.eProfileParamSlot1_F.value);
    }

    // Briefly wait for new values from the Talon.
    Timer.delay(kDelayForSolicitedSignals);

    double k = TalonSRXJNI.GetFgain(m_handle, m_profile);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  ", Get F");
	return k;
  }

  @SuppressWarnings("JavadocMethod")
  public double getIZone() {
    // if(!(m_controlMode.equals(ControlMode.Position) ||
    // m_controlMode.equals(ControlMode.Speed))) {
    // throw new
    // IllegalStateException("PID mode only applies in Position and Speed modes.");
    // }

    // Update the information that we have.
    if (m_profile == 0) {
      TalonSRXJNI.RequestParam(m_handle, TalonSRXJNI.param_t.eProfileParamSlot0_IZone.value);
    } else {
      TalonSRXJNI.RequestParam(m_handle, TalonSRXJNI.param_t.eProfileParamSlot1_IZone.value);
    }

    // Briefly wait for new values from the Talon.
    Timer.delay(kDelayForSolicitedSignals);

    double k = TalonSRXJNI.GetIzone(m_handle, m_profile);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  ", Get I Zone");
	return k;
  }

  /**
   * Get the closed loop ramp rate for the current profile.
   *
   * <p>Limits the rate at which the throttle will change. Only affects position and speed closed
   * loop modes.
   *
   * @return rampRate Maximum change in voltage, in volts / sec.
   * @see #setProfile For selecting a certain profile.
   */
  public double getCloseLoopRampRate() {
    // if(!(m_controlMode.equals(ControlMode.Position) ||
    // m_controlMode.equals(ControlMode.Speed))) {
    // throw new
    // IllegalStateException("PID mode only applies in Position and Speed modes.");
    // }

    // Update the information that we have.
    if (m_profile == 0) {
      TalonSRXJNI.RequestParam(m_handle, TalonSRXJNI.param_t.eProfileParamSlot0_CloseLoopRampRate
          .value);
    } else {
      TalonSRXJNI.RequestParam(m_handle, TalonSRXJNI.param_t.eProfileParamSlot1_CloseLoopRampRate
          .value);
    }

    // Briefly wait for new values from the Talon.
    Timer.delay(kDelayForSolicitedSignals);

    double throttlePerMs = TalonSRXJNI.GetCloseLoopRampRate(m_handle, m_profile);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  ", Get Closed Loop Ramp Rate");
    return throttlePerMs / 1023.0 * 12.0 * 1000.0;
  }

  /**
   * Firmware version running on the Talon.
   *
   * @return The version of the firmware running on the Talon
   */
  @SuppressWarnings("MethodName")
  public long GetFirmwareVersion() {

    // Update the information that we have.
    TalonSRXJNI.RequestParam(m_handle, TalonSRXJNI.param_t.eFirmVers.value);

    // Briefly wait for new values from the Talon.
    Timer.delay(kDelayForSolicitedSignals);

    long k = TalonSRXJNI.GetParamResponseInt32(m_handle, TalonSRXJNI.param_t.eFirmVers.value);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  " Get Get Firmware Version");
	return k;
  }

  @SuppressWarnings({"MethodName", "JavadocMethod"})
  public long GetIaccum() {

    // Update the information that we have.
    TalonSRXJNI.RequestParam(m_handle, TalonSRXJNI.param_t.ePidIaccum.value);

    // Briefly wait for new values from the Talon.
    Timer.delay(kDelayForSolicitedSignals);

    long k = TalonSRXJNI.GetParamResponseInt32(m_handle, TalonSRXJNI.param_t.ePidIaccum.value);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  " Get I Accum");
	return k;
  }

  /**
   * Set the proportional value of the currently selected profile.
   *
   * @param p Proportional constant for the currently selected PID profile.
   * @see #setProfile For selecting a certain profile.
   */
  @SuppressWarnings("ParameterName")
  public void setP(double p) {
    TalonSRXJNI.SetPgain(m_handle, m_profile, p);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  " Set P");
  }

  /**
   * Set the integration constant of the currently selected profile.
   *
   * @param i Integration constant for the currently selected PID profile.
   * @see #setProfile For selecting a certain profile.
   */
  @SuppressWarnings("ParameterName")
  public void setI(double i) {
    TalonSRXJNI.SetIgain(m_handle, m_profile, i);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  " Set I");
  }

  /**
   * Set the derivative constant of the currently selected profile.
   *
   * @param d Derivative constant for the currently selected PID profile.
   * @see #setProfile For selecting a certain profile.
   */
  @SuppressWarnings("ParameterName")
  public void setD(double d) {
    TalonSRXJNI.SetDgain(m_handle, m_profile, d);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  " Set D");
  }

  /**
   * Set the feedforward value of the currently selected profile.
   *
   * @param f Feedforward constant for the currently selected PID profile.
   * @see #setProfile For selecting a certain profile.
   */
  @SuppressWarnings("ParameterName")
  public void setF(double f) {
    TalonSRXJNI.SetFgain(m_handle, m_profile, f);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  " Set F");
  }

  /**
   * Set the integration zone of the current Closed Loop profile.
   *
   * <p>Whenever the error is larger than the izone value, the accumulated integration error is
   * cleared so that high errors aren't racked up when at high errors. An izone value of 0 means no
   * difference from a standard PIDF loop.
   *
   * @param izone Width of the integration zone.
   * @see #setProfile For selecting a certain profile.
   */
  public void setIZone(int izone) {
    TalonSRXJNI.SetIzone(m_handle, m_profile, izone);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  " Set I Zone");
  }

  /**
   * Set the closed loop ramp rate for the current profile.
   *
   * <p>Limits the rate at which the throttle will change. Only affects position and speed closed
   * loop modes.
   *
   * @param rampRate Maximum change in voltage, in volts / sec.
   * @see #setProfile For selecting a certain profile.
   */
  public void setCloseLoopRampRate(double rampRate) {
    // TalonSRXJNI takes units of Throttle (0 - 1023) / 1ms.
    int rate = (int) (rampRate * 1023.0 / 12.0 / 1000.0);
    TalonSRXJNI.SetCloseLoopRampRate(m_handle, m_profile, rate);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  " Set Closed Loop Ramp Rate");
  }

  /**
   * Set the voltage ramp rate for the current profile.
   *
   * <p>Limits the rate at which the throttle will change. Affects all modes.
   *
   * @param rampRate Maximum change in voltage, in volts / sec.
   */
  public void setVoltageRampRate(double rampRate) {
    // TalonSRXJNI takes units of Throttle (0 - 1023) / 10ms.
    int rate = (int) (rampRate * 1023.0 / 12.0 / 100.0);
    TalonSRXJNI.SetRampThrottle(m_handle, rate);
	applyUsageStats(UsageFlags.VRampRate);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  " Set Voltage Ramp Rate");
  }

  public void setVoltageCompensationRate(double rampRate) {
    TalonSRXJNI.SetVoltageCompensationRate(m_handle, rampRate / 1000);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  " Set Voltage Compensation");
  }

  /**
   * Clear the accumulator for I gain.
   */
  @SuppressWarnings("MethodName")
  public void ClearIaccum() {
    TalonSRXJNI.SetParam(m_handle, TalonSRXJNI.param_t.ePidIaccum.value, 0);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  " Clear I Accum");
  }

  /**
   * Sets control values for closed loop control.
   *
   * @param p                 Proportional constant.
   * @param i                 Integration constant.
   * @param d                 Differential constant.
   * @param f                 Feedforward constant.
   * @param izone             Integration zone -- prevents accumulation of integration error with
   *                          large errors. Setting this to zero will ignore any izone stuff.
   * @param closeLoopRampRate Closed loop ramp rate. Maximum change in voltage, in volts / sec.
   * @param profile           which profile to set the pid constants for. You can have two profiles,
   *                          with values of 0 or 1, allowing you to keep a second set of values on
   *                          hand in the talon. In order to switch profiles without recalling
   *                          setPID, you must call setProfile().
   */
  @SuppressWarnings("ParameterName")
  public void setPID(double p, double i, double d, double f, int izone, double closeLoopRampRate,
                     int profile) {
    if (profile != 0 && profile != 1) {
      throw new IllegalArgumentException("Talon PID profile must be 0 or 1.");
    }
    m_profile = profile;
    setProfile(profile);
    setP(p);
    setI(i);
    setD(d);
    setF(f);
    setIZone(izone);
    setCloseLoopRampRate(closeLoopRampRate);
  }

  @SuppressWarnings("ParameterName")
  public void setPID(double p, double i, double d) {
    setPID(p, i, d, 0, 0, 0, m_profile);
  }

  /**
   * @return The latest value set using set().
   */
  public double getSetpoint() {
    return m_setPoint;
  }

  /**
   * Select which closed loop profile to use, and uses whatever PIDF gains and the such that are
   * already there.
   */
  public void setProfile(int profile) {
    if (profile != 0 && profile != 1) {
      throw new IllegalArgumentException("Talon PID profile must be 0 or 1.");
    }
    m_profile = profile;
    TalonSRXJNI.SetProfileSlotSelect(m_handle, m_profile);
	
	applyUsageStats(UsageFlags.MultiProfile);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  " Set Profile");
  }

  /**
   * Common interface for stopping a motor.
   *
   * @deprecated Use disableControl instead.
   */
  @Override
  @Deprecated
  public void stopMotor() {
    disableControl();
    m_stopped = true;
  }

//  @Override
  public void disable() {
    disableControl();
  }

  public int getDeviceID() {
    return m_deviceNumber;
  }

  // TODO: Documentation for all these accessors/setters for misc. stuff.
  public void clearIAccum() {
    TalonSRXJNI.SetParam(m_handle, TalonSRXJNI.param_t.ePidIaccum.value, 0);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  " Clear I Accum");
  }

  public void setForwardSoftLimit(int forwardLimit) {
    TalonSRXJNI.SetForwardSoftLimit(m_handle, forwardLimit);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  " Set Forward Soft Limit");
  }

  public int getForwardSoftLimit() {
	TalonSRXJNI.RequestParam(m_handle, TalonSRXJNI.param_t.eProfileParamSoftLimitForThreshold.value);
    int k = TalonSRXJNI.GetForwardSoftLimit(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  " Get Forward Soft Limit");
	return k;
  }

  public void enableForwardSoftLimit(boolean enable) {
    TalonSRXJNI.SetForwardSoftEnable(m_handle, enable ? 1 : 0);
	applyUsageStats(UsageFlags.ForwardSoftLimit);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  " Enable Forward Soft Limit");
  }

  public boolean isForwardSoftLimitEnabled() {
	TalonSRXJNI.RequestParam(m_handle, TalonSRXJNI.param_t.eProfileParamSoftLimitForEnable.value);
    boolean k = TalonSRXJNI.GetForwardSoftEnable(m_handle) != 0;
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  " Is Forward Soft Limit Enabled");
	return k;
  }

  public void setReverseSoftLimit(int reverseLimit) {
    TalonSRXJNI.SetReverseSoftLimit(m_handle, reverseLimit);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  " Set Reverse Soft Limit");
  }

  public int getReverseSoftLimit() {
	TalonSRXJNI.RequestParam(m_handle, TalonSRXJNI.param_t.eProfileParamSoftLimitRevThreshold.value);
    int k = TalonSRXJNI.GetReverseSoftLimit(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  " Get Reverse Soft Limit");
	return k;
  }

  public void enableReverseSoftLimit(boolean enable) {
    TalonSRXJNI.SetReverseSoftEnable(m_handle, enable ? 1 : 0);
	applyUsageStats(UsageFlags.ReverseSoftLimit);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  " Enable Reverse Soft Limit");
  }

  public boolean isReverseSoftLimitEnabled() {
	TalonSRXJNI.RequestParam(m_handle, TalonSRXJNI.param_t.eProfileParamSoftLimitRevEnable.value);
    boolean k = TalonSRXJNI.GetReverseSoftEnable(m_handle) != 0;
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  " Is Reverse Soft Limit Enabled");
	return k;
  }

  /**
   * Configure the maximum voltage that the Talon SRX will ever output.
   *
   * <p>This can be used to limit the maximum output voltage (positive or negative)
   * in closed-loop modes so that motors which cannot withstand full bus voltage can be used safely.
   *
   * @param voltage The maximum voltage output by the Talon SRX.
   */
  public void configMaxOutputVoltage(double voltage) {
    /* config peak throttle when in closed-loop mode in the fwd and rev direction. */
    configPeakOutputVoltage(voltage, -voltage);
  }

  @SuppressWarnings("JavadocMethod")
  public void configPeakOutputVoltage(double forwardVoltage, double reverseVoltage) {
    /* bounds checking */
    if (forwardVoltage > 12) {
      forwardVoltage = 12;
    } else if (forwardVoltage < 0) {
      forwardVoltage = 0;
    }
    if (reverseVoltage > 0) {
      reverseVoltage = 0;
    } else if (reverseVoltage < -12) {
      reverseVoltage = -12;
    }
    /* config calls */
    setParameter(TalonSRXJNI.param_t.ePeakPosOutput, 1023 * forwardVoltage / 12.0);
    setParameter(TalonSRXJNI.param_t.ePeakNegOutput, 1023 * reverseVoltage / 12.0);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  " Config Peak Output Voltage");
  }

  @SuppressWarnings("JavadocMethod")
  public void configNominalOutputVoltage(double forwardVoltage, double reverseVoltage) {
    /* bounds checking */
    if (forwardVoltage > 12) {
      forwardVoltage = 12;
    } else if (forwardVoltage < 0) {
      forwardVoltage = 0;
    }
    if (reverseVoltage > 0) {
      reverseVoltage = 0;
    } else if (reverseVoltage < -12) {
      reverseVoltage = -12;
    }
    /* config calls */
    setParameter(TalonSRXJNI.param_t.eNominalPosOutput, 1023 * forwardVoltage / 12.0);
    setParameter(TalonSRXJNI.param_t.eNominalNegOutput, 1023 * reverseVoltage / 12.0);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  " Config Nominal Output Voltage");
  }

  /**
   * General set frame.  Since the parameter is a general integral type, this can be used for
   * testing future features.
   */
  public void setParameter(TalonSRXJNI.param_t paramEnum, double value) {
    TalonSRXJNI.SetParam(m_handle, paramEnum.value, value);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  " Set Parameter");
  }

  /**
   * General get frame.  Since the parameter is a general integral type, this can be used for
   * testing future features.
   */
  public double getParameter(TalonSRXJNI.param_t paramEnum) {
    /* transmit a request for this param */
    TalonSRXJNI.RequestParam(m_handle, paramEnum.value);
    /* Briefly wait for new values from the Talon. */
    Timer.delay(kDelayForSolicitedSignals);
    /* poll out latest response value */
    double k = TalonSRXJNI.GetParamResponse(m_handle, paramEnum.value);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  " Get Parameter");
	return k;
  }

  public void clearStickyFaults() {
    TalonSRXJNI.ClearStickyFaults(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  " Clear Sticky Faults");
  }

  public void enableLimitSwitch(boolean forward, boolean reverse) {
    int mask = 4 + (forward ? 1 : 0) * 2 + (reverse ? 1 : 0);
    TalonSRXJNI.SetOverrideLimitSwitchEn(m_handle, mask);
	
	if(forward)
		applyUsageStats(UsageFlags.ForwardLimitSwitch);
	if(reverse)
		applyUsageStats(UsageFlags.ReverseLimitSwitch);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  " Enable Limit Switch");
  }

  /**
   * Configure the fwd limit switch to be normally open or normally closed. Talon will disable
   * momentarilly if the Talon's current setting is dissimilar to the caller's requested setting.
   *
   * <p>Since Talon saves setting to flash this should only affect a given Talon initially during
   * robot install.
   *
   * @param normallyOpen true for normally open. false for normally closed.
   */
  @SuppressWarnings("MethodName")
  public void ConfigFwdLimitSwitchNormallyOpen(boolean normallyOpen) {
    TalonSRXJNI.SetParam(m_handle, TalonSRXJNI.param_t.eOnBoot_LimitSwitch_Forward_NormallyClosed
            .value,
        normallyOpen ? 0 : 1);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  " Config Forward Limit Switch Normally Open");
  }

  /**
   * Configure the rev limit switch to be normally open or normally closed. Talon will disable
   * momentarilly if the Talon's current setting is dissimilar to the caller's requested setting.
   *
   * <p>Since Talon saves setting to flash this should only affect a given Talon initially during
   * robot install.
   *
   * @param normallyOpen true for normally open. false for normally closed.
   */
  @SuppressWarnings("MethodName")
  public void ConfigRevLimitSwitchNormallyOpen(boolean normallyOpen) {
    TalonSRXJNI.SetParam(m_handle, TalonSRXJNI.param_t.eOnBoot_LimitSwitch_Reverse_NormallyClosed
            .value,
        normallyOpen ? 0 : 1);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  " Config Reverse Limit Switch Normally Open");
  }

  public void enableBrakeMode(boolean brake) {
    TalonSRXJNI.SetOverrideBrakeType(m_handle, brake ? 2 : 1);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  " Enable Brake Override");
  }

  public int getFaultOverTemp() {
    int k = TalonSRXJNI.GetFault_OverTemp(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  " Get Fault Over Temp");
	return k;
  }

  public int getFaultUnderVoltage() {
    int k = TalonSRXJNI.GetFault_UnderVoltage(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  " Get Fault Under Voltage");
	return k;
  }

  public int getFaultForLim() {
    int k = TalonSRXJNI.GetFault_ForLim(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber +  " Set Get Fault Forward Limit");
	return k;
  }

  public int getFaultRevLim() {
    int k = TalonSRXJNI.GetFault_RevLim(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " getFaultRevLim");
    return k;
  }

  public int getFaultHardwareFailure() {
    int k = TalonSRXJNI.GetFault_HardwareFailure(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " getFaultHardwareFailure");
    return k;
  }

  public int getFaultForSoftLim() {
    int k = TalonSRXJNI.GetFault_ForSoftLim(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " getFaultForSoftLim");
    return k;
  }

  public int getFaultRevSoftLim() {
    int k = TalonSRXJNI.GetFault_RevSoftLim(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " getFaultRevSoftLim");
    return k;
  }

  public int getStickyFaultOverTemp() {
    int k = TalonSRXJNI.GetStckyFault_OverTemp(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " getStickyFaultOverTemp");
    return k;
  }

  public int getStickyFaultUnderVoltage() {
    int k = TalonSRXJNI.GetStckyFault_UnderVoltage(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " getStickyFaultUnderVoltage");
    return k;
  }

  public int getStickyFaultForLim() {
    int k = TalonSRXJNI.GetStckyFault_ForLim(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " getStickyFaultForLim");
    return k;
  }

  public int getStickyFaultRevLim() {
    int k = TalonSRXJNI.GetStckyFault_RevLim(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " getStickyFaultRevLim");
    return k;
  }

  public int getStickyFaultForSoftLim() {
    int k = TalonSRXJNI.GetStckyFault_ForSoftLim(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " getStickyFaultForSoftLim");
    return k;
  }

  public int getStickyFaultRevSoftLim() {
    int k = TalonSRXJNI.GetStckyFault_RevSoftLim(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " getStickyFaultRevSoftLim");
    return k;
  }

  /**
   * Enables Talon SRX to automatically zero the Sensor Position whenever an edge is detected on the
   * index signal.
   *
   * @param enable     boolean input, pass true to enable feature or false to disable.
   * @param risingEdge boolean input, pass true to clear the position on rising edge, pass false to
   *                   clear the position on falling edge.
   */
  public void enableZeroSensorPositionOnIndex(boolean enable, boolean risingEdge) {
    if (enable) {
      /* enable the feature, update the edge polarity first to ensure
        it is correct before the feature is enabled. */
      setParameter(TalonSRXJNI.param_t.eQuadIdxPolarity, risingEdge ? 1 : 0);
      setParameter(TalonSRXJNI.param_t.eClearPositionOnIdx, 1);
    } else {
      /* disable the feature first, then update the edge polarity. */
      setParameter(TalonSRXJNI.param_t.eClearPositionOnIdx, 0);
      setParameter(TalonSRXJNI.param_t.eQuadIdxPolarity, risingEdge ? 1 : 0);
    }
	
	applyUsageStats(UsageFlags.ZeroSensorI);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Enable Zero Sensor Position on Index");
  }

  /**
   * Enables Talon SRX to automatically zero the Sensor Position whenever an edge is detected on the
   * Forward Limit Switch signal.
   *
   * @param enable     boolean input, pass true to enable feature or false to disable.
   */
  public void enableZeroSensorPositionOnForwardLimit(boolean enable){
	  setParameter(TalonSRXJNI.param_t.eClearPositionOnLimitF, enable ? 1 : 0);
	  applyUsageStats(UsageFlags.ZeroSensorF);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Enable Zero Sensor Position on Forward Limit");
  } 
  
  /**
   * Enables Talon SRX to automatically zero the Sensor Position whenever an edge is detected on the
   * Reverse Limit Switch signal.
   *
   * @param enable     boolean input, pass true to enable feature or false to disable.
   */
  public void enableZeroSensorPositionOnReverseLimit(boolean enable){
	  setParameter(TalonSRXJNI.param_t.eClearPositionOnLimitR, enable ? 1 : 0);
	  applyUsageStats(UsageFlags.ZeroSensorR);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Enable Zero Sensor Position on Reverse Limit");
  }
  
  
/**
* @param voltage       Motor voltage to output when closed loop features are being used (Position,
*                      Speed, Motion Profile, Motion Magic, etc.) in volts.
*                      Pass 0 to disable feature.  Input should be within [0.0 V,255.0 V]
*/
public void setNominalClosedLoopVoltage(double voltage){
	setParameter(TalonSRXJNI.param_t.eNominalBatteryVoltage, voltage);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Set Nominal Closed Loop Voltage");
}

/**
* Disables the nominal closed loop voltage compensation.
* Same as calling SetNominalClosedLoopVoltage(0).
*/
public void DisableNominalClosedLoopVoltage(){
	setNominalClosedLoopVoltage(0.0);
}

/**
* @return the currently selected nominal closed loop voltage. Zero (Default) means feature is disabled.
*/
public double GetNominalClosedLoopVoltage(){
  TalonSRXJNI.RequestParam(m_handle, TalonSRXJNI.param_t.eNominalBatteryVoltage.value);

  // small yield for getting response
  Timer.delay(kDelayForSolicitedSignals);
  
    /* get the last received update */
  double k = TalonSRXJNI.GetParamResponse(m_handle, TalonSRXJNI.param_t.eNominalBatteryVoltage.value);
  com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Get Nominal Closed Loop Voltage");
  return k;
}

  /**
   * Enumerated types for velocity measurement period ms.
   */
  public enum VelocityMeasurementPeriod {
    Period_1Ms(1), Period_2Ms(2), Period_5Ms(5), Period_10Ms(10),
	Period_20Ms(20), Period_25Ms(25), Period_50Ms(50), Period_100Ms(100);

    @SuppressWarnings("MemberName")
    public int value;

    @SuppressWarnings("JavadocMethod")
    public static VelocityMeasurementPeriod valueOf(int value) {
      for (VelocityMeasurementPeriod mode : values()) {
        if (mode.value == value) {
          return mode;
        }
      }
      return null;
    }

    VelocityMeasurementPeriod(int value) {
      this.value = value;
    }
  }
  
/**
* Sets the duration of time that the Talon measures for each velocity measurement (which occures at each 1ms process loop).
* The default value is 100, which means that every process loop (1ms), the Talon will measure the change in position
* between now and 100ms ago, and will insert into a rolling average.
*
* Decreasing this from the default (100ms) will yield a less-resolute measurement since there is less time for the sensor to change.
* This will be perceived as increased granularity in the measurement (or stair-stepping).  But doing so will also decrease the latency 
* between sensor motion and measurement.
* 
* Regardles of this setting value, native velocity units are still in change-in-sensor-per-100ms.
* 
* @param period      Support period enum.  Curent valid values are 1,2,5,10,20,25,50, or 100ms.
*/
public void SetVelocityMeasurementPeriod(VelocityMeasurementPeriod period){
	setParameter(TalonSRXJNI.param_t.eSampleVelocityPeriod, period.value);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Set Velocity Measurement Period");
}

/**
* Sets the window size of the rolling average used in velocity measurement.
* The default value is 64, which means that every process loop (1ms), the Talon will insert a velocity measurement 
* into a windowed averager with a history of 64 samples.
* Each sample is inserted every 1ms regardless of what Period is selected. 
* As a result the window is practically in ms units.
* 
* @param windowSize    Window size of rolling average.
*/
public void SetVelocityMeasurementWindow(int windowSize){
	setParameter(TalonSRXJNI.param_t.eSampleVelocityWindow, (int)windowSize);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Set Velocity Measurement Period Window");
}

public VelocityMeasurementPeriod GetVelocityMeasurementPeriod(){
  TalonSRXJNI.RequestParam(m_handle, TalonSRXJNI.param_t.eSampleVelocityPeriod.value);

  // small yield for getting response
  Timer.delay(kDelayForSolicitedSignals);
  
    /* get the last received update */
  VelocityMeasurementPeriod k = VelocityMeasurementPeriod.valueOf(TalonSRXJNI.GetParamResponseInt32(m_handle, TalonSRXJNI.param_t.eSampleVelocityPeriod.value));
  com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Get Velocity Measurement Period");
  return k;
}

public int GetVelocityMeasurementWindow(){
  TalonSRXJNI.RequestParam(m_handle, TalonSRXJNI.param_t.eSampleVelocityWindow.value);

  // small yield for getting response
  Timer.delay(kDelayForSolicitedSignals);
  
    /* get the last received update */
  int k = (int)TalonSRXJNI.GetParamResponseInt32(m_handle, TalonSRXJNI.param_t.eSampleVelocityWindow.value);
  com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Get Velocity Measurement Window");
  return k;
}
  
  
  /**
   * Calling application can opt to speed up the handshaking between the robot API and the Talon to
   * increase the download rate of the Talon's Motion Profile.  Ideally the period should be no more
   * than half the period of a trajectory point.
   */
  public void changeMotionControlFramePeriod(int periodMs) {
    TalonSRXJNI.ChangeMotionControlFramePeriod(m_handle, periodMs);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Change Motion Control Frame Period");
  }

  /**
   * Clear the buffered motion profile in both Talon RAM (bottom), and in the API (top). Be sure to
   * check getMotionProfileStatus() to know when the buffer is actually cleared.
   */
  public void clearMotionProfileTrajectories() {
    TalonSRXJNI.ClearMotionProfileTrajectories(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Clear Motion Profile Trajectories");
  }

  /**
   * Retrieve just the buffer count for the api-level (top) buffer. This routine performs no CAN or
   * data structure lookups, so its fast and ideal if caller needs to quickly poll the progress of
   * trajectory points being emptied into Talon's RAM. Otherwise just use GetMotionProfileStatus.
   *
   * @return number of trajectory points in the top buffer.
   */
  public int getMotionProfileTopLevelBufferCount() {
    int k = TalonSRXJNI.GetMotionProfileTopLevelBufferCount(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Get Motion Profile Top Level Buffer Count");
	return k;
  }

  /**
   * Push another trajectory point into the top level buffer (which is emptied into the Talon's
   * bottom buffer as room allows).
   *
   * <p>Will return CTR_OKAY if trajectory point push ok. CTR_BufferFull if buffer is full due to
   * kMotionProfileTopBufferCapacity.
   *
   * @param trajPt {@link TrajectoryPoint}
   * @return CTR_OKAY or CTR_BufferFull.
   */
  public boolean pushMotionProfileTrajectory(TrajectoryPoint trajPt) {
    /* check if there is room */
    if (isMotionProfileTopLevelBufferFull()) {
      return false;
    }
    /* convert position and velocity to native units */
    int targPos = (int)trajPt.position;
    int targVel = (int)trajPt.velocity;
    /* bounds check signals that require it */
    int profileSlotSelect = (trajPt.profileSlotSelect > 0) ? 1 : 0;
    int timeDurMs = trajPt.timeDurMs;
    /* cap time to [0ms, 255ms], 0 and 1 are both interpreted as 1ms. */
    if (timeDurMs > 255) {
      timeDurMs = 255;
    }
    if (timeDurMs < 0) {
      timeDurMs = 0;
    }
    /* send it to the top level buffer */
    TalonSRXJNI.PushMotionProfileTrajectory(m_handle, targPos, targVel, profileSlotSelect,
        timeDurMs, trajPt.velocityOnly ? 1 : 0, trajPt.isLastPoint ? 1 : 0, trajPt.zeroPos ? 1 : 0);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Push Motion Profile Trajectory");
    return true;
  }

  /**
   * @return true if api-level (top) buffer is full.
   */
  public boolean isMotionProfileTopLevelBufferFull() {
    boolean k = TalonSRXJNI.IsMotionProfileTopLevelBufferFull(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Is Motion Profile Buffer Full");
	return k;
  }

  /**
   * This must be called periodically to funnel the trajectory points from the API's top level
   * buffer to the Talon's bottom level buffer.  Recommendation is to call this twice as fast as the
   * executation rate of the motion profile. So if MP is running with 20ms trajectory points, try
   * calling this routine every 10ms.  All motion profile functions are thread-safe through the use
   * of a mutex, so there is no harm in having the caller utilize threading.
   */
  public void processMotionProfileBuffer() {
    TalonSRXJNI.ProcessMotionProfileBuffer(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Process Motion Profile Buffer");
  }

  /**
   * Retrieve all Motion Profile status information. Since this all comes from one CAN frame, its
   * ideal to have one routine to retrieve the frame once and decode everything.
   *
   * @param motionProfileStatus [out] contains all progress information on the currently running MP.
   *                            Caller should must instantiate the motionProfileStatus object first
   *                            then pass into this routine to be filled.
   */
  public void getMotionProfileStatus(MotionProfileStatus motionProfileStatus) {
    TalonSRXJNI.GetMotionProfileStatus(m_handle, this, motionProfileStatus);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Get Motion Profile Status");
  }

  /**
   * Internal method to set the contents.
   */
  protected void setMotionProfileStatusFromJNI(MotionProfileStatus motionProfileStatus,
                                               int flags, int profileSlotSelect, int targPos,
                                               int targVel, int topBufferRem, int topBufferCnt,
                                               int btmBufferCnt, int outputEnable) {
    motionProfileStatus.topBufferRem = topBufferRem;
    motionProfileStatus.topBufferCnt = topBufferCnt;
    motionProfileStatus.btmBufferCnt = btmBufferCnt;
    motionProfileStatus.hasUnderrun = ((flags & TalonSRXJNI.kMotionProfileFlag_HasUnderrun) > 0);
    motionProfileStatus.isUnderrun = ((flags & TalonSRXJNI.kMotionProfileFlag_IsUnderrun) > 0);
    motionProfileStatus.activePointValid =
        ((flags & TalonSRXJNI.kMotionProfileFlag_ActTraj_IsValid) > 0);
    motionProfileStatus.activePoint.isLastPoint =
        ((flags & TalonSRXJNI.kMotionProfileFlag_ActTraj_IsLast) > 0);
    motionProfileStatus.activePoint.velocityOnly =
        ((flags & TalonSRXJNI.kMotionProfileFlag_ActTraj_VelOnly) > 0);
    motionProfileStatus.activePoint.position = targPos;
    motionProfileStatus.activePoint.velocity = targVel;
    motionProfileStatus.activePoint.profileSlotSelect = profileSlotSelect;
    motionProfileStatus.outputEnable = SetValueMotionProfile.valueOf(outputEnable);
    motionProfileStatus.activePoint.zeroPos = false; // this signal is only used sending pts to
    // Talon
    motionProfileStatus.activePoint.timeDurMs = 0;   // this signal is only used sending pts to
	System.out.println(targVel);
  }

  /**
   * Clear the hasUnderrun flag in Talon's Motion Profile Executer when MPE is ready for another
   * point, but the low level buffer is empty.
   *
   * <p>Once the Motion Profile Executer sets the hasUnderrun flag, it stays set until Robot
   * Application clears it with this routine, which ensures Robot Application gets a chance to
   * instrument or react.  Caller could also check the isUnderrun flag which automatically clears
   * when fault condition is removed.
   */
	public void clearMotionProfileHasUnderrun() {
		setParameter(TalonSRXJNI.param_t.eMotionProfileHasUnderrunErr, 0);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Clear Motion Profile Has Underrun");
	}
	
	
	/**
	 * Set the Cruise Velocity used in Motion Magic Control Mode.
	 * @param motMagicCruiseVeloc Cruise(peak) velocity in RPM.
	 */
	public void setMotionMagicCruiseVelocity(int motMagicCruiseVeloc)
	{
		int velNative = motMagicCruiseVeloc;
		setParameter(TalonSRXJNI.param_t.eMotMag_VelCruise, (double)velNative);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Set Motion Magic Cruise Velocity");
	}
	/**
	 * Set the Acceleration used in Motion Magic Control Mode.
	 * @param motMagicAccel Accerleration in RPM per second.
	 */
	public void setMotionMagicAcceleration(int motMagicAccel)
	{
		int accel = motMagicAccel;
		setParameter(TalonSRXJNI.param_t.eMotMag_Accel, accel);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Set Motion Magic Acceleration");
	}
	/**
	 * @return polled motion magic cruise velocity setting from Talon.
	 * RPM if units are configured, velocity native units otherwise.
	 */
	public double getMotionMagicCruiseVelocity()
	{
	// Update the info in m_impl.
	  double retval = getParameter(TalonSRXJNI.param_t.eMotMag_VelCruise);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Get Motion Magic Cruise Velocity");
	  	  
	  //TODO: Add Error Checking
	  
	  return retval;
	}
	/**
	 * @return polled motion magic acceleration setting from Talon.
	 * RPM per second if units are configured, velocity native units per second otherwise.
	 */
	public double getMotionMagicAcceleration() {
		/* get the last received update */
		double retval = getParameter(TalonSRXJNI.param_t.eMotMag_Accel);

    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Get Motion Magic Acceleration");
		//TODO: Add Error Checking
		
		return retval;
	}
	
	/**
   * @return current Motion Magic trajectory point's target velocity.
   * RPM if units are configured, velocity native units otherwise.
   */
    public double getMotionMagicActTrajVelocity()
	{
		int retval = TalonSRXJNI.GetMotMagActTraj_Velocity(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Get Motion Magic Active Trajectory Velocity");
		
		return retval;
	}
	
	/**
   * @return current Motion Magic trajectory point's target position.
   * Rotations if units are configured, position native units otherwise.
   */
    public double getMotionMagicActTrajPosition()
	{
		int retval = TalonSRXJNI.GetMotMagActTraj_Position(m_handle);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Get Motion Magic Act Trajectory Position");
		return retval;
	}
	
	/**
	 * Set the Value of the current limit.  Use {@link #EnableCurrentLimit(boolean)}
	 * to turn feature on and off.
	 *
	 * @param amps Current Limit in amps.
	*/
	public void setCurrentLimit(int amps)
	{
		setParameter(TalonSRXJNI.param_t.eCurrentLimThreshold, amps);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Set Current Limit");
	}
	
	/**
	 * Enable or Disable the current limit.  Use {@link #setCurrentLimit(int)}
	 * to set the value of the limit.
	 *
	 * @param enable True to Enable, False to Disable.
	*/
	public void EnableCurrentLimit(boolean enable)
	{
		TalonSRXJNI.SetCurrentLimEnable(m_handle, enable);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Enable Current Limit");
		applyUsageStats(UsageFlags.CurrentLimit);
	}
	
	public void SetDataPortOutputPeriod(int periodMs)
	{
		TalonSRXJNI.SetDataPortOutputPeriodMs(m_handle, periodMs);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Set Data Port Output Period");
	}
	
	public void SetDataPortOutputEnable(int idx, boolean enable)
	{
		TalonSRXJNI.SetDataPortOutputEnable(m_handle, idx, enable);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Set Data Port Output Enable");
	}
	
	public void SetDataPortOutput(int idx, int onTimeMs)
	{
		TalonSRXJNI.SetDataPortOutputOnTimeMs(m_handle, idx, onTimeMs);
    com.ctre.phoenix.CTRLogger.Log(getLastError(), "Smart Motor Controller " + m_deviceNumber + " Set Data Port Output");
	}
	
	
	@Override
	public int GetGadgeteerStatus(GadgeteerUartStatus status) {
		return com.ctre.phoenix.CTR_Code.CTR_NotSupported.IntValue();
	}
	
/*	@Override
	public int GetGadgeteerStatus(GadgeteerUartStatus status) {

		int bitrate;
		int errCode = ReceiveCAN(0x02041680 | m_deviceNumber, 200);
		
		bitrate = (int)((_cache >> 0x10) & 0xFF);
		bitrate <<= 8;
		bitrate |= (int)((_cache >> 0x18) & 0xFF);
		bitrate *= 100;
		
		GadgeteerState st = GadgeteerUartClient.GadgeteerState.getEnum((int)((_cache >> 8) & 0x0F));

		GadgeteerConnection connection = GadgeteerConnection.NotConnected;
		switch (st)
		{
			case GadState_WaitChirp1:
				connection = GadgeteerConnection.NotConnected;
				break;
			case GadState_WaitBLInfo:
			case GadState_WaitBitrateResp:
			case GadState_WaitSwitchDelay:
			case GadState_WaitChirp2:
				connection = GadgeteerConnection.Connecting;
				break;
			case GadState_Connected_Idle:
			case GadState_Connected_ReqChirp:
			case GadState_Connected_RespChirp:
			case GadState_Connected_ReqCanBus:
			case GadState_Connected_RespCanBus:
			case GadState_Connected_RespIsoThenChirp:
			case GadState_Connected_RespIsoThenCanBus:
				connection = GadgeteerConnection.Connected;
				break;
			default:
				connection = GadgeteerConnection.NotConnected;
		}
		if(status != null)
		{
			status.bitrate = bitrate;
			status.type = GadgeteerUartClient.GadgeteerProxyType.getEnum((int)(_cache & 0xFF));
			status.conn = connection;
			status.resetCount = (int) ((_cache >> 0x38) & 0xFF);
		}
		return errCode;
	}
*/
	
  public int getLastError()
  {
	  return TalonSRXJNI.GetLastError(m_handle);
  }

  public String getLastErrorString()
  {
	  int error = TalonSRXJNI.GetLastError(m_handle);
	  
		  switch (error) {
		    case 0:
		      return "";
		    case TalonSRXJNI.CTR_RxTimeout:
		      return TalonSRXJNI.CTR_RxTimeout_MESSAGE;
		    case TalonSRXJNI.CTR_TxTimeout:
		      return TalonSRXJNI.CTR_TxTimeout_MESSAGE;
		    case TalonSRXJNI.CTR_InvalidParamValue:
		      return TalonSRXJNI.CTR_InvalidParamValue_MESSAGE;
		    case TalonSRXJNI.CTR_UnexpectedArbId:
		      return TalonSRXJNI.CTR_UnexpectedArbId_MESSAGE;
		    case TalonSRXJNI.CTR_TxFailed:
		      return TalonSRXJNI.CTR_TxFailed_MESSAGE;
		    case TalonSRXJNI.CTR_SigNotUpdated:
		      return TalonSRXJNI.CTR_SigNotUpdated_MESSAGE;
		    case TalonSRXJNI.ERR_CANSessionMux_InvalidBuffer:
		      return TalonSRXJNI.ERR_CANSessionMux_InvalidBuffer_MESSAGE;
		    case TalonSRXJNI.ERR_CANSessionMux_MessageNotFound:
		      return TalonSRXJNI.ERR_CANSessionMux_MessageNotFound_MESSAGE;
		    case TalonSRXJNI.WARN_CANSessionMux_NoToken:
		      return TalonSRXJNI.WARN_CANSessionMux_NoToken_MESSAGE;
		    case TalonSRXJNI.ERR_CANSessionMux_NotAllowed:
		      return TalonSRXJNI.ERR_CANSessionMux_NotAllowed_MESSAGE;
		    default:
		      return "Unknown error status";
		  }
  }
	
  @Override
  public void setExpiration(double timeout) {
    m_safetyHelper.setExpiration(timeout);
  }

  @Override
  public double getExpiration() {
    return m_safetyHelper.getExpiration();
  }

  @Override
  public boolean isAlive() {
    return m_safetyHelper.isAlive();
  }

  @Override
  public boolean isSafetyEnabled() {
    return m_safetyHelper.isSafetyEnabled();
  }

  @Override
  public void setSafetyEnabled(boolean enabled) {
    m_safetyHelper.setSafetyEnabled(enabled);
  }

  @Override
  public String getDescription() {
    return "CAN::Talon ID " + m_deviceNumber;
  }

  /*
   * Live Window code, only does anything if live window is activated.
   */
/*
  private ITable m_table = null;
  private ITableListener m_tableListener = null;

  @Override
  public void initTable(ITable subtable) {
    m_table = subtable;
    updateTable();
  }

  @Override
  public void updateTable() {
    CANSpeedController.super.updateTable();
  }

  @Override
  public ITable getTable() {
    return m_table;
  }

  @Override
  public void startLiveWindowMode() {
    set(0); // Stop for safety
    m_tableListener = createTableListener();
    m_table.addTableListener(m_tableListener, true);
  }

  @Override
  public void stopLiveWindowMode() {
    set(0); // Stop for safety
    // TODO: See if this is still broken
    m_table.removeTableListener(m_tableListener);
  }
 */ 
  public void follow(Object masterToFollow)
  {
	  SmartMotorController master = (SmartMotorController)masterToFollow;
	  setControlMode(5);
	  set(master.getDeviceID());
  }
  
  public void valueUpdated()
  {
	  
  }

}