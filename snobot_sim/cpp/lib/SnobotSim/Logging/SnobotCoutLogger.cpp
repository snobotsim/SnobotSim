/*
 * SnobotCoutLogger.cpp
 *
 *  Created on: Jun 21, 2017
 *      Author: PJ
 */

#include "SnobotSim/Logging/SnobotCoutLogger.h"
#include <iostream>


using namespace SnobotLogging;

SnobotCoutLogger::SnobotCoutLogger()
{

}

SnobotCoutLogger::~SnobotCoutLogger()
{

}



void SnobotCoutLogger::Log(
        LogLevel aLogLevel,
        int aLineNumber,
        const std::string& aFileName,
        const std::string& aMessage)
{
    if(aLogLevel < mLogLevel)
    {
        return;
    }

    std::stringstream logLevelStr;

    switch(aLogLevel)
    {
    case DEBUG:
        logLevelStr << "Debug";
        break;
    case INFO:
        logLevelStr << "Info";
        break;
    case WARN:
        logLevelStr << "Warn";
        break;
    case ERROR:
        logLevelStr << "Error";
        break;
    }

    if(aLogLevel <= INFO)
    {
        std::cout << logLevelStr.str() << " " << aFileName << ":" << aLineNumber << " - " << aMessage << std::endl;
    }
    else
    {
        std::cerr << logLevelStr.str() << " " << aFileName << ":" << aLineNumber << " - " << aMessage << std::endl;
    }


}
