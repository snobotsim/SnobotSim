/*
 * JavaHalCallbacks.h
 *
 *  Created on: Sep 5, 2017
 *      Author: preiniger
 */

#ifndef JAVAHALCALLBACKS_H_
#define JAVAHALCALLBACKS_H_

#include <jni.h>

namespace SnobotSimJava
{

    struct CallbackHelperContainer
    {
        jmethodID mMethodId;
        jclass mClazz;

        CallbackHelperContainer() :
            mMethodId(NULL),
            mClazz(NULL)
        {

        }
    };

    struct BufferCallbackHelperContainer : public CallbackHelperContainer
    {
        jmethodID mReadBufferMethodId;
        jmethodID mWriteBufferMethodId;
    };

    void SetGlobalEnvironment(JNIEnv * env);

    CallbackHelperContainer& GetAnalogCallback();
    CallbackHelperContainer& GetAnalogGyroCallback();
    CallbackHelperContainer& GetCanCallback();
    CallbackHelperContainer& GetDigitalCallback();
    CallbackHelperContainer& GetEncoderCallback();
    BufferCallbackHelperContainer& GetI2CCallback();
    CallbackHelperContainer& GetPCMCallback();
    CallbackHelperContainer& GetPDPCallback();
    CallbackHelperContainer& GetPWMCallback();
    CallbackHelperContainer& GetRelayCallback();
    BufferCallbackHelperContainer& GetSpiCallback();

    void InitializeSnobotCallbacks();
    void ResetMockData();
}

#endif /* JAVAHALCALLBACKS_H_ */
