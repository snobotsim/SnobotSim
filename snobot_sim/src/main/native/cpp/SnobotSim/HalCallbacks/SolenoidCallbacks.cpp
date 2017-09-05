

#include "MockData/PCMData.h"

#include "SnobotSim/HalCallbacks/SolenoidCallbacks.h"
#include "SnobotSim/SensorActuatorRegistry.h"
#include "SnobotSim/ModuleWrapper/SolenoidWrapper.h"
#include "SnobotSim/Logging/SnobotLogger.h"


void SolenoidCallback(const char* name, void* param, const struct HAL_Value* value)
{
    std::string nameStr = name;
    int port = *((int*) param);

    if(nameStr == "SolenoidInitialized")
    {
        SensorActuatorRegistry::Get().Register(port,
                std::shared_ptr < SolenoidWrapper
                        > (new SolenoidWrapper(port)));
    }
    else if(nameStr == "SolenoidOutput")
    {
        bool on = value->data.v_boolean;
        SensorActuatorRegistry::Get().GetSolenoidWrapper(port)->SetState(on);
    }
    else
    {
        SNOBOT_LOG(SnobotLogging::WARN, "Unknown name " << nameStr);
    }
}

int gSolenoidArrayIndices[20];

void SnobotSim::InitializeSolenoidCallbacks()
{
    for(int i = 0; i < HAL_GetNumSolenoidChannels(); ++i)
    {
        gSolenoidArrayIndices[i] = i;
        HALSIM_RegisterPCMSolenoidInitializedCallback(0, i, &SolenoidCallback, &gSolenoidArrayIndices[i], false);
        HALSIM_RegisterPCMSolenoidOutputCallback(0, i, &SolenoidCallback, &gSolenoidArrayIndices[i], false);
    }
}

void SnobotSim::ResetSolenoidCallbacks()
{
    for(int i = 0; i < HAL_GetNumSolenoidChannels(); ++i)
    {
        HALSIM_ResetPCMData(0);
    }
}
