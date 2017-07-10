/*
 * SpiGyro.cpp
 *
 *  Created on: May 7, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/Gyro/SpiGyro.h"
#include <cstring>

SpiGyro::SpiGyro():
    GyroWrapper("Spi Gyro")
{

}

SpiGyro::~SpiGyro()
{

}


double SpiGyro::GetAccumulatorValue()
{
    double accum = GetAngle();
    accum = accum / 0.0125;
    accum = accum / 0.001;

    return accum;
}

void SpiGyro::ResetAccumulatorValue()
{
    SetAngle(0);
}


int32_t SpiGyro::Read(uint8_t* buffer, int32_t count)
{
    uint32_t numToPut = 0x00400AE0;

    std::memcpy(&buffer[0], &numToPut, sizeof(numToPut));

    return 0xe;
}

void SpiGyro::Write(uint8_t* dataToSend, int32_t sendSize)
{

}
