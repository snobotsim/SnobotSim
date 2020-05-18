
#include "SnobotSim/SimulatorComponents/RevWrappers/RevManager.h"

#include <cstring>

#include "SnobotSim/GetSensorActuatorHelper.h"
#include "SnobotSim/Logging/SnobotLogger.h"
#include "SnobotSim/ModuleWrapper/Factories/FactoryContainer.h"
#include "SnobotSim/SimulatorComponents/RevWrappers/RevSpeedControllerSimWrapper.h"
#include "SnobotSim/SimulatorComponents/SmartSC/CanIdOffset.h"
#include "SnobotSim/SimulatorComponents/SmartSC/UnpackingUtils.h"

using namespace SnobotSim;

namespace
{

std::shared_ptr<RevpeedControllerSim> getMotorControllerWrapper(int aCanPort)
{
    auto rawWrapper = SensorActuatorRegistry::Get().GetISpeedControllerWrapper(aCanPort + CAN_SC_OFFSET, true);
    auto wrapper = std::dynamic_pointer_cast<RevpeedControllerSim>(rawWrapper);
    if (!wrapper)
    {
        SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Wrapper not found for port " << aCanPort);
    }
    return wrapper;
    // return (CtreTalonSrxSpeedControllerSim) SensorActuatorRegistry.get().getSpeedControllers().get(aCanPort + CtreTalonSrxSpeedControllerSim.sCAN_SC_OFFSET);
}

void createSim(int aCanPort)
{
    int simPort = aCanPort + CAN_SC_OFFSET;

    auto wrapper = SensorActuatorRegistry::Get().GetISpeedControllerWrapper(aCanPort, false);
    if (!wrapper)
    {
        SNOBOT_LOG(SnobotLogging::LOG_LEVEL_WARN, "Rev Motor Controller is being created dynamically instead of in the config file for port " << aCanPort);

        FactoryContainer::Get().GetSpeedControllerFactory()->Create(simPort, RevpeedControllerSim::TYPE);
        // SensorActuatorRegistry::Get().Register(simPort, std::shared_ptr<ISpeedControllerWrapper>(new CtreTalonSRXSpeedControllerSim(aCanPort)));
    }
    else if (!std::dynamic_pointer_cast<RevpeedControllerSim>(wrapper))
    {
        SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Wrong motor type set up on " << aCanPort);
        return;
    }

    SensorActuatorRegistry::Get().GetISpeedControllerWrapper(simPort, true)->SetInitialized(true);
}

} // namespace


RevManager::RevManager()
{
    mMotorControllerNormalCallbacks["SetpointCommand"] = [](std::shared_ptr<RevpeedControllerSim> wrapper) { wrapper->handleSetSetpointCommand(); };
    mMotorControllerNormalCallbacks["SetSensorType"] = [](std::shared_ptr<RevpeedControllerSim> wrapper) { wrapper->handleSetSensorType(); };
    mMotorControllerNormalCallbacks["SetFeedbackDevice"] = [](std::shared_ptr<RevpeedControllerSim> wrapper) { wrapper->handleSetFeedbackDevice(); };
    mMotorControllerNormalCallbacks["GetAppliedOutput"] = [](std::shared_ptr<RevpeedControllerSim> wrapper) { wrapper->handleGetAppliedOutput(); };
    mMotorControllerNormalCallbacks["GetEncoderPosition"] = [](std::shared_ptr<RevpeedControllerSim> wrapper) { wrapper->handleGetEncoderPosition(); };
    mMotorControllerNormalCallbacks["GetEncoderVelocity"] = [](std::shared_ptr<RevpeedControllerSim> wrapper) { wrapper->handleGetEncoderVelocity(); };

    mMotorControllerSlottedCallbacks["SetP"] = [](std::shared_ptr<RevpeedControllerSim> wrapper, int slot) { wrapper->handleSetPGain(slot); };
    mMotorControllerSlottedCallbacks["SetI"] = [](std::shared_ptr<RevpeedControllerSim> wrapper, int slot) { wrapper->handleSetIGain(slot); };
    mMotorControllerSlottedCallbacks["SetD"] = [](std::shared_ptr<RevpeedControllerSim> wrapper, int slot) { wrapper->handleSetDGain(slot); };
    mMotorControllerSlottedCallbacks["SetFF"] = [](std::shared_ptr<RevpeedControllerSim> wrapper, int slot) { wrapper->handleSetFFGain(slot); };
}

void RevManager::Reset()
{
}

void RevManager::handleMessage(const std::string& aCallback, int aCanPort, uint8_t* aBuffer, int aLength)
{
    SNOBOT_LOG(SnobotLogging::LOG_LEVEL_INFO, "Getting Motor Controller Message " << aCallback << " on port " << aCanPort << "(" << aLength << " bytes)");

    if ("Create" == aCallback)
    {
        createSim(aCanPort);
    }
    else if ("SetFollow" == aCallback)
    {
        auto [followerID] = ExtractData<int>(aBuffer, aLength);
        int leadId = followerID & 0x3F;

        auto leadWrapper = getMotorControllerWrapper(leadId);
        auto follower = getMotorControllerWrapper(aCanPort);

        SNOBOT_LOG(SnobotLogging::LOG_LEVEL_INFO, "Setting SparkMax " << aCanPort << " to follow " << leadId);

        leadWrapper->addFollower(follower);
    }
    else if (mMotorControllerNormalCallbacks.find(aCallback) != mMotorControllerNormalCallbacks.end())
    {
        auto wrapper = getMotorControllerWrapper(aCanPort);
        mMotorControllerNormalCallbacks[aCallback](wrapper);
    }
    else if (mMotorControllerSlottedCallbacks.find(aCallback) != mMotorControllerSlottedCallbacks.end())
    {
        auto wrapper = getMotorControllerWrapper(aCanPort);
        auto [slot, value] = ExtractData<int, double>(aBuffer, aLength);

        mMotorControllerSlottedCallbacks[aCallback](wrapper, slot);
    }
    else
    {
        SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Unknown motor callback: " << aCallback);
    }
}
