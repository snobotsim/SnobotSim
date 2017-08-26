/*
 * ConversionUtils.cpp
 *
 *  Created on: Jun 21, 2017
 *      Author: preiniger
 */

#include "ConversionUtils.h"

using namespace wpi::java;

namespace ConversionUtils
{

    DcMotorModelConfig ConvertDcMotorModelConfig(JNIEnv * env, jobject& aJavaModelConfig)
    {
        jstring motorType = (jstring) env->GetObjectField(aJavaModelConfig, env->GetFieldID(env->GetObjectClass(aJavaModelConfig), "mMotorType", "Ljava/lang/String;"));

        DcMotorModelConfig::FactoryParams factoryParams;
        factoryParams.mMotorName              = env->GetStringUTFChars(motorType, NULL);
        factoryParams.mNumMotors              = env->GetIntField(aJavaModelConfig, env->GetFieldID(env->GetObjectClass(aJavaModelConfig), "mNumMotors", "I"));
        factoryParams.mGearReduction          = env->GetDoubleField(aJavaModelConfig, env->GetFieldID(env->GetObjectClass(aJavaModelConfig), "mGearReduction", "D"));
        factoryParams.mTransmissionEfficiency = env->GetDoubleField(aJavaModelConfig, env->GetFieldID(env->GetObjectClass(aJavaModelConfig), "mGearboxEfficiency", "D"));

        DcMotorModelConfig config(
                factoryParams,
                env->GetDoubleField(aJavaModelConfig, env->GetFieldID(env->GetObjectClass(aJavaModelConfig), "NOMINAL_VOLTAGE", "D")),
                env->GetDoubleField(aJavaModelConfig, env->GetFieldID(env->GetObjectClass(aJavaModelConfig), "FREE_SPEED_RPM", "D")),
                env->GetDoubleField(aJavaModelConfig, env->GetFieldID(env->GetObjectClass(aJavaModelConfig), "FREE_CURRENT", "D")),
                env->GetDoubleField(aJavaModelConfig, env->GetFieldID(env->GetObjectClass(aJavaModelConfig), "STALL_TORQUE", "D")),
                env->GetDoubleField(aJavaModelConfig, env->GetFieldID(env->GetObjectClass(aJavaModelConfig), "STALL_CURRENT", "D")),
                env->GetDoubleField(aJavaModelConfig, env->GetFieldID(env->GetObjectClass(aJavaModelConfig), "mMotorInertia", "D")),

                env->GetBooleanField(aJavaModelConfig, env->GetFieldID(env->GetObjectClass(aJavaModelConfig), "mHasBrake", "Z")),
                env->GetBooleanField(aJavaModelConfig, env->GetFieldID(env->GetObjectClass(aJavaModelConfig), "mInverted", "Z")),

                env->GetDoubleField(aJavaModelConfig, env->GetFieldID(env->GetObjectClass(aJavaModelConfig), "mKT", "D")),
                env->GetDoubleField(aJavaModelConfig, env->GetFieldID(env->GetObjectClass(aJavaModelConfig), "mKV", "D")),
                env->GetDoubleField(aJavaModelConfig, env->GetFieldID(env->GetObjectClass(aJavaModelConfig), "mResistance", "D"))
        );

        return config;
    }

    jobject ConvertDcMotorModelConfig(
            JNIEnv *env,
            const DcMotorModelConfig& aConfig)
    {
        static JClass theClazz = JClass(env, "com/snobot/simulator/DcMotorModelConfig");
        static jmethodID constructor =
                env->GetMethodID(theClazz, "<init>", "(Ljava/lang/String;IDDDDDDDDZZDDD)V");

        jstring motorType = MakeJString(env, aConfig.mFactoryParams.mMotorName);

        return env->NewObject(theClazz, constructor,
                motorType,
                aConfig.mFactoryParams.mNumMotors,
                aConfig.mFactoryParams.mGearReduction,
                aConfig.mFactoryParams.mTransmissionEfficiency,
                aConfig.NOMINAL_VOLTAGE,
                aConfig.FREE_SPEED_RPM,
                aConfig.FREE_CURRENT,
                aConfig.STALL_TORQUE,
                aConfig.STALL_CURRENT,
                aConfig.mMotorInertia,

                aConfig.mHasBrake,
                aConfig.mInverted,

                aConfig.mKT,
                aConfig.mKV,
                aConfig.mResistance);
    }
}
