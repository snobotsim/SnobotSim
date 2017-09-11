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
#include "SnobotSim/SimulatorComponents/IFeedbackSensor.h"
#include "SnobotSim/ModuleWrapper/AModuleWrapper.h"

class EXPORT_ SpeedControllerWrapper: public AModuleWrapper
{
public:
    SpeedControllerWrapper(int aPort);
    virtual ~SpeedControllerWrapper();

    virtual int GetId();

    virtual void SetMotorSimulator(const std::shared_ptr<IMotorSimulator>& aSimulator);

    virtual const std::shared_ptr<IMotorSimulator>& GetMotorSimulator();

    virtual void SetFeedbackSensor(const std::shared_ptr<IFeedbackSensor>& aSimulator);

    virtual const std::shared_ptr<IFeedbackSensor>& GetFeedbackSensor();

    virtual double GetVoltagePercentage();

    virtual void SetVoltagePercentage(double aSpeed);

    virtual void Update(double aWaitTime);

    virtual double GetPosition();

    virtual double GetVelocity();

    virtual double GetCurrent();

    virtual double GetAcceleration();

    virtual void Reset();

    virtual void Reset(double aPosition, double aVelocity, double aCurrent);

protected:

    int mId;
    std::shared_ptr<IMotorSimulator> mMotorSimulator;
    std::shared_ptr<IFeedbackSensor> mFeedbackSensor;

};

#endif /* SPEEDCONTROLLERWRAPPER_H_ */
