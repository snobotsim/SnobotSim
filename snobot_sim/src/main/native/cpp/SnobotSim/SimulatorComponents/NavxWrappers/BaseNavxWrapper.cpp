/*
 * BaseNavxWrapper.cpp
 *
 *  Created on: Nov 23, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/NavxWrappers/BaseNavxWrapper.h"

#include "SnobotSim/SensorActuatorRegistry.h"
#include <iostream>

BaseNavxWrapper::BaseNavxWrapper(const std::string& aBaseName, const std::string& aDeviceName, int aBasePort) :
        mXWrapper(new AccelerometerWrapper(LazySimDoubleWrapper{aDeviceName, "X Accel"})),
        mYWrapper(new AccelerometerWrapper(LazySimDoubleWrapper{aDeviceName, "Y Accel"})),
        mZWrapper(new AccelerometerWrapper(LazySimDoubleWrapper{aDeviceName, "Z Accel"})),

        mYawWrapper(new GyroWrapper(LazySimDoubleWrapper{aDeviceName, "Yaw"})),
        mPitchWrapper(new GyroWrapper(LazySimDoubleWrapper{aDeviceName, "Pitch"})),
        mRollWrapper(new GyroWrapper(LazySimDoubleWrapper{aDeviceName, "Roll"}))
{
    std::cout << "Creating navx: " << std::endl;
    SensorActuatorRegistry::Get().Register(aBasePort + 0, mXWrapper);
    SensorActuatorRegistry::Get().Register(aBasePort + 1, mYWrapper);
    SensorActuatorRegistry::Get().Register(aBasePort + 2, mZWrapper);

    SensorActuatorRegistry::Get().Register(aBasePort + 0, mYawWrapper);
    SensorActuatorRegistry::Get().Register(aBasePort + 1, mPitchWrapper);
    SensorActuatorRegistry::Get().Register(aBasePort + 2, mRollWrapper);
}

BaseNavxWrapper::~BaseNavxWrapper()
{
}

BaseNavxWrapper::AccelerometerWrapper::AccelerometerWrapper(const LazySimDoubleWrapper& aSimWrapper) :
        AModuleWrapper("Hello"),
        mSimWrapper(aSimWrapper)
{
}

void BaseNavxWrapper::AccelerometerWrapper::SetAcceleration(double aAcceleration)
{
    mSimWrapper.set(aAcceleration);
}

double BaseNavxWrapper::AccelerometerWrapper::GetAcceleration()
{
    return mSimWrapper.get();
}

BaseNavxWrapper::GyroWrapper::GyroWrapper(const LazySimDoubleWrapper& aSimWrapper) :
        AModuleWrapper("Hello"),
        mSimWrapper(aSimWrapper)
{
}

void BaseNavxWrapper::GyroWrapper::SetAngle(double aAngle)
{
    mSimWrapper.set(aAngle);
}

double BaseNavxWrapper::GyroWrapper::GetAngle()
{
    return mSimWrapper.get();
}
