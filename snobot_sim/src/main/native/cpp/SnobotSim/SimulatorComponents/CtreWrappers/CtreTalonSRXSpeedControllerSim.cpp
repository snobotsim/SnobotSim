
#include "SnobotSim/SimulatorComponents/CtreWrappers/CtreTalonSRXSpeedControllerSim.h"

#include "SnobotSim/Logging/SnobotLogger.h"
#include "SnobotSim/ModuleWrapper/Factories/FactoryContainer.h"
#include "SnobotSim/SensorActuatorRegistry.h"
#include "SnobotSim/SimulatorComponents/SmartSC/CanIdOffset.h"

#include <iostream>

const std::string CtreTalonSRXSpeedControllerSim::TYPE = "com.snobot.simulator.simulator_components.ctre.CtreTalonSrxSpeedControllerSim";

CtreTalonSRXSpeedControllerSim::CtreTalonSRXSpeedControllerSim(int aCanHandle) :
        BaseCanSmartSpeedController(Type::CTRE, aCanHandle, "CTRE", 2)
{
    std::string deviceNum = std::to_string(aCanHandle - CAN_SC_OFFSET);
    std::string deviceName = "CTRE Motor Controller " + deviceNum + "[" + deviceNum + "]";
    HAL_SimDeviceHandle mDeviceSimHandle{ 0 };
    frc::sim::SimDeviceSim::EnumerateDevices(
            deviceName.c_str(), [&](const char* name, HAL_SimDeviceHandle handle) {
                if (wpi::StringRef(name) == deviceName)
                {
                    mDeviceSimHandle = handle;
                }
            });

    if(mDeviceSimHandle)
    {
        mDemand_mode       = HALSIM_GetSimValueHandle(mDeviceSimHandle, "mDemand_mode");
        mDemand_demand0    = HALSIM_GetSimValueHandle(mDeviceSimHandle, "mDemand_demand0");
        mDemand_demand1    = HALSIM_GetSimValueHandle(mDeviceSimHandle, "mDemand_demand1");
        m_Set4_mode        = HALSIM_GetSimValueHandle(mDeviceSimHandle, "m_Set4_mode");
        m_Set4_demand0     = HALSIM_GetSimValueHandle(mDeviceSimHandle, "m_Set4_demand0");
        m_Set4_demand1     = HALSIM_GetSimValueHandle(mDeviceSimHandle, "m_Set4_demand1");
        m_Set4_demand1Type = HALSIM_GetSimValueHandle(mDeviceSimHandle, "m_Set4_demand1Type");
        mInverted_invert   = HALSIM_GetSimValueHandle(mDeviceSimHandle, "mInverted_invert");
        m_kP_value         = HALSIM_GetSimValueHandle(mDeviceSimHandle, "m_kP_value");
        m_kI_value         = HALSIM_GetSimValueHandle(mDeviceSimHandle, "m_kI_value");
        m_kD_value         = HALSIM_GetSimValueHandle(mDeviceSimHandle, "m_kD_value");
        m_kF_value         = HALSIM_GetSimValueHandle(mDeviceSimHandle, "m_kF_value");
        m_motor_percentage = HALSIM_GetSimValueHandle(mDeviceSimHandle, "m_motor_precentage");
    }

    std::cout << "GETTING HANDLE " << mDeviceSimHandle << "(" << deviceName << ")" << m_Set4_demand0 << " --- xxffjd " << HALSIM_GetSimValueHandle(mDeviceSimHandle, "m_Set4_demanfd0") << std::endl;
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

void CtreTalonSRXSpeedControllerSim::RefreshSettings()
{
    std::cout << "Refreshing settings...." << std::endl;

    // Control mode
    {
        int mode = static_cast<int>(m_Set4_mode.Get());
        double demand0 = m_Set4_demand0.Get();

        std::cout << "  " << mode << ", " << demand0 << std::endl;
    
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
            // int followerPort = (static_cast<int>(demand0)) & 0xFF;
            // auto leadTalon = getMotorControllerWrapper(followerPort);
            // leadTalon->addFollower(wrapper);
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
}

void CtreTalonSRXSpeedControllerSim::RefreshOutputs()
{
    std::cout << "Writing settings...." << std::endl;
    std::cout << GetVoltagePercentage() << std::endl;
    m_motor_percentage.Set(GetVoltagePercentage());
}