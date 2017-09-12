/*
 * AnalogGyrWrapper.cpp
 *
 *  Created on: May 5, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/Gyro/AnalogGyroWrapper.h"
#include "MockData/AnalogGyroData.h"

AnalogGyroWrapper::AnalogGyroWrapper(const std::shared_ptr<AnalogSourceWrapper>& aAnalogWrapper):
    GyroWrapper("Analog Gyro"),
    mAnalogWrapper(aAnalogWrapper)
{


}

AnalogGyroWrapper::~AnalogGyroWrapper() {

}


void AnalogGyroWrapper::SetAngle(double aAngle)
{
    HALSIM_SetAnalogGyroAngle(mAnalogWrapper->GetHandle(), aAngle);
    GyroWrapper::SetAngle(aAngle);
}
