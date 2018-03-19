/*
 * IGyroWrapper.hpp
 *
 *  Created on: May 5, 2017
 *      Author: PJ
 */

#ifndef SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_GYRO_IGYROWRAPPER_H_
#define SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_GYRO_IGYROWRAPPER_H_

#include "SnobotSim/ModuleWrapper/AModuleWrapper.h"

class EXPORT_ IGyroWrapper: public AModuleWrapper
{
public:
    IGyroWrapper(const std::string& aName) :
        AModuleWrapper(aName)
    {

    }
    virtual ~IGyroWrapper()
    {

    }

    virtual void SetAngle(double aAngle) = 0;

    virtual double GetAngle() = 0;
};



#endif  // SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_GYRO_IGYROWRAPPER_H_
