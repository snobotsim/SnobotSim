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
#include "SnobotSim/SimulatorComponents/IFeedbackSensor.h"

class EXPORT_ EncoderWrapper: public std::enable_shared_from_this<EncoderWrapper>,
                              public AModuleWrapper,
                              public IFeedbackSensor
{
public:
    EncoderWrapper(int aPortA, int aPortB);
    EncoderWrapper(int aHandle, const std::string& aName);
    virtual ~EncoderWrapper();

    void Reset();

    int GetRaw();

    double GetDistance();

    double GetVelocity();

    bool IsHookedUp();

    void SetSpeedController(const std::shared_ptr<SpeedControllerWrapper>& aMotorWrapper);

    const std::shared_ptr<SpeedControllerWrapper>& GetSpeedController();

    void SetDistancePerTick(double aDPT);

    double GetDistancePerTick();

    double GetPosition() override;

    void SetPosition(double aPosition) override;

protected:

    std::shared_ptr<SpeedControllerWrapper> mMotorWrapper;
    double mEncodingFactor;
    double mDistancePerTick;
    const int mHandle;
};

#endif /* INCLUDE_ENCODERWRAPPER_H_ */
