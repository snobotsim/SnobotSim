/*
 * CtreTalonSrxSimulator.cpp
 *
 *  Created on: Nov 29, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/Ctre/CtreTalonSrxSimulator.h"

CtreTalonSrxSimulator::CtreTalonSrxSimulator(int aPort) :
        SpeedControllerWrapper(aPort)
{
}

CtreTalonSrxSimulator::~CtreTalonSrxSimulator()
{
}

void CtreTalonSrxSimulator::SetPGain(double aP)
{
    mPidConstants.mP = aP;
}

void CtreTalonSrxSimulator::SetIGain(double aI)
{
    mPidConstants.mI = aI;
}

void CtreTalonSrxSimulator::SetDGain(double aD)
{
    mPidConstants.mD = aD;
}

void CtreTalonSrxSimulator::SetFGain(double aF)
{
    mPidConstants.mF = aF;
}

void CtreTalonSrxSimulator::SetIzoneGain(double aIzone)
{
    mPidConstants.mIZone = aIzone;
}

void CtreTalonSrxSimulator::SetPositionGoal(double aPosition)
{
    mControlType = Position;
    mControlGoal = aPosition;
}

void CtreTalonSrxSimulator::SetSpeedGoal(double aSpeed)
{
    mControlType = Speed;
    mControlGoal = aSpeed;
}

const CtreTalonSrxSimulator::PIDFConstants& CtreTalonSrxSimulator::GetPidConstants()
{
    return mPidConstants;
}

//////////////////////////////////////////////
void CtreTalonSrxSimulator::Update(double aWaitTime)
{
    switch (mControlType)
    {
    case Position:
    {
        double output = CalculateFeedbackOutput(GetPosition(), mControlGoal);
        SpeedControllerWrapper::SetVoltagePercentage(output);
        break;
    }
    case Speed:
    {
        double output = CalculateFeedbackOutput(GetVelocity(), mControlGoal);
        SpeedControllerWrapper::SetVoltagePercentage(output);
        break;
    }
    // Just use normal update
    case Raw:
    default:
        break;
    }
    SpeedControllerWrapper::Update(aWaitTime);
}

double CtreTalonSrxSimulator::CalculateFeedbackOutput(double aCurrent, double aGoal)
{
    double error = aGoal - aCurrent;
    double dErr = error - mLastError;

    mSumError += error;
    if (error > mPidConstants.mIZone)
    {
        mSumError = 0;
    }

    double pComp = mPidConstants.mP * error;
    double iComp = mPidConstants.mI * mSumError;
    double dComp = mPidConstants.mD * dErr;
    double fComp = mPidConstants.mF * aGoal;

    double output = pComp + iComp + dComp + fComp;

    if (output > 1.0)
    {
        output = 1.0;
    }
    if (output < -1.0)
    {
        output = -1.0;
    }

    mLastError = error;

    //    sLOGGER.log(Level.DEBUG,
    //            "Updating CAN PID: Error: " + error + ", Output: " + output +
    //            " (Cur: " + aCurrent + ", Goal: " + aGoal + ") " +
    //            " (P: " + pComp + ", I: " + iComp + ", D: " + dComp + ", F: " + fComp+ ")");

    return output;
}
