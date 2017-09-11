/*
 * DcMotorModel.cpp
 *
 *  Created on: May 9, 2017
 *      Author: preiniger
 */

#include "SnobotSim/MotorSim/DcMotorModel.h"

// http://stackoverflow.com/questions/1903954/is-there-a-standard-sign-function-signum-sgn-in-c-c
template <typename T> int sgn(T val) {
    return (T(0) < val) - (val < T(0));
}

DcMotorModel::DcMotorModel(const DcMotorModelConfig& aModelConfig) :
        mModelConfig(aModelConfig), mPosition(0), mVelocity(0), mAcceleration(0), mCurrent(0)
{

}

DcMotorModel::~DcMotorModel()
{

}

const DcMotorModelConfig& DcMotorModel::GetModelConfig() const
{
    return mModelConfig;
}

void DcMotorModel::Reset(double aPosition, double aVelocity, double aCurrent)
{
    mPosition = aPosition;
    mVelocity = aVelocity;
    mCurrent = aCurrent;
    mAcceleration = 0;
}

void DcMotorModel::Step(double aAppliedVoltage, double aLoad, double aExternalTorque, double aTimestep)
{
    if (mModelConfig.mInverted)
    {
        aAppliedVoltage *= -1;
    }

    /*
     * Using the 971-style first order system model. V = I * R + Kv * w
     * torque = Kt * I
     *
     * V = torque / Kt * R + Kv * w torque = J * dw/dt + external_torque
     *
     * dw/dt = (V - Kv * w) * Kt / (R * J) - external_torque / J
     */

    if (mModelConfig.mHasBrake && aAppliedVoltage == 0)
    {
        mAcceleration = 0;
        mVelocity = 0;
        mCurrent = 0;
    }
    else
    {
        aLoad += mModelConfig.mMotorInertia;
        mAcceleration = (aAppliedVoltage - mVelocity / mModelConfig.mKV) * mModelConfig.mKT / (mModelConfig.mResistance * aLoad) + aExternalTorque / aLoad;
        mVelocity += mAcceleration * aTimestep;
        mPosition += mVelocity * aTimestep + .5 * mAcceleration * aTimestep * aTimestep;
        mCurrent = aLoad * mAcceleration * sgn(aAppliedVoltage) / mModelConfig.mKT;
    }
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
