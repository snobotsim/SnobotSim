
#include "SnobotSimJava/Jni/BufferCallbackUtilities.h"

#include "SnobotSimJava/Logging/SnobotLogger.h"
#include "support/jni_util.h"

using namespace wpi::java;

namespace SnobotSimJava
{

    void CallJavaBufferCallback(const jclass& aClazz, const jmethodID& aMethodId, const char* name, void* param, uint8_t* buffer, uint32_t count)
    {
        JavaVMAttachArgs args = {JNI_VERSION_1_2, 0, 0};
        JNIEnv* aEnv;
        gJvm->AttachCurrentThread(reinterpret_cast<void**>(&aEnv), &args);

        if(aEnv == NULL || aClazz == NULL || aMethodId == NULL)
        {
            SNOBOT_LOG(SnobotLogging::CRITICAL, "JNI Components not setup yet " << aEnv << ", " << aClazz << ", " << aMethodId);
            return;
        }

        int port = *reinterpret_cast<int*>(param);
        jstring nameString = MakeJString(aEnv, name);

        jobject data = aEnv->NewDirectByteBuffer(buffer, count);

        aEnv->CallStaticVoidMethod(aClazz, aMethodId, nameString, port, data);

        if (aEnv->ExceptionCheck())
        {
            aEnv->ExceptionDescribe();
        }
    }

    void CallJavaReadBufferCallback(const BufferCallbackHelperContainer& callbackHelper, const char* name, void* param, uint8_t* buffer, uint32_t count)
    {
        CallJavaBufferCallback(callbackHelper.mClazz, callbackHelper.mReadBufferMethodId, name, param, buffer, count);
    }

    void CallJavaWriteBufferCallback(const BufferCallbackHelperContainer& callbackHelper, const char* name, void* param, const uint8_t* buffer, uint32_t count)
    {
        CallJavaBufferCallback(callbackHelper.mClazz, callbackHelper.mWriteBufferMethodId, name, param, const_cast<uint8_t*>(buffer), count);
    }

}  // namespace SnobotSimJava
