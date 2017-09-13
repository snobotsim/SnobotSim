/*
 * ThreeAxisAccelerometer.cpp
 *
 *  Created on: Jul 12, 2017
 *      Author: preiniger
 */

#include "SnobotSim/SimulatorComponents/Accelerometer/ThreeAxisAccelerometer.h"
#include "SnobotSim/SensorActuatorRegistry.h"

ThreeAxisAccelerometer::ThreeAxisAccelerometer(int aPort, const std::string& aBaseName) :
    mXWrapper(new AccelerometerWrapper(aBaseName + " X")),
    mYWrapper(new AccelerometerWrapper(aBaseName + " Y")),
    mZWrapper(new AccelerometerWrapper(aBaseName + " Z"))
{
    SensorActuatorRegistry::Get().Register(aPort + 0, mXWrapper);
    SensorActuatorRegistry::Get().Register(aPort + 1, mYWrapper);
    SensorActuatorRegistry::Get().Register(aPort + 2, mZWrapper);
}

ThreeAxisAccelerometer::~ThreeAxisAccelerometer()
{

}


double ThreeAxisAccelerometer::GetX()
{
    return mXWrapper->GetAcceleration();
}

double ThreeAxisAccelerometer::GetY()
{
    return mYWrapper->GetAcceleration();
}

double ThreeAxisAccelerometer::GetZ()
{
    return mZWrapper->GetAcceleration();
}

std::shared_ptr<AccelerometerWrapper>& ThreeAxisAccelerometer::GetXWrapper()
{
    return mXWrapper;
}

std::shared_ptr<AccelerometerWrapper>& ThreeAxisAccelerometer::GetYWrapper()
{
    return mYWrapper;
}

std::shared_ptr<AccelerometerWrapper>& ThreeAxisAccelerometer::GetZWrapper()
{
    return mZWrapper;
}

const std::shared_ptr<AccelerometerWrapper>& ThreeAxisAccelerometer::GetXWrapper() const
{
    return mXWrapper;
}

const std::shared_ptr<AccelerometerWrapper>& ThreeAxisAccelerometer::GetYWrapper() const
{
    return mYWrapper;
}

const std::shared_ptr<AccelerometerWrapper>& ThreeAxisAccelerometer::GetZWrapper() const
{
    return mZWrapper;
}
