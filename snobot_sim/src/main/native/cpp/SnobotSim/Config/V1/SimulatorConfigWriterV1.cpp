
#include "SnobotSim/Config/SimulatorConfigWriterV1.h"

#include <filesystem>
#include <fstream>
#include <iostream>

#include "SnobotSim/Config/SimulatorConfigV1.h"
#include "SnobotSim/MotorSim/GravityLoadDcMotorSim.h"
#include "SnobotSim/MotorSim/RotationalLoadDcMotorSim.h"
#include "SnobotSim/MotorSim/SimpleMotorSimulator.h"
#include "SnobotSim/MotorSim/StaticLoadDcMotorSim.h"
#include "SnobotSim/SensorActuatorRegistry.h"
#include "yaml-cpp/yaml.h"

namespace
{

template <typename ItemType>
void DumpConfig(YAML::Emitter& out, int handle, const std::shared_ptr<ItemType>& wrapper)
{
    out << YAML::BeginMap;
    out << YAML::Key << "mHandle" << YAML::Value << handle;
    out << YAML::Key << "mName" << YAML::Value << wrapper->GetName();
    out << YAML::Key << "mType" << YAML::Value << wrapper->GetType();
    out << YAML::EndMap;
}

void DumpConfig(YAML::Emitter& out, int handle, const std::shared_ptr<IEncoderWrapper>& wrapper)
{
    SNOBOT_LOG(SnobotLogging::LOG_LEVEL_INFO, "Dumping encoder");
    out << YAML::BeginMap;
    out << YAML::Key << "mHandle" << YAML::Value << handle;
    out << YAML::Key << "mName" << YAML::Value << wrapper->GetName();
    out << YAML::Key << "mType" << YAML::Value << wrapper->GetType();
    if (wrapper->GetSpeedController())
    {
        out << YAML::Key << "mConnectedSpeedControllerHandle" << YAML::Value << wrapper->GetSpeedController()->GetId();
    }
    out << YAML::EndMap;
}

void DumpMotorModelConfig(YAML::Emitter& out, const DcMotorModel& config)
{
    out << YAML::Key << "mMotorModelConfig" << YAML::BeginMap;

    out << YAML::Key << "mGearReduction" << YAML::Value << config.GetModelConfig().mFactoryParams.mGearReduction;
    out << YAML::Key << "mGearboxEfficiency" << YAML::Value << config.GetModelConfig().mFactoryParams.mTransmissionEfficiency;
    out << YAML::Key << "mHasBrake" << YAML::Value << config.GetModelConfig().mHasBrake;
    out << YAML::Key << "mInverted" << YAML::Value << config.GetModelConfig().mInverted;
    out << YAML::Key << "mMotorType" << YAML::Value << config.GetModelConfig().mFactoryParams.mMotorName;
    out << YAML::Key << "mNumMotors" << YAML::Value << config.GetModelConfig().mFactoryParams.mNumMotors;

    out << YAML::EndMap;
}

void DumpConfig(YAML::Emitter& out, int handle, const std::shared_ptr<ISpeedControllerWrapper>& wrapper)
{
    out << YAML::BeginMap;
    out << YAML::Key << "mHandle" << YAML::Value << handle;
    out << YAML::Key << "mName" << YAML::Value << wrapper->GetName();
    out << YAML::Key << "mType" << YAML::Value << wrapper->GetType();

    auto motorSimulator = wrapper->GetMotorSimulator();
    bool dumpMotorModel = false;

    if (motorSimulator)
    {
        std::string simType = motorSimulator->GetSimulatorType();
        auto tag = YAML::SecondaryTag(simType);
        out << tag;
        out << YAML::Key << "mMotorSimConfig" << YAML::BeginMap;

        if (simType == SimpleMotorSimulator::GetType())
        {
            out << YAML::Key << "mMaxSpeed" << YAML::Value << std::static_pointer_cast<SimpleMotorSimulator>(motorSimulator)->GetMaxSpeed();
        }
        else if (simType == StaticLoadDcMotorSim::GetType())
        {
            dumpMotorModel = true;
            auto castSim = std::static_pointer_cast<StaticLoadDcMotorSim>(motorSimulator);

            out << YAML::Key << "mLoad" << YAML::Value << castSim->GetLoad();
            out << YAML::Key << "mConversionFactor" << YAML::Value << castSim->GetConversionFactor();
        }
        else if (simType == GravityLoadDcMotorSim::GetType())
        {
            dumpMotorModel = true;
            auto castSim = std::static_pointer_cast<GravityLoadDcMotorSim>(motorSimulator);

            out << YAML::Key << "mLoad" << YAML::Value << castSim->GetLoad();
        }
        else if (simType == RotationalLoadDcMotorSim::GetType())
        {
            dumpMotorModel = true;
            auto castSim = std::static_pointer_cast<RotationalLoadDcMotorSim>(motorSimulator);

            out << YAML::Key << "mArmCenterOfMass" << YAML::Value << castSim->GetArmCenterOfMass();
            out << YAML::Key << "mArmMass" << YAML::Value << castSim->GetArmMass();
            out << YAML::Key << "mConstantAssistTorque" << YAML::Value << 0;
            out << YAML::Key << "mOverCenterAssistTorque" << YAML::Value << 0;
        }
        else if (simType == "Null")
        {
        }
        else
        {
            SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Unknown type " << simType);
        }

        out << YAML::EndMap;

        if (dumpMotorModel)
        {
            DumpMotorModelConfig(out, std::static_pointer_cast<BaseDcMotorSimulator>(motorSimulator)->GetMotorModel());
        }
    }

    out << YAML::EndMap;
}

template <typename ItemType>
void DumpBasicConfig(YAML::Emitter& out, const std::string& name, const std::map<int, std::shared_ptr<ItemType>>& map)
{
    out << YAML::Key << name;
    out << YAML::BeginSeq;
    for (const auto& pair : map)
    {
        DumpConfig(out, pair.first, pair.second);
    }
    out << YAML::EndSeq;
}

// template<typename T>
// YAML::Emitter& operator << (YAML::Emitter& out, const std::shared_ptr<T>& v) {
//     out << Yaml::BeginMap;
//     out << YAML::Key << "name" << Yaml::Value << "Hello";
//     out << YAML::EndMap;
// 	return out;
// }

// template<typename T>
// YAML::Emitter& operator << (YAML::Emitter& out, const std::vector<std::shared_ptr<T>>& v) {
//     out << Yaml::BeginSeq;
//     for(auto x : v)
//     {
//         out << x;
//     }
//     out << YAML::EndSeqdMap;
// 	return out;
// }
// template<typename T>
// void PopulateBaseicConfig(BasicModuleConfig& outConfig, const std::shared_ptr<T>& data)
// {
//     outConfig.mHandle = data->GetHandle();
//     outConfig.mName = data->GetName();
//     outConfig.mType = data->GetType();
// }

// SimulatorConfigV1 CreateSimulatorConfig()
// {
//     SimulatorConfigV1 output;
//     // CreateBasicComponents(FactoryContainer::Get().GetAccelerometerFactory(), SensorActuatorRegistry::Get().GetIAccelerometerWrapperMap(), aConfig.mAccelerometers);
//     // CreateBasicComponents(FactoryContainer::Get().GetAnalogInFactory(), SensorActuatorRegistry::Get().GetIAnalogInWrapperMap(), aConfig.mAnalogIn);
//     // CreateBasicComponents(FactoryContainer::Get().GetAnalogOutFactory(), SensorActuatorRegistry::Get().GetIAnalogOutWrapperMap(), aConfig.mAnalogOut);
//     // CreateBasicComponents(FactoryContainer::Get().GetDigitalIoFactory(), SensorActuatorRegistry::Get().GetIDigitalIoWrapperMap(), aConfig.mDigitalIO);
//     // CreateBasicComponents(FactoryContainer::Get().GetGyroFactory(), SensorActuatorRegistry::Get().GetIGyroWrapperMap(), aConfig.mGyros);
//     // CreateBasicComponents(FactoryContainer::Get().GetRelayFactory(), SensorActuatorRegistry::Get().GetIRelayWrapperMap(), aConfig.mRelays);
//     // CreateBasicComponents(FactoryContainer::Get().GetSolenoidFactory(), SensorActuatorRegistry::Get().GetISolenoidWrapperMap(), aConfig.mSolenoids);

//     // CreatePwmComponents(FactoryContainer::Get().GetSpeedControllerFactory(), SensorActuatorRegistry::Get().GetISpeedControllerWrapperMap(), aConfig.mPwm);
//     // CreateEncoderComponents(FactoryContainer::Get().GetEncoderFactory(), SensorActuatorRegistry::Get().GetIEncoderWrapperMap(), aConfig.mEncoders);

//     return output;
// }

// const YAML::Node& operator>>(const YAML::Node& configNode, SimulatorConfigV1& config)
// {
//     DumpMap(configNode["mDefaultI2CWrappers"], config.mDefaultI2CWrappers);
//     DumpMap(configNode["mDefaultSpiWrappers"], config.mDefaultSpiWrappers);
//     DumpVector(configNode["mAccelerometers"], config.mAccelerometers);
//     DumpVector(configNode["mAnalogIn"], config.mAnalogIn);
//     DumpVector(configNode["mAnalogOut"], config.mAnalogOut);
//     DumpVector(configNode["mDigitalIO"], config.mDigitalIO);
//     DumpVector(configNode["mGyros"], config.mGyros);
//     DumpVector(configNode["mRelays"], config.mRelays);
//     DumpVector(configNode["mSolenoids"], config.mSolenoids);
//     DumpVector(configNode["mEncoders"], config.mEncoders);
//     DumpVector(configNode["mPwm"], config.mPwm);

//     return configNode;
// }
} // namespace

SimulatorConfigWriterV1::SimulatorConfigWriterV1()
{
}
SimulatorConfigWriterV1::~SimulatorConfigWriterV1()
{
}

bool SimulatorConfigWriterV1::DumpConfig(const std::string& aConfigFile)
{
    namespace fs = std::filesystem;
    fs::path configFile = aConfigFile;

    bool success = true;
    SNOBOT_LOG(SnobotLogging::LOG_LEVEL_INFO, "Dumping config file '" << configFile << "'");

    YAML::Emitter out;
    out << YAML::BeginMap;
    DumpBasicConfig(out, "mAccelerometers", SensorActuatorRegistry::Get().GetIAccelerometerWrapperMap());
    SNOBOT_LOG(SnobotLogging::LOG_LEVEL_INFO, "Dumping config file '" << configFile << "'");
    DumpBasicConfig(out, "mAnalogIn", SensorActuatorRegistry::Get().GetIAnalogInWrapperMap());
    SNOBOT_LOG(SnobotLogging::LOG_LEVEL_INFO, "Dumping config file '" << configFile << "'");
    DumpBasicConfig(out, "mAnalogOut", SensorActuatorRegistry::Get().GetIAnalogOutWrapperMap());
    SNOBOT_LOG(SnobotLogging::LOG_LEVEL_INFO, "Dumping config file '" << configFile << "'");
    DumpBasicConfig(out, "mDigitalIO", SensorActuatorRegistry::Get().GetIDigitalIoWrapperMap());
    SNOBOT_LOG(SnobotLogging::LOG_LEVEL_INFO, "Dumping config file '" << configFile << "'");
    DumpBasicConfig(out, "mGyros", SensorActuatorRegistry::Get().GetIGyroWrapperMap());
    SNOBOT_LOG(SnobotLogging::LOG_LEVEL_INFO, "Dumping config file '" << configFile << "'");
    DumpBasicConfig(out, "mRelays", SensorActuatorRegistry::Get().GetIRelayWrapperMap());
    SNOBOT_LOG(SnobotLogging::LOG_LEVEL_INFO, "Dumping config file '" << configFile << "'");
    DumpBasicConfig(out, "mSolenoids", SensorActuatorRegistry::Get().GetISolenoidWrapperMap());
    SNOBOT_LOG(SnobotLogging::LOG_LEVEL_INFO, "Dumping config file '" << configFile << "'");
    DumpBasicConfig(out, "mEncoders", SensorActuatorRegistry::Get().GetIEncoderWrapperMap());
    SNOBOT_LOG(SnobotLogging::LOG_LEVEL_INFO, "Dumping config file '" << configFile << "'");
    DumpBasicConfig(out, "mPwm", SensorActuatorRegistry::Get().GetISpeedControllerWrapperMap());
    SNOBOT_LOG(SnobotLogging::LOG_LEVEL_INFO, "Dumping config file '" << configFile << "'");

    // std::ofstream fileStream(aConfigFile.c_str());
    // fileStream << out.c_str() << std::endl;
    // out << YAML::EndMap;
    std::cout << out.c_str() << std::endl;

    // try
    // {
    //     YAML::Node configNode = YAML::LoadFile(aConfigFile);

    //     SimulatorConfigV1 config;
    //     configNode >> config;

    //     SetupSimulator(config);
    // }
    // catch (std::exception& ex)
    // {
    //     SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Could not parse config file... " << ex.what());
    //     success = false;
    // }

    return success;
}
