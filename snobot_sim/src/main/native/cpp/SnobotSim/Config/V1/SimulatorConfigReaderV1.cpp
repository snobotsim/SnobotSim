
#include "SnobotSim/Config/SimulatorConfigReaderV1.h"

#include <iostream>

#include <filesystem>

#include "SnobotSim/Config/SimulatorConfigV1.h"
#include "SnobotSim/GetSensorActuatorHelper.h"
#include "SnobotSim/Logging/SnobotLogger.h"
#include "SnobotSim/ModuleWrapper/Factories/FactoryContainer.h"
#include "SnobotSim/MotorSim/GravityLoadDcMotorSim.h"
#include "SnobotSim/MotorSim/RotationalLoadDcMotorSim.h"
#include "SnobotSim/MotorSim/SimpleMotorSimulator.h"
#include "SnobotSim/MotorSim/StaticLoadDcMotorSim.h"
#include "SnobotSim/MotorFactory/VexMotorFactory.h"
#include "SnobotSim/SensorActuatorRegistry.h"
#include "yaml-cpp/yaml.h"

namespace
{

std::ostream& operator<<(std::ostream& stream, const DcMotorModelConfig::FactoryParams& factoryParams)
{
    stream << factoryParams.mMotorName << ", " << factoryParams.mNumMotors << std::endl;

    return stream;
}

void ParseMap(const YAML::Node& aNode, std::map<int, std::string>& aMap)
{
    for (YAML::const_iterator it = aNode.begin(); it != aNode.end(); ++it)
    {
        aMap[it->first.as<int>()] = it->second.as<std::string>();
    }
}

template <typename T>
void ParseVector(const YAML::Node& aNode, std::vector<T>& aVector)
{
    for (YAML::const_iterator it = aNode.begin(); it != aNode.end(); ++it)
    {
        T value;
        (*it) >> value;
        aVector.push_back(value);
    }
}

void LoadBasicConfig(const YAML::Node& aNode, BasicModuleConfig& aOutput)
{
    aOutput.mHandle = aNode["mHandle"].as<int>();
    aOutput.mName = aNode["mName"].as<std::string>();
    aOutput.mType = aNode["mType"].as<std::string>();
}

const YAML::Node& operator>>(const YAML::Node& aNode, EncoderConfig& aOutput)
{
    LoadBasicConfig(aNode, aOutput);
    if(aNode["mConnectedSpeedControllerHandle"])
    {
        aOutput.mConnectedSpeedControllerHandle = aNode["mConnectedSpeedControllerHandle"].as<int>();
    }
    return aNode;
}

const YAML::Node& operator>>(const YAML::Node& aNode, PwmConfig& aOutput)
{
    LoadBasicConfig(aNode, aOutput);

    if (aNode["mMotorSimConfig"])
    {
        const YAML::Node& motorSimConfig = aNode["mMotorSimConfig"];
        std::string motorSimConfigTag = motorSimConfig.Tag();

        motorSimConfigTag = motorSimConfigTag.substr(std::string("tag:yaml.org,2002:").size());

        std::cout << "Loading tag '" << motorSimConfigTag << "'" << std::endl;

        if (motorSimConfigTag == SimpleMotorSimulator::GetType())
        {
            aOutput.mMotorSim.mMotorSimConfigType = FullMotorSimConfig::Simple;
            aOutput.mMotorSim.mMotorSimConfig.mSimple.mMaxSpeed = motorSimConfig["mMaxSpeed"].as<double>();
        }
        else if (motorSimConfigTag == StaticLoadDcMotorSim::GetType())
        {
            aOutput.mMotorSim.mMotorSimConfigType = FullMotorSimConfig::Static;
            aOutput.mMotorSim.mMotorSimConfig.mStatic.mLoad = motorSimConfig["mLoad"].as<double>();
            aOutput.mMotorSim.mMotorSimConfig.mStatic.mConversionFactor = motorSimConfig["mConversionFactor"].as<double>();
        }
        else if (motorSimConfigTag == GravityLoadDcMotorSim::GetType())
        {
            aOutput.mMotorSim.mMotorSimConfigType = FullMotorSimConfig::Gravity;
            aOutput.mMotorSim.mMotorSimConfig.mGravity.mLoad = motorSimConfig["mLoad"].as<double>();
        }
        else if (motorSimConfigTag == RotationalLoadDcMotorSim::GetType())
        {
            aOutput.mMotorSim.mMotorSimConfigType = FullMotorSimConfig::Rotational;
            aOutput.mMotorSim.mMotorSimConfig.mRotational.mArmCenterOfMass = motorSimConfig["mArmCenterOfMass"].as<double>();
            aOutput.mMotorSim.mMotorSimConfig.mRotational.mArmMass = motorSimConfig["mArmMass"].as<double>();
            aOutput.mMotorSim.mMotorSimConfig.mRotational.mConstantAssistTorque = motorSimConfig["mConstantAssistTorque"].as<double>();
            aOutput.mMotorSim.mMotorSimConfig.mRotational.mOverCenterAssistTorque = motorSimConfig["mOverCenterAssistTorque"].as<double>();
        }
        else
        {
        SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Unknown sim type " << motorSimConfigTag << "(" << aOutput.mHandle << ")");
        }

        if(aNode["mMotorModelConfig"])
        {
            const YAML::Node& motorModelConfig = aNode["mMotorModelConfig"];
            aOutput.mMotorSim.mMotorModelConfig.mFactoryParams.mMotorType = motorModelConfig["mMotorType"].as<std::string>();
            aOutput.mMotorSim.mMotorModelConfig.mFactoryParams.mNumMotors = motorModelConfig["mNumMotors"].as<int>();
            aOutput.mMotorSim.mMotorModelConfig.mFactoryParams.mGearReduction = motorModelConfig["mGearReduction"].as<double>();
            aOutput.mMotorSim.mMotorModelConfig.mFactoryParams.mGearboxEfficiency = motorModelConfig["mGearboxEfficiency"].as<double>();
            aOutput.mMotorSim.mMotorModelConfig.mFactoryParams.mHasBrake = motorModelConfig["mHasBrake"].as<bool>();
            aOutput.mMotorSim.mMotorModelConfig.mFactoryParams.mInverted = motorModelConfig["mInverted"].as<bool>();

            std::cout << "Getting motor model " << aOutput.mMotorSim.mMotorModelConfig.mFactoryParams.mMotorType << std::endl;
        }
        else{
            std::cout << "No motor model" << std::endl;
        }
    }
    else
    {
        SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "mMotorSimConfig is not set for " << aOutput.mName << "(" << aOutput.mHandle << ")");
    }

    return aNode;
}

const YAML::Node& operator>>(const YAML::Node& aNode, BasicModuleConfig& aOutput)
{
    LoadBasicConfig(aNode, aOutput);
    return aNode;
}

const YAML::Node& operator>>(const YAML::Node& configNode, SimulatorConfigV1& config)
{
    ParseMap(configNode["mDefaultI2CWrappers"], config.mDefaultI2CWrappers);
    ParseMap(configNode["mDefaultSpiWrappers"], config.mDefaultSpiWrappers);
    ParseVector(configNode["mAccelerometers"], config.mAccelerometers);
    ParseVector(configNode["mAnalogIn"], config.mAnalogIn);
    ParseVector(configNode["mAnalogOut"], config.mAnalogOut);
    ParseVector(configNode["mDigitalIO"], config.mDigitalIO);
    ParseVector(configNode["mGyros"], config.mGyros);
    ParseVector(configNode["mRelays"], config.mRelays);
    ParseVector(configNode["mSolenoids"], config.mSolenoids);
    ParseVector(configNode["mEncoders"], config.mEncoders);
    ParseVector(configNode["mPwm"], config.mPwm);

    return configNode;
}
}

SimulatorConfigReaderV1::SimulatorConfigReaderV1()
{
}

SimulatorConfigReaderV1::~SimulatorConfigReaderV1()
{
}

template <typename FactoryType, typename WrapperType>
void CreateBasicComponent(std::shared_ptr<FactoryType> aFactory, const std::map<int, WrapperType>& wrapperMap, const BasicModuleConfig& aConfig)
{
    aFactory->Create(aConfig.mHandle, aConfig.mType);
    auto findIter = wrapperMap.find(aConfig.mHandle);
    if (findIter != wrapperMap.end())
    {
        findIter->second->SetName(aConfig.mName);
    }
    else
    {
        SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Could not set name for handle " << aConfig.mHandle);
    }
}

template <typename FactoryType, typename WrapperType>
void CreateBasicComponents(std::shared_ptr<FactoryType> aFactory, const std::map<int, WrapperType>& wrapperMap, const std::vector<BasicModuleConfig>& aConfigs)
{
    for (auto it : aConfigs)
    {
        CreateBasicComponent(aFactory, wrapperMap, it);
    }
}

DcMotorModel GetMotorModle(const DcMotorModelConfigConfig::FactoryParams& factoryParams)
{
    
        DcMotorModelConfig motorModelConfig = VexMotorFactory::MakeTransmission(
                VexMotorFactory::CreateMotor(factoryParams.mMotorType),
                factoryParams.mNumMotors, factoryParams.mGearReduction, factoryParams.mGearboxEfficiency);
                
        motorModelConfig.mHasBrake = factoryParams.mHasBrake;
        motorModelConfig.mInverted = factoryParams.mInverted;

        DcMotorModel motorModel(motorModelConfig);

        std::cout << "Making motor model " << factoryParams.mMotorType << "'" << factoryParams.mNumMotors << std::endl;

        return motorModel;
}

void CreatePwmComponents(std::shared_ptr<SpeedControllerFactory> aFactory, const std::map<int, std::shared_ptr<ISpeedControllerWrapper>>& wrapperMap, const std::vector<PwmConfig>& aConfigs)
{
    for (auto it : aConfigs)
    {
        CreateBasicComponent(aFactory, wrapperMap, it);

        std::shared_ptr<ISpeedControllerWrapper> speedController = GetSensorActuatorHelper::GetISpeedControllerWrapper(it.mHandle);
        if (!speedController)
        {
            SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Invalid Speed Controller " << it.mHandle);
            continue;
        }
        const FullMotorSimConfig& motorConfig = it.mMotorSim;
        switch (motorConfig.mMotorSimConfigType)
        {
        case FullMotorSimConfig::Simple:
        {
            speedController->SetMotorSimulator(std::shared_ptr<IMotorSimulator>(new SimpleMotorSimulator(
                    motorConfig.mMotorSimConfig.mSimple.mMaxSpeed)));
            break;
        }
        case FullMotorSimConfig::Static:
        {
            speedController->SetMotorSimulator(std::shared_ptr<IMotorSimulator>(new StaticLoadDcMotorSim(
                    GetMotorModle(motorConfig.mMotorModelConfig.mFactoryParams),
                    motorConfig.mMotorSimConfig.mStatic.mLoad,
                    motorConfig.mMotorSimConfig.mStatic.mConversionFactor)));
            break;
        }
        case FullMotorSimConfig::Gravity:
        {
            speedController->SetMotorSimulator(std::shared_ptr<IMotorSimulator>(new GravityLoadDcMotorSim(
                    GetMotorModle(motorConfig.mMotorModelConfig.mFactoryParams),
                    motorConfig.mMotorSimConfig.mGravity.mLoad)));
            break;
        }
        case FullMotorSimConfig::Rotational:
        {
            speedController->SetMotorSimulator(std::shared_ptr<IMotorSimulator>(new RotationalLoadDcMotorSim(
                    GetMotorModle(motorConfig.mMotorModelConfig.mFactoryParams),
                    speedController,
                    motorConfig.mMotorSimConfig.mRotational.mArmCenterOfMass,
                    motorConfig.mMotorSimConfig.mRotational.mArmMass,
                    motorConfig.mMotorSimConfig.mRotational.mConstantAssistTorque,
                    motorConfig.mMotorSimConfig.mRotational.mOverCenterAssistTorque)));
            break;
        }
        case FullMotorSimConfig::None:
        default:
            break;
        }
    }
}

void CreateEncoderComponents(std::shared_ptr<EncoderFactory> aFactory, const std::map<int, std::shared_ptr<IEncoderWrapper>>& wrapperMap, const std::vector<EncoderConfig>& aConfigs)
{
    for (auto it : aConfigs)
    {
        CreateBasicComponent(aFactory, wrapperMap, it);
        if (it.mHandle != -1 && it.mConnectedSpeedControllerHandle != -1)
        {
            std::shared_ptr<IEncoderWrapper> encoder = GetSensorActuatorHelper::GetIEncoderWrapper(it.mHandle);
            std::shared_ptr<ISpeedControllerWrapper> speedController = GetSensorActuatorHelper::GetISpeedControllerWrapper(it.mConnectedSpeedControllerHandle);
            if (encoder && speedController)
            {
                encoder->SetSpeedController(speedController);
            }
            else
            {
                SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Necessary components not set (" << encoder << ", " << speedController << ")");
            }
        }
    }
}

void SetupSimulator(const SimulatorConfigV1& aConfig)
{
    for (auto it : aConfig.mDefaultI2CWrappers)
    {
        FactoryContainer::Get().GetI2CWrapperFactory()->RegisterDefaultWrapperType(it.first, it.second);
        FactoryContainer::Get().GetI2CWrapperFactory()->GetI2CWrapper(it.first);
    }
    for (auto it : aConfig.mDefaultSpiWrappers)
    {
        FactoryContainer::Get().GetSpiWrapperFactory()->RegisterDefaultWrapperType(it.first, it.second);
        FactoryContainer::Get().GetSpiWrapperFactory()->GetSpiWrapper(it.first);
    }

    CreateBasicComponents(FactoryContainer::Get().GetAccelerometerFactory(), SensorActuatorRegistry::Get().GetIAccelerometerWrapperMap(), aConfig.mAccelerometers);
    CreateBasicComponents(FactoryContainer::Get().GetAnalogInFactory(), SensorActuatorRegistry::Get().GetIAnalogInWrapperMap(), aConfig.mAnalogIn);
    CreateBasicComponents(FactoryContainer::Get().GetAnalogOutFactory(), SensorActuatorRegistry::Get().GetIAnalogOutWrapperMap(), aConfig.mAnalogOut);
    CreateBasicComponents(FactoryContainer::Get().GetDigitalIoFactory(), SensorActuatorRegistry::Get().GetIDigitalIoWrapperMap(), aConfig.mDigitalIO);
    CreateBasicComponents(FactoryContainer::Get().GetGyroFactory(), SensorActuatorRegistry::Get().GetIGyroWrapperMap(), aConfig.mGyros);
    CreateBasicComponents(FactoryContainer::Get().GetRelayFactory(), SensorActuatorRegistry::Get().GetIRelayWrapperMap(), aConfig.mRelays);
    CreateBasicComponents(FactoryContainer::Get().GetSolenoidFactory(), SensorActuatorRegistry::Get().GetISolenoidWrapperMap(), aConfig.mSolenoids);

    CreatePwmComponents(FactoryContainer::Get().GetSpeedControllerFactory(), SensorActuatorRegistry::Get().GetISpeedControllerWrapperMap(), aConfig.mPwm);
    CreateEncoderComponents(FactoryContainer::Get().GetEncoderFactory(), SensorActuatorRegistry::Get().GetIEncoderWrapperMap(), aConfig.mEncoders);
}

bool SimulatorConfigReaderV1::LoadConfig(const std::string& aConfigFile)
{
    std::cout << "Skipping it" << std::endl;
    namespace fs = std::filesystem;
    fs::path configFile = aConfigFile;

    bool success = true;
    SNOBOT_LOG(SnobotLogging::LOG_LEVEL_INFO, "Loading config file '" << configFile << "'");

    try
    {
        YAML::Node configNode = YAML::LoadFile(aConfigFile);

        SimulatorConfigV1 config;
        configNode >> config;

        SetupSimulator(config);
    }
    catch (std::exception& ex)
    {
        SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Could not parse config file... " << ex.what());
        success = false;
    }
    catch (std::runtime_error& ex)
    {
        SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Could not parse config file... " << ex.what());
        success = false;
    }
    catch (...)
    {
        SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Caught random exception... ");
        success = false;
    }

    return success;
}
