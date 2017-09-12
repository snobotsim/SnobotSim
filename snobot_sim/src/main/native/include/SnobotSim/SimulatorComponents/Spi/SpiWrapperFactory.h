/*
 * SpiWrapperFactory.h
 *
 *  Created on: Sep 11, 2017
 *      Author: PJ
 */

#ifndef SPIWRAPPERFACTORY_H_
#define SPIWRAPPERFACTORY_H_

#include "SnobotSim/SimulatorComponents/Spi/ISpiWrapper.h"
#include <string>
#include <memory>
#include <map>


class SpiWrapperFactory {
public:
    SpiWrapperFactory();
    virtual ~SpiWrapperFactory();

    std::shared_ptr<ISpiWrapper> GetSpiWrapper(int aPort);

protected:
    static const std::string AUTO_DISCOVER_NAME;
    static const std::string SPI_GYRO_NAME;
    static const std::string SPI_ACCELEROMETER_NAME;
    static const std::string NAVX;


    std::shared_ptr<ISpiWrapper> CreateWrapper(int aPort, const std::string& aType);

    std::map<int, std::string> mDefaultsMap;
};

#endif /* SPIWRAPPERFACTORY_H_ */
