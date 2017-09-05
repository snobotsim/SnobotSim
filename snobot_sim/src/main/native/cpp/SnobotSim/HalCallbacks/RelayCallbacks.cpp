

#include "MockData/RelayData.h"

#include "SnobotSim/HalCallbacks/RelayCallbacks.h"
#include "SnobotSim/SensorActuatorRegistry.h"
#include "SnobotSim/ModuleWrapper/RelayWrapper.h"
#include "SnobotSim/Logging/SnobotLogger.h"


void RelayCallback(const char* name, void* param, const struct HAL_Value* value)
{
    std::string nameStr = name;
    int port = *((int*) param);

    if(nameStr == "InitializedForward")
    {
        SensorActuatorRegistry::Get().Register(port,
                std::shared_ptr < RelayWrapper
                        > (new RelayWrapper(port)));
    }
    else if(nameStr == "InitializedReverse")
    {
//        SensorActuatorRegistry::Get().Register(port,
//                std::shared_ptr < RelayWrapper
//                        > (new RelayWrapper(port)));
    }
    else if(nameStr == "Forward")
    {
        bool on = value->data.v_boolean;
        SensorActuatorRegistry::Get().GetRelayWrapper(port)->SetRelayForwards(on);
    }
    else if(nameStr == "Reverse")
    {
        bool on = value->data.v_boolean;
        SensorActuatorRegistry::Get().GetRelayWrapper(port)->SetRelayReverse(on);
    }
    else
    {
        SNOBOT_LOG(SnobotLogging::WARN, "Unknown name " << nameStr);
    }
}

int gRelayArrayIndices[20];

void SnobotSim::InitializeRelayCallbacks()
{
    for(int i = 0; i < HAL_GetNumRelayHeaders(); ++i)
    {
        gRelayArrayIndices[i] = i;
        HALSIM_RegisterRelayInitializedForwardCallback(i, &RelayCallback, &gRelayArrayIndices[i], false);
        HALSIM_RegisterRelayInitializedReverseCallback(i, &RelayCallback, &gRelayArrayIndices[i], false);
        HALSIM_RegisterRelayForwardCallback(i, &RelayCallback, &gRelayArrayIndices[i], false);
        HALSIM_RegisterRelayReverseCallback(i, &RelayCallback, &gRelayArrayIndices[i], false);
    }
}

void SnobotSim::ResetRelayCallbacks()
{
    for(int i = 0; i < HAL_GetNumRelayHeaders(); ++i)
    {
        HALSIM_ResetRelayData(i);
    }
}
