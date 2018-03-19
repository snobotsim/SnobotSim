/*
 * RobotStateSingleton.hpp
 *
 *  Created on: May 5, 2017
 *      Author: preiniger
 */

#ifndef SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_ROBOTSTATESINGLETON_H_
#define SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_ROBOTSTATESINGLETON_H_

#include <chrono>
#include <condition_variable>
#include <mutex>
#include <thread>

#include "SnobotSim/ExportHelper.h"

class EXPORT_ RobotStateSingleton
{
private:
    RobotStateSingleton();
    RobotStateSingleton(const RobotStateSingleton& that) = delete;
    virtual ~RobotStateSingleton();
    static RobotStateSingleton sINSTANCE;

public:
    static RobotStateSingleton& Get();

    void Reset();

    void UpdateLoop();

    double GetMatchTime() const;

    void WaitForNextControlLoop(double aWaitTime);

protected:
    std::chrono::time_point<std::chrono::system_clock> mTimeEnabled;
};

#endif // SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_ROBOTSTATESINGLETON_H_
