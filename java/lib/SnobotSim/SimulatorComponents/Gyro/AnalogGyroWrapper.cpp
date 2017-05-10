/*
 * AnalogGyrWrapper.cpp
 *
 *  Created on: May 5, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/Gyro/AnalogGyroWrapper.h"
#include <iostream>

AnalogGyroWrapper::AnalogGyroWrapper(const std::shared_ptr<AnalogSourceWrapper>& aAnalogWrapper):
    GyroWrapper("Analog Gyro"),
    mAnalogWrapper(aAnalogWrapper)
{


}

AnalogGyroWrapper::~AnalogGyroWrapper() {

}
