/*
 * StaticLoadDcMotorSim.cpp
 *
 *  Created on: May 9, 2017
 *      Author: preiniger
 */

#include "SnobotSim/MotorSim/StaticLoadDcMotorSim.h"

StaticLoadDcMotorSim::StaticLoadDcMotorSim(const DcMotorModel& aMotorModel, double aLoad, double aConversionFactor) :
        BaseDcMotorSimulator("Static Load", aMotorModel, aConversionFactor), mLoad(aLoad)
{

}

StaticLoadDcMotorSim::~StaticLoadDcMotorSim()
{

}

void StaticLoadDcMotorSim::Update(double cycleTime)
{
    mMotorModel.Step(mVoltagePercentage * 12, mLoad, 0, cycleTime);
}
