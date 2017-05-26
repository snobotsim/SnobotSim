/*
 * CanTalonSpeedController.h
 *
 *  Created on: May 26, 2017
 *      Author: preiniger
 */

#ifndef SRC_CANTALONSPEEDCONTROLLER_H_
#define SRC_CANTALONSPEEDCONTROLLER_H_

#include "SnobotSim/ModuleWrapper/SpeedControllerWrapper.h"
#include <vector>

class CanTalonSpeedController: public SpeedControllerWrapper
{
public:

    enum ControlMode
    {
        ControlMode_ThrottleMode,
        ControlMode_Follower,
        ControlMode_Disabled,
        ControlMode_Unknown
    };


    CanTalonSpeedController(int aHandle);
    virtual ~CanTalonSpeedController();

    void SmartSet(double aValue);

    void SetControlMode(ControlMode aControlMode);

    void AddFollower(const std::shared_ptr<CanTalonSpeedController>& aFollower);

    void Update(double aWaitTime) override;

    double GetLastSetValue() const;

protected:

    double mLastSetValue;

    ControlMode mControlMode;
    std::vector<std::shared_ptr<CanTalonSpeedController>> mFollowers;
};

#endif /* SRC_CANTALONSPEEDCONTROLLER_H_ */
