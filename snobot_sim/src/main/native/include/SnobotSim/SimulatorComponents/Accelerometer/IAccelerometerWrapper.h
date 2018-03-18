/*
 * AccelerometerWrapper.h
 *
 *  Created on: May 7, 2017
 *      Author: PJ
 */

#ifndef ACCELEROMETERWRAPPER_H_
#define ACCELEROMETERWRAPPER_H_

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

#endif /* ACCELEROMETERWRAPPER_H_ */
