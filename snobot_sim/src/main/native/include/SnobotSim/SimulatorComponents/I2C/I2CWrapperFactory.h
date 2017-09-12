/*
 * I2CWrapperFactory.h
 *
 *  Created on: Sep 11, 2017
 *      Author: PJ
 */

#ifndef I2CWRAPPERFACTORY_H_
#define I2CWRAPPERFACTORY_H_

#include "SnobotSim/SimulatorComponents/I2C/II2CWrapper.h"
#include <string>
#include <memory>
#include <map>

class I2CWrapperFactory {
public:
	I2CWrapperFactory();
	virtual ~I2CWrapperFactory();

    std::shared_ptr<II2CWrapper> GetI2CWrapper(int aPort);

protected:
    static const std::string I2C_ACCELEROMETER_NAME;
    static const std::string NAVX;

    std::shared_ptr<II2CWrapper> CreateWrapper(int aPort, const std::string& aType);

    std::map<int, std::string> mDefaultsMap;
};

#endif /* I2CWRAPPERFACTORY_H_ */
