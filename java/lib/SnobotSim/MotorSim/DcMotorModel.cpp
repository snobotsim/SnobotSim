/*
 * DcMotorModel.cpp
 *
 *  Created on: May 9, 2017
 *      Author: preiniger
 */

#include "SnobotSim/MotorSim/DcMotorModel.h"

DcMotorModel::DcMotorModel(const DcMotorModelConfig& aModelConfig) :
        mModelConfig(aModelConfig), mPosition(0), mVelocity(0), mAcceleration(0), mCurrent(0)
{

}

DcMotorModel::~DcMotorModel()
{

}



void DcMotorModel::Reset(double aPosition, double aVelocity, double aCurrent)
{
    mPosition = aPosition;
    mVelocity = aVelocity;
    mCurrent = aCurrent;
}

void DcMotorModel::Step(double aAppliedVoltage, double aLoad, double aExternalTorque, double aTimestep)
{

}

double DcMotorModel::GetPosition()
{
    return mPosition;
}

double DcMotorModel::GetVelocity()
{
    return mVelocity;
}

double DcMotorModel::GetCurrent()
{
    return mCurrent;
}

double DcMotorModel::GetAcceleration()
{
    return mAcceleration;
}
