/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2016-2017. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

#include "HAL/AnalogTrigger.h"

#include "HAL/Errors.h"
#include "HAL/handles/HandlesInternal.h"
#include "HAL/handles/LimitedHandleResource.h"

#include "SnobotSim/Logging/SnobotLogger.h"

using namespace hal;


extern "C" {

HAL_AnalogTriggerHandle HAL_InitializeAnalogTrigger(
    HAL_AnalogInputHandle portHandle, int32_t* index, int32_t* status) {
  return 0;
}

void HAL_CleanAnalogTrigger(HAL_AnalogTriggerHandle analogTriggerHandle,
                            int32_t* status) {
    LOG_UNSUPPORTED();
}

void HAL_SetAnalogTriggerLimitsRaw(HAL_AnalogTriggerHandle analogTriggerHandle,
                                   int32_t lower, int32_t upper,
                                   int32_t* status) {
    LOG_UNSUPPORTED();
}

/**
 * Set the upper and lower limits of the analog trigger.
 * The limits are given as floating point voltage values.
 */
void HAL_SetAnalogTriggerLimitsVoltage(
    HAL_AnalogTriggerHandle analogTriggerHandle, double lower, double upper,
    int32_t* status) {
    LOG_UNSUPPORTED();
}

/**
 * Configure the analog trigger to use the averaged vs. raw values.
 * If the value is true, then the averaged value is selected for the analog
 * trigger, otherwise the immediate value is used.
 */
void HAL_SetAnalogTriggerAveraged(HAL_AnalogTriggerHandle analogTriggerHandle,
                                  HAL_Bool useAveragedValue, int32_t* status) {
    LOG_UNSUPPORTED();
}

/**
 * Configure the analog trigger to use a filtered value.
 * The analog trigger will operate with a 3 point average rejection filter. This
 * is designed to help with 360 degree pot applications for the period where the
 * pot crosses through zero.
 */
void HAL_SetAnalogTriggerFiltered(HAL_AnalogTriggerHandle analogTriggerHandle,
                                  HAL_Bool useFilteredValue, int32_t* status) {
    LOG_UNSUPPORTED();
}

/**
 * Return the InWindow output of the analog trigger.
 * True if the analog input is between the upper and lower limits.
 * @return The InWindow output of the analog trigger.
 */
HAL_Bool HAL_GetAnalogTriggerInWindow(
    HAL_AnalogTriggerHandle analogTriggerHandle, int32_t* status) {
    LOG_UNSUPPORTED();
  return 0;
}

/**
 * Return the TriggerState output of the analog trigger.
 * True if above upper limit.
 * False if below lower limit.
 * If in Hysteresis, maintain previous state.
 * @return The TriggerState output of the analog trigger.
 */
HAL_Bool HAL_GetAnalogTriggerTriggerState(
    HAL_AnalogTriggerHandle analogTriggerHandle, int32_t* status) {
    LOG_UNSUPPORTED();
  return 0;
}

/**
 * Get the state of the analog trigger output.
 * @return The state of the analog trigger output.
 */
HAL_Bool HAL_GetAnalogTriggerOutput(HAL_AnalogTriggerHandle analogTriggerHandle,
                                    HAL_AnalogTriggerType type,
                                    int32_t* status) {
    LOG_UNSUPPORTED();
  return 0;
}
}
