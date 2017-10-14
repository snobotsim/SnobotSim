/*
 * SnobotCoutLogger.h
 *
 *  Created on: Jun 21, 2017
 *      Author: PJ
 */

#ifndef SNOBOTCOUTLOGGER_H_
#define SNOBOTCOUTLOGGER_H_

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
}
#endif /* SNOBOTCOUTLOGGER_H_ */
