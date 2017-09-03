

#include "MockData/PWMData.h"

#include "SnobotSim/HalCallbacks/PwmCallbacks.h"
#include "SnobotSim/SensorActuatorRegistry.h"
#include "SnobotSim/ModuleWrapper/SpeedControllerWrapper.h"
#include "SnobotSim/Logging/SnobotLogger.h"


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
	else
	{
        SNOBOT_LOG(SnobotLogging::WARN, "Unknown name " << nameStr);
	}
}


void SnobotSim::InitializePwmCallbacks()
{
	for(int i = 0; i < HAL_GetNumPWMChannels(); ++i)
	{
		HALSIM_RegisterPWMInitializedCallback(i, &PwmCallback, new int(i), false);
	}
}
