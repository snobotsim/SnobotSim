/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2016-2017. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

#include "HAL/Solenoid.h"

#include "HAL/Errors.h"
#include "HAL/Ports.h"
#include "HAL/handles/HandlesInternal.h"
#include "HAL/handles/IndexedHandleResource.h"

using namespace hal;

extern "C" {

HAL_SolenoidHandle HAL_InitializeSolenoidPort(HAL_PortHandle portHandle,
                                              int32_t* status) {
    return portHandle;
}

void HAL_FreeSolenoidPort(HAL_SolenoidHandle solenoidPortHandle) {

}

HAL_Bool HAL_CheckSolenoidModule(int32_t module) {
  return true;
}

HAL_Bool HAL_CheckSolenoidChannel(int32_t channel) {
  return true;
}

HAL_Bool HAL_GetSolenoid(HAL_SolenoidHandle solenoidPortHandle,
                         int32_t* status) {
  return 0;
}

int32_t HAL_GetAllSolenoids(int32_t module, int32_t* status) {
  return 0;
}

void HAL_SetSolenoid(HAL_SolenoidHandle solenoidPortHandle, HAL_Bool value,
                     int32_t* status) {

}

void HAL_SetAllSolenoids(int32_t module, int32_t state, int32_t* status) {

}

int32_t HAL_GetPCMSolenoidBlackList(int32_t module, int32_t* status) {
  return 0;
}
HAL_Bool HAL_GetPCMSolenoidVoltageStickyFault(int32_t module, int32_t* status) {
  return 0;
}
HAL_Bool HAL_GetPCMSolenoidVoltageFault(int32_t module, int32_t* status) {
  return 0;
}
void HAL_ClearAllPCMStickyFaults(int32_t module, int32_t* status) {

}

}  // extern "C"
