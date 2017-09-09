/*
 * CanTalonSpeedController.h
 *
 *  Created on: May 26, 2017
 *      Author: preiniger
 */

#ifndef SRC_CANTALONSPEEDCONTROLLER_H_
#define SRC_CANTALONSPEEDCONTROLLER_H_

//#include "SnobotSim/ModuleWrapper/SpeedControllerWrapper.h"
#include <vector>
#include <memory>

class CanTalonSpeedController//: public SpeedControllerWrapper
{
public:

    enum ControlMode
    {
        ControlMode_ThrottleMode,
        ControlMode_Follower,
        ControlMode_Disabled,
        ControlMode_Unknown
    };

    enum ConnectedSensor
    {
        ConnectedSensor_Encoder,
		ConnectedSensor_Unknown
    };


    CanTalonSpeedController(int aHandle);
    virtual ~CanTalonSpeedController();

    void SmartSet(double aValue);

    void SetControlMode(ControlMode aControlMode);

    void SetFeedbackDevice(ConnectedSensor aSensor);

    void AddFollower(const std::shared_ptr<CanTalonSpeedController>& aFollower);

    void Update(double aWaitTime);

    double GetLastSetValue() const;

    double GetSensorPosition();

    double GetSensorVelocity();

    double GetEncoderPosition();

    double GetEncoderVelocity();

    void SetEncoderPosition(double aPosition);

    void SetEncoderTicksPerRotation(double aDistancePerTicks);

protected:

    int GetFeedbackSensorHandle();

    double mLastSetValue;

    ControlMode mControlMode;
    ConnectedSensor mFeedbackDevice;
    std::vector<std::shared_ptr<CanTalonSpeedController>> mFollowers;
};

#endif /* SRC_CANTALONSPEEDCONTROLLER_H_ */
