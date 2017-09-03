
#include "SnobotSim/HalCallbacks/CallbackSetup.h"

#include "SnobotSim/HalCallbacks/EncoderCallbacks.h"
#include "SnobotSim/HalCallbacks/PwmCallbacks.h"


void SnobotSim::InitializeSnobotCallbacks()
{
	InitializePwmCallbacks();
	InitializeEncoderCallbacks();
}
