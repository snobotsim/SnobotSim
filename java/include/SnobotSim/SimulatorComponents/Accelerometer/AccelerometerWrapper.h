/*
 * AccelerometerWrapper.h
 *
 *  Created on: May 7, 2017
 *      Author: PJ
 */

#ifndef ACCELEROMETERWRAPPER_H_
#define ACCELEROMETERWRAPPER_H_


class AccelerometerWrapper
{
public:
    AccelerometerWrapper();
    virtual ~AccelerometerWrapper();
    
    void SetAcceleration(double aAcceleration)
    {
        mAcceleration = aAcceleration;
    }
    
    double GetAcceleration()
    {
        return mAcceleration;
    }
    
protected:
    double mAcceleration;
};

#endif /* ACCELEROMETERWRAPPER_H_ */
