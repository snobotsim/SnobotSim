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
    NOMINAL_VOLTAGE(aNominalVoltage),
    FREE_SPEED_RPM(aFreeSpeedRpm),
    FREE_CURRENT(aFreeCurrent),
    STALL_TORQUE(aStallTorque),
    STALL_CURRENT(aStallCurrent),

    mKT(aStallTorque / aStallCurrent),
    mKV((aFreeSpeedRpm / aNominalVoltage) * (M_PI * 2.0) / 60.0),
    mResistance(aNominalVoltage / aStallCurrent),
    mMotorInertia(aMotorInertia),

    mInverted(aInverted),
    mHasBrake(aHasBrake)
{

}

DcMotorModelConfig::~DcMotorModelConfig() {

}

