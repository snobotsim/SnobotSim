
#include "MockData/EncoderData.h"

#include "SnobotSim/HalCallbacks/EncoderCallbacks.h"
#include "SnobotSim/SensorActuatorRegistry.h"
#include "SnobotSim/ModuleWrapper/EncoderWrapper.h"
#include "SnobotSim/Logging/SnobotLogger.h"


void EncoderCallback(const char* name, void* param, const struct HAL_Value* value)
{
	std::string nameStr = name;
	int port = *((int*) param);

	if(nameStr == "Initialized")
	{
		SensorActuatorRegistry::Get().Register(port,
				std::shared_ptr<EncoderWrapper> (new EncoderWrapper(port, port)));
	}
	else
	{
        SNOBOT_LOG(SnobotLogging::WARN, "Unknown name " << nameStr);
	}
}


void SnobotSim::InitializeEncoderCallbacks()
{

	for(int i = 0; i < HAL_GetNumEncoders(); ++i)
	{
		HALSIM_RegisterEncoderInitializedCallback(i, &EncoderCallback, new int(i), false);
	}
}
