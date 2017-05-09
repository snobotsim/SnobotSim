/*
 * SpeedControllerWrapper.h
 *
 *  Created on: May 3, 2017
 *      Author: PJ
 */

#ifndef SPEEDCONTROLLERWRAPPER_H_
#define SPEEDCONTROLLERWRAPPER_H_

#include <memory>
#include "SnobotSim/MotorSim/IMotorSimulator.h"
#include "SnobotSim/ModuleWrapper/AModuleWrapper.h"

class SpeedControllerWrapper: public AModuleWrapper
{
public:
    SpeedControllerWrapper(int aPort);
    virtual ~SpeedControllerWrapper();

    void SetMotorSimulator(const std::shared_ptr<IMotorSimulator>& aSimulator);

    double GetVoltagePercentage();

    void SetVoltagePercentage(double aSpeed);

    void Update(double aWaitTime);

    double GetPosition();

    double GetVelocity();

    void Reset();

    void Reset(double aPosition, double aVelocity, double aCurrent);

protected:

    std::shared_ptr<IMotorSimulator> mMotorSimulator;

};

#endif /* SPEEDCONTROLLERWRAPPER_H_ */
