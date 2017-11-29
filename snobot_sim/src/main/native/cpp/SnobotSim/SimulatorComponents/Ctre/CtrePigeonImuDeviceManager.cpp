/*
 * CtrePigeonImuDeviceManager.cpp
 *
 *  Created on: Nov 29, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/Ctre/CtrePigeonImuDeviceManager.h"
#include <iostream>

CtrePigeonImuDeviceManager::CtrePigeonImuDeviceManager()
{

}

CtrePigeonImuDeviceManager::~CtrePigeonImuDeviceManager()
{

}


void CtrePigeonImuDeviceManager::HandleSend(uint32_t aCanMessageId, uint32_t aCanPort, const uint8_t* aData, uint32_t aDataSize)
{

    if (aCanMessageId == 0x15042800 || aCanMessageId == 0x2042800)
    {
        std::shared_ptr<CtrePigeonImuSimulator> sim(new CtrePigeonImuSimulator(400 + aCanPort * 3));
        mPigeons[aCanPort] = sim;
    }
    else
    {
        std::cout << "Unsupported send " << std::hex << aCanMessageId << std::dec << std::endl;
    }
}

void CtrePigeonImuDeviceManager::HandleReceive(uint32_t aCanMessageId, uint32_t aCanPort, uint8_t* aData, uint8_t& aOutDataSize, int32_t& aOutStatus)
{
    if (aCanMessageId == 0x15041C40 || aCanMessageId == 0x2041C40)
    {
        PigeonMap_t::iterator findIter = mPigeons.find(aCanPort);
        if(findIter != mPigeons.end())
        {
            std::shared_ptr<CtrePigeonImuSimulator> sim = findIter->second;
            DumpAngles(sim, aData, aCanPort, 16.4, 2);
        }
    }
    else if (aCanMessageId == 0x15042140 || aCanMessageId == 0x2042140)
    {

    }
    else
    {
        std::cout << "Unsupported recv " << std::hex << aCanMessageId << std::dec << std::endl;
    }

    aOutDataSize = 8;
}

uint32_t CtrePigeonImuDeviceManager::ReadStreamSession(uint32_t aSessionHandle, struct HAL_CANStreamMessage* aMessages, uint32_t aMessagesToRead)
{
    std::cout << "Unsupported stream " << aSessionHandle << std::endl;
    return 0;
}



void CtrePigeonImuDeviceManager::DumpAngles(std::shared_ptr<CtrePigeonImuSimulator>& aSimulator, uint8_t* aData, int aPort, double aScaler, int aBytes)
{
    double yaw = aSimulator->GetYawWrapper()->GetAngle();
    double pitch = aSimulator->GetPitchWrapper()->GetAngle();
    double roll = aSimulator->GetRollWrapper()->GetAngle();

    int binnedYaw = (int) (yaw * aScaler);
    int binnedPitch = (int) (pitch * aScaler);
    int binnedRoll = (int) (roll * aScaler);

    int ctr = 0;

    aData[ctr++] = (binnedYaw >> 8) & 0xFF;
    aData[ctr++] = (binnedYaw >> 0) & 0xFF;
    aData[ctr++] = (binnedPitch >> 8) & 0xFF;
    aData[ctr++] = (binnedPitch >> 0) & 0xFF;
    aData[ctr++] = (binnedRoll >> 8) & 0xFF;
    aData[ctr++] = (binnedRoll >> 0) & 0xFF;
}
