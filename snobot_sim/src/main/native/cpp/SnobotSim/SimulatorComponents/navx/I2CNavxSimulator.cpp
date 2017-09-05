/*
 * I2CNavxSimulator.cpp
 *
 *  Created on: Jul 9, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/navx/I2CNavxSimulator.h"
#include "SnobotSim/Logging/SnobotLogger.h"

I2CNavxSimulator::I2CNavxSimulator(int aPort)  :
    NavxSimulator(aPort)
{

}

I2CNavxSimulator::~I2CNavxSimulator()
{

}

int32_t I2CNavxSimulator::Transaction(
        uint8_t* dataToSend, int32_t sendSize,
        uint8_t* dataReceived, int32_t receiveSize)
{
    SNOBOT_LOG(SnobotLogging::WARN, "Shouldn't be called");

    return sendSize;
}


int32_t I2CNavxSimulator::Read(
        int32_t deviceAddress, uint8_t* buffer, int32_t count)
{
    if(mLastWriteAddress == 0x00)
    {
        GetWriteConfig(buffer);
    }
    else if(mLastWriteAddress == 0x04 && count < 127)
    {
        GetCurrentData(buffer, 0x04);
    }
    else
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL,  "Unknown device address " << mLastWriteAddress);
    }

    return count;
}

int32_t I2CNavxSimulator::Write(
        int32_t deviceAddress, uint8_t* dataToSend, int32_t sendSize)
{
    mLastWriteAddress = dataToSend[0];

    return 0;
}

