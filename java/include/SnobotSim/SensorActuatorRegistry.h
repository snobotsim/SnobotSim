/*
 * SensorActuatorRegistry.h
 *
 *  Created on: May 3, 2017
 *      Author: PJ
 */

#ifndef INCLUDE_SNOBOTSIM_SENSORACTUATORREGISTRY_H_
#define INCLUDE_SNOBOTSIM_SENSORACTUATORREGISTRY_H_

#include <map>
#include <vector>
#include <iostream>
#include "SnobotSim/ExportHelper.h"
#include "SnobotSim/ModuleWrapper/SpeedControllerWrapper.h"
#include "SnobotSim/ModuleWrapper/RelayWrapper.h"
#include "SnobotSim/ModuleWrapper/DigitalSourceWrapper.h"
#include "SnobotSim/ModuleWrapper/AnalogSourceWrapper.h"
#include "SnobotSim/ModuleWrapper/SolenoidWrapper.h"
#include "SnobotSim/ModuleWrapper/EncoderWrapper.h"
#include "SnobotSim/SimulatorComponents/ISpiWrapper.h"
#include "SnobotSim/SimulatorComponents/ISimulatorUpdater.h"
#include "SnobotSim/SimulatorComponents/Accelerometer/AccelerometerWrapper.h"
#include "SnobotSim/SimulatorComponents/Gyro/GyroWrapper.h"

#define ACTUATOR_GETTERS(ItemType)                                              \
    bool Register(int aPort, const std::shared_ptr<ItemType>& aActuator);       \
    std::shared_ptr<ItemType> Get##ItemType(int aPort, bool aLogError = true);  \
    const std::map<int, std::shared_ptr<ItemType>>& Get##ItemType##Map() const; \
    std::map<int, std::shared_ptr<ItemType>>& Get##ItemType##Map();

#define REGISTRATION_LOG(x) x;
//#define REGISTRATION_LOG(x)

class EXPORT_ SensorActuatorRegistry
{
private:
    SensorActuatorRegistry();
    static SensorActuatorRegistry sInstance;

public:
    virtual ~SensorActuatorRegistry();

    static SensorActuatorRegistry& Get();

    void AddSimulatorComponent(const std::shared_ptr<ISimulatorUpdater>& aSimulatorComponent);

    std::vector<std::shared_ptr<ISimulatorUpdater>>& GetSimulatorComponents();

    std::shared_ptr<ISpiWrapper> GetSpiWrapper();
    void SetSpiWrapper(const std::shared_ptr<ISpiWrapper>& aSpiWrapper);

    ACTUATOR_GETTERS(SpeedControllerWrapper)
    ACTUATOR_GETTERS(RelayWrapper)
    ACTUATOR_GETTERS(DigitalSourceWrapper)
    ACTUATOR_GETTERS(AnalogSourceWrapper)
    ACTUATOR_GETTERS(SolenoidWrapper)
    ACTUATOR_GETTERS(EncoderWrapper)
    ACTUATOR_GETTERS(AccelerometerWrapper)
    ACTUATOR_GETTERS(GyroWrapper)

protected:

    template<typename ItemType>
    bool RegisterItem(int aPort, std::shared_ptr<ItemType> aItem,
            std::map<int, std::shared_ptr<ItemType>>& aMap, const std::string& aType)
    {
        if (aMap.find(aPort) != aMap.end())
        {
            return false;
        }

        aMap[aPort] = aItem;

        REGISTRATION_LOG(std::cout << "Registered " << aType << " on port " << aPort << ".  The map has " << aMap.size() << " elements." << std::endl)

        return true;
    }

    template<typename ItemType>
    std::shared_ptr<ItemType> GetItem(int aPort,
            const std::map<int, std::shared_ptr<ItemType>>& aMap, const std::string& aType, bool logError)
    {
        typename std::map<int, std::shared_ptr<ItemType>>::const_iterator iter =
                aMap.find(aPort);
        if (iter == aMap.end())
        {
            if (logError)
            {
                REGISTRATION_LOG(std::cerr << "Unregistered " << aType << " on port " << aPort << ".  Map has " << aMap.size() << " elements." << std::endl)
            }
            return std::shared_ptr<ItemType>();
        }

        return iter->second;
    }

    std::map<int, std::shared_ptr<SpeedControllerWrapper>> mSpeedControllerWrapperMap;
    std::map<int, std::shared_ptr<RelayWrapper>> mRelayWrapperMap;
    std::map<int, std::shared_ptr<DigitalSourceWrapper>> mDigitalSourceWrapperMap;
    std::map<int, std::shared_ptr<AnalogSourceWrapper>> mAnalogSourceWrapperMap;
    std::map<int, std::shared_ptr<SolenoidWrapper>> mSolenoidWrapperMap;
    std::map<int, std::shared_ptr<EncoderWrapper>> mEncoderWrapperMap;
    std::map<int, std::shared_ptr<GyroWrapper>> mGyroWrapperMap;
    std::map<int, std::shared_ptr<AccelerometerWrapper>> mAccelerometerWrapperMap;

    std::vector<std::shared_ptr<ISimulatorUpdater>> mSimulatorComponents;

    std::shared_ptr<ISpiWrapper> mSpiWrapper;
};

#undef ACTUATOR_GETTERS

#endif /* INCLUDE_SNOBOTSIM_SENSORACTUATORREGISTRY_H_ */
