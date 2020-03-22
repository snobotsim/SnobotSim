/*
 * BaseAdxAccelWrapper.h
 *
 *  Created on: Nov 23, 2017
 *      Author: PJ
 */

#pragma once
#include <memory>

#include "SnobotSim/ModuleWrapper/AModuleWrapper.h"
#include "SnobotSim/ModuleWrapper/Interfaces/IAccelerometerWrapper.h"
#include "SnobotSim/SimulatorComponents/LazySimDoubleWrapper.h"

class BaseAdxAccelWrapper
{
public:
    BaseAdxAccelWrapper(const std::string& aBaseName, const std::string& aDeviceName, int aBasePort);
    virtual ~BaseAdxAccelWrapper();

protected:
    class AccelerometerWrapper : public AModuleWrapper, public IAccelerometerWrapper
    {
    public:

        AccelerometerWrapper(const LazySimDoubleWrapper& aSimWrapper);

        void SetAcceleration(double aAcceleration) override;

        double GetAcceleration() override;

        LazySimDoubleWrapper mSimWrapper;
    };

    std::shared_ptr<AccelerometerWrapper> mXWrapper;
    std::shared_ptr<AccelerometerWrapper> mYWrapper;
    std::shared_ptr<AccelerometerWrapper> mZWrapper;
};
