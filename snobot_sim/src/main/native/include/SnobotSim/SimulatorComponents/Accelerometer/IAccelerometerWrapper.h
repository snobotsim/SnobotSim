/*
 * AccelerometerWrapper.h
 *
 *  Created on: May 7, 2017
 *      Author: PJ
 */

#ifndef SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_ACCELEROMETER_IACCELEROMETERWRAPPER_H_
#define SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_ACCELEROMETER_IACCELEROMETERWRAPPER_H_

#include "SnobotSim/ModuleWrapper/AModuleWrapper.h"

class EXPORT_ IAccelerometerWrapper: public AModuleWrapper
{
public:
    IAccelerometerWrapper(const std::string& aName):
        AModuleWrapper(aName)
    {

    }

    virtual ~IAccelerometerWrapper()
    {

    }

    virtual void SetAcceleration(double aAcceleration) = 0;

    virtual double GetAcceleration() = 0;
};

#endif  // SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_ACCELEROMETER_IACCELEROMETERWRAPPER_H_
