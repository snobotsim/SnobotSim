/*
 * RobotStateSingleton.cpp
 *
 *  Created on: May 5, 2017
 *      Author: preiniger
 */

#include "SnobotSim/RobotStateSingleton.h"
#include "SnobotSim/SensorActuatorRegistry.h"

#include <chrono>
#include <iostream>
#include <future>

RobotStateSingleton RobotStateSingleton::sINSTANCE;

RobotStateSingleton::RobotStateSingleton() :
        mRobotStarted(false), mEnabled(false), mAutonomous(false), mTest(false), mRunning(false)
{

}

RobotStateSingleton::~RobotStateSingleton()
{
	std::cout << "Destroying singleton" << std::endl;
	mRunning = false;
	mUpdateThread.join();
	std::cout << "Destroyed" << std::endl;
}

RobotStateSingleton& RobotStateSingleton::Get()
{
	return sINSTANCE;
}

void RobotStateSingleton::WaitForProgramToStart()
{
    if (!mRobotStarted)
    {
    	std::cout << "Waiting for robot to initialize..." << std::endl;
        std::unique_lock<std::mutex> lk(mProgramStartedMutex);
     	mProgramStartedCv.wait(lk);
    }
    else
    {
        std::cerr << "Robot already initialized!" << std::endl;
    }
}

void RobotStateSingleton::HandleRobotInitialized()
{
 	std::cout << "Robot initialized\n\n" << std::endl;
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
