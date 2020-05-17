
#pragma once

#include <string>
#include <vector>

#include "SnobotSim/SimulatorComponents/SmartSC/BaseCanSmartSpeedController.h"
#include "simulation/SimDeviceSim.h"

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

    void handleSetSetpointCommand();
    void handleSetSensorType();
    void handleSetFeedbackDevice();
    void handleSetPGain(int slot);
    void handleSetIGain(int slot);
    void handleSetDGain(int slot);
    void handleSetFFGain(int slot);
    void handleGetAppliedOutput();
    void handleGetEncoderPosition();
    void handleGetEncoderVelocity();

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

    hal::SimDouble  mSetpointCommandCtrl;
    hal::SimDouble  mSetpointCommandValue;
    hal::SimDouble  mAppliedOutputAppliedOutput;
    hal::SimDouble  mSensorTypeSensorType;
    hal::SimDouble  mFeedbackDeviceSensorID;
    hal::SimDouble  mEncoderPosition;
    hal::SimDouble  mEncoderVelocity;

    struct SlottedVariables
    {
        hal::SimDouble  m_P_gain;
        hal::SimDouble  m_I_gain;
        hal::SimDouble  m_D_gain;
        hal::SimDouble  m_FF_gain;
    };

    static constexpr int NUM_SLOTS = 6;
    SlottedVariables mSlottedVariables[NUM_SLOTS];
};
