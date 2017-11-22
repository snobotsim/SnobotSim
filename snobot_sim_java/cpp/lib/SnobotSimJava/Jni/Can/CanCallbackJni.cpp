
#include "com_snobot_simulator_jni_can_CanCallbackJni.h"

#include "MockData/CanData.h"

#include "SnobotSimJava/Logging/SnobotLogger.h"
#include "SnobotSimJava/Jni/RegisterJniUtilities.h"

#include "support/jni_util.h"

using namespace wpi::java;

struct CanCallbackHelperContainer : public SnobotSimJava::CallbackHelperContainer
{
    jmethodID mSendMessageMethodId;
    jmethodID mRecvMessageMethodId;
    jmethodID mOpenStreamMethodId;
    jmethodID mCloseStreamMethodId;
    jmethodID mReadStreamMethodId;
    jmethodID mGetCanStatusMethodId;
};


CanCallbackHelperContainer gCanCallbackContainer;

using namespace SnobotSimJava;


void CanSendMessageCallback(const char* name, void* param,

                  uint32_t messageID, const uint8_t* data,
                                           uint8_t dataSize, int32_t periodMs, int32_t* status)
{
  const jclass& aClazz = gCanCallbackContainer.mClazz;
  const jmethodID& aMethodId = gCanCallbackContainer.mSendMessageMethodId;

    JavaVMAttachArgs args = {JNI_VERSION_1_2, 0, 0};
    JNIEnv* aEnv;
    gJvm->AttachCurrentThread((void**) &aEnv, &args);

    if(aEnv == NULL || aClazz == NULL || aMethodId == NULL)
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "JNI Components not setup yet " << aEnv << ", " << aClazz << ", " << aMethodId);
        return;
    }

    int port = *((int*) param);
    jstring nameString = MakeJString(aEnv, name);

    jobject dataBuffer = aEnv->NewDirectByteBuffer(const_cast<uint8_t*>(data), (uint32_t) dataSize);
    aEnv->CallStaticVoidMethod(aClazz, aMethodId, nameString, port, messageID, dataBuffer, dataSize, periodMs);

    if (aEnv->ExceptionCheck())
    {
        aEnv->ExceptionDescribe();
    }
}
void CanReadMessageCallback(const char* name, void* param,
                  uint32_t* messageID, uint32_t messageIDMask,
                  uint8_t* data, uint8_t* dataSize,
                  uint32_t* timeStamp, int32_t* status)
{
  const jclass& aClazz = gCanCallbackContainer.mClazz;
  const jmethodID& aMethodId = gCanCallbackContainer.mRecvMessageMethodId;

    JavaVMAttachArgs args = {JNI_VERSION_1_2, 0, 0};
    JNIEnv* aEnv;
    gJvm->AttachCurrentThread((void**) &aEnv, &args);

    if(aEnv == NULL || aClazz == NULL || aMethodId == NULL)
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "JNI Components not setup yet " << aEnv << ", " << aClazz << ", " << aMethodId);
        return;
    }

    int port = *((int*) param);
    jstring nameString = MakeJString(aEnv, name);

    uint8_t castDataSize = *dataSize;
    jobject dataBuffer = aEnv->NewDirectByteBuffer(const_cast<uint8_t*>(data), 8);
  uint32_t castMessageId = *messageID;
  uint32_t castTimeStamp = *timeStamp;

    aEnv->CallStaticVoidMethod(aClazz, aMethodId, nameString, port, castMessageId, messageIDMask, dataBuffer, castDataSize, castTimeStamp);

    if (aEnv->ExceptionCheck())
    {
        aEnv->ExceptionDescribe();
    }
}

void CanOpenStreamCallback(
      const char* name, void* param,
      uint32_t* sessionHandle, uint32_t messageID,
        uint32_t messageIDMask, uint32_t maxMessages,
        int32_t* status)
{
  const jclass& aClazz = gCanCallbackContainer.mClazz;
  const jmethodID& aMethodId = gCanCallbackContainer.mOpenStreamMethodId;

    JavaVMAttachArgs args = {JNI_VERSION_1_2, 0, 0};
    JNIEnv* aEnv;
    gJvm->AttachCurrentThread((void**) &aEnv, &args);

    if(aEnv == NULL || aClazz == NULL || aMethodId == NULL)
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "JNI Components not setup yet " << aEnv << ", " << aClazz << ", " << aMethodId);
        return;
    }

    int port = *((int*) param);
    jstring nameString = MakeJString(aEnv, name);

    int castSessionHandle = *sessionHandle;

    *sessionHandle = aEnv->CallStaticIntMethod(aClazz, aMethodId, nameString, port, castSessionHandle, messageID, messageIDMask, maxMessages);

    if (aEnv->ExceptionCheck())
    {
        aEnv->ExceptionDescribe();
    }
}

void CanCloseStreamSessionCallback(
      const char* name, void* param,
      uint32_t sessionHandle)
{
  const jclass& aClazz = gCanCallbackContainer.mClazz;
  const jmethodID& aMethodId = gCanCallbackContainer.mCloseStreamMethodId;

    JavaVMAttachArgs args = {JNI_VERSION_1_2, 0, 0};
    JNIEnv* aEnv;
    gJvm->AttachCurrentThread((void**) &aEnv, &args);

    if(aEnv == NULL || aClazz == NULL || aMethodId == NULL)
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "JNI Components not setup yet " << aEnv << ", " << aClazz << ", " << aMethodId);
        return;
    }

    int port = *((int*) param);
    jstring nameString = MakeJString(aEnv, name);

    aEnv->CallStaticVoidMethod(aClazz, aMethodId, nameString, port, sessionHandle);

    if (aEnv->ExceptionCheck())
    {
        aEnv->ExceptionDescribe();
    }
}

void CanReadStreamSessionCallback(
      const char* name, void* param,
      uint32_t sessionHandle,
        struct HAL_CANStreamMessage* messages,
        uint32_t messagesToRead, uint32_t* messagesRead,
        int32_t* status)
{
  const jclass& aClazz = gCanCallbackContainer.mClazz;
  const jmethodID& aMethodId = gCanCallbackContainer.mReadStreamMethodId;

    JavaVMAttachArgs args = {JNI_VERSION_1_2, 0, 0};
    JNIEnv* aEnv;
    gJvm->AttachCurrentThread((void**) &aEnv, &args);

    if(aEnv == NULL || aClazz == NULL || aMethodId == NULL)
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "JNI Components not setup yet " << aEnv << ", " << aClazz << ", " << aMethodId);
        return;
    }

    int port = *((int*) param);
    jstring nameString = MakeJString(aEnv, name);

  jclass byteBufferClazz =  aEnv->FindClass("java/nio/ByteBuffer");

    jobjectArray dataBufferArray = aEnv->NewObjectArray(20, byteBufferClazz, NULL);

    // Copy all of the messages into the JNI buffer
    for(int i = 0; i < 20; ++i)
    {
      uint8_t* dataBuffer[sizeof(HAL_CANStreamMessage)];
      std::memset(dataBuffer, 0, sizeof(HAL_CANStreamMessage));

        jobject theBuffer = aEnv->NewDirectByteBuffer(dataBuffer, sizeof(HAL_CANStreamMessage));
        aEnv->SetObjectArrayElement(dataBufferArray, i, theBuffer);
    }

    // Call the Java method
    *messagesRead = aEnv->CallStaticIntMethod(aClazz, aMethodId, nameString, port, sessionHandle, dataBufferArray, messagesToRead);

    // Copy it all back to the raw buffer used by the HAL
    for(unsigned int i = 0; i < *messagesRead; ++i)
    {
        uint8_t *dataPtr = nullptr;
        jobject data = aEnv->GetObjectArrayElement(dataBufferArray, i);
        if (data != 0)
        {
            dataPtr = (uint8_t *)aEnv->GetDirectBufferAddress(data);
        }

      std::memcpy(&messages[i], dataPtr, sizeof(HAL_CANStreamMessage));
    }

    if (aEnv->ExceptionCheck())
    {
        aEnv->ExceptionDescribe();
    }
}

void CanGetCANStatusCallback(
      const char* name, void* param,
      float* percentBusUtilization, uint32_t* busOffCount,
        uint32_t* txFullCount, uint32_t* receiveErrorCount,
        uint32_t* transmitErrorCount, int32_t* status)
{
  const jclass& aClazz = gCanCallbackContainer.mClazz;
  const jmethodID& aMethodId = gCanCallbackContainer.mGetCanStatusMethodId;

    JavaVMAttachArgs args = {JNI_VERSION_1_2, 0, 0};
    JNIEnv* aEnv;
    gJvm->AttachCurrentThread((void**) &aEnv, &args);

    if(aEnv == NULL || aClazz == NULL || aMethodId == NULL)
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "JNI Components not setup yet " << aEnv << ", " << aClazz << ", " << aMethodId);
        return;
    }

    int port = *((int*) param);
    jstring nameString = MakeJString(aEnv, name);

    aEnv->CallStaticVoidMethod(aClazz, aMethodId, nameString, port,
          *percentBusUtilization, *busOffCount, *txFullCount, *receiveErrorCount, *transmitErrorCount);

    if (aEnv->ExceptionCheck())
    {
        aEnv->ExceptionDescribe();
    }
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
    std::string functionName = env->GetStringUTFChars(aFunctionName, NULL);
    SetCallbackContainerInfo(env, clz, functionName, gCanCallbackContainer);

    gCanCallbackContainer.mSendMessageMethodId = env->GetStaticMethodID(gCanCallbackContainer.mClazz, functionName.c_str(), "(Ljava/lang/String;IILjava/nio/ByteBuffer;II)V");
    if (gCanCallbackContainer.mSendMessageMethodId == NULL)
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "Failed to find method reference for function " << functionName << "(SendMessage)");
    }

    gCanCallbackContainer.mRecvMessageMethodId = env->GetStaticMethodID(gCanCallbackContainer.mClazz, functionName.c_str(), "(Ljava/lang/String;IIILjava/nio/ByteBuffer;II)V");
    if (gCanCallbackContainer.mRecvMessageMethodId == NULL)
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "Failed to find method reference for function " << functionName << " (RecvMessage)");
    }

    gCanCallbackContainer.mOpenStreamMethodId = env->GetStaticMethodID(gCanCallbackContainer.mClazz, functionName.c_str(), "(Ljava/lang/String;IIII)I");
    if (gCanCallbackContainer.mOpenStreamMethodId == NULL)
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "Failed to find method reference for function " << functionName << " (OpenStream)");
    }

    gCanCallbackContainer.mCloseStreamMethodId = env->GetStaticMethodID(gCanCallbackContainer.mClazz, functionName.c_str(), "(Ljava/lang/String;II)V");
    if (gCanCallbackContainer.mCloseStreamMethodId == NULL)
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "Failed to find method reference for function " << functionName << " (CloseStream)");
    }

    gCanCallbackContainer.mReadStreamMethodId = env->GetStaticMethodID(gCanCallbackContainer.mClazz, functionName.c_str(), "(Ljava/lang/String;II[Ljava/nio/ByteBuffer;I)I");
    if (gCanCallbackContainer.mReadStreamMethodId == NULL)
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "Failed to find method reference for function " << functionName << " (ReadStream)");
    }

    gCanCallbackContainer.mGetCanStatusMethodId = env->GetStaticMethodID(gCanCallbackContainer.mClazz, functionName.c_str(), "(Ljava/lang/String;IFIIII)V");
    if (gCanCallbackContainer.mGetCanStatusMethodId == NULL)
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "Failed to find method reference for function " << functionName << " (GetCanStatus)");
    }
}


/*
 * Class:     com_snobot_simulator_jni_can_CanCallbackJni
 * Method:    reset
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_can_CanCallbackJni_reset
  (JNIEnv *, jclass)
{
    static int dataPtr = 0;

    HALSIM_ResetCanData();

    // Initialize
    HALSIM_RegisterCanSendMessageCallback(&CanSendMessageCallback, &dataPtr);
    HALSIM_RegisterCanReceiveMessageCallback(&CanReadMessageCallback, &dataPtr);
    HALSIM_RegisterCanOpenStreamCallback(&CanOpenStreamCallback, &dataPtr);
    HALSIM_RegisterCanCloseStreamCallback(&CanCloseStreamSessionCallback, &dataPtr);
    HALSIM_RegisterCanReadStreamCallback(&CanReadStreamSessionCallback, &dataPtr);
    HALSIM_RegisterCanGetCANStatusCallback(&CanGetCANStatusCallback, &dataPtr);
}

