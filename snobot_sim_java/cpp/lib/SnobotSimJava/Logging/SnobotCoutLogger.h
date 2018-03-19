/*
 * SnobotCoutLogger.h
 *
 *  Created on: Jun 21, 2017
 *      Author: PJ
 */

#ifndef SNOBOTSIM_SNOBOT_SIM_JAVA_CPP_LIB_SNOBOTSIMJAVA_LOGGING_SNOBOTCOUTLOGGER_H_
#define SNOBOTSIM_SNOBOT_SIM_JAVA_CPP_LIB_SNOBOTSIMJAVA_LOGGING_SNOBOTCOUTLOGGER_H_

#include "SnobotSimJava/Logging/SnobotLogger.h"

namespace SnobotLogging
{
    class SnobotCoutLogger : public ISnobotLogger
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
}  // namespace SnobotLogging
#endif  // SNOBOTSIM_SNOBOT_SIM_JAVA_CPP_LIB_SNOBOTSIMJAVA_LOGGING_SNOBOTCOUTLOGGER_H_
