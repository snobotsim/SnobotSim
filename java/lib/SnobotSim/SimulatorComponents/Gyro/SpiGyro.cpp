/*
 * SpiGyro.cpp
 *
 *  Created on: May 7, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/Gyro/SpiGyro.h"
#include <iostream>

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
