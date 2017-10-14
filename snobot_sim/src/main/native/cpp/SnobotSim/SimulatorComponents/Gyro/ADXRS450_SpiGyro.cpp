/*
 * ADXRS450_SpiGyro.cpp
 *
 *  Created on: May 7, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/Gyro/ADXRS450_SpiGyro.h"
#include "MockData/SPIData.h"

ADXRS450_SpiGyro::ADXRS450_SpiGyro(int aSpiPort):
    GyroWrapper("Spi Gyro"),
    mSpiPort(aSpiPort)
{
}

ADXRS450_SpiGyro::~ADXRS450_SpiGyro()
{

}

void ADXRS450_SpiGyro::HandleWrite()
{

}

void ADXRS450_SpiGyro::HandleTransaction()
{

}


void ADXRS450_SpiGyro::HandleRead()
{
//    uint8_t buffer[4];
//    uint32_t numToPut = 0x00400AE0;
//    std::memcpy(&buffer[0], &numToPut, sizeof(numToPut));
//
//    HALSIM_SetSPISetValueForRead(mSpiPort, buffer, sizeof(numToPut));
}

void ADXRS450_SpiGyro::SetAngle(double aAngle)
{
    GyroWrapper::SetAngle(aAngle);

    double accum = aAngle;
    accum = accum / 0.0125;
    accum = accum / 0.001;

//    HALSIM_SetSPIGetAccumulatorValue(mSpiPort, accum);
}
