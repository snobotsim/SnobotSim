/*
* AnalogGyrWrapper.h
 *
 *  Created on: May 5, 2017
 *      Author: PJ
 */

#ifndef SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_GYRO_ANALOGGYROWRAPPER_H_
#define SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_GYRO_ANALOGGYROWRAPPER_H_

#include <memory>

#include "SnobotSim/ExportHelper.h"
#include "SnobotSim/ModuleWrapper/AnalogSourceWrapper.h"
#include "SnobotSim/SimulatorComponents/Gyro/IGyroWrapper.h"

class EXPORT_ AnalogGyroWrapper : public IGyroWrapper
{
public:
    explicit AnalogGyroWrapper(const std::shared_ptr<AnalogSourceWrapper>& aAnalogWrapper);
    virtual ~AnalogGyroWrapper();

    void SetAngle(double aAngle) override;

    double GetAngle() override;

protected:
    std::shared_ptr<AnalogSourceWrapper> mAnalogWrapper;
    double mAngle;
};

#endif // SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_GYRO_ANALOGGYROWRAPPER_H_
