/*
 * CtrePigeonImuDeviceManager.h
 *
 *  Created on: Nov 29, 2017
 *      Author: PJ
 */

#ifndef SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_CTRE_CTREPIGEONIMUDEVICEMANAGER_H_
#define SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_CTRE_CTREPIGEONIMUDEVICEMANAGER_H_

#include <map>
#include <memory>

#include "SnobotSim/SimulatorComponents/Ctre/CtrePigeonImuSimulator.h"
#include "SnobotSim/SimulatorComponents/Ctre/ICanDeviceManager.h"

class CtrePigeonImuDeviceManager : public ICanDeviceManager
{
public:
    CtrePigeonImuDeviceManager();
    virtual ~CtrePigeonImuDeviceManager();

    void HandleSend(uint32_t aCanMessageId, uint32_t aCanPort, const uint8_t* aData, uint32_t aDataSize);

    void HandleReceive(uint32_t aCanMessageId, uint32_t aCanPort, uint8_t* aData, uint8_t& aOutDataSize, int32_t& aOutStatus);

    uint32_t ReadStreamSession(uint32_t aSessionHandle, struct HAL_CANStreamMessage* aMessages, uint32_t aMessagesToRead);

protected:
    void DumpAngles(std::shared_ptr<CtrePigeonImuSimulator>& aSimulator, uint8_t* aData, int aPort, double aScaler, int aBytes);

    typedef std::map<int, std::shared_ptr<CtrePigeonImuSimulator>> PigeonMap_t;
    PigeonMap_t mPigeons;
};

#endif // SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_CTRE_CTREPIGEONIMUDEVICEMANAGER_H_
