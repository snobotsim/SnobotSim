/*
 * SpiNavxSimulator.cpp
 *
 *  Created on: Jul 9, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/navx/SpiNavxSimulator.h"
#include "SnobotSim/Logging/SnobotLogger.h"

SpiNavxSimulator::SpiNavxSimulator(int aPort) :
	NavxSimulator(aPort)
{

}

SpiNavxSimulator::~SpiNavxSimulator()
{

}



double SpiNavxSimulator::GetAccumulatorValue()
{
    SNOBOT_LOG(SnobotLogging::WARN, "Shouldn't be called");
	return 0;
}

void SpiNavxSimulator::ResetAccumulatorValue()
{
    SNOBOT_LOG(SnobotLogging::WARN, "Shouldn't be called");
}

void SpiNavxSimulator::Write(uint8_t* dataToSend, int32_t sendSize)
{
	mLastWriteAddress = dataToSend[0];
}

int32_t SpiNavxSimulator::Read(uint8_t* buffer, int32_t count)
{
	if(mLastWriteAddress == 0x00)
	{
		GetWriteConfig(buffer);
	}
	else if(mLastWriteAddress == 0x04)
	{
		GetCurrentData(buffer, 0x04);
	}
	else
	{
        SNOBOT_LOG(SnobotLogging::CRITICAL,  "Unknown last write address " << ((int) mLastWriteAddress));
	}

	buffer[count - 1] = GetCRC(buffer, count - 1);

    return count;
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

