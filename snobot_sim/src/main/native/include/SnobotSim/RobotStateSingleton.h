/*
 * RobotStateSingleton.hpp
 *
 *  Created on: May 5, 2017
 *      Author: preiniger
 */

#ifndef ROBOTSTATESINGLETON_H_
#define ROBOTSTATESINGLETON_H_

#include <chrono>
#include <mutex>
#include <condition_variable>
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
    
    void WaitForNextControlLoop();

protected:

    void RunUpdateLoopThread();

    bool mRunning;

    std::chrono::time_point<std::chrono::system_clock> mTimeEnabled;

    std::condition_variable mControlLoopCv;
    std::mutex mControlLoopMutex;

    std::thread mUpdateThread;
};

#endif /* ROBOTSTATESINGLETON_H_ */
