/*
 * AdxI2CAccelWrapper.cpp
 *
 *  Created on: Nov 23, 2017
 *      Author: PJ
 */


#include "SnobotSim/SimulatorComponents/AdxWrappers/AdxI2CAccelWrapper.h"
#include "SnobotSim/SensorActuatorRegistry.h"

AdxI2CAccelWrapper::AdxI2CAccelWrapper(int aPort) :
	mAccel(new hal::ADXL345_I2CData(aPort)),
	mXWrapper(new AccelerometerWrapper(AccelerometerWrapper::AXIS_X, mAccel)),
	mYWrapper(new AccelerometerWrapper(AccelerometerWrapper::AXIS_Y, mAccel)),
	mZWrapper(new AccelerometerWrapper(AccelerometerWrapper::AXIS_Z, mAccel))
{
	SensorActuatorRegistry::Get().Register(50 + aPort * 3, mXWrapper);
	SensorActuatorRegistry::Get().Register(51 + aPort * 3, mYWrapper);
	SensorActuatorRegistry::Get().Register(52 + aPort * 3, mZWrapper);
}

AdxI2CAccelWrapper::~AdxI2CAccelWrapper() {

}

AdxI2CAccelWrapper::AccelerometerWrapper::AccelerometerWrapper(AxisType aAxisType, const std::shared_ptr<hal::ADXL345_I2CData>& aAccel) :
	IAccelerometerWrapper("Hello"),
	mAxisType(aAxisType),
	mAccel(aAccel)
{

}

void AdxI2CAccelWrapper::AccelerometerWrapper::SetAcceleration(double aAcceleration)
{
    switch(mAxisType)
    {
    case AXIS_X:
    	mAccel->SetX(aAcceleration);
    case AXIS_Y:
    	mAccel->SetY(aAcceleration);
    case AXIS_Z:
    	mAccel->SetZ(aAcceleration);
    }
}


double AdxI2CAccelWrapper::AccelerometerWrapper::GetAcceleration()
{
    switch(mAxisType)
    {
    case AXIS_X:
        return mAccel->GetX();
    case AXIS_Y:
        return mAccel->GetY();
    case AXIS_Z:
        return mAccel->GetZ();
    }
	return 0;
}

