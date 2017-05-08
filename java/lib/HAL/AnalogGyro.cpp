/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2016-2017. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

#include "HAL/AnalogGyro.h"

#include <chrono>
#include <thread>

#include "HAL/AnalogAccumulator.h"
#include "HAL/AnalogInput.h"
#include "HAL/handles/IndexedHandleResource.h"
#include "SnobotSim/SensorActuatorRegistry.h"
#include "SnobotSim/SimulatorComponents/Gyro/AnalogGyroWrapper.h"

extern "C" {
HAL_GyroHandle HAL_InitializeAnalogGyro(HAL_AnalogInputHandle analogHandle,
                                        int32_t* status) {

    std::shared_ptr<AnalogSourceWrapper> analogWrapper = SensorActuatorRegistry::Get().GetAnalogSourceWrapper(analogHandle);
    analogWrapper->SetWantsHidden(true);
    
    std::shared_ptr<AnalogGyroWrapper> gyroWrapper(new AnalogGyroWrapper(analogWrapper));
    SensorActuatorRegistry::Get().Register(analogHandle, gyroWrapper);

    return analogHandle;
}

void HAL_SetupAnalogGyro(HAL_GyroHandle handle, int32_t* status) {

}

void HAL_FreeAnalogGyro(HAL_GyroHandle handle) {

}

void HAL_SetAnalogGyroParameters(HAL_GyroHandle handle,
                                 double voltsPerDegreePerSecond, double offset,
                                 int32_t center, int32_t* status) {

}

void HAL_SetAnalogGyroVoltsPerDegreePerSecond(HAL_GyroHandle handle,
                                              double voltsPerDegreePerSecond,
                                              int32_t* status) {

}

void HAL_ResetAnalogGyro(HAL_GyroHandle handle, int32_t* status) {

}

void HAL_CalibrateAnalogGyro(HAL_GyroHandle handle, int32_t* status) {

}

void HAL_SetAnalogGyroDeadband(HAL_GyroHandle handle, double volts,
                               int32_t* status) {

}

double HAL_GetAnalogGyroAngle(HAL_GyroHandle handle, int32_t* status) {
    return SensorActuatorRegistry::Get().GetGyroWrapper(handle)->GetAngle();
}

double HAL_GetAnalogGyroRate(HAL_GyroHandle handle, int32_t* status) {
    return 0;
}

double HAL_GetAnalogGyroOffset(HAL_GyroHandle handle, int32_t* status) {
    return 0;
}

int32_t HAL_GetAnalogGyroCenter(HAL_GyroHandle handle, int32_t* status) {
    return 0;
}
}
