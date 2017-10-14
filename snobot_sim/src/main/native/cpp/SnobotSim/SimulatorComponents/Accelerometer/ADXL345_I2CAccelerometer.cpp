/*
 * ADXL345_I2CAccelerometer.cpp
 *
 *  Created on: Jul 12, 2017
 *      Author: preiniger
 */

#include "SnobotSim/SimulatorComponents/Accelerometer/ADXL345_I2CAccelerometer.h"
#include "SnobotSim/Logging/SnobotLogger.h"
#include "MockData/I2CData.h"
#include <cstring>

ADXL345_I2CAccelerometer::ADXL345_I2CAccelerometer(int aPort, const std::string& aBaseName):
    mI2CPort(aPort),
    mThreeAxisAccelerometer(50  + aPort * 3, aBaseName)
{

}

ADXL345_I2CAccelerometer::~ADXL345_I2CAccelerometer()
{

}

void ADXL345_I2CAccelerometer::HandleRead()
{
//    uint8_t buffer[199];
//
//    HALSIM_GetI2CGetWriteBuffer(mI2CPort, buffer, 4);
//    int lastRegisterRequested = buffer[0] & 0x0F;
//    int count = 8;
//
//    SNOBOT_LOG(SnobotLogging::INFO, "Last read address... " << lastRegisterRequested);
//
//    bool includeAll = lastRegisterRequested == 0x02;
//    int byteIndex = 0;
//
//    if(lastRegisterRequested == 0x02)
//    {
//        short val = static_cast<short>(mThreeAxisAccelerometer.GetX() / 0.00390625);
//        std::memcpy(&buffer[byteIndex], &val, sizeof(val));
//        byteIndex += sizeof(val);
//    }
//
//    if(lastRegisterRequested == 0x04 || includeAll)
//    {
//        short val = static_cast<short>(mThreeAxisAccelerometer.GetY() / 0.00390625);
//        std::memcpy(&buffer[byteIndex], &val, sizeof(val));
//        byteIndex += sizeof(val);
//    }
//
//    if(lastRegisterRequested == 0x06 || includeAll)
//    {
//        short val = static_cast<short>(mThreeAxisAccelerometer.GetZ() / 0.00390625);
//        std::memcpy(&buffer[byteIndex], &val, sizeof(val));
//        byteIndex += sizeof(val);
//    }
//
//    HALSIM_SetI2CSetValueForRead(mI2CPort, buffer, count);
}
