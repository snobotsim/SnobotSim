/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2016-2017. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

#include "HAL/AnalogOutput.h"

#include "HAL/Errors.h"
#include "HAL/handles/HandlesInternal.h"
#include "HAL/handles/IndexedHandleResource.h"
#include "PortsInternal.h"

#include "SnobotSim/SensorActuatorRegistry.h"
#include "SnobotSim/ModuleWrapper/AnalogSourceWrapper.h"

using namespace hal;

extern "C" {

/**
 * Initialize the analog output port using the given port object.
 */
HAL_AnalogOutputHandle HAL_InitializeAnalogOutputPort(HAL_PortHandle portHandle,
                                                      int32_t* status) {

    if (SensorActuatorRegistry::Get().GetAnalogSourceWrapper(portHandle, false))
    {
        *status = RESOURCE_IS_ALLOCATED;
    }
    else
    {
        SensorActuatorRegistry::Get().Register(portHandle, std::shared_ptr < AnalogSourceWrapper > (new AnalogSourceWrapper(portHandle)));
    }

    return portHandle;
}

void HAL_FreeAnalogOutputPort(HAL_AnalogOutputHandle analogOutputHandle) {
    LOG_UNSUPPORTED();

}

/**
 * Check that the analog output channel number is value.
 * Verify that the analog channel number is one of the legal channel numbers.
 * Channel numbers are 0-based.
 *
 * @return Analog channel is valid
 */
HAL_Bool HAL_CheckAnalogOutputChannel(int32_t channel) {
  return channel < kNumAnalogOutputs && channel >= 0;
}

void HAL_SetAnalogOutput(HAL_AnalogOutputHandle analogOutputHandle,
                         double voltage, int32_t* status) {
    LOG_UNSUPPORTED();

}

double HAL_GetAnalogOutput(HAL_AnalogOutputHandle analogOutputHandle,
                           int32_t* status) {
    LOG_UNSUPPORTED();
  return 0;
}
}
