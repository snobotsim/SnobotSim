
#include "SnobotSim/HalCallbacks/CallbackSetup.h"

#include "SnobotSim/HalCallbacks/AnalogIOCallbacks.h"
#include "SnobotSim/HalCallbacks/DigitalIOCallbacks.h"
#include "SnobotSim/HalCallbacks/EncoderCallbacks.h"
#include "SnobotSim/HalCallbacks/PwmCallbacks.h"
#include "SnobotSim/HalCallbacks/RelayCallbacks.h"
#include "SnobotSim/HalCallbacks/SolenoidCallbacks.h"
#include "SnobotSim/Logging/SnobotLogger.h"


void SnobotSim::InitializeSnobotCallbacks()
{
    SNOBOT_LOG(SnobotLogging::INFO, "Initializing Callbacks");
	InitializeAnalogIOCallbacks();
	InitializeDigitalIOCallbacks();
	InitializeEncoderCallbacks();
	InitializePwmCallbacks();
	InitializeRelayCallbacks();
	InitializeSolenoidCallbacks();
}

void SnobotSim::ResetSnobotCallbacks()
{
    SNOBOT_LOG(SnobotLogging::INFO, "Resetting Callbacks");
	ResetAnalogIOCallbacks();
	ResetDigitalIOCallbacks();
	ResetEncoderCallbacks();
	ResetPwmCallbacks();
	ResetRelayCallbacks();
	ResetSolenoidCallbacks();
}
