/*
 * CanTalonSpeedController.cpp
 *
 *  Created on: May 26, 2017
 *      Author: preiniger
 */

#include "CanTalonSpeedController.h"
#include "SnobotSim/SensorActuatorRegistry.h"
#include "SnobotSim/ModuleWrapper/EncoderWrapper.h"
#include "SnobotSim/Logging/SnobotLogger.h"

CanTalonSpeedController::CanTalonSpeedController(int aHandle) :
    SpeedControllerWrapper(aHandle),
    mLastSetValue(0)
{
    SetName("CAN " + GetName());

    mControlMode = ControlMode_Unknown;
    mFeedbackDevice = ConnectedSensor_Encoder; // TODO maybe change to unknown
}

CanTalonSpeedController::~CanTalonSpeedController()
{

}

void CanTalonSpeedController::SmartSet(double aValue)
{
    mLastSetValue = aValue;
}

double CanTalonSpeedController::GetLastSetValue() const
{
    return mLastSetValue;
}


double CanTalonSpeedController::GetSensorPosition()
{
	if(mFeedbackDevice != ConnectedSensor_Encoder)
	{

	}
	return GetPosition();
}

double CanTalonSpeedController::GetSensorVelocity()
{
	if(mFeedbackDevice != ConnectedSensor_Encoder)
	{
		LOG_UNSUPPORTED_WITH_MESSAGE("Feedback device not supported");
	}
	return GetVelocity();
}

double CanTalonSpeedController::GetEncoderPosition()
{
	if(mFeedbackDevice == ConnectedSensor_Encoder)
	{
		return GetPosition();
	}

	SNOBOT_LOG(SnobotLogging::WARN, "Feedback device is not an encoder");
	return 0;
}

double CanTalonSpeedController::GetEncoderVelocity()
{
	if(mFeedbackDevice == ConnectedSensor_Encoder)
	{
		return GetVelocity();
	}

	SNOBOT_LOG(SnobotLogging::WARN, "Feedback device is not an encoder");
	return 0;
}

void CanTalonSpeedController::SetControlMode(ControlMode aControlMode)
{
    mControlMode = aControlMode;

    switch(mControlMode)
    {
    case ControlMode_Disabled:
    {
        break;
    }
    case ControlMode_ThrottleMode:
    {
        SetVoltagePercentage(mLastSetValue);
        break;
    }
    case ControlMode_Follower:
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "This shouldn't be called directly");
        break;
    }
    default:
        SNOBOT_LOG(SnobotLogging::CRITICAL, "Unknown control mode");
    }
}

void CanTalonSpeedController::SetFeedbackDevice(ConnectedSensor aSensor)
{
	mFeedbackDevice = aSensor;

    switch(aSensor)
    {
    case ConnectedSensor_Encoder:
    {
        int encoderHandle = mId | 0xF0000000;
        std::string encoderName = "CAN Encoder " + std::to_string(mId);
        SensorActuatorRegistry::Get().Register(encoderHandle, std::shared_ptr<EncoderWrapper>(new EncoderWrapper(encoderName)));
        break;
    }
    case ConnectedSensor_Unknown:
    {
        SetVoltagePercentage(mLastSetValue);
        break;
    }
    default:
        SNOBOT_LOG(SnobotLogging::CRITICAL, "Unknown control mode");
    }
}

void CanTalonSpeedController::AddFollower(const std::shared_ptr<CanTalonSpeedController>& aFollower)
{
    mFollowers.push_back(aFollower);
}

void CanTalonSpeedController::Update(double aWaitTime)
{
    SpeedControllerWrapper::Update(aWaitTime);

    for(unsigned int i = 0; i < mFollowers.size(); ++i)
    {
        mFollowers[i]->SetVoltagePercentage(GetVoltagePercentage());
    }
}



