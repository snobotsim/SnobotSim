/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2016-2017. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

#include "HAL/Notifier.h"

// For std::atexit()
#include <cstdlib>

#include <atomic>
#include <memory>
#include <mutex>

#include "HAL/Errors.h"
#include "HAL/HAL.h"
#include "HAL/cpp/make_unique.h"
#include "HAL/cpp/priority_mutex.h"
#include "HAL/handles/UnlimitedHandleResource.h"
#include "support/SafeThread.h"


extern "C" {

HAL_NotifierHandle HAL_InitializeNotifier(HAL_NotifierProcessFunction process,
                                          void* param, int32_t* status) {
  return 0;
}

HAL_NotifierHandle HAL_InitializeNotifierThreaded(
    HAL_NotifierProcessFunction process, void* param, int32_t* status) {
  return 0;
}

void HAL_CleanNotifier(HAL_NotifierHandle notifierHandle, int32_t* status) {

}

void* HAL_GetNotifierParam(HAL_NotifierHandle notifierHandle, int32_t* status) {
    return NULL;
}

void HAL_UpdateNotifierAlarm(HAL_NotifierHandle notifierHandle,
                             uint64_t triggerTime, int32_t* status) {

}

void HAL_StopNotifierAlarm(HAL_NotifierHandle notifierHandle, int32_t* status) {

}

}  // extern "C"
