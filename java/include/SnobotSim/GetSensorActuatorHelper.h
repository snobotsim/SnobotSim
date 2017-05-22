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
	std::shared_ptr<SpeedControllerWrapper> GetSpeedControllerWrapper(int aHandle);
	std::shared_ptr<GyroWrapper> GetGyroWrapper(int aHandle);
	std::shared_ptr<DigitalSourceWrapper> GetDigitalSourceWrapper(int aHandle);
	std::shared_ptr<AnalogSourceWrapper> GetAnalogSourceWrapper(int aHandle);
	std::shared_ptr<RelayWrapper> GetRelayWrapper(int aHandle);
	std::shared_ptr<SolenoidWrapper> GetSolenoidWrapper(int aHandle);
	std::shared_ptr<SpeedControllerWrapper> GetSpeedControllerWrapper(int aHandle);
}

#endif /* SRC_GETSENSORACTUATORHELPER_H_ */
