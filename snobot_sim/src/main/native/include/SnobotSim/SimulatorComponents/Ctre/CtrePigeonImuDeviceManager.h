/*
 * CtrePigeonImuDeviceManager.h
 *
 *  Created on: Nov 29, 2017
 *      Author: PJ
 */

#ifndef CTREPIGEONIMUDEVICEMANAGER_H_
#define CTREPIGEONIMUDEVICEMANAGER_H_

#include <map>

#include "SnobotSim/SimulatorComponents/Ctre/ICanDeviceManager.h"
#include "SnobotSim/SimulatorComponents/Ctre/CtrePigeonImuSimulator.h"

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

#endif /* CTREPIGEONIMUDEVICEMANAGER_H_ */
