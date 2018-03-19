/*
 * BaseNavxWrapper.h
 *
 *  Created on: Nov 23, 2017
 *      Author: PJ
 */

#ifndef SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_NAVXWRAPPERS_BASENAVXWRAPPER_H_
#define SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_NAVXWRAPPERS_BASENAVXWRAPPER_H_

#include "NavxSim/NavxSimulator.h"
#include "SnobotSim/SimulatorComponents/Accelerometer/IAccelerometerWrapper.h"
#include "SnobotSim/SimulatorComponents/Gyro/IGyroWrapper.h"

class BaseNavxWrapper
{
public:
    BaseNavxWrapper(int aBasePort, const std::shared_ptr<NavxSimulator>& aNavx);
    virtual ~BaseNavxWrapper();

    class AccelerometerWrapper : public IAccelerometerWrapper
    {
    public:
        enum AxisType
        {
            AXIS_X,
            AXIS_Y,
            AXIS_Z
        };

        AccelerometerWrapper(AxisType aAxisType, const std::shared_ptr<NavxSimulator>& aNavx);

        void SetAcceleration(double aAcceleration) override;

        double GetAcceleration() override;

        AxisType mAxisType;
        std::shared_ptr<NavxSimulator> mNavx;
    };
    class GyroWrapper : public IGyroWrapper
    {
    public:
        enum AxisType
        {
            AXIS_YAW,
            AXIS_PITCH,
            AXIS_ROLL
        };

        GyroWrapper(AxisType aAxisType, const std::shared_ptr<NavxSimulator>& aAccel);

        void SetAngle(double aAngle) override;

        double GetAngle() override;

        AxisType mAxisType;
        std::shared_ptr<NavxSimulator> mNavx;
    };

    std::shared_ptr<AccelerometerWrapper> mXWrapper;
    std::shared_ptr<AccelerometerWrapper> mYWrapper;
    std::shared_ptr<AccelerometerWrapper> mZWrapper;

    std::shared_ptr<GyroWrapper> mYawWrapper;
    std::shared_ptr<GyroWrapper> mPitchWrapper;
    std::shared_ptr<GyroWrapper> mRollWrapper;
};

#endif // SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_NAVXWRAPPERS_BASENAVXWRAPPER_H_
