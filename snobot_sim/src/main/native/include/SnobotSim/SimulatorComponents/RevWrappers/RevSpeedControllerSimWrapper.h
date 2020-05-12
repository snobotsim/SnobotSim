
#pragma once

#include <string>
#include <vector>

#include "SnobotSim/SimulatorComponents/SmartSC/BaseCanSmartSpeedController.h"

class RevpeedControllerSim : public BaseCanSmartSpeedController
{
public:
    static const std::string TYPE;
    std::string GetType() override
    {
        return TYPE;
    }

    explicit RevpeedControllerSim(int aCanHandle);

    void setCanFeedbackDevice(int aFeedbackDevice);

    void RefreshSettings() override;
    void RefreshOutputs() override;
protected:
    
    
    double calculateMotionProfileOutput(double aCurrentPosition, double aCurrentVelocity, int aModeType) override;

    double getPositionUnitConversion() override
    {
        return 1;
    }

    double getMotionMagicAccelerationUnitConversion() override
    {
        return 1;
    }

    double getMotionMagicVelocityUnitConversion() override
    {
        return 1;
    }
};
