/*
 * SensorActuatorRegistry.cpp
 *
 *  Created on: May 3, 2017
 *      Author: PJ
 */

#include "SnobotSim/SensorActuatorRegistry.h"

SensorActuatorRegistry SensorActuatorRegistry::sInstance =
        SensorActuatorRegistry();

SensorActuatorRegistry::SensorActuatorRegistry()
{

}

SensorActuatorRegistry::~SensorActuatorRegistry()
{

}


#define ACTUATOR_GETTERS(ItemType)                                                                              \
    bool SensorActuatorRegistry::Register(int aPort,  const std::shared_ptr<ItemType>& aActuator)               \
    {                                                                                                           \
        return RegisterItem(aPort, aActuator, m##ItemType##Map, #ItemType);                                     \
    }                                                                                                           \
    std::shared_ptr<ItemType> SensorActuatorRegistry::Get##ItemType(int aPort)                                  \
    {                                                                                                           \
        return GetItem(aPort, m##ItemType##Map, #ItemType);                                                     \
    }                                                                                                           \
    const std::map<int, std::shared_ptr<ItemType>>& SensorActuatorRegistry::Get##ItemType##Map()                \
    {                                                                                                           \
        return m##ItemType##Map;                                                                                \
    }                                                                                                           \

ACTUATOR_GETTERS(SpeedControllerWrapper)
ACTUATOR_GETTERS(RelayWrapper)
ACTUATOR_GETTERS(DigitalSourceWrapper)
ACTUATOR_GETTERS(AnalogSourceWrapper)
ACTUATOR_GETTERS(SolenoidWrapper)
