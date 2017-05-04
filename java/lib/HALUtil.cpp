/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2016. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

#include "HALUtil.h"

#include <assert.h>
#include <errno.h>
#include <jni.h>

#include <cstring>
#include <string>

#include "HAL/HAL.h"
#include "HAL/DriverStation.h"
#include "HAL/Errors.h"
#include "HAL/cpp/Log.h"
#include "edu_wpi_first_wpilibj_hal_HALUtil.h"
#include "llvm/SmallString.h"
#include "support/jni_util.h"

using namespace wpi::java;


namespace frc {

void ThrowAllocationException(JNIEnv *env, int32_t minRange, int32_t maxRange, 
    int32_t requestedValue, int32_t status) {

}

void ThrowHalHandleException(JNIEnv *env, int32_t status) {

}

constexpr const char wpilibjPrefix[] = "edu.wpi.first.wpilibj";  

void ReportError(JNIEnv *env, int32_t status, bool do_throw) {

}

void ThrowError(JNIEnv *env, int32_t status, int32_t minRange, int32_t maxRange, 
                int32_t requestedValue) {

}

void ThrowIllegalArgumentException(JNIEnv *env, const char *msg) {

}

void ThrowBoundaryException(JNIEnv *env, double value, double lower,
                            double upper) {

}

jobject CreatePWMConfigDataResult(JNIEnv *env, int32_t maxPwm,
                  int32_t deadbandMaxPwm, int32_t centerPwm,
                  int32_t deadbandMinPwm, int32_t minPwm) {
  return NULL;
}

}  // namespace frc

using namespace frc;

extern "C" {

JNIEXPORT void JNICALL JNI_OnUnload(JavaVM *vm, void *reserved) {

}

/*
 * Class:     edu_wpi_first_wpilibj_hal_HALUtil
 * Method:    getFPGAVersion
 * Signature: ()S
 */
JNIEXPORT jshort JNICALL
Java_edu_wpi_first_wpilibj_hal_HALUtil_getFPGAVersion(JNIEnv *env, jclass) {
  return 0;
}

/*
 * Class:     edu_wpi_first_wpilibj_hal_HALUtil
 * Method:    getFPGARevision
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_edu_wpi_first_wpilibj_hal_HALUtil_getFPGARevision(JNIEnv *env, jclass) {
  return 0;
}

/*
 * Class:     edu_wpi_first_wpilibj_hal_HALUtil
 * Method:    getFPGATime
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL
Java_edu_wpi_first_wpilibj_hal_HALUtil_getFPGATime(JNIEnv *env, jclass) {
  return 0;
}

/*
 * Class:     edu_wpi_first_wpilibj_hal_HALUtil
 * Method:    getHALRuntimeType
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_edu_wpi_first_wpilibj_hal_HALUtil_getHALRuntimeType(JNIEnv *env, jclass) {
  return 0;
}

/*
 * Class:     edu_wpi_first_wpilibj_hal_HALUtil
 * Method:    getFPGAButton
 * Signature: ()I
 */
JNIEXPORT jboolean JNICALL
Java_edu_wpi_first_wpilibj_hal_HALUtil_getFPGAButton(JNIEnv *env, jclass) {
  return 0;
}

/*
 * Class:     edu_wpi_first_wpilibj_hal_HALUtil
 * Method:    getHALErrorMessage
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_edu_wpi_first_wpilibj_hal_HALUtil_getHALErrorMessage(
    JNIEnv *paramEnv, jclass, jint paramId) {
  return MakeJString(paramEnv, "msg");
}

/*
 * Class:     edu_wpi_first_wpilibj_hal_HALUtil
 * Method:    getHALErrno
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_edu_wpi_first_wpilibj_hal_HALUtil_getHALErrno(JNIEnv *, jclass) {
  return errno;
}

/*
 * Class:     edu_wpi_first_wpilibj_hal_HALUtil
 * Method:    getHALstrerror
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_edu_wpi_first_wpilibj_hal_HALUtil_getHALstrerror(
    JNIEnv *env, jclass, jint errorCode) {
  return MakeJString(env, "msg");
}

}  // extern "C"
