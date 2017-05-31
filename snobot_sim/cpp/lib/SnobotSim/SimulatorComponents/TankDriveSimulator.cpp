/*
 * TankDriveSimulator.cpp
 *
 *  Created on: May 7, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/TankDriveSimulator.h"
#include <iostream>


TankDriveSimulator::TankDriveSimulator(
        const std::shared_ptr<EncoderWrapper>& aLeftEncoder,
        const std::shared_ptr<EncoderWrapper>& aRightEncoder,
        const std::shared_ptr<GyroWrapper>& aGyroWrapper,
        double aTurnKp):

    mLeftEncoder(aLeftEncoder),
    mRightEncoder(aRightEncoder),
    mGyroWrapper(aGyroWrapper),
    mTurnKp(aTurnKp)
{
    mIsSetup = mLeftEncoder && mRightEncoder && mGyroWrapper;

    if(!mIsSetup)
    {
        std::cerr << "Tank drive simulator is not set up! Will not update gyro!" << std::endl;
    }
}

TankDriveSimulator::~TankDriveSimulator() {

}

void TankDriveSimulator::Update()
{
    if (mIsSetup)
    {
    
        double rightDist = mRightEncoder->GetDistance();
        double leftDist = mLeftEncoder->GetDistance();
    
        double angle = (leftDist - rightDist) / (3.14159 * mTurnKp) * (180.0);
    
        mGyroWrapper->SetAngle(angle);
    }
}

