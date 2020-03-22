

#include "SnobotSim/SimulatorComponents/LazySimDoubleWrapper.h"

#include "SnobotSim/Logging/SnobotLogger.h"

LazySimDoubleWrapper::LazySimDoubleWrapper(
        const std::string& aDeviceName,
        const std::string& aValueName) :
        mDeviceName(aDeviceName),
        mValueName(aValueName)
{
}

double LazySimDoubleWrapper::get()
{
    setupSimValue();
    if (mSimDouble)
    {
        return mSimDouble.Get();
    }

    SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Could not get sim value for device '" << mDeviceName << "' (" << mDeviceSimHandle << "), value '" << mValueName << "' (" << mSimDouble << ")");
    return 0;
}

void LazySimDoubleWrapper::set(double aValue)
{
    setupSimValue();
    if (mSimDouble)
    {
        mSimDouble.Set(aValue);
    }
    else
    {
        SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Could not get sim value for device '" << mDeviceName << "' (" << mDeviceSimHandle << "), value '" << mValueName << "' (" << mSimDouble << ")");
    }
}

void LazySimDoubleWrapper::setupDeviceSim()
{
    if (mDeviceSimHandle == 0)
    {
        frc::sim::SimDeviceSim::EnumerateDevices(
                mDeviceName.c_str(), [&](const char* name, HAL_SimDeviceHandle handle) {
                    stream << "Getting " << name << std::endl;
                    if (wpi::StringRef(name) == mDeviceName)
                    {
                        mDeviceSimHandle = handle;
                    }
                });
    }
}

void LazySimDoubleWrapper::setupSimValue()
{
    if (!mSimDouble)
    {
        setupDeviceSim();
        if (mDeviceSimHandle != 0)
        {
            mSimDouble = HALSIM_GetSimValueHandle(mDeviceSimHandle, mValueName.c_str());
        }
    }
}
