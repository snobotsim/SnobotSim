/*
 * AnalogGyrWrapper.cpp
 *
 *  Created on: May 5, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/Gyro/AnalogGyroWrapper.h"
#include "MockData/AnalogGyroData.h"

AnalogGyroWrapper::AnalogGyroWrapper(const std::shared_ptr<AnalogSourceWrapper>& aAnalogWrapper):
    IGyroWrapper("Analog Gyro"),
    mAnalogWrapper(aAnalogWrapper),
	mAngle(0)
{


}

AnalogGyroWrapper::~AnalogGyroWrapper() {

}


void AnalogGyroWrapper::SetAngle(double aAngle)
{
	mAngle = aAngle;
    HALSIM_SetAnalogGyroAngle(mAnalogWrapper->GetHandle(), mAngle);
}

double AnalogGyroWrapper::GetAngle()
{
	return mAngle;
}
