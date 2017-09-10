/*
 * JavaHalCallbacks.cpp
 *
 *  Created on: Sep 5, 2017
 *      Author: preiniger
 */

#include "SnobotSimJava/JavaHalCallbacks/JavaHalCallbacks.h"

#include "MockData/AnalogInData.h"
#include "MockData/AnalogOutData.h"
#include "MockData/AnalogGyroData.h"
#include "MockData/DIOData.h"
#include "MockData/EncoderData.h"
#include "MockData/I2CData.h"
#include "MockData/PCMData.h"
#include "MockData/PDPData.h"
#include "MockData/PWMData.h"
#include "MockData/RelayData.h"
#include "MockData/SPIData.h"

#include "support/jni_util.h"
#include <iostream>

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
            std::cerr << "HalCallbackValue not set up correctly, bailing" << std::endl;
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
            std::cerr << "JNI Components not setup yet" << std::endl;
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

    void SetGlobalEnvironment(JNIEnv * env)
    {
        env->GetJavaVM(&gJvm);
    }

    //////////////////////////////////////////////
    // Getters
    //////////////////////////////////////////////
    CallbackHelperContainer gAnalogCallbackContainer;
    CallbackHelperContainer gAnalogGyroCallbackContainer;
    CallbackHelperContainer gDigitalCallbackContainer;
    CallbackHelperContainer gEncoderCallbackContainer;
    CallbackHelperContainer gI2CCallbackContainer;
    CallbackHelperContainer gPcmCallbackContainer;
    CallbackHelperContainer gPdpCallbackContainer;
    CallbackHelperContainer gPwmCallbackContainer;
    CallbackHelperContainer gRelayCallbackContainer;
    CallbackHelperContainer gSpiCallbackContainer;

    CallbackHelperContainer& GetAnalogCallback()     { return gAnalogCallbackContainer; }
    CallbackHelperContainer& GetAnalogGyroCallback() { return gAnalogGyroCallbackContainer; }
    CallbackHelperContainer& GetDigitalCallback()    { return gDigitalCallbackContainer; }
    CallbackHelperContainer& GetEncoderCallback()    { return gEncoderCallbackContainer; }
    CallbackHelperContainer& GetI2CCallback()        { return gI2CCallbackContainer; }
    CallbackHelperContainer& GetPCMCallback()        { return gPcmCallbackContainer; }
    CallbackHelperContainer& GetPDPCallback()        { return gPdpCallbackContainer; }
    CallbackHelperContainer& GetPWMCallback()        { return gPwmCallbackContainer; }
    CallbackHelperContainer& GetRelayCallback()      { return gRelayCallbackContainer; }
    CallbackHelperContainer& GetSpiCallback()        { return gSpiCallbackContainer; }

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
        }
        for(int i = 0; i < 2; ++i)
        {
            gI2CInArrayIndices[i] = i;
            HALSIM_RegisterI2CInitializedCallback(i, &I2CCallback, &gI2CInArrayIndices[i], false);
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
            HALSIM_RegisterSPIWriteCallback(i, &SpiCallback, &gSpiInArrayIndices[i], false);
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
