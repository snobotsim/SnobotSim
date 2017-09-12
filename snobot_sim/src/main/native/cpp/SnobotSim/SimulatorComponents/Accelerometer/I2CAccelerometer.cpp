/*
 * I2CAccelerometer.cpp
 *
 *  Created on: Jul 12, 2017
 *      Author: preiniger
 */

#include "SnobotSim/SimulatorComponents/Accelerometer/I2CAccelerometer.h"
#include "SnobotSim/Logging/SnobotLogger.h"
#include <cstring>

I2CAccelerometer::I2CAccelerometer(int aPort, const std::string& aBaseName):
    mThreeAxisAccelerometer((aPort * 50), aBaseName)
{
    mThreeAxisAccelerometer.GetXWrapper()->SetAcceleration(-.5);
    mThreeAxisAccelerometer.GetYWrapper()->SetAcceleration(-1);
    mThreeAxisAccelerometer.GetZWrapper()->SetAcceleration(-2);
}

I2CAccelerometer::~I2CAccelerometer()
{

}

void I2CAccelerometer::HandleRead()
{

}
