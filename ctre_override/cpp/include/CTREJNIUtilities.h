#pragma once

#ifndef _JAVASOFT_JNI_H_
#include <jni.h>
#endif
#include <stdint.h>

namespace CTRE {

void ReportError(JNIEnv *env, int32_t status, bool do_throw);

}

inline bool CheckStatus(JNIEnv *env, int32_t status, bool do_throw = true) {
  if (status != 0) CTRE::ReportError(env, status, do_throw);
  return status == 0;
}
