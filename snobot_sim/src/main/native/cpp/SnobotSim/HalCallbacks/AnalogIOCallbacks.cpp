
#include "MockData/AnalogInData.h"
#include "MockData/AnalogOutData.h"

#include "SnobotSim/HalCallbacks/AnalogIOCallbacks.h"
#include "SnobotSim/SensorActuatorRegistry.h"
#include "SnobotSim/ModuleWrapper/AnalogSourceWrapper.h"
#include "SnobotSim/Logging/SnobotLogger.h"


void AnalogIOCallback(const char* name, void* param, const struct HAL_Value* value)
{
    std::string nameStr = name;
    int port = *((int*) param);

    if(nameStr == "Initialized")
    {
        SensorActuatorRegistry::Get().Register(port,
                std::shared_ptr<AnalogSourceWrapper> (new AnalogSourceWrapper(port)));
    }
    else
    {
        SNOBOT_LOG(SnobotLogging::WARN, "Unknown name " << nameStr);
    }
}

int gAnalogInArrayIndices[26];
int gAnalogOutArrayIndices[26];

void SnobotSim::InitializeAnalogIOCallbacks()
{
    for(int i = 0; i < HAL_GetNumAnalogInputs(); ++i)
    {
        gAnalogInArrayIndices[i] = i;
        HALSIM_RegisterAnalogInInitializedCallback(i, &AnalogIOCallback, &gAnalogInArrayIndices[i], false);
    }

    for(int i = 0; i < HAL_GetNumAnalogOutputs(); ++i)
    {
        gAnalogOutArrayIndices[i] = i;
        HALSIM_RegisterAnalogOutInitializedCallback(i, &AnalogIOCallback, &gAnalogOutArrayIndices[i], false);
    }
}

void SnobotSim::ResetAnalogIOCallbacks()
{
    for(int i = 0; i < HAL_GetNumAnalogInputs(); ++i)
    {
        HALSIM_ResetAnalogInData(i);
    }
    for(int i = 0; i < HAL_GetNumAnalogOutputs(); ++i)
    {
        HALSIM_ResetAnalogOutData(i);
    }
}
