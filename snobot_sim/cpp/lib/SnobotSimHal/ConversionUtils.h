/*
 * ConversionUtils.h
 *
 *  Created on: Jun 21, 2017
 *      Author: preiniger
 */

#ifndef CONVERSIONUTILS_H_
#define CONVERSIONUTILS_H_

#include <jni.h>
#include "support/jni_util.h"
#include "SnobotSim/MotorSim/DcMotorModelConfig.h"

namespace ConversionUtils
{
    DcMotorModelConfig ConvertDcMotorModelConfig(JNIEnv * env, jobject& aJavaModelConfig);

    jobject ConvertDcMotorModelConfig(JNIEnv *env, const DcMotorModelConfig& aConfig);
}

#endif /* CONVERSIONUTILS_H_ */
