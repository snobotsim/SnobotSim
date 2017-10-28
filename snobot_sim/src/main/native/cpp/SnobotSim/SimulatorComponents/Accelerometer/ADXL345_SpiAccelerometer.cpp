/*
 * SpiAccelerometer.cpp
 *
 *  Created on: Jul 12, 2017
 *      Author: preiniger
 */

#include "MockData/SPIData.h"

#include "SnobotSim/SimulatorComponents/Accelerometer/ADXL345_SpiAccelerometer.h"
#include "SnobotSim/Logging/SnobotLogger.h"
#include <cstring>

ADXL345_SpiAccelerometer::ADXL345_SpiAccelerometer(int aPort, const std::string& aBaseName):
    mThreeAxisAccelerometer(100 + aPort * 3, aBaseName),
    mSpiPort(aPort)
{

}

ADXL345_SpiAccelerometer::~ADXL345_SpiAccelerometer()
{

}
void ADXL345_SpiAccelerometer::HandleWrite()
{

}

void ADXL345_SpiAccelerometer::HandleTransaction()
{
//    uint8_t buffer[20];
//    HALSIM_GetSPIGetTransactionBuffer(mSpiPort, buffer, 10);
//
//    int lastWrittenAddress = buffer[0] & 0xF;
//    PopulateRead(lastWrittenAddress);
}

void ADXL345_SpiAccelerometer::HandleRead()
{

}


void ADXL345_SpiAccelerometer::PopulateRead(int lastRegisterRequested)
{

    uint8_t buffer[199];

    int count = 8;

    SNOBOT_LOG(SnobotLogging::INFO, "Last read address... " << lastRegisterRequested);

    bool includeAll = lastRegisterRequested == 0x02;
    int byteIndex = 1;

    if(lastRegisterRequested == 0x02)
    {
        short val = static_cast<short>(mThreeAxisAccelerometer.GetX() / 0.00390625);
        std::memcpy(&buffer[byteIndex], &val, sizeof(val));
        byteIndex += sizeof(val);
    }

    if(lastRegisterRequested == 0x04 || includeAll)
    {
        short val = static_cast<short>(mThreeAxisAccelerometer.GetY() / 0.00390625);
        std::memcpy(&buffer[byteIndex], &val, sizeof(val));
        byteIndex += sizeof(val);
    }

    if(lastRegisterRequested == 0x06 || includeAll)
    {
        short val = static_cast<short>(mThreeAxisAccelerometer.GetZ() / 0.00390625);
        std::memcpy(&buffer[byteIndex], &val, sizeof(val));
        byteIndex += sizeof(val);
    }

//    HALSIM_SetSPISetValueForRead(mSpiPort, buffer, count);
}