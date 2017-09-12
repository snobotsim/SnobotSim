/*
 * GetSensorActuatorHelper.h
 *
 *  Created on: May 19, 2017
 *      Author: preiniger
 */

#ifndef SRC_GETSENSORACTUATORHELPER_H_
#define SRC_GETSENSORACTUATORHELPER_H_

#include "SnobotSim/SensorActuatorRegistry.h"

namespace GetSensorActuatorHelper
{
    EXPORT_ std::shared_ptr<SpeedControllerWrapper> GetSpeedControllerWrapper(int aHandle);
    EXPORT_ std::shared_ptr<GyroWrapper> GetGyroWrapper(int aHandle);
    EXPORT_ std::shared_ptr<DigitalSourceWrapper> GetDigitalSourceWrapper(int aHandle);
    EXPORT_ std::shared_ptr<AnalogSourceWrapper> GetAnalogSourceWrapper(int aHandle);
    EXPORT_ std::shared_ptr<RelayWrapper> GetRelayWrapper(int aHandle);
    EXPORT_ std::shared_ptr<SolenoidWrapper> GetSolenoidWrapper(int aHandle);
    EXPORT_ std::shared_ptr<EncoderWrapper> GetEncoderWrapper(int aHandle);
    EXPORT_ std::shared_ptr<ISpiWrapper> GetISpiWrapper(int aHandle);
    EXPORT_ std::shared_ptr<II2CWrapper> GetII2CWrapper(int aHandle);
}

#endif /* SRC_GETSENSORACTUATORHELPER_H_ */
