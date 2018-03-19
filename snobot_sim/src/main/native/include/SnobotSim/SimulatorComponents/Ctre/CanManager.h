/*
 * CanManager.h
 *
 *  Created on: Nov 29, 2017
 *      Author: PJ
 */

#ifndef SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_CTRE_CANMANAGER_H_
#define SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_CTRE_CANMANAGER_H_

#include <map>
#include <memory>

#include "MockData/NotifyListenerVector.h"
#include "SnobotSim/SimulatorComponents/Ctre/ICanDeviceManager.h"

class CanManager
{
public:

    CanManager();
    virtual ~CanManager();

    void HandleSendMessage(uint32_t aCanMessageId, uint32_t aCanPort, const uint8_t* aData, uint32_t aDataSize);
    void HandleReceiveMessage(uint32_t aCanMessageId, uint32_t aCanPort, uint8_t* aData, uint8_t& aOutDataSize, int32_t& aOutStatus);
    uint32_t HandleOpenStream(uint32_t aMessageId, uint32_t aMessageIdMask, uint32_t aMaxMessages, int32_t& aOutStatus);
    uint32_t HandleReadStream(uint32_t aSessionHandle, struct HAL_CANStreamMessage* aMessages, uint32_t aMessagesToRead);
    void HandleCloseStream(uint32_t aSessionHandle);

protected:

    typedef std::map<uint32_t, std::shared_ptr<ICanDeviceManager>> IdToDeviceManagerMap_t;
    IdToDeviceManagerMap_t mMessageIdToDeviceManagerMap;
    IdToDeviceManagerMap_t mStreamIdToDeviceManagerMap;

    int32_t mSendMessageCallbackId;
    int32_t mReceiveMessageCallbackId;
    int32_t mOpenStreamCallbackId;
    int32_t mCloseStreamCallbackId;
    int32_t mReadStreamCallbackId;
    int32_t mGetCanStatusCallbackId;

    static int sSTREAM_CTR;
};

#endif  // SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_CTRE_CANMANAGER_H_
