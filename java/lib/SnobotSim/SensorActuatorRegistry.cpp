/*
 * SensorActuatorRegistry.cpp
 *
 *  Created on: May 3, 2017
 *      Author: PJ
 */

#include "SnobotSim/SensorActuatorRegistry.h"

SensorActuatorRegistry SensorActuatorRegistry::sInstance =
        SensorActuatorRegistry();


SensorActuatorRegistry& SensorActuatorRegistry::Get()
{
	return sInstance;
}
    
SensorActuatorRegistry::SensorActuatorRegistry()
{

}

SensorActuatorRegistry::~SensorActuatorRegistry()
{

}


std::shared_ptr<ISpiWrapper> SensorActuatorRegistry::GetSpiWrapper()
{
    return mSpiWrapper;
}

void SensorActuatorRegistry::SetSpiWrapper(const std::shared_ptr<ISpiWrapper>& aSpiWrapper)
{
    mSpiWrapper = aSpiWrapper;
}

void SensorActuatorRegistry::AddSimulatorComponent(const std::shared_ptr<ISimulatorUpdater>& aSimulatorComponent)
{
    mSimulatorComponents.push_back(aSimulatorComponent);
}


std::vector<std::shared_ptr<ISimulatorUpdater>>& SensorActuatorRegistry::GetSimulatorComponents()
{
    return mSimulatorComponents;
}


#define ACTUATOR_GETTERS(ItemType)                                                                              \
    bool SensorActuatorRegistry::Register(int aPort,  const std::shared_ptr<ItemType>& aActuator)               \
    {                                                                                                           \
        return RegisterItem(aPort, aActuator, m##ItemType##Map, #ItemType);                                     \
    }                                                                                                           \
    std::shared_ptr<ItemType> SensorActuatorRegistry::Get##ItemType(int aPort, bool aLogError)                  \
    {                                                                                                           \
        return GetItem(aPort, m##ItemType##Map, #ItemType, aLogError);                                          \
    }                                                                                                           \
    const std::map<int, std::shared_ptr<ItemType>>& SensorActuatorRegistry::Get##ItemType##Map() const          \
    {                                                                                                           \
        return m##ItemType##Map;                                                                                \
    }                                                                                                           \
    std::map<int, std::shared_ptr<ItemType>>& SensorActuatorRegistry::Get##ItemType##Map()                      \
    {                                                                                                           \
        return m##ItemType##Map;                                                                                \
    }                                                                                                           \

ACTUATOR_GETTERS(SpeedControllerWrapper)
ACTUATOR_GETTERS(RelayWrapper)
ACTUATOR_GETTERS(DigitalSourceWrapper)
ACTUATOR_GETTERS(AnalogSourceWrapper)
ACTUATOR_GETTERS(SolenoidWrapper)
ACTUATOR_GETTERS(EncoderWrapper)
ACTUATOR_GETTERS(AccelerometerWrapper)
ACTUATOR_GETTERS(GyroWrapper)
