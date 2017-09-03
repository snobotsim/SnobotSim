
#include "SnobotSim/HalCallbacks/CallbackSetup.h"

#include "SnobotSim/HalCallbacks/EncoderCallbacks.h"
#include "SnobotSim/HalCallbacks/PwmCallbacks.h"
#include "SnobotSim/HalCallbacks/RelayCallbacks.h"


void SnobotSim::InitializeSnobotCallbacks()
{
	InitializePwmCallbacks();
	InitializeEncoderCallbacks();
	InitializeRelayCallbacks();
}
