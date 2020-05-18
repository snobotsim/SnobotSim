
#include "SnobotSim/SimulatorComponents/CtreWrappers/CtreTalonSRXSpeedControllerSim.h"

#include "SnobotSim/Logging/SnobotLogger.h"
#include "SnobotSim/ModuleWrapper/Factories/FactoryContainer.h"
#include "SnobotSim/SensorActuatorRegistry.h"
#include "SnobotSim/SimulatorComponents/SmartSC/CanIdOffset.h"
#include <iostream>

const std::string CtreTalonSRXSpeedControllerSim::TYPE = "com.snobot.simulator.simulator_components.ctre.CtreTalonSrxSpeedControllerSim";

namespace
{
hal::SimDouble getSimDouble(HAL_SimDeviceHandle aDeviceHandle, std::string aName)
{
    hal::SimDouble output = HALSIM_GetSimValueHandle(aDeviceHandle, aName.c_str());
    if (!output)
    {
//        throw new IllegalArgumentException("Sim device '" + aName + "' does not exist");
        SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Sim device '" << aName << "' does not exist");
    }

    return output;
}
}

std::shared_ptr<CtreTalonSRXSpeedControllerSim> CtreTalonSRXSpeedControllerSim::getMotorControllerWrapper(int aCanPort)
{
    auto rawWrapper = SensorActuatorRegistry::Get().GetISpeedControllerWrapper(aCanPort + CAN_SC_OFFSET, true);
    auto wrapper = std::dynamic_pointer_cast<CtreTalonSRXSpeedControllerSim>(rawWrapper);
    if (!wrapper)
    {
        SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Wrapper not found for port " << aCanPort);
    }
    return wrapper;
    // return (CtreTalonSrxSpeedControllerSim) SensorActuatorRegistry.get().getSpeedControllers().get(aCanPort + CtreTalonSrxSpeedControllerSim.sCAN_SC_OFFSET);
}

CtreTalonSRXSpeedControllerSim::CtreTalonSRXSpeedControllerSim(int aCanHandle) :
        BaseCanSmartSpeedController(aCanHandle, "CTRE", 2)
{
}


void CtreTalonSRXSpeedControllerSim::SetInitialized(bool aIsInitialized)
{
    BaseCanSmartSpeedController::SetInitialized(aIsInitialized);

    std::string deviceNum = std::to_string(mCanHandle);
    std::string deviceName = "CtreMotControllerWrapper " + deviceNum + "[" + deviceNum + "]";
    std::cout << "XXXX " << deviceName << std::endl;
    HAL_SimDeviceHandle deviceSimHandle{ 0 };
    frc::sim::SimDeviceSim::EnumerateDevices(
            deviceName.c_str(), [&](const char* name, HAL_SimDeviceHandle handle) {
                if (wpi::StringRef(name) == deviceName)
                {
                    deviceSimHandle = handle;
                }
            });

    if (deviceSimHandle)
    {
        mDemandDemand0 = getSimDouble(deviceSimHandle, "Demand_demand0");
        mDemandDemand1 = getSimDouble(deviceSimHandle, "Demand_demand1");
        mDemandMode = getSimDouble(deviceSimHandle, "Demand_mode");
        mSet4Demand0 = getSimDouble(deviceSimHandle, "_4_demand0");
        mSet4Demand1 = getSimDouble(deviceSimHandle, "_4_demand1");
        mSet4Demand1Type = getSimDouble(deviceSimHandle, "_4_demand1Type");
        mSet4Mode = getSimDouble(deviceSimHandle, "_4_mode");
        mMotorOutputPercent = getSimDouble(deviceSimHandle, "MotorOutputPercent_percentOutput");
        mConfigMotionAccelerationSensorUnitsPer100msPerSec = getSimDouble(deviceSimHandle, "ConfigMotionAcceleration_sensorUnitsPer100msPerSec");
        mConfigMotionCruiseVelocitySensorUnitsPer100ms = getSimDouble(deviceSimHandle, "ConfigMotionCruiseVelocity_sensorUnitsPer100ms");


        mMotionProfileStatusTopBufferCnt = getSimDouble(deviceSimHandle, "MotionProfileStatus_topBufferCnt");
        mPushMotionProfileTrajectoryPosition = getSimDouble(deviceSimHandle, "PushMotionProfileTrajectory_position");
        mPushMotionProfileTrajectoryVelocity = getSimDouble(deviceSimHandle, "PushMotionProfileTrajectory_velocity");
        mPushMotionProfileTrajectory2Position = getSimDouble(deviceSimHandle, "PushMotionProfileTrajectory_2_position");
        mPushMotionProfileTrajectory2Velocity = getSimDouble(deviceSimHandle, "PushMotionProfileTrajectory_2_velocity");
        mInverted2 = getSimDouble(deviceSimHandle, "Inverted_2_invertType");
        mQuadratureVelocity = getSimDouble(deviceSimHandle, "QuadratureVelocity_param");
        mQuadraturePosition = getSimDouble(deviceSimHandle, "QuadraturePosition_param");
        mActiveTrajectoryPosition = getSimDouble(deviceSimHandle, "ActiveTrajectoryPosition_param");
        mActiveTrajectoryVelocity = getSimDouble(deviceSimHandle, "ActiveTrajectoryVelocity_param");

        for (int i = 0; i < NUM_SLOTS; ++i)
        {
            mSlottedVariables[i].mPGain = getSimDouble(deviceSimHandle, std::string("Config_kP_value[" + std::to_string(i) + "]").c_str());
            mSlottedVariables[i].mIGain = getSimDouble(deviceSimHandle, std::string("Config_kI_value[" + std::to_string(i) + "]").c_str());
            mSlottedVariables[i].mDGain = getSimDouble(deviceSimHandle, std::string("Config_kD_value[" + std::to_string(i) + "]").c_str());
            mSlottedVariables[i].mFFGain = getSimDouble(deviceSimHandle, std::string("Config_kF_value[" + std::to_string(i) + "]").c_str());
            mSlottedVariables[i].mIZone = getSimDouble(deviceSimHandle, std::string("Config_IntegralZone_izone[" + std::to_string(i) + "]").c_str());
            mSlottedVariables[i].mSelectedSensorPosition = getSimDouble(deviceSimHandle, std::string("SelectedSensorPosition_param[" + std::to_string(i) + "]").c_str());
            mSlottedVariables[i].mSelectedSensorVelocity = getSimDouble(deviceSimHandle, std::string("SelectedSensorVelocity_param[" + std::to_string(i) + "]").c_str());
            mSlottedVariables[i].mClosedLoopError = getSimDouble(deviceSimHandle, std::string("ClosedLoopError_closedLoopError[" + std::to_string(i) + "]").c_str());
            mSlottedVariables[i].mConfigSelectedFeedbackSensor = getSimDouble(deviceSimHandle, std::string("ConfigSelectedFeedbackSensor_feedbackDevice[" + std::to_string(i) + "]").c_str());
        }
    }
    else
    {
        SNOBOT_LOG(SnobotLogging::LOG_LEVEL_WARN, "This isn't going to work ");
    }
}

void CtreTalonSRXSpeedControllerSim::setMotionProfilingCommand(double aDemand)
{
    mControlType = ControlType::MotionProfile;
    mControlGoal = aDemand;
}
double CtreTalonSRXSpeedControllerSim::getLastClosedLoopError()
{
    double multiplier = 1.0;

    switch (mControlType)
    {
    case ControlType::MotionMagic:
    case ControlType::MotionProfile:
    case ControlType::Position:
        multiplier = getPositionUnitConversion();
        break;
    case ControlType::Speed:
        multiplier = getVelocityUnitConversion();
        break;
    default:
        // sLOGGER.log(Level.WARN, "I don't think get closed loop error should be called with " + mControlType);
        break;
    }
    return mLastError * multiplier;
}
void CtreTalonSRXSpeedControllerSim::addMotionProfilePoint(MotionProfilePoint aPoint)
{
    // if (aPoint.mIndex == 0)
    // {
    //     mMotionProfilePoints.clear();
    //     mMotionProfileProcessedCounter = 0;
    //     mMotionProfileCurrentPointIndex = 0;
    // }
    // else
    // {
    //     mMotionProfilePoints.add(aPoint);
    //     mMotionProfileProcessedCounter++;
    // }
}

int CtreTalonSRXSpeedControllerSim::getMotionProfileSize()
{
    return mMotionProfilePoints.size();
}

// MotionProfilePoint CtreTalonSRXSpeedControllerSim::getMotionProfilePoint()
// {
//     if (mMotionProfileProcessedCounter <= mMotionProfilePoints.size() && mMotionProfileProcessedCounter != 0)
//     {
//         return mMotionProfilePoints.get(mMotionProfileProcessedCounter - 1);
//     }
//     return null;
// }

void CtreTalonSRXSpeedControllerSim::setLimitSwitchOverride(bool aOverrideFwdLimitSwitch, bool aOverrideRevLimitSwitch)
{
    // if (aOverrideFwdLimitSwitch && !mLoggedCantOverrideFwdLimitSwitch)
    // {
    //     mLoggedCantOverrideFwdLimitSwitch = true;
    //     sLOGGER.log(Level.WARN, "Cannot override forward limit switches");
    // }

    // if (aOverrideRevLimitSwitch && !mLoggedCantOverrideRevLimitSwitch)
    // {
    //     mLoggedCantOverrideRevLimitSwitch = true;
    //     sLOGGER.log(Level.WARN, "Cannot override reverse limit switch");
    // }
}

void CtreTalonSRXSpeedControllerSim::setCurrentProfile(char aProfileSelect)
{
    mCurrentPidProfile = aProfileSelect;
}

int CtreTalonSRXSpeedControllerSim::getBinnedPosition()
{
    return static_cast<int>(GetPosition() * getPositionUnitConversion());
}
int CtreTalonSRXSpeedControllerSim::getBinnedVelocity()
{
    return static_cast<int>(GetVelocity() * getVelocityUnitConversion());
}
void CtreTalonSRXSpeedControllerSim::setCanFeedbackDevice(char aFeedbackDevice)
{
    FeedbackDevice newDevice = FeedbackDevice::None;
    switch (aFeedbackDevice)
    {
    // Default feedback sensor, handle with care
    case 0:
        newDevice = FeedbackDevice::Encoder;
        break;
    case 2:
        newDevice = FeedbackDevice::Analog;
        break;
    // The Absolute and Relative encoders behave the same
    case 8:
        newDevice = FeedbackDevice::Encoder;
        break;
    default:
        SNOBOT_LOG(SnobotLogging::LOG_LEVEL_WARN, "Unsupported feedback device " << static_cast<int>(aFeedbackDevice));
        break;
    }

    BaseCanSmartSpeedController::setCanFeedbackDevice(newDevice);
}

double CtreTalonSRXSpeedControllerSim::calculateMotionProfileOutput(double aCurrentPosition, double aCurrentVelocity, int aModeType)
{
    return 0;
}


////////////////////////////////////////////////////////////////
void CtreTalonSRXSpeedControllerSim::handleSetDemand()
{
    int mode = (int) mDemandMode.Get();
    int param0 = (int) mDemandDemand0.Get();
    int param1 = (int) mDemandDemand1.Get();
    SNOBOT_LOG(SnobotLogging::LOG_LEVEL_DEBUG, "SetDemand " << mode << ", " << param0 << ", " << param1);

    switch (mode)
    {
    case 0:
        SetVoltagePercentage(param0 / 1023.0);
        break;
    case 1:
        setPositionGoal(param0);
        break;
    case 2:
        setSpeedGoal(param0);
        break;
//    case 5:
//        int followerPort = param0 & 0xFF;
//        CtreTalonSrxSpeedControllerSim leadTalon = getMotorControllerWrapper(followerPort);
//        leadTalon.addFollower(this);
//        break;
    case 6:
        setMotionProfilingCommand(param0);
        break;
    case 7:
        setMotionMagicGoal(param0);
        break;
    case 15:
        SetVoltagePercentage(0);
        break;
    default:
        SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Unknown demand mode " << mode);
        break;
    }
}

void CtreTalonSRXSpeedControllerSim::handleSet4()
{
    int mode = (int) mSet4Mode.Get();
    double demand0 = mSet4Demand0.Get();
    double demand1 = mSet4Demand1.Get();
    int demand1Type = (int) mSet4Demand1Type.Get();
    SNOBOT_LOG(SnobotLogging::LOG_LEVEL_DEBUG, "Setting_4 " << mode << ", " << demand0 << ", " << demand1 << ", " << demand1Type);

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
    {
        int followerPort = ((int) demand0) & 0xFF;
        auto leadTalon = getMotorControllerWrapper(followerPort);
        leadTalon->addFollower(shared_from_this());
        break;
    }
    case 6:
        setMotionProfilingCommand(demand0);
        break;
    case 7:
        setMotionMagicGoal(demand0);
        break;
    case 15:
        SetVoltagePercentage(0);
        break;
    default:
        SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Unknown demand mode " << mode);
        break;
    }
}

void CtreTalonSRXSpeedControllerSim::handleConfigSelectedFeedbackSensor()
{
    int slotId = 0;
    int feedbackDevice = (int) mSlottedVariables[slotId].mConfigSelectedFeedbackSensor.Get();
    setCanFeedbackDevice(feedbackDevice);
}

void CtreTalonSRXSpeedControllerSim::handleSetKP(int aSlot)
{
    double value = mSlottedVariables[aSlot].mPGain.Get();
    setPGain(aSlot, value);
}

void CtreTalonSRXSpeedControllerSim::handleSetKI(int aSlot)
{
    double value = mSlottedVariables[aSlot].mIGain.Get();
    setIGain(aSlot, value);
}

void CtreTalonSRXSpeedControllerSim::handleSetKD(int aSlot)
{
    double value = mSlottedVariables[aSlot].mDGain.Get();
    setDGain(aSlot, value);
}

void CtreTalonSRXSpeedControllerSim::handleSetKF(int aSlot)
{
    double value = mSlottedVariables[aSlot].mFFGain.Get();
    setFGain(aSlot, value);
}

void CtreTalonSRXSpeedControllerSim::handleSetIntegralZone(int aSlot)
{
    double value = mSlottedVariables[aSlot].mIZone.Get();
    setIZone(aSlot, value);
}

void CtreTalonSRXSpeedControllerSim::handleSetMotionCruiseVelocity()
{
    setMotionMagicMaxVelocity((int) mConfigMotionCruiseVelocitySensorUnitsPer100ms.Get());
}

void CtreTalonSRXSpeedControllerSim::handleSetMotionAcceleration()
{
    setMotionMagicMaxAcceleration((int) mConfigMotionAccelerationSensorUnitsPer100msPerSec.Get());
}

void CtreTalonSRXSpeedControllerSim::handlePushMotionProfileTrajectory()
{
    double position = mPushMotionProfileTrajectoryPosition.Get();
    double velocity = mPushMotionProfileTrajectoryVelocity.Get();

    MotionProfilePoint point{getMotionProfileSize() + 1, position, velocity};
    addMotionProfilePoint(point);
}

void CtreTalonSRXSpeedControllerSim::handlePushMotionProfileTrajectory_2()
{
    double position = mPushMotionProfileTrajectory2Position.Get();
    double velocity = mPushMotionProfileTrajectory2Velocity.Get();

    MotionProfilePoint point{getMotionProfileSize() + 1, position, velocity};
    addMotionProfilePoint(point);
}

void CtreTalonSRXSpeedControllerSim::handleSetSelectedSensorPosition()
{
    Reset();
}

void CtreTalonSRXSpeedControllerSim::handleSetInverted_2()
{
    int inverted = (int) mInverted2.Get();
    setInverted(inverted != 0);
}




//------------------------------------------------------------
void CtreTalonSRXSpeedControllerSim::handleGetMotorOutputPercent()
{
    mMotorOutputPercent.Set(GetVoltagePercentage());
}

void CtreTalonSRXSpeedControllerSim::handleGetSelectedSensorPosition()
{
    int slotId = 0;
    mSlottedVariables[slotId].mSelectedSensorPosition.Set(getBinnedPosition());
}

void CtreTalonSRXSpeedControllerSim::handleGetSelectedSensorVelocity()
{
    int slotId = 0;
    mSlottedVariables[slotId].mSelectedSensorVelocity.Set(getBinnedVelocity());
}

void CtreTalonSRXSpeedControllerSim::handleGetClosedLoopError()
{
    int slotId = 0;
    mSlottedVariables[slotId].mClosedLoopError.Set(getLastClosedLoopError());
}

void CtreTalonSRXSpeedControllerSim::handleGetMotionProfileStatus()
{
    mMotionProfileStatusTopBufferCnt.Set(getMotionProfileSize());
}

void CtreTalonSRXSpeedControllerSim::handleGetActiveTrajectoryPosition()
{
    SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Unsupported");
//    MotionProfilePoint point = getMotionProfilePoint();
//    mActiveTrajectoryPosition.Set(point == nullptr ? 0 : (int) point.mPosition);
}

void CtreTalonSRXSpeedControllerSim::handleGetActiveTrajectoryVelocity()
{
    SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Unsupported");
//    MotionProfilePoint point = getMotionProfilePoint();
//    mActiveTrajectoryVelocity.Set(point == nullptr ? 0 : (int) point.mVelocity);
}

void CtreTalonSRXSpeedControllerSim::handleGetQuadraturePosition()
{
    mQuadraturePosition.Set(getBinnedPosition());
}

void CtreTalonSRXSpeedControllerSim::handleGetQuadratureVelocity()
{
    mQuadratureVelocity.Set(getBinnedVelocity());
}