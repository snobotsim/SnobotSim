/*
 * SpiNavxSimulator.cpp
 *
 *  Created on: Jul 9, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/navx/SpiNavxSimulator.h"
#include "SnobotSim/Logging/SnobotLogger.h"
#include "MockData/SPIData.h"

SpiNavxSimulator::SpiNavxSimulator(int aPort) :
    NavxSimulator(aPort, 200)
{

}

SpiNavxSimulator::~SpiNavxSimulator()
{

}

void SpiNavxSimulator::HandleWrite()
{

}

void SpiNavxSimulator::HandleTransaction()
{

}

void SpiNavxSimulator::HandleRead()
{
//    uint8_t buffer[199];
//    int count = 0;
//
//    mLastWriteAddress = 0x04;
//    if(mLastWriteAddress == 0x00)
//    {
//        GetWriteConfig(buffer);
//        count = 17 + 1;
//    }
//    else if(mLastWriteAddress == 0x04)
//    {
//        GetCurrentData(buffer, 0x04);
//        count = 86 - 0x04 + 1;
//    }
//    else
//    {
//        SNOBOT_LOG(SnobotLogging::CRITICAL,  "Unknown last write address " << ((int) mLastWriteAddress));
//    }
//
//    buffer[count - 1] = GetCRC(buffer, count - 1);
//
//    HALSIM_SetSPISetValueForRead(mNativePort, buffer, count);
}

uint8_t SpiNavxSimulator::GetCRC(uint8_t* buffer, int length)
{
    int i, j;
    uint8_t crc = 0;

    for (i = 0; i < length; i++)
    {
          crc ^= (int)(0x00ff & buffer[i]);
          for (j = 0; j < 8; j++)
          {
              if ((crc & 0x0001)!=0)
              {
                  crc ^= 0x0091;
              }
              crc >>= 1;
          }
    }
    return crc;
}

