package com.snobot.simulator.simulator_components.ctre;

import java.util.LinkedList;
import java.util.List;

import com.snobot.simulator.SimDeviceDumpHelper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.wpi.first.hal.SimDouble;
import edu.wpi.first.hal.sim.SimDeviceSim;
import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.simulator_components.smart_sc.BaseCanSmartSpeedController;
import com.snobot.simulator.simulator_components.smart_sc.SmartScEncoder;
import com.snobot.simulator.simulator_components.smart_sc.SmartScAnalogIn;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;

public class CtreTalonSrxSpeedControllerSim extends BaseCanSmartSpeedController
{
    private static final Logger sLOGGER = LogManager.getLogger(CtreTalonSrxSpeedControllerSim.class);

    private static final int NUM_SLOTS = 2;

    private boolean mLoggedCantOverrideFwdLimitSwitch;
    private boolean mLoggedCantOverrideRevLimitSwitch;


    public static CtreTalonSrxSpeedControllerSim getMotorControllerWrapper(int aCanPort)
    {
        return (CtreTalonSrxSpeedControllerSim) SensorActuatorRegistry.get().getSpeedControllers().get(aCanPort + CtreTalonSrxSpeedControllerSim.sCAN_SC_OFFSET);
    }

    public static class MotionProfilePoint
    {
        public final int mIndex;
        public final double mPosition;
        public final double mVelocity;

        public MotionProfilePoint(int aIndex, double aPosition, double aVelocity)
        {
            mIndex = aIndex;
            mPosition = aPosition;
            mVelocity = aVelocity;
        }

        @Override
        public String toString()
        {
            return "MotionProfilePoint [mIndex=" + mIndex + ", mPosition=" + mPosition + ", mVelocity=" + mVelocity + "]";
        }

    }

    // Motion Profile
    protected List<MotionProfilePoint> mMotionProfilePoints;
    protected int mMotionProfileProcessedCounter;
    protected int mMotionProfileCurrentPointIndex;
    
    ///////////////////////////////////////////////
    SimDeviceSim  mSimDevice;

    SimDouble m_Demand_demand0;
    SimDouble m_Demand_demand1;
    SimDouble m_Demand_mode;
    SimDouble mSet4Demand0;
    SimDouble mSet4Demand1;
    SimDouble mSet4Demand1Type;
    SimDouble mSet4Mode;
    SimDouble mMotorOutputPercent;
    SimDouble mConfigMotionAccelerationSensorUnitsPer100msPerSec;
    SimDouble mConfigMotionCruiseVelocitySensorUnitsPer100ms;

    SimDouble m_MotionProfileStatus_topBufferCnt;
    SimDouble mPushMotionProfileTrajectoryPosition;
    SimDouble mPushMotionProfileTrajectoryVelocity;
    SimDouble mPushMotionProfileTrajectory2Position;
    SimDouble mPushMotionProfileTrajectory2Velocity;
    SimDouble mInverted_2;
    SimDouble mQuadratureVelocity;
    SimDouble mQuadraturePosition;
    SimDouble mActiveTrajectoryPosition;
    SimDouble mActiveTrajectoryVelocity;

    private static final class SlottedVariables
    {
        SimDouble mPGain;
        SimDouble mIGain;
        SimDouble mDGain;
        SimDouble mFFGain;
        SimDouble mIZone;
        SimDouble mSelectedSensorPosition;
        SimDouble mSelectedSensorVelocity;
        SimDouble mClosedLoopError;
        SimDouble mConfigSelectedFeedbackSensor;
    }

    private final SlottedVariables[] mSlottedVariables = new SlottedVariables[NUM_SLOTS];
    ///////////////////////////////////////////////

    public CtreTalonSrxSpeedControllerSim(int aCanHandle)
    {
        super(aCanHandle, "CTRE", NUM_SLOTS);

        mMotionProfilePoints = new LinkedList<>();
        mMotionProfileProcessedCounter = 0;

    }

    private SimDouble getSimDouble(String name)
    {
        SimDouble output = mSimDevice.getDouble(name);
        if(output == null)
        {
            throw new IllegalArgumentException("Sim device '" + name + "' is not set");
        }

        return output;
    }

    @Override
    public void setInitialized(boolean aInitialized)
    {
        super.setInitialized(aInitialized);


        int deviceId = mCanHandle;
        String deviceName = "CtreMotControllerWrapper " + deviceId + "[" + deviceId + "]";
        System.out.println("----------------- Initializing" + deviceName);
        mSimDevice = new SimDeviceSim(deviceName);

        m_Demand_demand0 = getSimDouble("Demand_demand0");
        m_Demand_demand1 = getSimDouble("Demand_demand1");
        m_Demand_mode = getSimDouble("Demand_mode");
        mSet4Demand0 = getSimDouble("_4_demand0");
        mSet4Demand1 = getSimDouble("_4_demand1");
        mSet4Demand1Type = getSimDouble("_4_demand1Type");
        mSet4Mode = getSimDouble("_4_mode");
        mMotorOutputPercent = getSimDouble("MotorOutputPercent_percentOutput");
        mConfigMotionAccelerationSensorUnitsPer100msPerSec = getSimDouble("ConfigMotionAcceleration_sensorUnitsPer100msPerSec");
        mConfigMotionCruiseVelocitySensorUnitsPer100ms = getSimDouble("ConfigMotionCruiseVelocity_sensorUnitsPer100ms");


        m_MotionProfileStatus_topBufferCnt = getSimDouble("MotionProfileStatus_topBufferCnt");
        mPushMotionProfileTrajectoryPosition = getSimDouble("PushMotionProfileTrajectory_position");
        mPushMotionProfileTrajectoryVelocity = getSimDouble("PushMotionProfileTrajectory_velocity");
        mPushMotionProfileTrajectory2Position = getSimDouble("PushMotionProfileTrajectory_2_position");
        mPushMotionProfileTrajectory2Velocity = getSimDouble("PushMotionProfileTrajectory_2_velocity");
        mInverted_2 = getSimDouble("Inverted_2_invertType");
        mQuadratureVelocity = getSimDouble("QuadratureVelocity_param");
        mQuadraturePosition = getSimDouble("QuadraturePosition_param");
        mActiveTrajectoryPosition = getSimDouble("ActiveTrajectoryPosition_param");
        mActiveTrajectoryVelocity = getSimDouble("ActiveTrajectoryVelocity_param");

        for (int i = 0; i < NUM_SLOTS; ++i)
        {
            mSlottedVariables[i] = new SlottedVariables();
            mSlottedVariables[i].mPGain = getSimDouble("Config_kP_value[" + i + "]");
            mSlottedVariables[i].mIGain = getSimDouble("Config_kI_value[" + i + "]");
            mSlottedVariables[i].mDGain = getSimDouble("Config_kD_value[" + i + "]");
            mSlottedVariables[i].mFFGain = getSimDouble("Config_kF_value[" + i + "]");
            mSlottedVariables[i].mIZone = getSimDouble("Config_IntegralZone_izone[" + i + "]");
            mSlottedVariables[i].mSelectedSensorPosition = getSimDouble("SelectedSensorPosition_param[" + i + "]");
            mSlottedVariables[i].mSelectedSensorVelocity = getSimDouble("SelectedSensorVelocity_param[" + i + "]");
            mSlottedVariables[i].mClosedLoopError = getSimDouble("ClosedLoopError_closedLoopError[" + i + "]");
            mSlottedVariables[i].mConfigSelectedFeedbackSensor = getSimDouble("ConfigSelectedFeedbackSensor_feedbackDevice[" + i + "]");
            System.out.println("XXXX" + mSlottedVariables[i].mConfigSelectedFeedbackSensor + " -- " + mSlottedVariables[i].mPGain);
        }
    }

    public void setMotionProfilingCommand(double aDemand)
    {
        mControlType = ControlType.MotionProfile;
        mControlGoal = aDemand;
    }

    @SuppressWarnings("PMD.AvoidLiteralsInIfCondition")
    @Override
    protected double calculateMotionProfileOutput(double aCurrentPosition, double aCurrentVelocity, int aModeType)
    {
        if (aModeType != 1)
        {
            // System.out.println(mMotionProfilePoints);
            return 0;
        }

        if (mMotionProfileCurrentPointIndex >= mMotionProfilePoints.size())
        {
            return 0;
        }

        MotionProfilePoint currentPoint = mMotionProfilePoints.get(mMotionProfileCurrentPointIndex++);

        return calculateMotionProfileOutput(aCurrentPosition, aCurrentVelocity, currentPoint.mPosition, currentPoint.mVelocity);
    }

    @SuppressWarnings("PMD.UnusedFormalParameter")
    private double calculateMotionProfileOutput(double aCurrentPosition, double aCurrentVelocity, double aGoalPosition, double aGoalVelocity)
    {
        double error = aGoalPosition - aCurrentPosition;

        double pTerm = error * mPidConstants[mCurrentPidProfile].mP;
        double dTerm = 0; // mPidConstants.mD * ((error - last_error_) /
                          // segment.dt - segment.vel);
        double vTerm = mPidConstants[mCurrentPidProfile].mF * aGoalVelocity;
        double output = pTerm + dTerm + vTerm;

//        DecimalFormat df = new DecimalFormat("#.##");
//        System.out.println(output + "  (" + "P: " + df.format(aCurrentPosition) + ", " + "V: " + df.format(aCurrentVelocity) + " - " + "GP: "
//                + aGoalPosition + ", GV: " + aGoalVelocity + ") " + "P: " + pTerm + ", V: " + vTerm);

        return output; // NOPMD
    }

    public double getLastClosedLoopError()
    {
        double multiplier = 1.0;

        switch (mControlType)
        {
        case MotionMagic:
        case MotionProfile:
        case Position:
            multiplier = getPositionUnitConversion();
            break;
        case Speed:
            multiplier = getVelocityUnitConversion();
            break;
        default:
            sLOGGER.log(Level.WARN, "I don't think get closed loop error should be called with " + mControlType);
            break;

        }
        return mLastError * multiplier;
    }

    public void addMotionProfilePoint(MotionProfilePoint aPoint)
    {
        if (aPoint.mIndex == 0)
        {
            mMotionProfilePoints.clear();
            mMotionProfileProcessedCounter = 0;
            mMotionProfileCurrentPointIndex = 0;
        }
        else
        {
            mMotionProfilePoints.add(aPoint);
            mMotionProfileProcessedCounter++;
        }
    }

    public int getMotionProfileSize()
    {
        return mMotionProfilePoints.size();
    }

    public MotionProfilePoint getMotionProfilePoint()
    {
        if (mMotionProfileProcessedCounter <= mMotionProfilePoints.size() && mMotionProfileProcessedCounter != 0)
        {
            return mMotionProfilePoints.get(mMotionProfileProcessedCounter - 1);
        }
        return null;
    }

    public void setLimitSwitchOverride(boolean aOverrideFwdLimitSwitch, boolean aOverrideRevLimitSwitch)
    {
        if (aOverrideFwdLimitSwitch && !mLoggedCantOverrideFwdLimitSwitch)
        {
            mLoggedCantOverrideFwdLimitSwitch = true;
            sLOGGER.log(Level.WARN, "Cannot override forward limit switches");
        }

        if (aOverrideRevLimitSwitch && !mLoggedCantOverrideRevLimitSwitch)
        {
            mLoggedCantOverrideRevLimitSwitch = true;
            sLOGGER.log(Level.WARN, "Cannot override reverse limit switch");
        }
    }

    public void setCurrentProfile(byte aProfileSelect)
    {
        mCurrentPidProfile = aProfileSelect;
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
                sLOGGER.log(Level.WARN, "CTRE Encoder on port " + mCanHandle + " was not registerd before starting the robot");
            }
            SensorActuatorRegistry.get().getEncoders().get(getHandle()).setInitialized(true);
            break;
        case Analog:
            if (!DataAccessorFactory.getInstance().getAnalogInAccessor().getPortList().contains(mHandle))
            {
                DataAccessorFactory.getInstance().getAnalogInAccessor().createSimulator(mHandle, SmartScAnalogIn.class.getName());
                sLOGGER.log(Level.WARN, "CTRE Analog on port " + mCanHandle + " was not registerd before starting the robot");
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
        return 4096;
    }

    protected double getVelocityUnitConversion()
    {
        return 600;
    }

    @Override
    protected double getMotionMagicAccelerationUnitConversion()
    {
        return 600;
    }

    @Override
    protected double getMotionMagicVelocityUnitConversion()
    {
        return 600;
    }

    public int getBinnedPosition()
    {
        return (int) (getPosition() * this.getPositionUnitConversion());
    }

    public int getBinnedVelocity()
    {
        return (int) (getVelocity() * this.getVelocityUnitConversion());
    }

    public void setCanFeedbackDevice(byte aFeedbackDevice)
    {
        FeedbackDevice newDevice = null;
        switch (aFeedbackDevice)
        {
        // Default feedback sensor, handle with care
        case 0:
            newDevice = FeedbackDevice.Encoder;
            break;
        case 2:
            newDevice = FeedbackDevice.Analog;
            break;
        // The Absolute and Relative encoders behave the same
        case 8:
            newDevice = FeedbackDevice.Encoder;
            break;
        default:
            sLOGGER.log(Level.WARN, "Unsupported feedback device " + aFeedbackDevice);
            break;
        }

        setCanFeedbackDevice(newDevice);
    }
    
    ////////////////////////////////////////////////////////////////
    public void handleSetDemand()
    {
        int mode = (int) m_Demand_mode.get();
        int param0 = (int) m_Demand_demand0.get();
        int param1 = (int) m_Demand_demand1.get();
        sLOGGER.log(Level.INFO, "Setting demand " + mode + ", " + param0 + ", " + param1);

        switch (mode)
        {
        case 0:
            set(param0 / 1023.0);
            break;
        case 1:
            setPositionGoal(param0);
            break;
        case 2:
            setSpeedGoal(param0);
            break;
        case 5:
            int followerPort = param0 & 0xFF;
            CtreTalonSrxSpeedControllerSim leadTalon = getMotorControllerWrapper(followerPort);
            leadTalon.addFollower(this);
            break;
        case 6:
            setMotionProfilingCommand(param0);
            break;
        case 7:
            setMotionMagicGoal(param0);
            break;
        case 15:
            set(0);
            break;
        default:
            sLOGGER.log(Level.ERROR, String.format("Unknown demand mode %d", mode));
            break;
        }
    }
    
    public void handleSet4()
    {
        int mode = (int) mSet4Mode.get();
        double demand0 = mSet4Demand0.get();
        double demand1 = mSet4Demand1.get();
        int demand1Type = (int) mSet4Demand1Type.get();
        sLOGGER.log(Level.INFO, "Setting_4 " + mode + ", " + demand0 + ", " + demand1 + ", " + demand1Type);

        switch (mode)
        {
        case 0:
            setRawGoal(demand0);
            break;
        case 1:
            setPositionGoal(demand0);
            break;
        case 2:
            setSpeedGoal(demand0);
            break;
        case 5:
            int followerPort = ((int) demand0) & 0xFF;
            System.out.println("Adding follower " + followerPort);
            CtreTalonSrxSpeedControllerSim leadTalon = getMotorControllerWrapper(followerPort);
            leadTalon.addFollower(this);
            break;
        case 6:
            setMotionProfilingCommand(demand0);
            break;
        case 7:
            setMotionMagicGoal(demand0);
            break;
        case 15:
            set(0);
            break;
        default:
            sLOGGER.log(Level.ERROR, String.format("Unknown demand mode %d", mode));
            break;
        }
    }
    
    public void handleConfigSelectedFeedbackSensor()
    {
        int slotId = 0;
        SimDeviceDumpHelper.dumpSimDevices();
        System.out.println("Config selected sensor..." + mSlottedVariables);
        System.out.println("Config selected sensor..." + mSlottedVariables[slotId].mConfigSelectedFeedbackSensor);
        int feedbackDevice = (int) mSlottedVariables[slotId].mConfigSelectedFeedbackSensor.get();
        setCanFeedbackDevice((byte) feedbackDevice);
    }
    
    public void handleSetKP(int aSlot)
    {
        double value = mSlottedVariables[aSlot].mPGain.get();
        setPGain(aSlot, value);
    }
    
    public void handleSetKI(int aSlot)
    {
        double value = mSlottedVariables[aSlot].mIGain.get();
        setIGain(aSlot, value);
    }
    
    public void handleSetKD(int aSlot)
    {
        double value = mSlottedVariables[aSlot].mDGain.get();
        setDGain(aSlot, value);
    }
    
    public void handleSetKF(int aSlot)
    {
        double value = mSlottedVariables[aSlot].mFFGain.get();
        setFGain(aSlot, value);
    }
    
    public void handleSetIntegralZone(int aSlot)
    {
        double value = mSlottedVariables[aSlot].mIZone.get();
        setIZone(aSlot, value);
    }
    
    public void handleSetMotionCruiseVelocity()
    {
        setMotionMagicMaxVelocity((int) mConfigMotionCruiseVelocitySensorUnitsPer100ms.get());
    }
    
    public void handleSetMotionAcceleration()
    {
        setMotionMagicMaxAcceleration((int) mConfigMotionAccelerationSensorUnitsPer100msPerSec.get());
    }
    
    public void handlePushMotionProfileTrajectory()
    {
        double position = mPushMotionProfileTrajectoryPosition.get();
        double velocity = mPushMotionProfileTrajectoryVelocity.get();

        MotionProfilePoint point = new MotionProfilePoint(getMotionProfileSize() + 1, position, velocity);
        addMotionProfilePoint(point);
    }
    
    public void handlePushMotionProfileTrajectory_2()
    {
        double position = mPushMotionProfileTrajectory2Position.get();
        double velocity = mPushMotionProfileTrajectory2Velocity.get();

        MotionProfilePoint point = new MotionProfilePoint(getMotionProfileSize() + 1, position, velocity);
        addMotionProfilePoint(point);
    }
    
    public void handleSetSelectedSensorPosition()
    {
        reset();
    }
    
    public void handleSetInverted_2()
    {
        int inverted = (int) mInverted_2.get();
        sLOGGER.log(Level.DEBUG, "SetInverted_2 " + inverted);
        setInverted(inverted != 0);
    }
    
    
    
    
    //------------------------------------------------------------
    public void handleGetMotorOutputPercent()
    {
        mMotorOutputPercent.set(get());
    }

    public void handleGetSelectedSensorPosition()
    {
        int slotId = 0;
        mSlottedVariables[slotId].mSelectedSensorPosition.set(getBinnedPosition());
    }

    public void handleGetSelectedSensorVelocity()
    {
        int slotId = 0;
        mSlottedVariables[slotId].mSelectedSensorVelocity.set(getBinnedVelocity());
    }

    public void handleGetClosedLoopError()
    {
        int slotId = 0;
        mSlottedVariables[slotId].mClosedLoopError.set(getLastClosedLoopError());
    }

    public void handleGetMotionProfileStatus()
    {
        m_MotionProfileStatus_topBufferCnt.set(getMotionProfileSize());
    }

    public void handleGetActiveTrajectoryPosition()
    {
        MotionProfilePoint point = getMotionProfilePoint();
        mActiveTrajectoryPosition.set(point == null ? 0 : (int) point.mPosition);
    }

    public void handleGetActiveTrajectoryVelocity()
    {
        MotionProfilePoint point = getMotionProfilePoint();
        mActiveTrajectoryVelocity.set(point == null ? 0 : (int) point.mVelocity);
    }

    public void handleGetQuadraturePosition()
    {
        mQuadraturePosition.set(getBinnedPosition());
    }

    public void handleGetQuadratureVelocity()
    {
        mQuadratureVelocity.set(getBinnedVelocity());
    }
}
