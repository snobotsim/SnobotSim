package com.snobot.simulator.simulator_components.rev;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.simulator_components.smart_sc.SmartScAnalogIn;
import com.snobot.simulator.simulator_components.smart_sc.SmartScEncoder;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;

import edu.wpi.first.hal.HALValue;
import edu.wpi.first.hal.SimDouble;
import edu.wpi.first.hal.sim.SimDeviceSim;
import edu.wpi.first.hal.sim.mockdata.SimDeviceDataJNI.SimDeviceInfo;
import edu.wpi.first.hal.sim.mockdata.SimDeviceDataJNI.SimValueInfo;

import java.lang.reflect.Field;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.snobot.simulator.simulator_components.smart_sc.BaseCanSmartSpeedController;

public class RevSpeedControllerSimWrapper extends BaseCanSmartSpeedController
{
    private static final Logger sLOGGER = LogManager.getLogger(RevSpeedControllerSimWrapper.class);
    

//    SimDevice  m_simDevice;
//
//
//    SimDouble  m_AltEncoderAverageDepth_depth;
//    SimDouble  m_AltEncoderCountsPerRevolution_cpr;
//    SimDouble  m_AltEncoderInverted_inverted;
//    SimDouble  m_AltEncoderMeasurementPeriod_samples;
//    SimDouble  m_AltEncoderPositionFactor_conversion;
//    SimDouble  m_AltEncoderPosition_position;
//    SimDouble  m_AltEncoderVelocityFactor_conversion;
//    SimDouble  m_AltEncoderVelocity_velocity;
//    SimDouble  m_AnalogAverageDepth_depth;
//    SimDouble  m_AnalogInverted_inverted;
//    SimDouble  m_AnalogMeasurementPeriod_samples;
//    SimDouble  m_AnalogMode_mode;
//    SimDouble  m_AnalogPositionConversionFactor_conversion;
//    SimDouble  m_AnalogPosition_position;
//    SimDouble  m_AnalogVelocityConversionFactor_conversion;
//    SimDouble  m_AnalogVelocity_velocity;
//    SimDouble  m_AnalogVoltage_voltage;
//    SimDouble  m_AverageDepth_depth;
//    SimDouble  m_BusVoltage_busVoltage;
//    SimDouble  m_CloseTelemetryStream_telemetryHandle;
//    SimDouble  m_ClosedLoopRampRate_rate;
//    SimDouble  m_ControlFramePeriod_periodMs;
//    SimDouble  m_CountsPerRevolution_cpr;
//    SimDouble  m_DRVStatus_drvStatus;
//    SimDouble  m_DataPortConfig_config;
//    SimDouble  m_DeviceId_deviceId;
//    SimDouble  m_EnableLimitSwitch_enable;
//    SimDouble  m_EnableLimitSwitch_sw;
//    SimDouble  m_EnableSoftLimit_dir;
//    SimDouble  m_EnableSoftLimit_enable;
//    SimDouble  m_EnableVoltageCompensation_nominalVoltage;
//    SimDouble  m_EncoderInverted_inverted;
//    SimDouble  m_FactoryWipe_persist;
//    SimDouble  m_Fault_fault;
//    SimDouble  m_Fault_faultId;
//    SimDouble  m_Faults_faults;
//    SimDouble  m_FeedbackDeviceID_id;
//    SimDouble  m_FeedbackDeviceRange_max;
//    SimDouble  m_FeedbackDeviceRange_min;
//    SimDouble  m_Follow_followerArbId;
//    SimDouble  m_Follow_followerCfg;
//    SimDouble  m_Follower_isFollower;
//    SimDouble  m_GenerateError_deviceID;
//    SimDouble  m_GenerateError_error;
//    SimDouble  m_IAccum_iAccum;
//    SimDouble  m_IDAssign_deviceId;
//    SimDouble  m_IDAssign_uniqueId;
//    SimDouble  m_IDQuery_numberOfDevices;
//    SimDouble  m_IDQuery_uniqueIdArray;
//    SimDouble  m_IDQuery_uniqueIdArraySize;
//    SimDouble  m_IdentifyUniqueId_uniqueId;
//    SimDouble  m_IdleMode_idlemode;
//    SimDouble  m_Inverted_inverted;
//    SimDouble  m_LastError_error;
//    SimDouble  m_LimitEnabled_enabled;
//    SimDouble  m_LimitEnabled_sw;
//    SimDouble  m_LimitPolarity_polarity;
//    SimDouble  m_LimitPolarity_sw;
//    SimDouble  m_LimitSwitch_limit;
//    SimDouble  m_LimitSwitch_sw;
//    SimDouble  m_ListTelemetryStream_messages;
//    SimDouble  m_MeasurementPeriod_samples;
//    SimDouble  m_MotorTemperature_motorTemperature;
//    SimDouble  m_MotorType_type;
//    SimDouble  m_OpenLoopRampRate_rate;
//    SimDouble  m_OpenTelemetryStream_telemetryHandle;
//    SimDouble  m_OutputCurrent_outputCurrent;
//    SimDouble  m_ParameterBool_paramId;
//    SimDouble  m_ParameterBool_value;
//    SimDouble  m_ParameterFloat32_paramId;
//    SimDouble  m_ParameterFloat32_value;
//    SimDouble  m_ParameterInt32_paramId;
//    SimDouble  m_ParameterInt32_value;
//    SimDouble  m_ParameterUint32_paramId;
//    SimDouble  m_ParameterUint32_value;
//    SimDouble  m_PeriodicFramePeriod_frameId;
//    SimDouble  m_PeriodicFramePeriod_periodMs;
//    SimDouble  m_PeriodicStatus0_rawframe;
//    SimDouble  m_PeriodicStatus1_rawframe;
//    SimDouble  m_PeriodicStatus2_rawframe;
//    SimDouble  m_PeriodicStatus3_rawframe;
//    SimDouble  m_PeriodicStatus4_rawframe;
//    SimDouble  m_PositionConversionFactor_conversion;
//    SimDouble  m_ReadTelemetryStream_ids;
//    SimDouble  m_ReadTelemetryStream_messages;
//    SimDouble  m_ReadTelemetryStream_numOfStreams;
//    SimDouble  m_ReadTelemetryStream_telemetryHandle;
//    SimDouble  m_RestoreFactoryDefaults_persist;
//    SimDouble  m_SecondaryCurrentLimit_chopCycles;
//    SimDouble  m_SecondaryCurrentLimit_limit;
//    SimDouble  m_SensorType_sensorType;
//    SimDouble  m_SerialNumber_serialNumber;
//    SimDouble  m_SmartCurrentLimit_freeLimit;
//    SimDouble  m_SmartCurrentLimit_limitRPM;
//    SimDouble  m_SmartCurrentLimit_stallLimit;
//    SimDouble  m_SoftLimitEnabled_dir;
//    SimDouble  m_SoftLimitEnabled_enabled;
//    SimDouble  m_SoftLimit_dir;
//    SimDouble  m_SoftLimit_limit;
//    SimDouble  m_StickyFault_faultId;
//    SimDouble  m_StickyFault_stickyfault;
//    SimDouble  m_StickyFaults_stickyFaults;
//    SimDouble  m_VelocityConversionFactor_conversion;
//    SimDouble  m_VoltageCompensationNominalVoltage_nominalVoltage;

    SimDeviceSim  mSimDevice;
    
    SimDouble  mSetpointCommandCtrl;
    SimDouble  mSetpointCommandValue;
    SimDouble  mAppliedOutputAppliedOutput;
    SimDouble  mSensorTypeSensorType;
    SimDouble  mFeedbackDeviceSensorID;
    SimDouble  mEncoderPosition;
    SimDouble  mEncoderVelocity;



    private static final class SlottedVariables
    {
        SimDouble mPGain;
        SimDouble mIGain;
        SimDouble mDGain;
        SimDouble mFFGain;
    
//        SimDouble  m_DFilter_gain;
//        SimDouble  m_IMaxAccum_iMaxAccum;
//        SimDouble  m_IZone_IZone;
//        SimDouble  m_OutputMax_max;
//        SimDouble  m_OutputMin_min;
//        SimDouble  m_OutputRange_max;
//        SimDouble  m_OutputRange_min;
//        SimDouble  m_SmartMotionAccelStrategy_accelStrategy;
//        SimDouble  m_SmartMotionAllowedClosedLoopError_allowedError;
//        SimDouble  m_SmartMotionMaxAccel_maxAccel;
//        SimDouble  m_SmartMotionMaxVelocity_maxVel;
//        SimDouble  m_SmartMotionMinOutputVelocity_minVel;
    }
    
    private static final int NUM_SLOTS = 6;
    private final SlottedVariables[] mSlottedVariables = new SlottedVariables[NUM_SLOTS];
    
    public RevSpeedControllerSimWrapper(int aCanHandle)
    {
        super(aCanHandle, "Rev", NUM_SLOTS);
        
        int deviceId = aCanHandle - 100;
        String deviceName = "RevCANSparkMaxDriverWrapper " + deviceId + "[" + deviceId + "]";
        mSimDevice = new SimDeviceSim(deviceName);
        mSetpointCommandCtrl = mSimDevice.getDouble("pointCommand_ctrl");
        mSetpointCommandValue = mSimDevice.getDouble("pointCommand_value");
        mAppliedOutputAppliedOutput = mSimDevice.getDouble("AppliedOutput_appliedOutput");
        mSensorTypeSensorType = mSimDevice.getDouble("SensorType_sensorType");
        mFeedbackDeviceSensorID = mSimDevice.getDouble("FeedbackDevice_sensorID");
        mEncoderPosition = mSimDevice.getDouble("EncoderPosition_position");
        mEncoderVelocity = mSimDevice.getDouble("EncoderVelocity_velocity");
        System.out.println(deviceName  + ", " + mSimDevice + ", " +  mSetpointCommandCtrl + ", " + mSetpointCommandValue);
        
        for (int i = 0; i < NUM_SLOTS; ++i)
        {
            mSlottedVariables[i] = new SlottedVariables();
            mSlottedVariables[i].mPGain = mSimDevice.getDouble("P_gain[" + i + "]");
            mSlottedVariables[i].mIGain = mSimDevice.getDouble("I_gain[" + i + "]");
            mSlottedVariables[i].mDGain = mSimDevice.getDouble("D_gain[" + i + "]");
            mSlottedVariables[i].mFFGain = mSimDevice.getDouble("FF_gain[" + i + "]");
        }
    }

    @Override
    protected void registerFeedbackSensor()
    {
        switch (mFeedbackDevice)
        {
        case Encoder:
            if (!DataAccessorFactory.getInstance().getEncoderAccessor().getPortList().contains(mHandle))
            {
                DataAccessorFactory.getInstance().getEncoderAccessor().createSimulator(mHandle, SmartScEncoder.class.getName());
                DataAccessorFactory.getInstance().getEncoderAccessor().connectSpeedController(getHandle(), getHandle());
                sLOGGER.log(Level.WARN, "REV Encoder on port " + mCanHandle + " was not registerd before starting the robot");
            }
            SensorActuatorRegistry.get().getEncoders().get(getHandle()).setInitialized(true);
            break;
        case Analog:
            if (!DataAccessorFactory.getInstance().getAnalogInAccessor().getPortList().contains(mHandle))
            {
                DataAccessorFactory.getInstance().getAnalogInAccessor().createSimulator(mHandle, SmartScAnalogIn.class.getName());
                sLOGGER.log(Level.WARN, "REV Analog on port " + mCanHandle + " was not registerd before starting the robot");
            }
            SensorActuatorRegistry.get().getAnalogIn().get(getHandle()).setInitialized(true);
            break;
        default:
            sLOGGER.log(Level.ERROR, "Unsupported feedback device " + mFeedbackDevice);
            break;
        }
    }

    @Override
    protected double getPositionUnitConversion()
    {
        return 1;
    }

    @Override
    protected double getMotionMagicAccelerationUnitConversion()
    {
        return 1;
    }

    @Override
    protected double getMotionMagicVelocityUnitConversion()
    {
        return 1;
    }

    @Override
    protected double calculateMotionProfileOutput(double aCurrentPosition, double aCurrentVelocity, int aModeType)
    {
        throw new IllegalStateException("Not supported");
    }

    @SuppressWarnings("PMD.AvoidLiteralsInIfCondition")
    public void setCanFeedbackDevice(int aFeedbackDevice)
    {
        FeedbackDevice newDevice = null;
        if (aFeedbackDevice == 1 || aFeedbackDevice == 2)
        {
            newDevice = FeedbackDevice.Encoder;
        }
        else
        {
            sLOGGER.log(Level.WARN, "Unsupported feedback device " + aFeedbackDevice);
        }

        setCanFeedbackDevice(newDevice);
    }
    
    public void handleSetSetpointCommand()
    {
        float value = (float) mSetpointCommandValue.get();
        int ctrl = (int) mSetpointCommandCtrl.get();
        sLOGGER.log(Level.DEBUG, "SetpointCommand " + value + ", " + ctrl);

        switch (ctrl)
        {
        // Throttle
        case 0:
            setRawGoal(value);
            break;
        // Velocity
        case 1:
            setSpeedGoal(value);
            break;
        // Position
        case 3:
            setPositionGoal(value);
            break;
        // SmartMotion
        case 4:
            setMotionMagicGoal(value);
            break;
        default:
            sLOGGER.log(Level.ERROR, String.format("Unknown demand mode %d", ctrl));
            break;
        }
    }
    
    public void handleSetSensorType()
    {
        int type = (int) mSensorTypeSensorType.get();
        setCanFeedbackDevice(type);
    }
    
    public void handleSetFeedbackDevice()
    {
        int type = (int) mFeedbackDeviceSensorID.get();
        setCanFeedbackDevice(type);
    }
    
    public void handleSetPGain(int aSlot)
    {
        double value = mSlottedVariables[aSlot].mPGain.get();
        setPGain(aSlot, value);
    }
    
    public void handleSetIGain(int aSlot)
    {
        double value = mSlottedVariables[aSlot].mIGain.get();
        setIGain(aSlot, value);
    }
    
    public void handleSetDGain(int aSlot)
    {
        double value = mSlottedVariables[aSlot].mDGain.get();
        setDGain(aSlot, value);
    }
    
    public void handleSetFFGain(int aSlot)
    {
        double value = mSlottedVariables[aSlot].mFFGain.get();
        setFGain(aSlot, value);
    }

    
    public void handleGetAppliedOutput()
    {
        mAppliedOutputAppliedOutput.set(get());
    }
    
    public void handleGetEncoderPosition()
    {
        mEncoderPosition.set(getPosition());
    }
    
    public void handleGetEncoderVelocity()
    {
        mEncoderVelocity.set(getVelocity());
    }
}
