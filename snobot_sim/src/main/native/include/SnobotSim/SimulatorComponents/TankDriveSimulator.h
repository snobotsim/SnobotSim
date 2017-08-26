/*
 * TankDriveSimulator.h
 *
 *  Created on: May 7, 2017
 *      Author: PJ
 */

#ifndef TANKDRIVESIMULATOR_H_
#define TANKDRIVESIMULATOR_H_

#include "SnobotSim/ModuleWrapper/EncoderWrapper.h"
#include "SnobotSim/SimulatorComponents/ISimulatorUpdater.h"
#include "SnobotSim/SimulatorComponents/Gyro/GyroWrapper.h"

class EXPORT_ TankDriveSimulator: public ISimulatorUpdater
{
public:
    TankDriveSimulator(
        const std::shared_ptr<EncoderWrapper>& aLeftEncoder,
        const std::shared_ptr<EncoderWrapper>& aRightEncoder,
        const std::shared_ptr<GyroWrapper>& aGyroWrapper,
        double aTurnKp);

    virtual ~TankDriveSimulator();

    void Update();

protected:

    std::shared_ptr<EncoderWrapper> mLeftEncoder;
    std::shared_ptr<EncoderWrapper> mRightEncoder;
    std::shared_ptr<GyroWrapper> mGyroWrapper;

    double mTurnKp;
    bool mIsSetup;

};

#endif /* TANKDRIVESIMULATOR_H_ */
