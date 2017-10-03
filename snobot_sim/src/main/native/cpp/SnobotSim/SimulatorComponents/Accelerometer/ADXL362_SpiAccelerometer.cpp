/*
 * SpiAccelerometer.cpp
 *
 *  Created on: Jul 12, 2017
 *      Author: preiniger
 */

#include "MockData/SPIData.h"

#include "SnobotSim/SimulatorComponents/Accelerometer/ADXL362_SpiAccelerometer.h"
#include "SnobotSim/Logging/SnobotLogger.h"
#include <cstring>

ADXL362_SpiAccelerometer::ADXL362_SpiAccelerometer(int aPort, const std::string& aBaseName):
    mThreeAxisAccelerometer(150 + aPort * 3, aBaseName),
    mSpiPort(aPort)
{

}

ADXL362_SpiAccelerometer::~ADXL362_SpiAccelerometer()
{

}


void ADXL362_SpiAccelerometer::HandleWrite()
{

}

void ADXL362_SpiAccelerometer::HandleTransaction()
{

}

void ADXL362_SpiAccelerometer::HandleRead()
{
    uint8_t buffer[199];
    HALSIM_GetSPIGetTransactionBuffer(mSpiPort, buffer, 4);
    int lastRegisterRequested = buffer[1] & 0xFF;
    PopulateRead(lastRegisterRequested);
}

void ADXL362_SpiAccelerometer::PopulateRead(int lastRegisterRequested)
{

    uint8_t buffer[199];

    int count = 8;

    SNOBOT_LOG(SnobotLogging::INFO, "Last read address... " << lastRegisterRequested);

    // Initialization message
    if(lastRegisterRequested == 0x02)
    {
        uint32_t numToPut = 0xF20000;
        std::memcpy(&buffer[0], &numToPut, sizeof(numToPut));
    }
    // Requesting actual data...
    else if(lastRegisterRequested >= 0x0E && lastRegisterRequested <= (0x0E + 4))
    {
        bool includeAll = lastRegisterRequested == 0x0E;
        int byteIndex = 2;

        if(lastRegisterRequested == 0x0E)
        {
            short val = static_cast<short>(mThreeAxisAccelerometer.GetX() / 0.001);
            std::memcpy(&buffer[byteIndex], &val, sizeof(val));
            byteIndex += sizeof(val);
        }

        if(lastRegisterRequested == 0x10 || includeAll)
        {
            short val = static_cast<short>(mThreeAxisAccelerometer.GetY() / 0.001);
            std::memcpy(&buffer[byteIndex], &val, sizeof(val));
            byteIndex += sizeof(val);
        }

        if(lastRegisterRequested == 0x12 || includeAll)
        {
            short val = static_cast<short>(mThreeAxisAccelerometer.GetZ() / 0.001);
            std::memcpy(&buffer[byteIndex], &val, sizeof(val));
            byteIndex += sizeof(val);
        }
    }
    else
    {
        SNOBOT_LOG(SnobotLogging::WARN, "Unknown request " << lastRegisterRequested);
    }


    HALSIM_SetSPISetValueForRead(mSpiPort, buffer, count);
}

