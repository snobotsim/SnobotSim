/*
 * AccelerometerWrapper.h
 *
 *  Created on: May 7, 2017
 *      Author: PJ
 */

#ifndef ACCELEROMETERWRAPPER_H_
#define ACCELEROMETERWRAPPER_H_

#include "SnobotSim/ModuleWrapper/AModuleWrapper.h"

class EXPORT_ AccelerometerWrapper: public AModuleWrapper
{
public:
    AccelerometerWrapper(const std::string& aName);
    virtual ~AccelerometerWrapper();
    
    void SetAcceleration(double aAcceleration);
    
    double GetAcceleration();
    
protected:
    double mAcceleration;
};

#endif /* ACCELEROMETERWRAPPER_H_ */
