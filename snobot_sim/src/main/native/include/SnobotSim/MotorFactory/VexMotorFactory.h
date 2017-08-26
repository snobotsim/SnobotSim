/*
 * VexMotorFactory.h
 *
 *  Created on: Jun 21, 2017
 *      Author: preiniger
 */

#ifndef SRC_VEXMOTORFACTORY_H_
#define SRC_VEXMOTORFACTORY_H_

#include "SnobotSim/MotorSim/DcMotorModelConfig.h"
#include <string>

class EXPORT_ VexMotorFactory
{
private:
    VexMotorFactory();
public:
    virtual ~VexMotorFactory();

    static DcMotorModelConfig MakeTransmission(
            const DcMotorModelConfig& aMotorConfig,
            int aNumMotors, double aReduction, double aEfficiency);

    static DcMotorModelConfig CreateMotor(
            const std::string& aName);

    static const std::string MOTOR_NAME_CIM;
    static const std::string MOTOR_NAME_MINI_CIM;
    static const std::string MOTOR_NAME_BAG;
    static const std::string MOTOR_NAME_775_PRO;
    static const std::string MOTOR_NAME_AM_RS_775_125;
    static const std::string MOTOR_NAME_BB_RS_775;
    static const std::string MOTOR_NAME_AM_9015;
    static const std::string MOTOR_NAME_BB_RS_550;
};

#endif /* SRC_VEXMOTORFACTORY_H_ */
