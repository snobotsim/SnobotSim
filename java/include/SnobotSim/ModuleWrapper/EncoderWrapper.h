/*
 * EncoderWrapper.h
 *
 *  Created on: May 4, 2017
 *      Author: PJ
 */

#ifndef INCLUDE_ENCODERWRAPPER_H_
#define INCLUDE_ENCODERWRAPPER_H_

#include "SnobotSim/ModuleWrapper/AModuleWrapper.h"
#include "SnobotSim/ModuleWrapper/SpeedControllerWrapper.h"

class EncoderWrapper: public AModuleWrapper
{
public:
    EncoderWrapper(int aPortA, int aPortB);
    virtual ~EncoderWrapper();

    void Reset();

    int GetRaw();

    double GetDistance();

    bool IsHookedUp();

    void SetSpeedController(const std::shared_ptr<SpeedControllerWrapper>& aMotorWrapper);

protected:

    std::shared_ptr<SpeedControllerWrapper> mMotorWrapper;
    double mEncodingFactor;
    double mDistancePerTick;
};

#endif /* INCLUDE_ENCODERWRAPPER_H_ */
