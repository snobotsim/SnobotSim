/*
 * IFeedbackSensor.hpp
 *
 *  Created on: Sep 10, 2017
 *      Author: PJ
 */

#ifndef IFEEDBACKSENSOR_HPP_
#define IFEEDBACKSENSOR_HPP_

class IFeedbackSensor
{
public:
    virtual void SetPosition(double aPosition) = 0;
    virtual double GetPosition() = 0;
};

class NullFeedbackSensor : public IFeedbackSensor
{
    void SetPosition(double aPosition)
    {

    }
    double GetPosition()
    {
        return 0;
    }
};


#endif /* IFEEDBACKSENSOR_HPP_ */
