
#include <assert.h>
#include <jni.h>
#include "support/jni_util.h"


#include "com_snobot_simulator_jni_navx_NavxSimulatorJni.h"

#include "NavxSim/I2CNavxSimulator.h"
#include "NavxSim/SpiNavxSimulator.h"

#include "SnobotSimJava/Logging/SnobotLogger.h"
#include <cstring>

/*
 * Class:     com_snobot_simulator_jni_NavxSimulatorHal
 * Method:    createNavx
 * Signature: (Ljava/lang/String;I)J
 */
JNIEXPORT jlong JNICALL Java_com_snobot_simulator_jni_navx_NavxSimulatorJni_createNavx
  (JNIEnv * env, jclass, jstring aType, jint aPort)
{
    std::string type = env->GetStringUTFChars(aType, NULL);

    if(type == "SPI NavX")
    {
        SpiNavxSimulator* output = new SpiNavxSimulator(aPort);
        return (jlong)(output);
    }
    else if(type == "I2C NavX")
    {
        I2CNavxSimulator* output = new I2CNavxSimulator(aPort);
        return (jlong)(output);
    }
    else
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "Unknown type : " << type);
    }

    return 0;
}

/*
 * Class:     com_snobot_simulator_jni_NavxSimulatorHal
 * Method:    getNavxData
 * Signature: (Ljava/lang/String;JI)D
 */
JNIEXPORT jdouble JNICALL Java_com_snobot_simulator_jni_navx_NavxSimulatorJni_getNavxData
  (JNIEnv * env, jclass, jstring aType, jlong aPointerAddress, jint aDataType)
{
    std::string type = env->GetStringUTFChars(aType, NULL);

    if(type == "SPI NavX" || type == "I2C NavX")
    {
        NavxSimulator* simulator = (NavxSimulator*) aPointerAddress;
        switch(aDataType)
        {
        case 0:
            return simulator->GetX();
        case 1:
            return simulator->GetY();
        case 2:
            return simulator->GetZ();
        case 3:
            return simulator->GetYaw();
        case 4:
            return simulator->GetPitch();
        case 5:
            return simulator->GetRoll();
        default:
            SNOBOT_LOG(SnobotLogging::CRITICAL, "Unknown type : " << aDataType);
        }

    }
    else
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "Unknown type : " << type);
    }

    return 0;
}

/*
 * Class:     com_snobot_simulator_jni_NavxSimulatorHal
 * Method:    setNavxData
 * Signature: (Ljava/lang/String;JID)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_navx_NavxSimulatorJni_setNavxData
  (JNIEnv * env, jclass, jstring aType, jlong aPointerAddress, jint aDataType, jdouble aData)
{
    std::string type = env->GetStringUTFChars(aType, NULL);

    if(type == "SPI NavX" || type == "I2C NavX")
    {
        NavxSimulator* simulator = (NavxSimulator*) aPointerAddress;
        switch(aDataType)
        {
        case 0:
            simulator->SetX(aData);
            break;
        case 1:
            simulator->SetY(aData);
            break;
        case 2:
            simulator->SetZ(aData);
            break;
        case 3:
            simulator->SetYaw(aData);
            break;
        case 4:
            simulator->SetPitch(aData);
            break;
        case 5:
            simulator->SetRoll(aData);
            break;
        default:
            SNOBOT_LOG(SnobotLogging::CRITICAL, "Unknown type : " << aDataType);
        }
    }
    else
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "Unknown type : " << type);
    }
}
