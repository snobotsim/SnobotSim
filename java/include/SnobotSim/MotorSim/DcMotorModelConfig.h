/*
 * DcMotorModelConfig.h
 *
 *  Created on: May 5, 2017
 *      Author: PJ
 */

#ifndef DCMOTORMODELCONFIG_H_
#define DCMOTORMODELCONFIG_H_

struct DcMotorModelConfig {
public:
    DcMotorModelConfig(
            double aNominalVoltage, 
            double aFreeSpeedRpm, 
            double aFreeCurrent, 
            double aStallTorque, 
            double aStallCurrent,
            double aMotorInertia,
            bool aHasBrake=false, bool aInverted=false);
            
    virtual ~DcMotorModelConfig();


    // Motor Parameters
    const double NOMINAL_VOLTAGE;
    const double FREE_SPEED_RPM;
    const double FREE_CURRENT;
    const double STALL_TORQUE;
    const double STALL_CURRENT;

    // Motor constants
    double mKT;
    double mKV;
    double mResistance;
    double mMotorInertia;
    bool mInverted;

    // Indicates the motor has a brake, i.e. when givin 0 volts it will stay put
    bool mHasBrake;
};

#endif /* DCMOTORMODELCONFIG_H_ */
