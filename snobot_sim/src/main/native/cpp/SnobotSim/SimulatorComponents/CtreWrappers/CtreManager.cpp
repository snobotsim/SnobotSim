
#include "SnobotSim/SimulatorComponents/CtreWrappers/CtreManager.h"

#include <cstring>

#include "SnobotSim/GetSensorActuatorHelper.h"
#include "SnobotSim/Logging/SnobotLogger.h"
#include "SnobotSim/ModuleWrapper/Factories/FactoryContainer.h"
#include "SnobotSim/SimulatorComponents/CtreWrappers/CtrePigeonImuSim.h"
#include "SnobotSim/SimulatorComponents/CtreWrappers/CtreTalonSRXSpeedControllerSim.h"
#include "SnobotSim/SimulatorComponents/SmartSC/CanIdOffset.h"
#include "SnobotSim/SimulatorComponents/SmartSC/UnpackingUtils.h"

using namespace SnobotSim;

CtreManager::CtreManager()
{
    mMotorControllerNormalCallbacks["Set_4"] = [](std::shared_ptr<CtreTalonSRXSpeedControllerSim> wrapper) { wrapper->handleSet4(); };
    mMotorControllerNormalCallbacks["ConfigSelectedFeedbackSensor"] = [](std::shared_ptr<CtreTalonSRXSpeedControllerSim> wrapper) { wrapper->handleConfigSelectedFeedbackSensor(); };
    mMotorControllerNormalCallbacks["ConfigMotionCruiseVelocity"] = [](std::shared_ptr<CtreTalonSRXSpeedControllerSim> wrapper) { wrapper->handleSetMotionCruiseVelocity(); };
    mMotorControllerNormalCallbacks["ConfigMotionAcceleration"] = [](std::shared_ptr<CtreTalonSRXSpeedControllerSim> wrapper) { wrapper->handleSetMotionAcceleration(); };
    mMotorControllerNormalCallbacks["PushMotionProfileTrajectory"] = [](std::shared_ptr<CtreTalonSRXSpeedControllerSim> wrapper) { wrapper->handlePushMotionProfileTrajectory(); };
    mMotorControllerNormalCallbacks["PushMotionProfileTrajectory_2"] = [](std::shared_ptr<CtreTalonSRXSpeedControllerSim> wrapper) { wrapper->handlePushMotionProfileTrajectory_2(); };
    mMotorControllerNormalCallbacks["SetSelectedSensorPosition"] = [](std::shared_ptr<CtreTalonSRXSpeedControllerSim> wrapper) { wrapper->handleSetSelectedSensorPosition(); };
    mMotorControllerNormalCallbacks["SetInverted_2"] = [](std::shared_ptr<CtreTalonSRXSpeedControllerSim> wrapper) { wrapper->handleSetInverted_2(); };

    mMotorControllerNormalCallbacks["GetMotorOutputPercent"] = [](std::shared_ptr<CtreTalonSRXSpeedControllerSim> wrapper) { wrapper->handleGetMotorOutputPercent(); };
    mMotorControllerNormalCallbacks["GetSelectedSensorPosition"] = [](std::shared_ptr<CtreTalonSRXSpeedControllerSim> wrapper) { wrapper->handleGetSelectedSensorPosition(); };
    mMotorControllerNormalCallbacks["GetSelectedSensorVelocity"] = [](std::shared_ptr<CtreTalonSRXSpeedControllerSim> wrapper) { wrapper->handleGetSelectedSensorVelocity(); };
    mMotorControllerNormalCallbacks["GetClosedLoopError"] = [](std::shared_ptr<CtreTalonSRXSpeedControllerSim> wrapper) { wrapper->handleGetClosedLoopError(); };
    mMotorControllerNormalCallbacks["GetMotionProfileStatus"] = [](std::shared_ptr<CtreTalonSRXSpeedControllerSim> wrapper) { wrapper->handleGetMotionProfileStatus(); };
    mMotorControllerNormalCallbacks["GetActiveTrajectoryPosition"] = [](std::shared_ptr<CtreTalonSRXSpeedControllerSim> wrapper) { wrapper->handleGetActiveTrajectoryPosition(); };
    mMotorControllerNormalCallbacks["GetActiveTrajectoryVelocity"] = [](std::shared_ptr<CtreTalonSRXSpeedControllerSim> wrapper) { wrapper->handleGetActiveTrajectoryVelocity(); };
    mMotorControllerNormalCallbacks["GetQuadraturePosition"] = [](std::shared_ptr<CtreTalonSRXSpeedControllerSim> wrapper) { wrapper->handleGetQuadraturePosition(); };
    mMotorControllerNormalCallbacks["GetQuadratureVelocity"] = [](std::shared_ptr<CtreTalonSRXSpeedControllerSim> wrapper) { wrapper->handleGetQuadratureVelocity(); };

    mMotorControllerSlottedCallbacks["Config_kP"] = [](std::shared_ptr<CtreTalonSRXSpeedControllerSim> wrapper, int slot) { wrapper->handleSetKP(slot); };
    mMotorControllerSlottedCallbacks["Config_kI"] = [](std::shared_ptr<CtreTalonSRXSpeedControllerSim> wrapper, int slot) { wrapper->handleSetKI(slot); };
    mMotorControllerSlottedCallbacks["Config_kD"] = [](std::shared_ptr<CtreTalonSRXSpeedControllerSim> wrapper, int slot) { wrapper->handleSetKD(slot); };
    mMotorControllerSlottedCallbacks["Config_kF"] = [](std::shared_ptr<CtreTalonSRXSpeedControllerSim> wrapper, int slot) { wrapper->handleSetKF(slot); };
    mMotorControllerSlottedCallbacks["Config_IntegralZone"] = [](std::shared_ptr<CtreTalonSRXSpeedControllerSim> wrapper, int slot) { wrapper->handleSetIntegralZone(slot); };

    mPigeonNormalCallbacks["SetYaw"] = [](std::shared_ptr<CtrePigeonImuSim> wrapper) { wrapper->handleSetYaw(); };
    mPigeonNormalCallbacks["GetRawGyro"] = [](std::shared_ptr<CtrePigeonImuSim> wrapper) { wrapper->handleGetRawGyro(); };
    mPigeonNormalCallbacks["GetYawPitchRoll"] = [](std::shared_ptr<CtrePigeonImuSim> wrapper) { wrapper->handleGetYawPitchRoll(); };
    mPigeonNormalCallbacks["GetFusedHeading"] = [](std::shared_ptr<CtrePigeonImuSim> wrapper) { wrapper->handleGetFusedHeading(); };
    mPigeonNormalCallbacks["GetFusedHeading1"] = [](std::shared_ptr<CtrePigeonImuSim> wrapper) { wrapper->handleGetFusedHeading(); };
}

void CtreManager::Reset()
{
    mPigeonMap.clear();
}

void CtreManager::handleMotorControllerMessage(const std::string& aCallback, int aCanPort, uint8_t* aBuffer, int aLength)
{
    SNOBOT_LOG(SnobotLogging::LOG_LEVEL_DEBUG, "Getting Motor Controller Message " << aCallback << " on port " << aCanPort << "(" << aLength << " bytes)");

    if ("Create" == aCallback)
    {
        createMotorController(aCanPort);
    }
    else if (mMotorControllerNormalCallbacks.find(aCallback) != mMotorControllerNormalCallbacks.end())
    {
        auto wrapper = CtreTalonSRXSpeedControllerSim::getMotorControllerWrapper(aCanPort);
        mMotorControllerNormalCallbacks[aCallback](wrapper);
    }
    else if (mMotorControllerSlottedCallbacks.find(aCallback) != mMotorControllerSlottedCallbacks.end())
    {
        auto wrapper = CtreTalonSRXSpeedControllerSim::getMotorControllerWrapper(aCanPort);
        auto [slot, value] = ExtractData<int, double>(aBuffer, aLength);

        mMotorControllerSlottedCallbacks[aCallback](wrapper, slot);
    }
    else
    {
        SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Unknown motor callback: " << aCallback);
    }
}

void CtreManager::handlePigeonMessage(const std::string& aCallback, int aCanPort, uint8_t* aBuffer, int aLength)
{
    SNOBOT_LOG(SnobotLogging::LOG_LEVEL_DEBUG, "Getting Pigeon Message " << aCallback << " on port " << aCanPort << "(" << aLength << " bytes)");

    if ("Create" == aCallback)
    {
        if (mPigeonMap.find(aCanPort) == mPigeonMap.end())
        {
            SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "CTRE Pigeon is being created dynamically instead of in the config file for port " << aCanPort);
            createPigeon(aCanPort);
        }

        mPigeonMap[aCanPort]->SetInitialized(true);
    }
    else if (mPigeonNormalCallbacks.find(aCallback) != mPigeonNormalCallbacks.end())
    {
        auto wrapper = getPigeonWrapper(aCanPort);
        mPigeonNormalCallbacks[aCallback](wrapper);
    }
    else
    {
        SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Unknown pigeon callback: " << aCallback);
    }
}

void CtreManager::handleCanifierMessage(const std::string& aCallback, int aCanPort, uint8_t* aBuffer, int aLength)
{
    SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Getting Canifier Message " << aCallback << " on port " << aCanPort << "(" << aLength << " bytes)");
}

void CtreManager::handleCanCoderMessage(const std::string& aCallback, int aCanPort, uint8_t* aBuffer, int aLength)
{
    SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Getting CanCoder Message " << aCallback << " on port " << aCanPort << "(" << aLength << " bytes)");
}

void CtreManager::handleBuffTrajPointStreamMessage(const std::string& aCallback, int aCanPort, uint8_t* aBuffer, int aLength)
{
    SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Getting Trajectory Point Message " << aCallback << " on port " << aCanPort << "(" << aLength << " bytes)");
}

std::shared_ptr<CtrePigeonImuSim> CtreManager::createPigeon(int aPort)
{
    if (mPigeonMap.find(aPort) != mPigeonMap.end())
    {
        SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Pigeon alrady registered on " << aPort);
    }
    else
    {
        std::shared_ptr<CtrePigeonImuSim> sim(new CtrePigeonImuSim(aPort, CtrePigeonImuSim::CTRE_OFFSET + aPort * 3));
        mPigeonMap[aPort] = sim;
    }

    return mPigeonMap[aPort];
}

void CtreManager::createMotorController(int aCanPort)
{
    int simPort = aCanPort + CAN_SC_OFFSET;

    auto wrapper = SensorActuatorRegistry::Get().GetISpeedControllerWrapper(aCanPort, false);
    if (!wrapper)
    {
        SNOBOT_LOG(SnobotLogging::LOG_LEVEL_WARN, "CTRE Motor Controller is being created dynamically instead of in the config file for port " << aCanPort);

        FactoryContainer::Get().GetSpeedControllerFactory()->Create(simPort, CtreTalonSRXSpeedControllerSim::TYPE);
        // SensorActuatorRegistry::Get().Register(simPort, std::shared_ptr<ISpeedControllerWrapper>(new CtreTalonSRXSpeedControllerSim(aCanPort)));
    }
    else if (!std::dynamic_pointer_cast<CtreTalonSRXSpeedControllerSim>(wrapper))
    {
        SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Wrong motor type set up on " << aCanPort);
        return;
    }

    SensorActuatorRegistry::Get().GetISpeedControllerWrapper(simPort, true)->SetInitialized(true);
}

std::shared_ptr<CtrePigeonImuSim> CtreManager::getPigeonWrapper(int aCanPort)
{
    if (mPigeonMap.find(aCanPort) == mPigeonMap.end())
    {
        SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "No pigeon on " << aCanPort);
        StackTraceHelper::PrintStackTracker();
    }
    return mPigeonMap[aCanPort];
}
