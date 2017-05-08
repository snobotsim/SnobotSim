/*
 * AnalogGyrWrapper.cpp
 *
 *  Created on: May 5, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/Gyro/AnalogGyroWrapper.h"
#include <iostream>

AnalogGyroWrapper::AnalogGyroWrapper(const std::shared_ptr<AnalogSourceWrapper>& aAnalogWrapper):
    mAnalogWrapper(aAnalogWrapper)
{


}

AnalogGyroWrapper::~AnalogGyroWrapper() {

}
