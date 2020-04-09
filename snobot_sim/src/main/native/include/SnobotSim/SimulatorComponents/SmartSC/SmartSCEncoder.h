
#pragma once

#include <memory>
#include <string>

#include "SnobotSim/ModuleWrapper/AModuleWrapper.h"
#include "SnobotSim/ModuleWrapper/BaseEncoderWrapper.h"
#include "SnobotSim/ModuleWrapper/Interfaces/IEncoderWrapper.h"
#include "SnobotSim/ModuleWrapper/Interfaces/ISpeedControllerWrapper.h"
#include "SnobotSim/SimulatorComponents/IFeedbackSensor.h"

class SmartSCEncoder : public BaseEncoderWrapper
{
public:
    static const std::string TYPE;

    SmartSCEncoder(int aPort);
    virtual ~SmartSCEncoder();

    const std::string& GetType() override
    { 
        return TYPE; 
    }
};
