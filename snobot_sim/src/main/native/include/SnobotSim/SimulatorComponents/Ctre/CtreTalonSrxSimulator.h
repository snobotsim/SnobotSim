/*
 * CtreTalonSrxSimulator.h
 *
 *  Created on: Nov 29, 2017
 *      Author: PJ
 */

#ifndef CTRETALONSRXSIMULATOR_H_
#define CTRETALONSRXSIMULATOR_H_

#include "SnobotSim/ModuleWrapper/SpeedControllerWrapper.h"

class CtreTalonSrxSimulator : public SpeedControllerWrapper
{
public:

    struct PIDFConstants
    {
        double mP;
        double mI;
        double mD;
        double mF;
        double mIZone;

        PIDFConstants() :
            mP(0),
            mI(0),
            mD(0),
            mF(0),
            mIZone(0)
        {

        }
    };

    CtreTalonSrxSimulator(int aPort);
    virtual ~CtreTalonSrxSimulator();


    void SetPGain(double aP);
    void SetIGain(double aI);
    void SetDGain(double aD);
    void SetFGain(double aF);
    void SetIzoneGain(double aIzone);

    void SetPositionGoal(double aPosition);

    void SetSpeedGoal(double aSpeed);

    const PIDFConstants& GetPidConstants();

    // Overrides
    virtual void Update(double aWaitTime) override;

protected:
    enum ControlType
    {
        Raw, Position, Speed
    };

    double CalculateFeedbackOutput(double aCurrent, double aGoal);

    PIDFConstants mPidConstants;
    ControlType mControlType;
    double mControlGoal;
    double mSumError;
    double mLastError;
};

#endif /* CTRETALONSRXSIMULATOR_H_ */
