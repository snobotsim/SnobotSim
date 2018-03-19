
#include "SnobotSim/HalCallbacks/DigitalIOCallbacks.h"

#include "MockData/DIOData.h"
#include "SnobotSim/Logging/SnobotLogger.h"
#include "SnobotSim/ModuleWrapper/DigitalSourceWrapper.h"
#include "SnobotSim/SensorActuatorRegistry.h"

void DigitalIOCallback(const char* name, void* param, const struct HAL_Value* value)
{
    std::string nameStr = name;
    int port = *reinterpret_cast<int*>(param);

    if (nameStr == "Initialized")
    {
        SensorActuatorRegistry::Get().Register(port,
                std::shared_ptr<DigitalSourceWrapper>(new DigitalSourceWrapper(port)));
    }
    else if (nameStr == "Value")
    {
        bool state = value->data.v_boolean;
        SensorActuatorRegistry::Get().GetDigitalSourceWrapper(port)->Set(state);
    }
    else
    {
        SNOBOT_LOG(SnobotLogging::WARN, "Unknown name " << nameStr);
    }
}

int gDigitalInArrayIndices[26];
int gDigitalOutArrayIndices[26];

void SnobotSim::InitializeDigitalIOCallbacks()
{
    for (int i = 0; i < HAL_GetNumDigitalHeaders(); ++i)
    {
        gDigitalInArrayIndices[i] = i;
        HALSIM_RegisterDIOInitializedCallback(i, &DigitalIOCallback, &gDigitalInArrayIndices[i], false);
        HALSIM_RegisterDIOValueCallback(i, &DigitalIOCallback, &gDigitalInArrayIndices[i], false);
    }
}

void SnobotSim::ResetDigitalIOCallbacks()
{
    for (int i = 0; i < HAL_GetNumDigitalHeaders(); ++i)
    {
        HALSIM_ResetDIOData(i);
    }
}
