
#pragma once

#include <string>

#include "SnobotSim/ExportHelper.h"

class EXPORT_ SimulatorConfigWriterV1
{
public:
    SimulatorConfigWriterV1();
    virtual ~SimulatorConfigWriterV1();

    bool DumpConfig(const std::string& aConfigFile);
};
