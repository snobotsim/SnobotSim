#include <iostream>

#include "SnobotSim/CallbackSetup.h"
#include "MockData/EncoderData.h"
#include "MockData/PwmData.h"
#include "HAL/Ports.h"

#include "SnobotSim/SensorActuatorRegistry.h"
#include "SnobotSim/ModuleWrapper/SpeedControllerWrapper.h"


void MyFunctionCallback(const char* name, void* param,
					    const struct HAL_Value* value)
{
	std::cout << "Callback function: " << name << ", " << std::endl;

	std::string nameStr = name;

	if(nameStr == "Initialized")
	{
		SensorActuatorRegistry::Get().Register(1,
				std::shared_ptr < SpeedControllerWrapper
						> (new SpeedControllerWrapper(1)));
	}
	else
	{
		std::cout << "Unknown name " << nameStr << std::endl;
	}
}

HAL_NotifyCallback testCallback = &MyFunctionCallback;

void SnobotSim::InitializeSnobotCallbacks()
{
	for(int i = 0; i < HAL_GetNumPWMChannels(); ++i)
	{
		HALSIM_RegisterPWMInitializedCallback(i, testCallback, NULL, false);
	}

	for(int i = 0; i < HAL_GetNumEncoders(); ++i)
	{
		HALSIM_RegisterEncoderInitializedCallback(i, testCallback, NULL, false);
	}

	std::cout << "Hello WOrld" << std::endl;
}
