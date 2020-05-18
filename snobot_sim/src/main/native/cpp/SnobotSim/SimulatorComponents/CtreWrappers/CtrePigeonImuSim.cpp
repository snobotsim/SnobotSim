
#include "SnobotSim/SimulatorComponents/CtreWrappers/CtrePigeonImuSim.h"

#include "SnobotSim/SensorActuatorRegistry.h"
#include <iostream>

namespace
{
hal::SimDouble getSimDouble(HAL_SimDeviceHandle aDeviceHandle, std::string aName)
{
    hal::SimDouble output = HALSIM_GetSimValueHandle(aDeviceHandle, aName.c_str());
    if (!output)
    {
//        throw new IllegalArgumentException("Sim device '" + aName + "' does not exist");
        SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Sim device '" << aName << "' does not exist");
    }

    return output;
}

class PigeonGyroWrapper : public AModuleWrapper, public IGyroWrapper
{
public:
    double mAngle{ 0 };
    double mAngleOffset{ 0 };

    static const std::string TYPE;
    std::string GetType() override
    {
        return TYPE;
    }

    explicit PigeonGyroWrapper(const std::string& aName) :
            AModuleWrapper(aName)
    {
    }

    // public void setDesiredYaw(double aOffset)
    // {
    //     mAngleOffset = aOffset - mAngle;
    // }

    double GetAngle() override
    {
        return mAngle + mAngleOffset;
    }

    void SetAngle(double aAngle) override
    {
        mAngle = aAngle;
    }
};

class PigeonAccelWrapper : public AModuleWrapper, public IAccelerometerWrapper
{
public:
    double mAccel{ 0 };

    explicit PigeonAccelWrapper(const std::string& aName) :
            AModuleWrapper(aName)
    {
    }

    double GetAcceleration() override
    {
        return mAccel;
    }

    void SetAcceleration(double aAccel) override
    {
        mAccel = aAccel;
    }
    static const std::string TYPE;
    std::string GetType() override
    {
        return TYPE;
    }
};

const std::string PigeonGyroWrapper::TYPE = "PigeonGyro";
const std::string PigeonAccelWrapper::TYPE = "PigeonAccel";

} // namespace

const std::string CtrePigeonImuSim::TYPE = "Pigeon";

CtrePigeonImuSim::CtrePigeonImuSim(int canHandle, int aBasePort) :
        AModuleWrapper("Pigeon IMU"),

        mYaw(new PigeonGyroWrapper("Pigeon Yaw")),
        mPitch(new PigeonGyroWrapper("Pigeon Pitch")),
        mRoll(new PigeonGyroWrapper("Pigeon Roll")),
        mX(new PigeonAccelWrapper("Pigeon X")),
        mY(new PigeonAccelWrapper("Pigeon Y")),
        mZ(new PigeonAccelWrapper("Pigeon Z")),
        mCanHandle(canHandle)

{
    SensorActuatorRegistry::Get().Register(aBasePort + 0, mX);
    SensorActuatorRegistry::Get().Register(aBasePort + 1, mY);
    SensorActuatorRegistry::Get().Register(aBasePort + 2, mZ);

    SensorActuatorRegistry::Get().Register(aBasePort + 0, mYaw);
    SensorActuatorRegistry::Get().Register(aBasePort + 1, mPitch);
    SensorActuatorRegistry::Get().Register(aBasePort + 2, mRoll);
}

void CtrePigeonImuSim::SetInitialized(bool aIsInitialized)
{
    AModuleWrapper::SetInitialized(aIsInitialized);

    mX->SetInitialized(aIsInitialized);
    mY->SetInitialized(aIsInitialized);
    mZ->SetInitialized(aIsInitialized);

    mYaw->SetInitialized(aIsInitialized);
    mPitch->SetInitialized(aIsInitialized);
    mRoll->SetInitialized(aIsInitialized);

    std::string deviceNum = std::to_string(mCanHandle);
    std::string deviceName = "CtrePigeonIMUWrapper " + deviceNum + "[" + deviceNum + "]";
    std::cout << "XXXX " << deviceName << std::endl;
    HAL_SimDeviceHandle deviceSimHandle{ 0 };
    frc::sim::SimDeviceSim::EnumerateDevices(
            deviceName.c_str(), [&](const char* name, HAL_SimDeviceHandle handle) {
                if (wpi::StringRef(name) == deviceName)
                {
                    deviceSimHandle = handle;
                }
            });

    if (deviceSimHandle)
    {
        mYawAngleDeg = getSimDouble(deviceSimHandle, "Yaw_angleDeg");
        mRawGyroXyzDps0 = getSimDouble(deviceSimHandle, "RawGyro_xyz_dps_0");
        mRawGyroXyzDps1 = getSimDouble(deviceSimHandle, "RawGyro_xyz_dps_1");
        mRawGyroXyzDps2 = getSimDouble(deviceSimHandle, "RawGyro_xyz_dps_2");
        mYawPitchRollYpr0 = getSimDouble(deviceSimHandle, "YawPitchRoll_ypr_0");
        mYawPitchRollYpr1 = getSimDouble(deviceSimHandle, "YawPitchRoll_ypr_1");
        mYawPitchRollYpr2 = getSimDouble(deviceSimHandle, "YawPitchRoll_ypr_2");
        mFusedHeading2Value = getSimDouble(deviceSimHandle, "FusedHeading1_value");
    }
}

std::shared_ptr<IGyroWrapper> CtrePigeonImuSim::getYawWrapper()
{
    return mYaw;
}
std::shared_ptr<IGyroWrapper> CtrePigeonImuSim::getPitchWrapper()
{
    return mPitch;
}
std::shared_ptr<IGyroWrapper> CtrePigeonImuSim::getRollWrapper()
{
    return mRoll;
}

std::shared_ptr<IAccelerometerWrapper> CtrePigeonImuSim::getXWrapper()
{
    return mX;
}
std::shared_ptr<IAccelerometerWrapper> CtrePigeonImuSim::getYWrapper()
{
    return mY;
}
std::shared_ptr<IAccelerometerWrapper> CtrePigeonImuSim::getZWrapper()
{
    return mZ;
}

///////////////////////////////////////////////
void CtrePigeonImuSim::handleSetYaw()
{
    SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Unsupported");
//    ((CtrePigeonImuSim.PigeonGyroWrapper) getYawWrapper()).setDesiredYaw(mYawAngleDeg.Get());
}


void CtrePigeonImuSim::handleGetRawGyro()
{
    mRawGyroXyzDps0.Set(getYawWrapper()->GetAngle());
    mRawGyroXyzDps1.Set(getPitchWrapper()->GetAngle());
    mRawGyroXyzDps2.Set(getRollWrapper()->GetAngle());
}

void CtrePigeonImuSim::handleGetYawPitchRoll()
{
    mYawPitchRollYpr0.Set(getYawWrapper()->GetAngle());
    mYawPitchRollYpr1.Set(getPitchWrapper()->GetAngle());
    mYawPitchRollYpr2.Set(getRollWrapper()->GetAngle());
}

void CtrePigeonImuSim::handleGetFusedHeading()
{
    mFusedHeading2Value.Set(getYawWrapper()->GetAngle());
}
///////////////////////////////////////////////
