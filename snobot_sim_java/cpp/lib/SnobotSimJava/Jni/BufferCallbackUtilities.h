
#pragma once

#include "SnobotSimJava/Jni/RegisterJniUtilities.h"

namespace SnobotSimJava
{

    struct BufferCallbackHelperContainer : public CallbackHelperContainer
    {
        jmethodID mReadBufferMethodId;
        jmethodID mWriteBufferMethodId;
    };

    void CallJavaReadBufferCallback(const BufferCallbackHelperContainer& callbackHelper, const char* name, void* param, uint8_t* buffer, uint32_t count);

    void CallJavaWriteBufferCallback(const BufferCallbackHelperContainer& callbackHelper, const char* name, void* param, const uint8_t* buffer, uint32_t count);



}  // namespace SnobotSimJava
