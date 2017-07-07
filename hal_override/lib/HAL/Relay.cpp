/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2016-2017. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

#include "HAL/Relay.h"

#include "HAL/handles/IndexedHandleResource.h"
#include "PortsInternal.h"

#include "SnobotSim/SensorActuatorRegistry.h"
#include "SnobotSim/ModuleWrapper/RelayWrapper.h"

using namespace hal;

// Create a mutex to protect changes to the relay values
static priority_recursive_mutex digitalRelayMutex;

extern "C" {
HAL_RelayHandle HAL_InitializeRelayPort(HAL_PortHandle portHandle, HAL_Bool fwd,
                                        int32_t* status) {

    // Relays call this function twice; once for forwards, once for reverse.  Assume
    // it is OK if the relay is already initialized
    if (!SensorActuatorRegistry::Get().GetAnalogSourceWrapper(portHandle, false) && !fwd)
    {
        SensorActuatorRegistry::Get().Register(portHandle, std::shared_ptr < RelayWrapper > (new RelayWrapper(portHandle)));

        HAL_RelayHandle output = fwd ? portHandle : portHandle + kNumRelayHeaders;

        SNOBOT_LOG(SnobotLogging::DEBUG, "Relay for handle " << portHandle << " (" <<
                (fwd ? "Forwards" : "Reverse") <<
                " is " << output << ")");
    }

    return output;
}

void HAL_FreeRelayPort(HAL_RelayHandle relayPortHandle) {
    LOG_UNSUPPORTED();

}

HAL_Bool HAL_CheckRelayChannel(int32_t channel) {
  // roboRIO only has 4 headers, and the FPGA has
  // seperate functions for forward and reverse,
  // instead of seperate channel IDs
  return channel < kNumRelayHeaders && channel >= 0;
}

/**
 * Set the state of a relay.
 * Set the state of a relay output.
 */
void HAL_SetRelay(HAL_RelayHandle relayPortHandle, HAL_Bool on,
                  int32_t* status) {

	static int maxRelayHandle = 0x2000100 + kNumRelayHeaders;

    if (relayPortHandle < maxRelayHandle)
    {
        std::shared_ptr<RelayWrapper> wrapper = SensorActuatorRegistry::Get().GetRelayWrapper(relayPortHandle);
        if(wrapper)
        {
            wrapper->SetRelayForwards(on);
        }
        else
        {
            SNOBOT_LOG(SnobotLogging::CRITICAL, "Relay port " << relayPortHandle << " does not exist");
        }
    }
    else
    {
        int actualPort = relayPortHandle - kNumRelayHeaders;
        std::shared_ptr<RelayWrapper> wrapper = SensorActuatorRegistry::Get().GetRelayWrapper(relayPortHandle - kNumRelayHeaders);
        if(wrapper)
        {
            wrapper->SetRelayReverse(on);
        }
        else
        {
            SNOBOT_LOG(SnobotLogging::CRITICAL, "Relay port " << actualPort << " does not exist");
        }
    }
}

/**
 * Get the current state of the relay channel
 */
HAL_Bool HAL_GetRelay(HAL_RelayHandle relayPortHandle, int32_t* status) {

    if (relayPortHandle < kNumRelayHeaders)
    {
        return SensorActuatorRegistry::Get().GetRelayWrapper(relayPortHandle)->GetRelayForwards();
    }
    else
    {
        return SensorActuatorRegistry::Get().GetRelayWrapper(relayPortHandle - kNumRelayHeaders)->GetRelayReverse();
    }
}
}
