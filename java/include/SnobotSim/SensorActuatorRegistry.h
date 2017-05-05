/*
 * SensorActuatorRegistry.h
 *
 *  Created on: May 3, 2017
 *      Author: PJ
 */

#ifndef INCLUDE_SNOBOTSIM_SENSORACTUATORREGISTRY_H_
#define INCLUDE_SNOBOTSIM_SENSORACTUATORREGISTRY_H_

#include <map>
#include <iostream>
#include "SnobotSim/ModuleWrapper/SpeedControllerWrapper.h"
#include "SnobotSim/ModuleWrapper/RelayWrapper.h"
#include "SnobotSim/ModuleWrapper/DigitalSourceWrapper.h"
#include "SnobotSim/ModuleWrapper/AnalogSourceWrapper.h"
#include "SnobotSim/ModuleWrapper/SolenoidWrapper.h"
#include "SnobotSim/ModuleWrapper/EncoderWrapper.h"

#define ACTUATOR_GETTERS(ItemType)                                              \
    bool Register(int aPort, const std::shared_ptr<ItemType>& aActuator);       \
    std::shared_ptr<ItemType> Get##ItemType(int aPort, bool aLogError = true);  \
    const std::map<int, std::shared_ptr<ItemType>>& Get##ItemType##Map() const; \
    std::map<int, std::shared_ptr<ItemType>>& Get##ItemType##Map();

#define REGISTRATION_LOG(x) std::cout << x << std::endl;
//#define REGISTRATION_LOG(x)

class SensorActuatorRegistry
{
private:
    SensorActuatorRegistry();
    static SensorActuatorRegistry sInstance;

public:
    virtual ~SensorActuatorRegistry();

    static SensorActuatorRegistry& Get()
    {
        return sInstance;
    }

    ACTUATOR_GETTERS(SpeedControllerWrapper)
    ACTUATOR_GETTERS(RelayWrapper)
    ACTUATOR_GETTERS(DigitalSourceWrapper)
    ACTUATOR_GETTERS(AnalogSourceWrapper)
    ACTUATOR_GETTERS(SolenoidWrapper)
    ACTUATOR_GETTERS(EncoderWrapper)

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

        REGISTRATION_LOG("Registered " << aType << " on port " << aPort << ".  The map has " << aMap.size() << " elements.")

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
                REGISTRATION_LOG("Unregistered " << aType << " on port " << aPort << ".  Map has " << aMap.size() << " elements." << std::endl)
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

};

#undef ACTUATOR_GETTERS

#endif /* INCLUDE_SNOBOTSIM_SENSORACTUATORREGISTRY_H_ */
