/*
* AnalogGyrWrapper.h
 *
 *  Created on: May 5, 2017
 *      Author: PJ
 */

#ifndef ANALOGGYROWRAPPER_H_
#define ANALOGGYROWRAPPER_H_

#include "SnobotSim/ExportHelper.h"
#include "SnobotSim/SimulatorComponents/Gyro/IGyroWrapper.h"
#include "SnobotSim/ModuleWrapper/AnalogSourceWrapper.h"
#include <memory>

class EXPORT_ AnalogGyroWrapper: public IGyroWrapper
{
public:
    AnalogGyroWrapper(const std::shared_ptr<AnalogSourceWrapper>& aAnalogWrapper);
    virtual ~AnalogGyroWrapper();


    void SetAngle(double aAngle) override;

    double GetAngle() override;

protected:

    std::shared_ptr<AnalogSourceWrapper> mAnalogWrapper;
    double mAngle;

};

#endif /* ANALOGGYROWRAPPER_H_ */
