
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
#include "SnobotSim/SimulatorComponents/TankDriveSimulator.h"
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

    if (motorSimulator && motorSimulator->GetSimulatorType() != NullMotorSimulator::GetType())
    {
        std::string simType = motorSimulator->GetSimulatorType();
        out << YAML::Key << "mMotorSimConfig" << YAML::BeginMap;
        auto tag = YAML::SecondaryTag(simType);
        out << tag << YAML::Newline;

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

void DumpSimulatorComponents(YAML::Emitter& out, const std::vector<std::shared_ptr<ISimulatorUpdater>>& simulatorComponents)
{
    std::cout << "XXXXXXXXXXXXXXXX" << simulatorComponents.size() << std::endl;
    out << YAML::Key << "mSimulatorComponents";
    out << YAML::BeginSeq;
    for (const auto& sim : simulatorComponents)
    {
        if (sim->GetSimulatorType() == TankDriveSimulator::GetType())
        {
            // bool IsSetup() const;
            // const std::shared_ptr<ISpeedControllerWrapper>& GetLeftMotor() const;
            // const std::shared_ptr<ISpeedControllerWrapper>& GetRightMotor() const;
            // const std::shared_ptr<IGyroWrapper>& GetGyro() const;

            auto castSim = std::static_pointer_cast<TankDriveSimulator>(sim);
            if (castSim->IsSetup())
            {
                out << YAML::BeginMap;
                out << YAML::Key << "mGyroHandle" << YAML::Value << castSim->GetGyro()->GetId();
                out << YAML::Key << "mLeftMotorHandle" << YAML::Value << castSim->GetLeftMotor()->GetId();
                out << YAML::Key << "mRightMotorHandle" << YAML::Value << castSim->GetRightMotor()->GetId();
                out << YAML::Key << "mTurnKp" << YAML::Value << castSim->GetTurnKp();
                out << YAML::EndMap;
            }
        }
    }
    out << YAML::EndSeq;
}

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
    DumpBasicConfig(out, "mAnalogIn", SensorActuatorRegistry::Get().GetIAnalogInWrapperMap());
    DumpBasicConfig(out, "mAnalogOut", SensorActuatorRegistry::Get().GetIAnalogOutWrapperMap());
    DumpBasicConfig(out, "mDigitalIO", SensorActuatorRegistry::Get().GetIDigitalIoWrapperMap());
    DumpBasicConfig(out, "mGyros", SensorActuatorRegistry::Get().GetIGyroWrapperMap());
    DumpBasicConfig(out, "mRelays", SensorActuatorRegistry::Get().GetIRelayWrapperMap());
    DumpBasicConfig(out, "mSolenoids", SensorActuatorRegistry::Get().GetISolenoidWrapperMap());
    DumpBasicConfig(out, "mEncoders", SensorActuatorRegistry::Get().GetIEncoderWrapperMap());
    DumpBasicConfig(out, "mPwm", SensorActuatorRegistry::Get().GetISpeedControllerWrapperMap());

    DumpSimulatorComponents(out, SensorActuatorRegistry::Get().GetSimulatorComponents());

    std::ofstream fileStream(aConfigFile.c_str());
    fileStream << out.c_str() << std::endl;
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
