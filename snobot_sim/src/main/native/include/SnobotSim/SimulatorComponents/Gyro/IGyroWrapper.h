/*
 * IGyroWrapper.hpp
 *
 *  Created on: May 5, 2017
 *      Author: PJ
 */

#ifndef GYROWRAPPER_HPP_
#define GYROWRAPPER_HPP_

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



#endif /* GYROWRAPPER_HPP_ */
