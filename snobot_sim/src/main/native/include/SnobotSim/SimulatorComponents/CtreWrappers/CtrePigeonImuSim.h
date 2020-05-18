
#pragma once

#include <memory>
#include <string>

#include "SnobotSim/ModuleWrapper/AModuleWrapper.h"
#include "SnobotSim/ModuleWrapper/Interfaces/IAccelerometerWrapper.h"
#include "SnobotSim/ModuleWrapper/Interfaces/IGyroWrapper.h"
#include "simulation/SimDeviceSim.h"

class CtrePigeonImuSim : public AModuleWrapper
{
public:
    static constexpr int CTRE_OFFSET = 400;
    static const std::string TYPE;
    std::string GetType() override
    {
        return TYPE;
    }

    CtrePigeonImuSim(int canHandle, int abasePort);

    void SetInitialized(bool aIsInitialized) override;

    std::shared_ptr<IGyroWrapper> getYawWrapper();
    std::shared_ptr<IGyroWrapper> getPitchWrapper();
    std::shared_ptr<IGyroWrapper> getRollWrapper();

    std::shared_ptr<IAccelerometerWrapper> getXWrapper();
    std::shared_ptr<IAccelerometerWrapper> getYWrapper();
    std::shared_ptr<IAccelerometerWrapper> getZWrapper();

    ///////////////////////////////////////////////
    void handleSetYaw();
    void handleGetRawGyro();
    void handleGetYawPitchRoll();
    void handleGetFusedHeading();
    ///////////////////////////////////////////////
protected:
    std::shared_ptr<IGyroWrapper> mYaw;
    std::shared_ptr<IGyroWrapper> mPitch;
    std::shared_ptr<IGyroWrapper> mRoll;

    std::shared_ptr<IAccelerometerWrapper> mX;
    std::shared_ptr<IAccelerometerWrapper> mY;
    std::shared_ptr<IAccelerometerWrapper> mZ;

    int mCanHandle;


    hal::SimDouble mYawAngleDeg;
    hal::SimDouble mRawGyroXyzDps0;
    hal::SimDouble mRawGyroXyzDps1;
    hal::SimDouble mRawGyroXyzDps2;
    hal::SimDouble mYawPitchRollYpr0;
    hal::SimDouble mYawPitchRollYpr1;
    hal::SimDouble mYawPitchRollYpr2;
    hal::SimDouble mFusedHeading2Value;
};
