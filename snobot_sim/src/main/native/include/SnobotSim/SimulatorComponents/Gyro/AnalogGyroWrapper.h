/*
* AnalogGyrWrapper.h
 *
 *  Created on: May 5, 2017
 *      Author: PJ
 */

#ifndef ANALOGGYROWRAPPER_H_
#define ANALOGGYROWRAPPER_H_

#include "SnobotSim/ExportHelper.h"
#include "SnobotSim/SimulatorComponents/Gyro/GyroWrapper.h"
#include "SnobotSim/ModuleWrapper/AnalogSourceWrapper.h"
#include <memory>

class EXPORT_ AnalogGyroWrapper: public GyroWrapper
{
public:
    AnalogGyroWrapper(const std::shared_ptr<AnalogSourceWrapper>& aAnalogWrapper);
    virtual ~AnalogGyroWrapper();


    void SetAngle(double aAngle) override;

protected:

    std::shared_ptr<AnalogSourceWrapper> mAnalogWrapper;

};

#endif /* ANALOGGYROWRAPPER_H_ */
