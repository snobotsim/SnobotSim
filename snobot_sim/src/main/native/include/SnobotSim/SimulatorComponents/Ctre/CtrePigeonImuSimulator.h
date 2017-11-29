/*
 * CtrePigeonImuSimulator.h
 *
 *  Created on: Nov 29, 2017
 *      Author: PJ
 */

#ifndef CTREPIGEONIMUSIMULATOR_H_
#define CTREPIGEONIMUSIMULATOR_H_

#include <memory>

#include "SnobotSim/SimulatorComponents/Gyro/IGyroWrapper.h"
#include "SnobotSim/SimulatorComponents/Accelerometer/IAccelerometerWrapper.h"

class CtrePigeonImuSimulator
{
public:
    CtrePigeonImuSimulator(int aPort);
    virtual ~CtrePigeonImuSimulator();

    std::shared_ptr<IAccelerometerWrapper> GetXWrapper();
    std::shared_ptr<IAccelerometerWrapper> GetYWrapper();
    std::shared_ptr<IAccelerometerWrapper> GetZWrapper();

    std::shared_ptr<IGyroWrapper> GetYawWrapper();
    std::shared_ptr<IGyroWrapper> GetPitchWrapper();
    std::shared_ptr<IGyroWrapper> GetRollWrapper();

protected:
    class PigeonAccelWrapper : public IAccelerometerWrapper
    {
    public:

        PigeonAccelWrapper();

        virtual void SetAcceleration(double aAcceleration);

        virtual double GetAcceleration();

    protected:
        double mAcceleration;
    };

    class PigeonGyroWrapper : public IGyroWrapper
    {
    public:

        PigeonGyroWrapper();

        virtual void SetAngle(double aAngle);

        virtual double GetAngle();

    protected:
        double mAngle;
    };

    std::shared_ptr<IAccelerometerWrapper> mXWrapper;
    std::shared_ptr<IAccelerometerWrapper> mYWrapper;
    std::shared_ptr<IAccelerometerWrapper> mZWrapper;

    std::shared_ptr<IGyroWrapper> mYawWrapper;
    std::shared_ptr<IGyroWrapper> mPitchWrapper;
    std::shared_ptr<IGyroWrapper> mRollWrapper;
};

#endif /* CTREPIGEONIMUSIMULATOR_H_ */
