/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2016-2017. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

#include "HAL/Compressor.h"

#include "HAL/Errors.h"
#include "HAL/handles/HandlesInternal.h"
#include "PortsInternal.h"

#include "SnobotSim/Logging/SnobotLogger.h"
#include "SnobotSim/SensorActuatorRegistry.h"
#include "SnobotSim/SimulatorComponents/CompressorWrapper.h"

using namespace hal;

extern "C" {

HAL_CompressorHandle HAL_InitializeCompressor(int32_t module, int32_t* status) {

    return module;
}

HAL_Bool HAL_CheckCompressorModule(int32_t module) {
  return module < kNumPCMModules && module >= 0;
}

HAL_Bool HAL_GetCompressor(HAL_CompressorHandle compressorHandle,
                           int32_t* status) {
    LOG_UNSUPPORTED();
    return true;
}

void HAL_SetCompressorClosedLoopControl(HAL_CompressorHandle compressorHandle,
                                        HAL_Bool value, int32_t* status) {
    LOG_UNSUPPORTED();
}

HAL_Bool HAL_GetCompressorClosedLoopControl(
    HAL_CompressorHandle compressorHandle, int32_t* status) {

    LOG_UNSUPPORTED();
    return true;
}

HAL_Bool HAL_GetCompressorPressureSwitch(HAL_CompressorHandle compressorHandle,
                                         int32_t* status) {

    return SensorActuatorRegistry::Get().GetCompressorWrapper()->IsPressureSwitchFull();
}

double HAL_GetCompressorCurrent(HAL_CompressorHandle compressorHandle,
                                int32_t* status) {

    LOG_UNSUPPORTED();
    return 0;
}
HAL_Bool HAL_GetCompressorCurrentTooHighFault(
    HAL_CompressorHandle compressorHandle, int32_t* status) {

    LOG_UNSUPPORTED();
    return false;
}
HAL_Bool HAL_GetCompressorCurrentTooHighStickyFault(
    HAL_CompressorHandle compressorHandle, int32_t* status) {
    LOG_UNSUPPORTED();
    return false;
}
HAL_Bool HAL_GetCompressorShortedStickyFault(
    HAL_CompressorHandle compressorHandle, int32_t* status) {
    LOG_UNSUPPORTED();
    return false;
}
HAL_Bool HAL_GetCompressorShortedFault(HAL_CompressorHandle compressorHandle,
                                       int32_t* status) {
    LOG_UNSUPPORTED();
    return false;
}
HAL_Bool HAL_GetCompressorNotConnectedStickyFault(
    HAL_CompressorHandle compressorHandle, int32_t* status) {
    LOG_UNSUPPORTED();
    return false;
}
HAL_Bool HAL_GetCompressorNotConnectedFault(
    HAL_CompressorHandle compressorHandle, int32_t* status) {
    LOG_UNSUPPORTED();
    return false;
}
}  // extern "C"
