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

class EXPORT_ I2CWrapperFactory {

private:
	I2CWrapperFactory();
	virtual ~I2CWrapperFactory();

public:
    static const std::string I2C_ACCELEROMETER_NAME;
    static const std::string NAVX;

    static I2CWrapperFactory& Get();

    std::shared_ptr<II2CWrapper> GetI2CWrapper(int aPort);

    void RegisterDefaultWrapperType(int aPort, const std::string& aWrapperType);
    void ResetDefaults();

protected:

    std::shared_ptr<II2CWrapper> CreateWrapper(int aPort, const std::string& aType);

    std::map<int, std::string> mDefaultsMap;

    static I2CWrapperFactory sINSTANCE;
};

#endif /* I2CWRAPPERFACTORY_H_ */
