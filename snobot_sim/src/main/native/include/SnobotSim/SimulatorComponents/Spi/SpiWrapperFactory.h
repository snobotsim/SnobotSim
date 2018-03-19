/*
 * SpiWrapperFactory.h
 *
 *  Created on: Sep 11, 2017
 *      Author: PJ
 */

#ifndef SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_SPI_SPIWRAPPERFACTORY_H_
#define SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_SPI_SPIWRAPPERFACTORY_H_

#include <map>
#include <memory>
#include <string>

#include "SnobotSim/SimulatorComponents/Spi/ISpiWrapper.h"

class EXPORT_ SpiWrapperFactory
{

private:
    SpiWrapperFactory();
    virtual ~SpiWrapperFactory();

public:
    static const std::string AUTO_DISCOVER_NAME;
    static const std::string ADXRS450_GYRO_NAME;
    static const std::string ADXL345_ACCELEROMETER_NAME;
    static const std::string ADXL362_ACCELEROMETER_NAME;
    static const std::string NAVX;

    static SpiWrapperFactory& Get();

    std::shared_ptr<ISpiWrapper> GetSpiWrapper(int aPort);

    void RegisterDefaultWrapperType(int aPort, const std::string& aWrapperType);
    void ResetDefaults();

protected:


    std::shared_ptr<ISpiWrapper> CreateWrapper(int aPort, const std::string& aType);

    std::map<int, std::string> mDefaultsMap;

    static SpiWrapperFactory sINSTANCE;
};

#endif  // SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_SPI_SPIWRAPPERFACTORY_H_
