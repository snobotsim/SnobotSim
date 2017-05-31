/*
 * StaticLoadDcMotorSim.h
 *
 *  Created on: May 9, 2017
 *      Author: preiniger
 */

#ifndef STATICLOADDCMOTORSIM_H_
#define STATICLOADDCMOTORSIM_H_

#include "SnobotSim/MotorSim/BaseDcMotorSimulator.h"

class StaticLoadDcMotorSim: public BaseDcMotorSimulator
{
public:
    StaticLoadDcMotorSim(const DcMotorModel& aMotorModel, double aLoad, double aConversionFactor = 1);
    virtual ~StaticLoadDcMotorSim();

    void Update(double aCycleTime) override;

protected:
    const double mLoad;
};

#endif /* STATICLOADDCMOTORSIM_H_ */
