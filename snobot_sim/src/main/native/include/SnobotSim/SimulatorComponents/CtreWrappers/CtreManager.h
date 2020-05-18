
#pragma once

#include <map>
#include <memory>
#include <string>
#include <functional>

class CtreTalonSRXSpeedControllerSim;
class CtrePigeonImuSim;

class CtreManager
{
public:

    CtreManager();
    void Reset();

    void handleMotorControllerMessage(const std::string& aCallback, int aCanPort, uint8_t* aBuffer, int aLength);
    void handlePigeonMessage(const std::string& aCallback, int aCanPort, uint8_t* aBuffer, int aLength);
    void handleCanifierMessage(const std::string& aCallback, int aCanPort, uint8_t* aBuffer, int aLength);
    void handleCanCoderMessage(const std::string& aCallback, int aCanPort, uint8_t* aBuffer, int aLength);
    void handleBuffTrajPointStreamMessage(const std::string& aCallback, int aCanPort, uint8_t* aBuffer, int aLength);

    std::shared_ptr<CtrePigeonImuSim> createPigeon(int aPort);
    void createMotorController(int aCanPort);

protected:
    std::shared_ptr<CtrePigeonImuSim> getPigeonWrapper(int aCanPort);

    std::map<int, std::shared_ptr<CtrePigeonImuSim>> mPigeonMap;

    using NormalMotorCallback = std::function<void(std::shared_ptr<CtreTalonSRXSpeedControllerSim>)>;
    using SlottedMotorCallback = std::function<void(std::shared_ptr<CtreTalonSRXSpeedControllerSim>, int)>;
    std::map<std::string, NormalMotorCallback> mMotorControllerNormalCallbacks;
    std::map<std::string, SlottedMotorCallback> mMotorControllerSlottedCallbacks;

    using NormalPigeonCallback = std::function<void(std::shared_ptr<CtrePigeonImuSim>)>;
    std::map<std::string, NormalPigeonCallback> mPigeonNormalCallbacks;
};
