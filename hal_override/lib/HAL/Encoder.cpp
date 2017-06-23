/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2016-2017. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

#include "HAL/Encoder.h"

#include "HAL/Counter.h"
#include "HAL/Errors.h"
#include "HAL/handles/LimitedClassedHandleResource.h"

#include "SnobotSim/SensorActuatorRegistry.h"
#include "SnobotSim/ModuleWrapper/EncoderWrapper.h"
#include "SnobotSim/ModuleWrapper/DigitalSourceWrapper.h"

extern "C" {
HAL_EncoderHandle HAL_InitializeEncoder(
    HAL_Handle digitalSourceHandleA, HAL_AnalogTriggerType analogTriggerTypeA,
    HAL_Handle digitalSourceHandleB, HAL_AnalogTriggerType analogTriggerTypeB,
    HAL_Bool reverseDirection, HAL_EncoderEncodingType encodingType,
    int32_t* status) {

    int32_t handle = (digitalSourceHandleA << 8) + digitalSourceHandleB;

    if (SensorActuatorRegistry::Get().GetEncoderWrapper(handle, false))
    {
        *status = RESOURCE_IS_ALLOCATED;
    }
    else
    {
        SensorActuatorRegistry::Get().Register(handle, std::shared_ptr < EncoderWrapper > (new EncoderWrapper(digitalSourceHandleA, digitalSourceHandleB)));

        SensorActuatorRegistry::Get().GetDigitalSourceWrapper(digitalSourceHandleA)->SetWantsHidden(true);
        SensorActuatorRegistry::Get().GetDigitalSourceWrapper(digitalSourceHandleB)->SetWantsHidden(true);
    }


    return handle;
}

void HAL_FreeEncoder(HAL_EncoderHandle encoderHandle, int32_t* status) {
    LOG_UNSUPPORTED();

}

int32_t HAL_GetEncoder(HAL_EncoderHandle encoderHandle, int32_t* status) {
    LOG_UNSUPPORTED();
    return 0;
}

int32_t HAL_GetEncoderRaw(HAL_EncoderHandle encoderHandle, int32_t* status) {
    return SensorActuatorRegistry::Get().GetEncoderWrapper(encoderHandle)->GetRaw();
}

int32_t HAL_GetEncoderEncodingScale(HAL_EncoderHandle encoderHandle,
                                    int32_t* status) {
    LOG_UNSUPPORTED();
    return 0;
}

void HAL_ResetEncoder(HAL_EncoderHandle encoderHandle, int32_t* status) {
    SensorActuatorRegistry::Get().GetEncoderWrapper(encoderHandle)->Reset();
}

double HAL_GetEncoderPeriod(HAL_EncoderHandle encoderHandle, int32_t* status) {
    LOG_UNSUPPORTED();
    return 0;
}

void HAL_SetEncoderMaxPeriod(HAL_EncoderHandle encoderHandle, double maxPeriod,
                             int32_t* status) {
    LOG_UNSUPPORTED();
}

HAL_Bool HAL_GetEncoderStopped(HAL_EncoderHandle encoderHandle,
                               int32_t* status) {
    LOG_UNSUPPORTED();
    return 0;
}

HAL_Bool HAL_GetEncoderDirection(HAL_EncoderHandle encoderHandle,
                                 int32_t* status) {
    LOG_UNSUPPORTED();
    return 0;
}

double HAL_GetEncoderDistance(HAL_EncoderHandle encoderHandle,
                              int32_t* status) {
    return SensorActuatorRegistry::Get().GetEncoderWrapper(encoderHandle)->GetDistance();
}

double HAL_GetEncoderRate(HAL_EncoderHandle encoderHandle, int32_t* status) {
    LOG_UNSUPPORTED();
    return 0;
}

void HAL_SetEncoderMinRate(HAL_EncoderHandle encoderHandle, double minRate,
                           int32_t* status) {
    LOG_UNSUPPORTED();

}

void HAL_SetEncoderDistancePerPulse(HAL_EncoderHandle encoderHandle,
                                    double distancePerPulse, int32_t* status) {
    return SensorActuatorRegistry::Get().GetEncoderWrapper(encoderHandle)->SetDistancePerTick(distancePerPulse);
}

void HAL_SetEncoderReverseDirection(HAL_EncoderHandle encoderHandle,
                                    HAL_Bool reverseDirection,
                                    int32_t* status) {

    LOG_UNSUPPORTED();
}

void HAL_SetEncoderSamplesToAverage(HAL_EncoderHandle encoderHandle,
                                    int32_t samplesToAverage, int32_t* status) {

    LOG_UNSUPPORTED();
}

int32_t HAL_GetEncoderSamplesToAverage(HAL_EncoderHandle encoderHandle,
                                       int32_t* status) {
    LOG_UNSUPPORTED();
    return 0;
}

double HAL_GetEncoderDecodingScaleFactor(HAL_EncoderHandle encoderHandle,
                                         int32_t* status) {
    LOG_UNSUPPORTED();
    return 0;
}

double HAL_GetEncoderDistancePerPulse(HAL_EncoderHandle encoderHandle,
                                      int32_t* status) {
    LOG_UNSUPPORTED();
    return 0;
}

HAL_EncoderEncodingType HAL_GetEncoderEncodingType(
    HAL_EncoderHandle encoderHandle, int32_t* status) {

    return HAL_Encoder_k4X;
}

void HAL_SetEncoderIndexSource(HAL_EncoderHandle encoderHandle,
                               HAL_Handle digitalSourceHandle,
                               HAL_AnalogTriggerType analogTriggerType,
                               HAL_EncoderIndexingType type, int32_t* status) {
    LOG_UNSUPPORTED();

}

int32_t HAL_GetEncoderFPGAIndex(HAL_EncoderHandle encoderHandle,
                                int32_t* status) {
    // Nothing for simulator to do
    return 0;
}
}
