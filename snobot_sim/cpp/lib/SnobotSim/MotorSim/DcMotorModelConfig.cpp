/*
 * DcMotorModelConfig.cpp
 *
 *  Created on: May 5, 2017
 *      Author: PJ
 */

#include "SnobotSim/MotorSim/DcMotorModelConfig.h"

#ifdef _WIN32
#define _USE_MATH_DEFINES
#endif

#include <math.h>
#include <cmath>

DcMotorModelConfig::DcMotorModelConfig(
            double aNominalVoltage, 
            double aFreeSpeedRpm, 
            double aFreeCurrent, 
            double aStallTorque, 
            double aStallCurrent,
            double aMotorInertia,
            bool aHasBrake,
            bool aInverted):

            DcMotorModelConfig(
                    aNominalVoltage,
                    aFreeSpeedRpm,
                    aFreeCurrent,
                    aStallTorque,
                    aStallCurrent,
                    aMotorInertia,
                    aHasBrake,
                    aInverted,
                    aStallTorque / aStallCurrent,
                    (aFreeSpeedRpm / aNominalVoltage) * (M_PI * 2.0) / 60.0,
                    aNominalVoltage / aStallCurrent)
{

}


DcMotorModelConfig::DcMotorModelConfig(
            double aNominalVoltage,
            double aFreeSpeedRpm,
            double aFreeCurrent,
            double aStallTorque,
            double aStallCurrent,
            double aMotorInertia,

            bool aHasBrake,
            bool aInverted,

            double aKT,
            double aKV,
            double aResistance):

    NOMINAL_VOLTAGE(aNominalVoltage),
    FREE_SPEED_RPM(aFreeSpeedRpm),
    FREE_CURRENT(aFreeCurrent),
    STALL_TORQUE(aStallTorque),
    STALL_CURRENT(aStallCurrent),

    mKT(aKT),
    mKV(aKV),
    mResistance(aResistance),
    mMotorInertia(aMotorInertia),

    mInverted(aInverted),
    mHasBrake(aHasBrake)
{

}

DcMotorModelConfig::~DcMotorModelConfig() {

}

