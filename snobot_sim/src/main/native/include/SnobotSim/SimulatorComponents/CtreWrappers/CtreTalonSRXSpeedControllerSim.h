
#pragma once

#include <memory>
#include <string>
#include <vector>

#include "SnobotSim/SimulatorComponents/SmartSC/BaseCanSmartSpeedController.h"
#include "simulation/SimDeviceSim.h"

class CtreTalonSRXSpeedControllerSim : public BaseCanSmartSpeedController,
                                       public std::enable_shared_from_this<CtreTalonSRXSpeedControllerSim>
{
public:
    static std::shared_ptr<CtreTalonSRXSpeedControllerSim> getMotorControllerWrapper(int aCanPort);

    static const std::string TYPE;
    std::string GetType() override
    {
        return TYPE;
    }

    struct MotionProfilePoint
    {
        const int mIndex;
        const double mPosition;
        const double mVelocity;

        MotionProfilePoint(int aIndex, double aPosition, double aVelocity) :
                mIndex(aIndex),
                mPosition(aPosition),
                mVelocity(aVelocity)
        {
        }
    };

    explicit CtreTalonSRXSpeedControllerSim(int aCanHandle);

    void SetInitialized(bool aIsInitialized) override;

    void setMotionProfilingCommand(double aDemand);
    double getLastClosedLoopError();
    void addMotionProfilePoint(MotionProfilePoint aPoint);

    int getMotionProfileSize();
    MotionProfilePoint getMotionProfilePoint();
    void setLimitSwitchOverride(bool aOverrideFwdLimitSwitch, bool aOverrideRevLimitSwitch);

    void setCurrentProfile(char aProfileSelect);

    int getBinnedPosition();
    int getBinnedVelocity();
    void setCanFeedbackDevice(char aFeedbackDevice);

    ////////////////////////////////////////////////////////////////
    void handleSetDemand();
    void handleSet4();
    void handleConfigSelectedFeedbackSensor();
    void handleSetKP(int aSlot);
    void handleSetKI(int aSlot);
    void handleSetKD(int aSlot);
    void handleSetKF(int aSlot);
    void handleSetIntegralZone(int aSlot);
    void handleSetMotionCruiseVelocity();
    void handleSetMotionAcceleration();
    void handlePushMotionProfileTrajectory();
    void handlePushMotionProfileTrajectory_2();
    void handleSetSelectedSensorPosition();
    void handleSetInverted_2();

    void handleGetMotorOutputPercent();
    void handleGetSelectedSensorPosition();
    void handleGetSelectedSensorVelocity();
    void handleGetClosedLoopError();
    void handleGetMotionProfileStatus();
    void handleGetActiveTrajectoryPosition();
    void handleGetActiveTrajectoryVelocity();
    void handleGetQuadraturePosition();
    void handleGetQuadratureVelocity();

protected:
    double calculateMotionProfileOutput(double aCurrentPosition, double aCurrentVelocity, int aModeType) override;
    double calculateMotionProfileOutput(double aCurrentPosition, double aCurrentVelocity, double aGoalPosition, double aGoalVelocity);

    double getPositionUnitConversion() override
    {
        return 4096;
    }

    double getVelocityUnitConversion()
    {
        return 600;
    }

    double getMotionMagicAccelerationUnitConversion() override
    {
        return 600;
    }

    double getMotionMagicVelocityUnitConversion() override
    {
        return 600;
    }

    std::vector<MotionProfilePoint> mMotionProfilePoints;
    int mMotionProfileProcessedCounter{ 0 };
    int mMotionProfileCurrentPointIndex{ 0 };

    hal::SimDouble mDemandDemand0;
    hal::SimDouble mDemandDemand1;
    hal::SimDouble mDemandMode;
    hal::SimDouble mSet4Demand0;
    hal::SimDouble mSet4Demand1;
    hal::SimDouble mSet4Demand1Type;
    hal::SimDouble mSet4Mode;
    hal::SimDouble mMotorOutputPercent;
    hal::SimDouble mConfigMotionAccelerationSensorUnitsPer100msPerSec;
    hal::SimDouble mConfigMotionCruiseVelocitySensorUnitsPer100ms;

    hal::SimDouble mMotionProfileStatusTopBufferCnt;
    hal::SimDouble mPushMotionProfileTrajectoryPosition;
    hal::SimDouble mPushMotionProfileTrajectoryVelocity;
    hal::SimDouble mPushMotionProfileTrajectory2Position;
    hal::SimDouble mPushMotionProfileTrajectory2Velocity;
    hal::SimDouble mInverted2;
    hal::SimDouble mQuadratureVelocity;
    hal::SimDouble mQuadraturePosition;
    hal::SimDouble mActiveTrajectoryPosition;
    hal::SimDouble mActiveTrajectoryVelocity;

    struct SlottedVariables
    {
        hal::SimDouble mPGain;
        hal::SimDouble mIGain;
        hal::SimDouble mDGain;
        hal::SimDouble mFFGain;
        hal::SimDouble mIZone;
        hal::SimDouble mSelectedSensorPosition;
        hal::SimDouble mSelectedSensorVelocity;
        hal::SimDouble mClosedLoopError;
        hal::SimDouble mConfigSelectedFeedbackSensor;
    };

    static constexpr int NUM_SLOTS = 2;
    SlottedVariables mSlottedVariables[NUM_SLOTS];
};
