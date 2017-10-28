
#include <assert.h>
#include <jni.h>
#include "support/jni_util.h"


#include "com_snobot_simulator_jni_SpiI2CSimulatorHal.h"

#include "ADXL345_I2CAccelerometerData.h"
#include "ADXL345_SpiAccelerometerData.h"
#include "ADXL362_SpiAccelerometerData.h"
#include "ADXRS450_SpiGyroWrapperData.h"

#include "SnobotSimJava/Logging/SnobotLogger.h"

using namespace hal;


void XXXXNullCallback(const char* name, void* param, const struct HAL_Value* value)
{
    SNOBOT_LOG(SnobotLogging::INFO, "Null Callback..." << name << ", " << value->data.v_double);
}

extern "C"
{

/*
 * Class:     com_snobot_simulator_jni_SpiI2CSimulatorHal
 * Method:    setSpiI2cAccelerometerData
 * Signature: (Ljava/lang/String;JID)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_SpiI2CSimulatorHal_setSpiI2cAccelerometerData
  (JNIEnv * env, jclass, jstring aType, jlong aPointerAddress, jint aDataType, jdouble aAcceleration)
{
    std::string type = env->GetStringUTFChars(aType, NULL);

    if(type == "I2C ADXL345" || type == "SPI ADXL345" || type == "SPI ADXL362")
    {
        ThreeAxisAccelerometerData* accel = (ThreeAxisAccelerometerData*) aPointerAddress;
        switch(aDataType)
        {
        case 0:
            accel->SetX(aAcceleration);
            break;
        case 1:
            accel->SetY(aAcceleration);
            break;
        case 2:
            accel->SetZ(aAcceleration);
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

/*
 * Class:     com_snobot_simulator_jni_SpiI2CSimulatorHal
 * Method:    getSpiI2cAccelerometerData
 * Signature: (Ljava/lang/String;JI)D
 */
JNIEXPORT jdouble JNICALL Java_com_snobot_simulator_jni_SpiI2CSimulatorHal_getSpiI2cAccelerometerData
  (JNIEnv * env, jclass, jstring aType, jlong aPointerAddress, jint aDataType)
{
    std::string type = env->GetStringUTFChars(aType, NULL);

    if(type == "I2C ADXL345" || type == "SPI ADXL345" || type == "SPI ADXL362")
    {
        ThreeAxisAccelerometerData* accel = (ThreeAxisAccelerometerData*) aPointerAddress;
        switch(aDataType)
        {
        case 0:
            return accel->GetX();
        case 1:
            return accel->GetY();
        case 2:
            return accel->GetZ();
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
 * Class:     com_snobot_simulator_jni_SpiI2CSimulatorHal
 * Method:    createSpiI2cAccelerometer
 * Signature: (Ljava/lang/String;I)J
 */
JNIEXPORT jlong JNICALL Java_com_snobot_simulator_jni_SpiI2CSimulatorHal_createSpiI2cAccelerometer
  (JNIEnv * env, jclass, jstring aType, jint aPort)
{
    std::string type = env->GetStringUTFChars(aType, NULL);

    if(type == "I2C ADXL345")
    {
        SNOBOT_LOG(SnobotLogging::INFO, "Creating " << type);
        ADXL345_I2CData* output = new ADXL345_I2CData(aPort);
        output->RegisterXCallback(XXXXNullCallback, output, false);
        output->RegisterYCallback(XXXXNullCallback, output, false);
        output->RegisterZCallback(XXXXNullCallback, output, false);
        return (jlong)(output);
    }
    else if(type == "SPI ADXL345")
    {
        SNOBOT_LOG(SnobotLogging::INFO, "Creating " << type);
        ADXL345_SpiAccelerometer* output = new ADXL345_SpiAccelerometer(aPort);
        output->RegisterXCallback(XXXXNullCallback, output, false);
        output->RegisterYCallback(XXXXNullCallback, output, false);
        output->RegisterZCallback(XXXXNullCallback, output, false);
        return (jlong)(output);
    }
    else if(type == "SPI ADXL362")
    {
        SNOBOT_LOG(SnobotLogging::INFO, "Creating " << type);
        ADXL362_SpiAccelerometer* output = new ADXL362_SpiAccelerometer(aPort);
        output->RegisterXCallback(XXXXNullCallback, output, false);
        output->RegisterYCallback(XXXXNullCallback, output, false);
        output->RegisterZCallback(XXXXNullCallback, output, false);
        return (jlong)(output);
    }
    else
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "Unknown type : " << type);
    }

    return -1;
}

/*
 * Class:     com_snobot_simulator_jni_SpiI2CSimulatorHal
 * Method:    createSpiGyro
 * Signature: (Ljava/lang/String;I)J
 */
JNIEXPORT jlong JNICALL Java_com_snobot_simulator_jni_SpiI2CSimulatorHal_createSpiGyro
  (JNIEnv * env, jclass, jstring aType, jint aPort)
{
    std::string type = env->GetStringUTFChars(aType, NULL);

    if(type == "SPI ADXRS450")
    {
        ADXRS450_SpiGyroWrapper* output = new ADXRS450_SpiGyroWrapper(aPort);
        output->RegisterAngleCallback(XXXXNullCallback, output, false);
        return (jlong)(output);
    }
    else
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "Unknown type : " << type);
    }

    return -1;
}


/*
 * Class:     com_snobot_simulator_jni_SpiI2CSimulatorHal
 * Method:    setSpiGyroAngle
 * Signature: (Ljava/lang/String;JD)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_SpiI2CSimulatorHal_setSpiGyroAngle
  (JNIEnv * env, jclass, jstring aType, jlong aPointerAddress, jdouble aAngle)
{
    std::string type = env->GetStringUTFChars(aType, NULL);

    if(type == "SPI ADXRS450")
    {
        ((ADXRS450_SpiGyroWrapper*)aPointerAddress)->SetAngle(aAngle);
    }
    else
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "Unknown type : " << type);
    }
}


/*
 * Class:     com_snobot_simulator_jni_SpiI2CSimulatorHal
 * Method:    getSpiGyroAngle
 * Signature: (Ljava/lang/String;J)D
 */
JNIEXPORT jdouble JNICALL Java_com_snobot_simulator_jni_SpiI2CSimulatorHal_getSpiGyroAngle
  (JNIEnv * env, jclass, jstring aType, jlong aPointerAddress)
{
    std::string type = env->GetStringUTFChars(aType, NULL);

    if(type == "SPI ADXRS450")
    {
        return ((ADXRS450_SpiGyroWrapper*)aPointerAddress)->GetAngle();
    }
    else
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "Unknown type : " << type);
    }

    return -1;
}

} // extern c
