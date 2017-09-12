/*
 * SpiAccelerometer.cpp
 *
 *  Created on: Jul 12, 2017
 *      Author: preiniger
 */

#include "MockData/SPIData.h"

#include "SnobotSim/SimulatorComponents/Accelerometer/SpiAccelerometer.h"
#include "SnobotSim/Logging/SnobotLogger.h"
#include <cstring>

SpiAccelerometer::SpiAccelerometer(int aPort, const std::string& aBaseName):
    mThreeAxisAccelerometer((aPort * 75), aBaseName),
    mSpiPort(aPort),
    mLastRegisterRequest(0)
{
    mThreeAxisAccelerometer.GetXWrapper()->SetAcceleration(.5);
    mThreeAxisAccelerometer.GetYWrapper()->SetAcceleration(1);
    mThreeAxisAccelerometer.GetZWrapper()->SetAcceleration(2);
}

SpiAccelerometer::~SpiAccelerometer()
{

}

void SpiAccelerometer::HandleRead()
{
    uint8_t buffer[199];

    HALSIM_GetSPIGetWriteBuffer(mSpiPort, buffer, 4);
    mLastRegisterRequest = buffer[0];
    int count = 8;

    if(mLastRegisterRequest == 0x2)
    {
        uint32_t numToPut = 0xF20000;
        std::memcpy(&buffer[0], &numToPut, sizeof(numToPut));
    }
    else if(mLastRegisterRequest >= 0x0E && mLastRegisterRequest <= (0x0E + 4))
    {
        bool includeAll = count == 8;
        int byteIndex = 2;

        if(mLastRegisterRequest == 0x0E)
        {
            short val = static_cast<short>(mThreeAxisAccelerometer.GetX() / 0.001);
            std::memcpy(&buffer[byteIndex], &val, sizeof(val));
            byteIndex += sizeof(val);
        }

        if(mLastRegisterRequest == 0x10 || includeAll)
        {
            short val = static_cast<short>(mThreeAxisAccelerometer.GetY() / 0.001);
            std::memcpy(&buffer[byteIndex], &val, sizeof(val));
            byteIndex += sizeof(val);
        }

        if(mLastRegisterRequest == 0x12 || includeAll)
        {
            short val = static_cast<short>(mThreeAxisAccelerometer.GetZ() / 0.001);
            std::memcpy(&buffer[byteIndex], &val, sizeof(val));
            byteIndex += sizeof(val);
        }
    }
}




//double SpiAccelerometer::GetAccumulatorValue()
//{
//    SNOBOT_LOG(SnobotLogging::WARN, "Shouldn't be called");
//    return 0;
//}
//
//void SpiAccelerometer::ResetAccumulatorValue()
//{
//    SNOBOT_LOG(SnobotLogging::WARN, "Shouldn't be called");
//}
//
//
//int32_t SpiAccelerometer::Read(uint8_t* buffer, int32_t count)
//{
//    else
//    {
//        SNOBOT_LOG(SnobotLogging::WARN, "Unknown register " << mLastRegisterRequest);
//    }
//
//    return count;
//}
//
//void SpiAccelerometer::Write(uint8_t* dataToSend, int32_t sendSize)
//{
//    mLastRegisterRequest = dataToSend[1];
//}
