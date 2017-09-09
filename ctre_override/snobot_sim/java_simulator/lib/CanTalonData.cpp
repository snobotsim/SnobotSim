
#include "CanTalonDataInternal.h"
#include "NotifyCallbackHelpers.h"
#include <iostream>

using namespace SnobotSim;


void CanTalonData::HandleInitialized()
{
    std::cout << "CanTalonData::HandleInitialized..." << std::endl;

    HAL_Value value = MakeBoolean(true);
    InvokeCallback(mCallbacks, "Initialized", &value);
}


void CanTalonData::HandleSet(double aValue)
{
    double oldValue = mVoltagePercentage.exchange(aValue);
    if(oldValue != aValue)
    {
        std::cout << "CanTalonData::HandleSet... " << aValue << std::endl;
    }
}
void CanTalonData::SetParam(int aParam, double aValue)
{
    std::cout << "CanTalonData::SetParam..." << std::endl;
}
void CanTalonData::SetFeedbackDeviceSelect(int aParam)
{
    std::cout << "CanTalonData::SetFeedbackDeviceSelect..." << std::endl;
}
void CanTalonData::SetModeSelect(int aParam)
{
    std::cout << "CanTalonData::SetModeSelect..." << std::endl;
}

void CanTalonData::SetModeSelect(int aParam, int demand)
{
    std::cout << "CanTalonData::SetModeSelect..." << std::endl;
}

void CanTalonData::SetDemand(int aParam)
{
    std::cout << "CanTalonData::SetDemand..." << std::endl;
}


double CanTalonData::GetAppliedThrottle()
{
    return mVoltagePercentage * 1023;
}
double CanTalonData::GetSensorPosition()
{
    std::cout << "CanTalonData::GetSensorPosition..." << std::endl;
    return 0;
}
double CanTalonData::GetSensorVelocity()
{
    std::cout << "CanTalonData::GetSensorVelocity..." << std::endl;
    return 0;
}
double CanTalonData::GetCurrent()
{
    std::cout << "CanTalonData::GetCurrent..." << std::endl;
    return 0;
}
double CanTalonData::GetEncPosition()
{
    std::cout << "CanTalonData::GetEncPosition..." << std::endl;
    return 0;
}
double CanTalonData::GetEncVel()
{
    std::cout << "CanTalonData::GetEncVel..." << std::endl;
    return 0;
}
