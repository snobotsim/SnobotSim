
#include "MockData/CanData.h"
#include "MockHooks.h"
#include "SnobotSimJava/Jni/RegisterJniUtilities.h"
#include "SnobotSimJava/Logging/SnobotLogger.h"
#include "com_snobot_simulator_jni_can_CanCallbackJni.h"
#include "ctre/phoenix/CCI/MotController_CCI.h"
#include "support/jni_util.h"

using namespace wpi::java;
using namespace SnobotSimJava;

struct CanCallbackHelperContainer : public CallbackHelperContainer
{
    jmethodID mMotorControllerCallback;
    jmethodID mPigeonCallback;
};


CanCallbackHelperContainer gCanCallbackContainer;


void CtreCallback(const char* name, uint32_t messageId, uint8_t* buffer, const jmethodID& aMethodId)
{
    const jclass& aClazz = gCanCallbackContainer.mClazz;

    JavaVMAttachArgs args = {JNI_VERSION_1_2, 0, 0};
    JNIEnv* aEnv;
    gJvm->AttachCurrentThread((void**) &aEnv, &args);

    if(aEnv == NULL || aClazz == NULL || aMethodId == NULL)
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "JNI Components not setup yet " << aEnv << ", " << aClazz << ", " << aMethodId);
        return;
    }

    jstring nameString = MakeJString(aEnv, name);

    jobject dataBuffer = aEnv->NewDirectByteBuffer(const_cast<uint8_t*>(buffer), (uint32_t) 100);
    aEnv->CallStaticVoidMethod(aClazz, aMethodId, nameString, messageId, dataBuffer);

    if (aEnv->ExceptionCheck())
    {
        aEnv->ExceptionDescribe();
    }
}

void CtreMotorControllerCallback(const char* name, uint32_t messageId, uint8_t* buffer)
{
    CtreCallback(name, messageId, buffer, gCanCallbackContainer.mMotorControllerCallback);
}

void CtrePigeonCallback(const char* name, uint32_t messageId, uint8_t* buffer)
{
    CtreCallback(name, messageId, buffer, gCanCallbackContainer.mPigeonCallback);
}


/*
 * Class:     com_snobot_simulator_jni_can_CanCallbackJni
 * Method:    registerCanCallback
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_can_CanCallbackJni_registerCanCallback
  (JNIEnv * env, jclass clz, jstring aFunctionName)
{
    SnobotSimJava::SetGlobalEnvironment(env);
    std::string baseFunctionName = env->GetStringUTFChars(aFunctionName, NULL);

    SetCallbackContainerInfo(env, clz, baseFunctionName, gCanCallbackContainer);

    std::string motorFunctionName = baseFunctionName + "MotorController";
    std::string pigeonFunctionName = baseFunctionName + "Pigeon";

    gCanCallbackContainer.mMotorControllerCallback = env->GetStaticMethodID(gCanCallbackContainer.mClazz, motorFunctionName.c_str(), "(Ljava/lang/String;ILjava/nio/ByteBuffer;)V");
    if (gCanCallbackContainer.mMotorControllerCallback == NULL)
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "Failed to find method reference for function " << motorFunctionName << "(Motor)");
    }

    gCanCallbackContainer.mPigeonCallback = env->GetStaticMethodID(gCanCallbackContainer.mClazz, pigeonFunctionName.c_str(), "(Ljava/lang/String;ILjava/nio/ByteBuffer;)V");
    if (gCanCallbackContainer.mPigeonCallback == NULL)
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "Failed to find method reference for function " << pigeonFunctionName << "(Pigeon)");
    }
    SnobotSim::SetMotControllerCallback(&CtreMotorControllerCallback);
    SnobotSim::SetPigeonCallback(&CtrePigeonCallback);
}


/*
 * Class:     com_snobot_simulator_jni_can_CanCallbackJni
 * Method:    reset
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_can_CanCallbackJni_reset
  (JNIEnv *, jclass)
{
}
