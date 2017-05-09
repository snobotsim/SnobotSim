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

using namespace hal;

// Create a mutex to protect changes to the relay values
static priority_recursive_mutex digitalRelayMutex;

extern "C" {
HAL_RelayHandle HAL_InitializeRelayPort(HAL_PortHandle portHandle, HAL_Bool fwd,
                                        int32_t* status) {
  
    SensorActuatorRegistry::Get().Register(portHandle, std::shared_ptr < RelayWrapper > (new RelayWrapper(portHandle)));

    return fwd ? portHandle : portHandle + kNumRelayHeaders;
}

void HAL_FreeRelayPort(HAL_RelayHandle relayPortHandle) {

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

    if (relayPortHandle < kNumRelayHeaders)
    {
        SensorActuatorRegistry::Get().GetRelayWrapper(relayPortHandle)->SetRelayForwards(on);
    }
    else
    {
        SensorActuatorRegistry::Get().GetRelayWrapper(relayPortHandle - kNumRelayHeaders)->SetRelayReverse(on);
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
