/*
 * IGyroWrapper.hpp
 *
 *  Created on: May 5, 2017
 *      Author: PJ
 */

#ifndef GYROWRAPPER_HPP_
#define GYROWRAPPER_HPP_

class GyroWrapper
{
public:
    GyroWrapper():
        mAngle(0)
    {

    }
    ~GyroWrapper() {}
    
    void SetAngle(double aAngle)
    {
        mAngle = aAngle;
    }
    
    double GetAngle()
    {
        return mAngle;
    }
    
protected:
    double mAngle;
};



#endif /* GYROWRAPPER_HPP_ */
