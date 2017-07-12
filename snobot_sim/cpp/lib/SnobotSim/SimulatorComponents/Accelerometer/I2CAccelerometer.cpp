/*
 * I2CAccelerometer.cpp
 *
 *  Created on: Jul 12, 2017
 *      Author: preiniger
 */

#include "SnobotSim/SimulatorComponents/Accelerometer/I2CAccelerometer.h"
#include "SnobotSim/Logging/SnobotLogger.h"

#include <iostream>

I2CAccelerometer::I2CAccelerometer(int aPort, const std::string& aBaseName):
    mThreeAxisAccelerometer((aPort * 50), aBaseName)
{
    mThreeAxisAccelerometer.GetXWrapper()->SetAcceleration(-.5);
    mThreeAxisAccelerometer.GetYWrapper()->SetAcceleration(-1);
    mThreeAxisAccelerometer.GetZWrapper()->SetAcceleration(-2);
}

I2CAccelerometer::~I2CAccelerometer()
{

}


int I2CAccelerometer::Transaction(
        uint8_t* dataToSend, int32_t sendSize,
        uint8_t* dataReceived, int32_t receiveSize)
{
    int deviceAddress = dataToSend[0];
    bool includeAll = receiveSize == 6;

    int byteIndex = 0;

    if(deviceAddress == 0x32)
    {
        short val = static_cast<short>(mThreeAxisAccelerometer.GetX() / 0.00390625);
        std::memcpy(&dataReceived[byteIndex], &val, sizeof(val));
        byteIndex += sizeof(val);
    }

    if(deviceAddress == 0x34 || includeAll)
    {
        short val = static_cast<short>(mThreeAxisAccelerometer.GetY() / 0.00390625);
        std::memcpy(&dataReceived[byteIndex], &val, sizeof(val));
        byteIndex += sizeof(val);
    }

    if(deviceAddress == 0x36 || includeAll)
    {
        short val = static_cast<short>(mThreeAxisAccelerometer.GetZ() / 0.00390625);
        std::memcpy(&dataReceived[byteIndex], &val, sizeof(val));
        byteIndex += sizeof(val);
    }

    return receiveSize;
}


int32_t I2CAccelerometer::Read(
        int32_t deviceAddress, uint8_t* buffer, int32_t count)
{
    SNOBOT_LOG(SnobotLogging::WARN, "Shouldn't be called");
    return count;
}

int32_t I2CAccelerometer::Write(
        int32_t deviceAddress, uint8_t* dataToSend, int32_t sendSize)
{
    return sendSize;
}
