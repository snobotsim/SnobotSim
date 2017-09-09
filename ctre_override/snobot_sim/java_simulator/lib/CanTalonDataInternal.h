
#include <atomic>
#include <memory>
#include "MockData/NotifyListener.h"
#include "MockData/NotifyListenerVector.h"

namespace SnobotSim
{


class CanTalonData
{
public:
    void HandleInitialized();

    void HandleSet(double aValue);
    void SetParam(int aParam, double aValue);
    void SetFeedbackDeviceSelect(int aParam);
    void SetModeSelect(int aParam);
    void SetModeSelect(int aParam, int demand);
    void SetDemand(int aParam);

    double GetAppliedThrottle();
    double GetSensorPosition();
    double GetSensorVelocity();
    double GetCurrent();
    double GetEncPosition();
    double GetEncVel();

private:
    std::mutex mRegisterMutex;
    std::shared_ptr<hal::NotifyListenerVector> mCallbacks = nullptr;

    std::atomic<HAL_Bool> mInitialized{false};
    std::atomic<double> mVoltagePercentage{0.0};
};

//extern CanTalonData SimCanTalonData[];

}
