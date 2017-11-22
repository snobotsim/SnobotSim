
#include "SnobotSimJava/Jni/RegisterJniUtilities.h"
#include "SnobotSimJava/Logging/SnobotLogger.h"

#include "support/jni_util.h"

using namespace wpi::java;

JavaVM* SnobotSimJava::gJvm = NULL;

namespace SnobotSimJava
{
    void SetGlobalEnvironment(JNIEnv * env)
    {
        env->GetJavaVM(&gJvm);
    }

    void SetCallbackContainerInfo(JNIEnv * env, jclass clz, const std::string& functionName, SnobotSimJava::CallbackHelperContainer& outContainer)
    {
        outContainer.mClazz = clz;
        outContainer.mMethodId = env->GetStaticMethodID(clz, functionName.c_str(), "(Ljava/lang/String;ILcom/snobot/simulator/jni/HalCallbackValue;)V");
        if (outContainer.mMethodId == NULL)
        {
            SNOBOT_LOG(SnobotLogging::CRITICAL, "Failed to find method reference for function " << functionName);
        }
    }

    jobject ConvertHalValue(JNIEnv* env, const struct HAL_Value* value)
    {
        static JClass theClazz = JClass(env, "com/snobot/simulator/jni/HalCallbackValue");
        static jmethodID constructor =
                env->GetMethodID(theClazz, "<init>", "(IZIJD)V");

        if(constructor == NULL)
        {
            SNOBOT_LOG(SnobotLogging::CRITICAL, "HalCallbackValue not set up correctly, bailing");
            return NULL;
        }

        return env->NewObject(theClazz, constructor,
                (int) value->type,
                value->data.v_boolean,
                value->data.v_enum,
                value->data.v_int,
                value->data.v_long,
                value->data.v_double);
    }

    void CallJavaCallback(const CallbackHelperContainer& callbackHelper, const char* name, void* param, const struct HAL_Value* value)
    {
        JavaVMAttachArgs args = {JNI_VERSION_1_2, 0, 0};
        JNIEnv* aEnv;
        gJvm->AttachCurrentThread((void**) &aEnv, &args);

        if(aEnv == NULL || callbackHelper.mClazz == NULL || callbackHelper.mMethodId == NULL)
        {
            SNOBOT_LOG(SnobotLogging::CRITICAL, "JNI Components not setup yet");
            return;
        }

        int port = *((int*) param);
        jstring nameString = MakeJString(aEnv, name);
        jobject halValue = ConvertHalValue(aEnv, value);

        aEnv->CallStaticVoidMethod(callbackHelper.mClazz, callbackHelper.mMethodId, nameString, port, halValue);

        if (aEnv->ExceptionCheck())
        {
            aEnv->ExceptionDescribe();
        }
    }
}

