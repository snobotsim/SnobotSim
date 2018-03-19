/*
 * DcMotorModel.h
 *
 *  Created on: May 9, 2017
 *      Author: preiniger
 */

#ifndef SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_MOTORSIM_DCMOTORMODEL_H_
#define SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_MOTORSIM_DCMOTORMODEL_H_

#include "SnobotSim/MotorSim/DcMotorModelConfig.h"

class EXPORT_ DcMotorModel
{
public:
    explicit DcMotorModel(const DcMotorModelConfig& aModelConfig);
    virtual ~DcMotorModel();

    const DcMotorModelConfig& GetModelConfig() const;

    void Reset(double aPosition, double aVelocity, double aCurrent);

    void Step(double aAppliedVoltage, double aLoad, double aExternalTorque, double aTimestep);

    double GetPosition();
    double GetVelocity();
    double GetCurrent();
    double GetAcceleration();

protected:
    DcMotorModelConfig mModelConfig;
    double mPosition;
    double mVelocity;
    double mAcceleration;
    double mCurrent;
};

#endif // SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_MOTORSIM_DCMOTORMODEL_H_
