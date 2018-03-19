/*
 * CtreTalonxSrxDeviceManager.h
 *
 *  Created on: Nov 29, 2017
 *      Author: PJ
 */

#ifndef SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_CTRE_CTRETALONSRXDEVICEMANAGER_H_
#define SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_CTRE_CTRETALONSRXDEVICEMANAGER_H_

#include <map>

#include "MockData/CanData.h"
#include "SnobotSim/SimulatorComponents/Ctre/ICanDeviceManager.h"

class CtreTalonSrxDeviceManager : public ICanDeviceManager
{
public:
    CtreTalonSrxDeviceManager();
    virtual ~CtreTalonSrxDeviceManager();

    void HandleSend(uint32_t aCanMessageId, uint32_t aCanPort, const uint8_t* aData, uint32_t aDataSize);

    void HandleReceive(uint32_t aCanMessageId, uint32_t aCanPort, uint8_t* aData, uint8_t& aOutDataSize, int32_t& aOutStatus);

    uint32_t ReadStreamSession(uint32_t aSessionHandle, struct HAL_CANStreamMessage* aMessages, uint32_t aMessagesToRead);

protected:
    // Send
    void HandleTx1(const uint8_t* aData, int aPort);
    void HandleSetParamCommand(const uint8_t* aData, int aPort);
    void HandleParamRequest(const uint8_t* aData, int aPort);

    void HandleSetDemandCommand(const uint8_t* aData, int aPort);

    // Receive
    void PopulateStatus1(int aPort, uint8_t* aData);
    void PopulateStatus2(int aPort, uint8_t* aData);
    void PopulateStatus3(int aPort, uint8_t* aData);
    void PopulateStatus4(int aPort, uint8_t* aData);

    HAL_CANStreamMessage mStreamMessage;
};

#endif // SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_CTRE_CTRETALONSRXDEVICEMANAGER_H_
