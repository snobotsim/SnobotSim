/*
 * JavaHalCallbacks.cpp
 *
 *  Created on: Sep 5, 2017
 *      Author: preiniger
 */

#include "SnobotSimJava/JavaHalCallbacks/JavaHalCallbacks.h"
#include "SnobotSimJava/Logging/SnobotLogger.h"

#include "MockData/AnalogInData.h"
#include "MockData/AnalogOutData.h"
#include "MockData/AnalogGyroData.h"
#include "MockData/CanData.h"
#include "MockData/DIOData.h"
#include "MockData/EncoderData.h"
#include "MockData/I2CData.h"
#include "MockData/PCMData.h"
#include "MockData/PDPData.h"
#include "MockData/PWMData.h"
#include "MockData/RelayData.h"
#include "MockData/SPIData.h"

#include "support/jni_util.h"

using namespace wpi::java;

namespace SnobotSimJava
{
    int gPwmArrayIndices[26];
    int gAnalogInArrayIndices[26];
    int gAnalogOutArrayIndices[26];
    int gAnalogGyroArrayIndices[26];
    int gDigitalInArrayIndices[26];
    int gDigitalOutArrayIndices[26];
    int gEncoderArrayIndices[26];
    int gI2CInArrayIndices[2];
    int gRelayArrayIndices[20];
    int gPdpArrayIndices[64];
    int gSolenoidArrayIndices[20];
    int gSpiInArrayIndices[5];


    JavaVM* gJvm;

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

    void CallJavaBufferCallback(const jclass& aClazz, const jmethodID& aMethodId, const char* name, void* param, uint8_t* buffer, uint32_t count)
    {
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

    void CallJavaWriteBufferCallback(const BufferCallbackHelperContainer& callbackHelper, const char* name, void* param, uint8_t* buffer, uint32_t count)
    {
        CallJavaBufferCallback(callbackHelper.mClazz, callbackHelper.mWriteBufferMethodId, name, param, buffer, count);
    }

    void SetGlobalEnvironment(JNIEnv * env)
    {
        env->GetJavaVM(&gJvm);
    }

    //////////////////////////////////////////////
    // Getters
    //////////////////////////////////////////////
    CallbackHelperContainer gAnalogCallbackContainer;
    CallbackHelperContainer gAnalogGyroCallbackContainer;
    CanCallbackHelperContainer gCanCallbackContainer;
    CallbackHelperContainer gDigitalCallbackContainer;
    CallbackHelperContainer gEncoderCallbackContainer;
    BufferCallbackHelperContainer gI2CCallbackContainer;
    CallbackHelperContainer gPcmCallbackContainer;
    CallbackHelperContainer gPdpCallbackContainer;
    CallbackHelperContainer gPwmCallbackContainer;
    CallbackHelperContainer gRelayCallbackContainer;
    BufferCallbackHelperContainer gSpiCallbackContainer;

    CallbackHelperContainer& GetAnalogCallback()     { return gAnalogCallbackContainer; }
    CallbackHelperContainer& GetAnalogGyroCallback() { return gAnalogGyroCallbackContainer; }
    CanCallbackHelperContainer& GetCanCallback()     { return gCanCallbackContainer; }
    CallbackHelperContainer& GetDigitalCallback()    { return gDigitalCallbackContainer; }
    CallbackHelperContainer& GetEncoderCallback()    { return gEncoderCallbackContainer; }
    BufferCallbackHelperContainer& GetI2CCallback()  { return gI2CCallbackContainer; }
    CallbackHelperContainer& GetPCMCallback()        { return gPcmCallbackContainer; }
    CallbackHelperContainer& GetPDPCallback()        { return gPdpCallbackContainer; }
    CallbackHelperContainer& GetPWMCallback()        { return gPwmCallbackContainer; }
    CallbackHelperContainer& GetRelayCallback()      { return gRelayCallbackContainer; }
    BufferCallbackHelperContainer& GetSpiCallback()  { return gSpiCallbackContainer; }

    //////////////////////////////////////////////
    // Callbacks
    //////////////////////////////////////////////
    void AnalogIOCallback(const char* name, void* param, const struct HAL_Value* value)
    {
        CallJavaCallback(gAnalogCallbackContainer, name, param, value);
    }
    void AnalogGyroCallback(const char* name, void* param, const struct HAL_Value* value)
    {
        CallJavaCallback(gAnalogGyroCallbackContainer, name, param, value);
    }
    void DigitalIOCallback(const char* name, void* param, const struct HAL_Value* value)
    {
        CallJavaCallback(gDigitalCallbackContainer, name, param, value);
    }
    void EncoderCallback(const char* name, void* param, const struct HAL_Value* value)
    {
        CallJavaCallback(gEncoderCallbackContainer, name, param, value);
    }
    void I2CCallback(const char* name, void* param, const struct HAL_Value* value)
    {
        CallJavaCallback(gI2CCallbackContainer, name, param, value);
    }
    void I2CReadCallback(const char* name, void* param, uint8_t* buffer, uint32_t count)
    {
        CallJavaReadBufferCallback(gI2CCallbackContainer, name, param, buffer, count);
    }
    void I2CWriteCallback(const char* name, void* param, uint8_t* buffer, uint32_t count)
    {
        CallJavaWriteBufferCallback(gI2CCallbackContainer, name, param, buffer, count);
    }
    void PdpCallback(const char* name, void* param, const struct HAL_Value* value)
    {
        CallJavaCallback(gPdpCallbackContainer, name, param, value);
    }
    void PwmCallback(const char* name, void* param, const struct HAL_Value* value)
    {
        CallJavaCallback(gPwmCallbackContainer, name, param, value);
    }
    void SolenoidCallback(const char* name, void* param, const struct HAL_Value* value)
    {
        CallJavaCallback(gPcmCallbackContainer, name, param, value);
    }
    void RelayCallback(const char* name, void* param, const struct HAL_Value* value)
    {
        CallJavaCallback(gRelayCallbackContainer, name, param, value);
    }
    void SpiCallback(const char* name, void* param, const struct HAL_Value* value)
    {
        CallJavaCallback(gSpiCallbackContainer, name, param, value);
    }
    void SpiReadCallback(const char* name, void* param, uint8_t* buffer, uint32_t count)
    {
        CallJavaReadBufferCallback(gSpiCallbackContainer, name, param, buffer, count);
    }
    void SpiWriteCallback(const char* name, void* param, uint8_t* buffer, uint32_t count)
    {
        CallJavaWriteBufferCallback(gSpiCallbackContainer, name, param, buffer, count);
    }




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




    //////////////////////////////////////////////
    // Connect Callbacks
    //////////////////////////////////////////////
    void InitializeSnobotCallbacks()
    {
        for(int i = 0; i < HAL_GetNumAnalogInputs(); ++i)
        {
            gAnalogInArrayIndices[i] = i;
            HALSIM_RegisterAnalogInInitializedCallback(i, &AnalogIOCallback, &gAnalogInArrayIndices[i], false);
        }

        for(int i = 0; i < HAL_GetNumAnalogOutputs(); ++i)
        {
            gAnalogOutArrayIndices[i] = i;
            HALSIM_RegisterAnalogOutInitializedCallback(i, &AnalogIOCallback, &gAnalogOutArrayIndices[i], false);
        }

        for(int i = 0; i < HAL_GetNumAccumulators(); ++i)
        {
            gAnalogGyroArrayIndices[i] = i;
            HALSIM_RegisterAnalogGyroInitializedCallback(i, &AnalogGyroCallback, &gAnalogGyroArrayIndices[i], false);
            HALSIM_RegisterAnalogGyroAngleCallback(i, &AnalogGyroCallback, &gAnalogGyroArrayIndices[i], false);
            HALSIM_RegisterAnalogGyroRateCallback(i, &AnalogGyroCallback, &gAnalogGyroArrayIndices[i], false);
        }

        {
            HALSIM_RegisterCanSendMessageCallback(&CanSendMessageCallback, &gAnalogGyroArrayIndices[0]);
            HALSIM_RegisterCanReceiveMessageCallback(&CanReadMessageCallback, &gAnalogGyroArrayIndices[0]);
            HALSIM_RegisterCanOpenStreamCallback(&CanOpenStreamCallback, &gAnalogGyroArrayIndices[0]);
            HALSIM_RegisterCanCloseStreamCallback(&CanCloseStreamSessionCallback, &gAnalogGyroArrayIndices[0]);
            HALSIM_RegisterCanReadStreamCallback(&CanReadStreamSessionCallback, &gAnalogGyroArrayIndices[0]);
            HALSIM_RegisterCanGetCANStatusCallback(&CanGetCANStatusCallback, &gAnalogGyroArrayIndices[0]);
        }

        for(int i = 0; i < HAL_GetNumDigitalHeaders(); ++i)
        {
            gDigitalInArrayIndices[i] = i;
            HALSIM_RegisterDIOInitializedCallback(i, &DigitalIOCallback, &gDigitalInArrayIndices[i], false);
            HALSIM_RegisterDIOValueCallback(i, &DigitalIOCallback, &gDigitalInArrayIndices[i], false);
        }

        for(int i = 0; i < HAL_GetNumEncoders(); ++i)
        {
            gEncoderArrayIndices[i] = i;
            HALSIM_RegisterEncoderInitializedCallback(i, &EncoderCallback, &gEncoderArrayIndices[i], false);
            HALSIM_RegisterEncoderCountCallback(i, &EncoderCallback, &gEncoderArrayIndices[i], false);
            HALSIM_RegisterEncoderPeriodCallback(i, &EncoderCallback, &gEncoderArrayIndices[i], false);
            HALSIM_RegisterEncoderResetCallback(i, &EncoderCallback, &gEncoderArrayIndices[i], false);
            HALSIM_RegisterEncoderMaxPeriodCallback(i, &EncoderCallback, &gEncoderArrayIndices[i], false);
            HALSIM_RegisterEncoderDirectionCallback(i, &EncoderCallback, &gEncoderArrayIndices[i], false);
            HALSIM_RegisterEncoderReverseDirectionCallback(i, &EncoderCallback, &gEncoderArrayIndices[i], false);
            HALSIM_RegisterEncoderSamplesToAverageCallback(i, &EncoderCallback, &gEncoderArrayIndices[i], false);
        }
        for(int i = 0; i < 2; ++i)
        {
            gI2CInArrayIndices[i] = i;
            HALSIM_RegisterI2CInitializedCallback(i, &I2CCallback, &gI2CInArrayIndices[i], false);
//            HALSIM_RegisterI2CReadCallback(i, &I2CReadCallback, &gI2CInArrayIndices[i]);
//            HALSIM_RegisterI2CWriteCallback(i, &I2CWriteCallback, &gI2CInArrayIndices[i]);
        }

        for(int i = 0; i < HAL_GetNumPWMChannels(); ++i)
        {
            gPwmArrayIndices[i] = i;
            HALSIM_RegisterPWMInitializedCallback(i, &PwmCallback, &gPwmArrayIndices[i], false);
            HALSIM_RegisterPWMSpeedCallback(i, &PwmCallback, &gPwmArrayIndices[i], false);
            HALSIM_RegisterPWMRawValueCallback(i, &PwmCallback, &gPwmArrayIndices[i], false);
            HALSIM_RegisterPWMPositionCallback(i, &PwmCallback, &gPwmArrayIndices[i], false);
        }

        for(int i = 0; i < HAL_GetNumSolenoidChannels(); ++i)
        {
            gSolenoidArrayIndices[i] = i;
            HALSIM_RegisterPCMSolenoidInitializedCallback(0, i, &SolenoidCallback, &gSolenoidArrayIndices[i], false);
            HALSIM_RegisterPCMSolenoidOutputCallback(0, i, &SolenoidCallback, &gSolenoidArrayIndices[i], false);
        }

        for(int i = 0; i < HAL_GetNumRelayHeaders(); ++i)
        {
            gRelayArrayIndices[i] = i;
            HALSIM_RegisterRelayInitializedForwardCallback(i, &RelayCallback, &gRelayArrayIndices[i], false);
            HALSIM_RegisterRelayInitializedReverseCallback(i, &RelayCallback, &gRelayArrayIndices[i], false);
            HALSIM_RegisterRelayForwardCallback(i, &RelayCallback, &gRelayArrayIndices[i], false);
            HALSIM_RegisterRelayReverseCallback(i, &RelayCallback, &gRelayArrayIndices[i], false);
        }

        for(int i = 0; i < HAL_GetNumPDPModules(); ++i)
        {
            gPdpArrayIndices[i] = i;
            HALSIM_RegisterPDPInitializedCallback(i, &PdpCallback, &gPdpArrayIndices[i], false);
        }
        for(int i = 0; i < 5; ++i)
        {
            gSpiInArrayIndices[i] = i;
            HALSIM_RegisterSPIInitializedCallback(i, &SpiCallback, &gSpiInArrayIndices[i], false);
//            HALSIM_RegisterSPIReadCallback(i, &SpiReadCallback, &gSpiInArrayIndices[i]);
//            HALSIM_RegisterSPIWriteCallback(i, &SpiWriteCallback, &gSpiInArrayIndices[i]);
////            HALSIM_RegisterSPITransactionCallback(i, &SpiCallback, &gSpiInArrayIndices[i], false);
//            HALSIM_RegisterSPIResetAccumulatorCallback(i, &SpiCallback, &gSpiInArrayIndices[i], false);
//
        }
    }

    void ResetMockData()
    {
        for(int i = 0; i < HAL_GetNumAnalogInputs(); ++i)
        {
            HALSIM_ResetAnalogInData(i);
        }
        for(int i = 0; i < HAL_GetNumAnalogOutputs(); ++i)
        {
            HALSIM_ResetAnalogOutData(i);
        }
        for(int i = 0; i < HAL_GetNumAccumulators(); ++i)
        {
            HALSIM_ResetAnalogGyroData(i);
        }
        for(int i = 0; i < HAL_GetNumDigitalHeaders(); ++i)
        {
            HALSIM_ResetDIOData(i);
        }

        {
//            HALSIM_ResetCAN();
        }

        for(int i = 0; i < HAL_GetNumEncoders(); ++i)
        {
            HALSIM_ResetEncoderData(i);
        }
        for(int i = 0; i < 2; ++i)
        {
            HALSIM_ResetI2CData(i);
        }
        for(int i = 0; i < HAL_GetNumPWMChannels(); ++i)
        {
            HALSIM_ResetPWMData(i);
        }
        for(int i = 0; i < HAL_GetNumRelayHeaders(); ++i)
        {
            HALSIM_ResetRelayData(i);
        }
        for(int i = 0; i < HAL_GetNumSolenoidChannels(); ++i)
        {
            HALSIM_ResetPCMData(i);
        }
        for(int i = 0; i < HAL_GetNumPDPModules(); ++i)
        {
            HALSIM_ResetPDPData(i);
        }
        for(int i = 0; i < 5; ++i)
        {
            HALSIM_ResetSPIData(i);
        }

        InitializeSnobotCallbacks();
    }

}
