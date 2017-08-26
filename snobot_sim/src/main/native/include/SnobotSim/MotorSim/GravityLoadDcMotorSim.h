/*
 * GravityLoadDcMotorSim.h
 *
 *  Created on: May 9, 2017
 *      Author: preiniger
 */

#ifndef GRAVITYLOADDCMOTORSIM_H_
#define GRAVITYLOADDCMOTORSIM_H_

#include "SnobotSim/MotorSim/BaseDcMotorSimulator.h"

class GravityLoadDcMotorSim: public BaseDcMotorSimulator
{
public:

    GravityLoadDcMotorSim(const DcMotorModel& aMotorModel, double aLoad);
    virtual ~GravityLoadDcMotorSim();

    void Update(double aCycleTime) override;

    double GetLoad();

protected:
    const double mLoad;

    static const double sGRAVITY;
};

#endif /* GRAVITYLOADDCMOTORSIM_H_ */
