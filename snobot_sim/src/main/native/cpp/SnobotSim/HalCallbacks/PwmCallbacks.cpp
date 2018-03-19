

#include "SnobotSim/HalCallbacks/PwmCallbacks.h"

#include "MockData/PWMData.h"
#include "SnobotSim/Logging/SnobotLogger.h"
#include "SnobotSim/ModuleWrapper/SpeedControllerWrapper.h"
#include "SnobotSim/SensorActuatorRegistry.h"

void PwmCallback(const char* name, void* param, const struct HAL_Value* value)
{
    std::string nameStr = name;
    int port = *((int*) param);

    if(nameStr == "Initialized")
    {
        SensorActuatorRegistry::Get().Register(port,
                std::shared_ptr < SpeedControllerWrapper
                        > (new SpeedControllerWrapper(port)));
    }
    else if(nameStr == "Speed")
    {
        double speed = value->data.v_double;
        SensorActuatorRegistry::Get().GetSpeedControllerWrapper(port)->SetVoltagePercentage(speed);
    }
    else
    {
        SNOBOT_LOG(SnobotLogging::WARN, "Unknown name " << nameStr);
    }
}

int gPwmArrayIndices[26];

void SnobotSim::InitializePwmCallbacks()
{
    for(int i = 0; i < HAL_GetNumPWMChannels(); ++i)
    {
        gPwmArrayIndices[i] = i;
        HALSIM_RegisterPWMInitializedCallback(i, &PwmCallback, &gPwmArrayIndices[i], false);
        HALSIM_RegisterPWMSpeedCallback(i, &PwmCallback, &gPwmArrayIndices[i], false);
        HALSIM_RegisterPWMRawValueCallback(i, &PwmCallback, &gPwmArrayIndices[i], false);
        HALSIM_RegisterPWMPositionCallback(i, &PwmCallback, &gPwmArrayIndices[i], false);
    }
}

void SnobotSim::ResetPwmCallbacks()
{
    for(int i = 0; i < HAL_GetNumPWMChannels(); ++i)
    {
        HALSIM_ResetPWMData(i);
    }
}
