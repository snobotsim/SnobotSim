/*
 * RobotStateSingleton.cpp
 *
 *  Created on: May 5, 2017
 *      Author: preiniger
 */

#include "SnobotSim/RobotStateSingleton.h"
#include "SnobotSim/SensorActuatorRegistry.h"
#include "SnobotSim/Logging/SnobotLogger.h"

#include <chrono>
#include <future>

RobotStateSingleton RobotStateSingleton::sINSTANCE;

RobotStateSingleton::RobotStateSingleton() :
        mRobotStarted(false), mEnabled(false), mAutonomous(false), mTest(false), mRunning(false)
{

}

RobotStateSingleton::~RobotStateSingleton()
{
    SNOBOT_LOG(SnobotLogging::INFO, "Destroying Singleton");

    if(mRunning)
    {
        mRunning = false;
        mUpdateThread.join();
    }

    SNOBOT_LOG(SnobotLogging::INFO, "Destroyed");
}

RobotStateSingleton& RobotStateSingleton::Get()
{
	return sINSTANCE;
}

void RobotStateSingleton::WaitForProgramToStart()
{
    if (!mRobotStarted)
    {
        SNOBOT_LOG(SnobotLogging::INFO, "Waiting for robot to initialize...");
        std::unique_lock<std::mutex> lk(mProgramStartedMutex);
     	mProgramStartedCv.wait(lk);
    }
    else
    {
        SNOBOT_LOG(SnobotLogging::WARN, "Robot already initialized!");
    }
}

void RobotStateSingleton::HandleRobotInitialized()
{
    SNOBOT_LOG(SnobotLogging::INFO, "Robot initialized\n\n");
    mRobotStarted = true;
    mRunning = true;
    mProgramStartedCv.notify_all();

    mUpdateThread = std::thread(&RobotStateSingleton::RunUpdateLoopThread, this);
}

void RobotStateSingleton::WaitForNextControlLoop()
{
    std::unique_lock<std::mutex> lk(mControlLoopMutex);
    mControlLoopCv.wait(lk);
}

void RobotStateSingleton::RunUpdateLoopThread()
{
    while (mRunning)
    {
        std::this_thread::sleep_for(std::chrono::milliseconds(20));
        mControlLoopCv.notify_all();
    }
}

void RobotStateSingleton::UpdateLoop()
{
    std::vector<std::shared_ptr<ISimulatorUpdater>>& comps =
            SensorActuatorRegistry::Get().GetSimulatorComponents();

    for(unsigned int i = 0; i < comps.size(); ++i)
    {
        comps[i]->Update();
    }
}

double RobotStateSingleton::GetMatchTime() const
{
    if (mEnabled)
    {
        std::chrono::duration<double> diff = std::chrono::system_clock::now() - mTimeEnabled;
        return diff.count();
    }
    return 0;
}

void RobotStateSingleton::SetDisabled(bool aDisabled)
{
    mEnabled = !aDisabled;
    if (mEnabled)
    {
        mTimeEnabled = std::chrono::system_clock::now();
    }
}

void RobotStateSingleton::SetAutonomous(bool aAutonomous)
{
    mAutonomous = aAutonomous;
}

void RobotStateSingleton::SetTest(bool aTest)
{
    mTest = aTest;
}

bool RobotStateSingleton::GetDisabled() const
{
    return !mEnabled;
}

bool RobotStateSingleton::GetAutonomous() const
{
    return mAutonomous;
}

bool RobotStateSingleton::GetTest() const
{
    return mTest;
}
