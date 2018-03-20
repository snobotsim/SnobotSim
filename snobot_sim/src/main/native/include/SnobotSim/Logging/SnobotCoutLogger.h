/*
 * SnobotCoutLogger.h
 *
 *  Created on: Jun 21, 2017
 *      Author: PJ
 */

#ifndef SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_LOGGING_SNOBOTCOUTLOGGER_H_
#define SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_LOGGING_SNOBOTCOUTLOGGER_H_

#include <string>

#include "SnobotSim/Logging/SnobotLogger.h"

namespace SnobotLogging
{
class EXPORT_ SnobotCoutLogger : public ISnobotLogger
{
public:
    SnobotCoutLogger();
    virtual ~SnobotCoutLogger();

    void Log(
            LogLevel aLogLevel,
            int aLineNumber,
            const std::string& aFileName,
            const std::string& aMessage);

protected:
    std::string FixWindowsSlashes(const std::string& aInput);

    std::string mDirectorySubstring;
};
} // namespace SnobotLogging
#endif // SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_LOGGING_SNOBOTCOUTLOGGER_H_
