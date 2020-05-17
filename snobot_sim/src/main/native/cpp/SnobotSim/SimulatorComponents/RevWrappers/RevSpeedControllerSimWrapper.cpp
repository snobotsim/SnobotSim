

#include "SnobotSim/SimulatorComponents/RevWrappers/RevSpeedControllerSimWrapper.h"

#include <iostream>

#include "SnobotSim/Logging/SnobotLogger.h"
#include "SnobotSim/SimulatorComponents/SmartSC/CanIdOffset.h"

const std::string RevpeedControllerSim::TYPE = "com.snobot.simulator.simulator_components.ctre.RevpeedControllerSim";

RevpeedControllerSim::RevpeedControllerSim(int aCanHandle) :
        BaseCanSmartSpeedController(aCanHandle, "Rev", 2)
{
    std::string deviceNum = std::to_string(aCanHandle - CAN_SC_OFFSET);
    std::string deviceName = "RevCANSparkMaxDriverWrapper " + deviceNum + "[" + deviceNum + "]";
    HAL_SimDeviceHandle mDeviceSimHandle{ 0 };
    frc::sim::SimDeviceSim::EnumerateDevices(
            deviceName.c_str(), [&](const char* name, HAL_SimDeviceHandle handle) {
                if (wpi::StringRef(name) == deviceName)
                {
                    mDeviceSimHandle = handle;
                }
            });

    if (mDeviceSimHandle)
    {
        mSetpointCommandCtrl = HALSIM_GetSimValueHandle(mDeviceSimHandle, "pointCommand_ctrl");
        mSetpointCommandValue = HALSIM_GetSimValueHandle(mDeviceSimHandle, "pointCommand_value");
        mAppliedOutputAppliedOutput = HALSIM_GetSimValueHandle(mDeviceSimHandle, "AppliedOutput_appliedOutput");
        mSensorTypeSensorType = HALSIM_GetSimValueHandle(mDeviceSimHandle, "SensorType_sensorType");
        mFeedbackDeviceSensorID = HALSIM_GetSimValueHandle(mDeviceSimHandle, "FeedbackDevice_sensorID");
        mEncoderPosition = HALSIM_GetSimValueHandle(mDeviceSimHandle, "EncoderPosition_position");
        mEncoderVelocity = HALSIM_GetSimValueHandle(mDeviceSimHandle, "EncoderVelocity_velocity");

        for (int i = 0; i < NUM_SLOTS; ++i)
        {
            mSlottedVariables[i].m_P_gain = HALSIM_GetSimValueHandle(mDeviceSimHandle, std::string("P_gain[" + std::to_string(i) + "]").c_str());
            mSlottedVariables[i].m_I_gain = HALSIM_GetSimValueHandle(mDeviceSimHandle, std::string("I_gain[" + std::to_string(i) + "]").c_str());
            mSlottedVariables[i].m_D_gain = HALSIM_GetSimValueHandle(mDeviceSimHandle, std::string("D_gain[" + std::to_string(i) + "]").c_str());
            mSlottedVariables[i].m_FF_gain = HALSIM_GetSimValueHandle(mDeviceSimHandle, std::string("FF_gain[" + std::to_string(i) + "]").c_str());
        }
    }
    else
    {
        SNOBOT_LOG(SnobotLogging::LOG_LEVEL_WARN, "This isn't going to work ");
    }
}

double RevpeedControllerSim::calculateMotionProfileOutput(double aCurrentPosition, double aCurrentVelocity, int aModeType)
{
    return 0;
}

void RevpeedControllerSim::setCanFeedbackDevice(int aFeedbackDevice)
{
    FeedbackDevice newDevice = FeedbackDevice::None;
    if (aFeedbackDevice == 1 || aFeedbackDevice == 2)
    {
        newDevice = FeedbackDevice::Encoder;
    }
    else
    {
        SNOBOT_LOG(SnobotLogging::LOG_LEVEL_WARN, "Unsupported feedback device " << static_cast<int>(aFeedbackDevice));
    }

    BaseCanSmartSpeedController::setCanFeedbackDevice(newDevice);
}

void RevpeedControllerSim::handleSetSetpointCommand()
{
    float value = static_cast<float>(mSetpointCommandValue.Get());
    int ctrl = static_cast<int>(mSetpointCommandCtrl.Get());

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
        //        SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Unknown demand mode " << ctrl);
        break;
    }
}

void RevpeedControllerSim::handleSetSensorType()
{
    int type = static_cast<int>(mSensorTypeSensorType.Get());
    setCanFeedbackDevice(type);
}

void RevpeedControllerSim::handleSetFeedbackDevice()
{
    int type = static_cast<int>(mFeedbackDeviceSensorID.Get());
    setCanFeedbackDevice(type);
}

void RevpeedControllerSim::handleSetPGain(int slot)
{
    double value = mSlottedVariables[slot].m_P_gain.Get();
    setPGain(slot, value);
}

void RevpeedControllerSim::handleSetIGain(int slot)
{
    double value = mSlottedVariables[slot].m_I_gain.Get();
    setIGain(slot, value);
}

void RevpeedControllerSim::handleSetDGain(int slot)
{
    double value = mSlottedVariables[slot].m_D_gain.Get();
    setDGain(slot, value);
}

void RevpeedControllerSim::handleSetFFGain(int slot)
{
    double value = mSlottedVariables[slot].m_FF_gain.Get();
    setFGain(slot, value);
}

void RevpeedControllerSim::handleGetAppliedOutput()
{
    mAppliedOutputAppliedOutput.Set(GetVoltagePercentage());
}

void RevpeedControllerSim::handleGetEncoderPosition()
{
    mEncoderPosition.Set(GetPosition());
}

void RevpeedControllerSim::handleGetEncoderVelocity()
{
    mEncoderVelocity.Set(GetVelocity());
}
