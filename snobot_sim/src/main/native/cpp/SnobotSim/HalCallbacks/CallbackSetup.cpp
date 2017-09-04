
#include "SnobotSim/HalCallbacks/CallbackSetup.h"

#include "SnobotSim/HalCallbacks/EncoderCallbacks.h"
#include "SnobotSim/HalCallbacks/PwmCallbacks.h"
#include "SnobotSim/HalCallbacks/RelayCallbacks.h"
#include "SnobotSim/HalCallbacks/SolenoidCallbacks.h"


void SnobotSim::InitializeSnobotCallbacks()
{
	InitializePwmCallbacks();
	InitializeEncoderCallbacks();
	InitializeRelayCallbacks();
	InitializeSolenoidCallbacks();
}

void SnobotSim::ResetSnobotCallbacks()
{
	ResetPwmCallbacks();
	ResetEncoderCallbacks();
	ResetRelayCallbacks();
	ResetSolenoidCallbacks();
}
