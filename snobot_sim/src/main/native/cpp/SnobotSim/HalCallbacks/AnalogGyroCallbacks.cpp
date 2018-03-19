/*
 * AnalogGyroCallbacks.cpp
 *
 *  Created on: Sep 10, 2017
 *      Author: PJ
 */

#include "SnobotSim/HalCallbacks/AnalogGyroCallbacks.h"

#include "MockData/AnalogGyroData.h"
#include "SnobotSim/Logging/SnobotLogger.h"
#include "SnobotSim/SensorActuatorRegistry.h"
#include "SnobotSim/SimulatorComponents/Gyro/AnalogGyroWrapper.h"

void AnalogGyroCallback(const char* name, void* param, const struct HAL_Value* value)
{
    std::string nameStr = name;
    int port = *((int*) param);

    if(nameStr == "Initialized")
    {
        std::shared_ptr<AnalogSourceWrapper> analogWrapper = SensorActuatorRegistry::Get().GetAnalogSourceWrapper(port);
        analogWrapper->SetWantsHidden(true);

        std::shared_ptr<AnalogGyroWrapper> gyroWrapper(new AnalogGyroWrapper(analogWrapper));
        SensorActuatorRegistry::Get().Register(port, gyroWrapper);
    }
    else if (nameStr == "Angle")
    {
        double angle = value->data.v_double;
        SensorActuatorRegistry::Get().GetIGyroWrapper(port)->SetAngle(angle);
    }
    else
    {
        SNOBOT_LOG(SnobotLogging::WARN, "Unknown name " << nameStr);
    }
}

int gAnalogGyroArrayIndices[26];

void SnobotSim::InitializeAnalogGyroCallbacks()
{
    for(int i = 0; i < HAL_GetNumAccumulators(); ++i)
    {
        gAnalogGyroArrayIndices[i] = i;
        HALSIM_RegisterAnalogGyroInitializedCallback(i, &AnalogGyroCallback, &gAnalogGyroArrayIndices[i], false);
        HALSIM_RegisterAnalogGyroAngleCallback(i, &AnalogGyroCallback, &gAnalogGyroArrayIndices[i], false);
        HALSIM_RegisterAnalogGyroRateCallback(i, &AnalogGyroCallback, &gAnalogGyroArrayIndices[i], false);
    }
}

void SnobotSim::ResetAnalogGyroCallbacks()
{
    for(int i = 0; i < HAL_GetNumAccumulators(); ++i)
    {
        HALSIM_ResetAnalogGyroData(i);
    }
}
