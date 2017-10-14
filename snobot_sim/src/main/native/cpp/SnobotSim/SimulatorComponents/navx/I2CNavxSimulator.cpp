/*
 * I2CNavxSimulator.cpp
 *
 *  Created on: Jul 9, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/navx/I2CNavxSimulator.h"
#include "SnobotSim/Logging/SnobotLogger.h"
#include "MockData/I2CData.h"

I2CNavxSimulator::I2CNavxSimulator(int aPort)  :
    NavxSimulator(aPort, 250)
{

}

I2CNavxSimulator::~I2CNavxSimulator()
{

}

void I2CNavxSimulator::HandleRead()
{
//    uint8_t buffer[199];
//    int count = 0;
//
//    mLastWriteAddress = 4;
//
//    if(mLastWriteAddress == 0x00)
//    {
//        GetWriteConfig(buffer);
//        count = 17;
//    }
//    else if(mLastWriteAddress == 0x04 && count < 127)
//    {
//        GetCurrentData(buffer, 0x04);
//        count = 86 - 0x04 ;
//    }
//    else
//    {
//        SNOBOT_LOG(SnobotLogging::CRITICAL,  "Unknown device address " << mLastWriteAddress);
//    }
//
//    HALSIM_SetI2CSetValueForRead(mNativePort, buffer, count);
}
