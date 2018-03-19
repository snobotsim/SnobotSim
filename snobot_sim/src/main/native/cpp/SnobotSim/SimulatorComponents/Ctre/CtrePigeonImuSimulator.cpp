/*
 * CtrePigeonImuSimulator.cpp
 *
 *  Created on: Nov 29, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/Ctre/CtrePigeonImuSimulator.h"

#include "SnobotSim/SensorActuatorRegistry.h"

CtrePigeonImuSimulator::CtrePigeonImuSimulator(int aPort) :
    mXWrapper(new PigeonAccelWrapper),
    mYWrapper(new PigeonAccelWrapper),
    mZWrapper(new PigeonAccelWrapper),
    mYawWrapper(new PigeonGyroWrapper),
    mPitchWrapper(new PigeonGyroWrapper),
    mRollWrapper(new PigeonGyroWrapper)
{
    SensorActuatorRegistry::Get().Register(aPort + 0, mXWrapper);
    SensorActuatorRegistry::Get().Register(aPort + 1, mYWrapper);
    SensorActuatorRegistry::Get().Register(aPort + 2, mZWrapper);

    SensorActuatorRegistry::Get().Register(aPort + 0, mYawWrapper);
    SensorActuatorRegistry::Get().Register(aPort + 1, mPitchWrapper);
    SensorActuatorRegistry::Get().Register(aPort + 2, mRollWrapper);
}

CtrePigeonImuSimulator::~CtrePigeonImuSimulator()
{

}


std::shared_ptr<IAccelerometerWrapper> CtrePigeonImuSimulator::GetXWrapper()
{
    return mXWrapper;
}

std::shared_ptr<IAccelerometerWrapper> CtrePigeonImuSimulator::GetYWrapper()
{
    return mYWrapper;
}

std::shared_ptr<IAccelerometerWrapper> CtrePigeonImuSimulator::GetZWrapper()
{
    return mZWrapper;
}

std::shared_ptr<IGyroWrapper> CtrePigeonImuSimulator::GetYawWrapper()
{
    return mYawWrapper;
}

std::shared_ptr<IGyroWrapper> CtrePigeonImuSimulator::GetPitchWrapper()
{
    return mPitchWrapper;
}

std::shared_ptr<IGyroWrapper> CtrePigeonImuSimulator::GetRollWrapper()
{
    return mRollWrapper;
}


CtrePigeonImuSimulator::PigeonAccelWrapper::PigeonAccelWrapper() :
    IAccelerometerWrapper(""),
    mAcceleration(0)
{

}

void CtrePigeonImuSimulator::PigeonAccelWrapper::SetAcceleration(double aAcceleration)
{
    mAcceleration = aAcceleration;
}

double CtrePigeonImuSimulator::PigeonAccelWrapper::GetAcceleration()
{
    return mAcceleration;
}



CtrePigeonImuSimulator::PigeonGyroWrapper::PigeonGyroWrapper() :
        IGyroWrapper(""),
        mAngle(0)
{

}

void CtrePigeonImuSimulator::PigeonGyroWrapper::SetAngle(double aAngle)
{
    mAngle = aAngle;
}

double CtrePigeonImuSimulator::PigeonGyroWrapper::GetAngle()
{
    return mAngle;
}
