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

    void SetGlobalEnvironment(JNIEnv * env);

    CallbackHelperContainer& GetAnalogCallback();
    CallbackHelperContainer& GetDigitalCallback();
    CallbackHelperContainer& GetEncoderCallback();
    CallbackHelperContainer& GetPCMCallback();
    CallbackHelperContainer& GetPDPCallback();
    CallbackHelperContainer& GetPWMCallback();
    CallbackHelperContainer& GetRelayCallback();

    void InitializeSnobotCallbacks();
    void ResetMockData();
}

#endif /* JAVAHALCALLBACKS_H_ */
