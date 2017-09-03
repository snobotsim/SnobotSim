

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
//		SensorActuatorRegistry::Get().Register(port,
//				std::shared_ptr < RelayWrapper
//						> (new RelayWrapper(port)));
	}
	else
	{
        SNOBOT_LOG(SnobotLogging::WARN, "Unknown name " << nameStr);
	}
}


void SnobotSim::InitializeRelayCallbacks()
{
	for(int i = 0; i < HAL_GetNumRelayHeaders(); ++i)
	{
		HALSIM_RegisterRelayInitializedForwardCallback(i, &RelayCallback, new int(i), false);
		HALSIM_RegisterRelayInitializedReverseCallback(i, &RelayCallback, new int(i), false);
	}
}
