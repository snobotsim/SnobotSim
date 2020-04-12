
#pragma once

#include <map>
#include <string>
#include <vector>

#include "SnobotSim/MotorSim/GravityLoadDcMotorSim.h"
#include "SnobotSim/MotorSim/RotationalLoadDcMotorSim.h"
#include "SnobotSim/MotorSim/SimpleMotorSimulator.h"
#include "SnobotSim/MotorSim/StaticLoadDcMotorSim.h"

struct BasicModuleConfig
{
    int mHandle = 0;
    std::string mName = "";
    std::string mType = "";

    virtual void Print(std::ostream& aStream, const std::string& aIndent = "");
};

struct EncoderConfig : public BasicModuleConfig
{
    int mConnectedSpeedControllerHandle = -1;

    void Print(std::ostream& aStream, const std::string& aIndent = "") override;
};




struct DcMotorModelConfigConfig
{
    DcMotorModelConfig::FactoryParams mFactoryParams;
    bool mInverted{false};
    bool mHasBrake{false};
};


struct FullMotorSimConfig
{

    enum MotorSimConfigType
    {
        None,
        Simple,
        Static,
        Gravity,
        Rotational,
    };

    SimpleMotorSimulator::SimpleMotorSimulationConfig mSimple;
    StaticLoadDcMotorSim::StaticLoadMotorSimulationConfig mStatic;
    GravityLoadDcMotorSim::GravityLoadMotorSimulationConfig mGravity;
    RotationalLoadDcMotorSim::RotationalLoadMotorSimulationConfig mRotational;
    MotorSimConfigType mMotorSimConfigType = None;
    DcMotorModelConfigConfig mMotorModelConfig;
};

struct PwmConfig : public BasicModuleConfig
{
    FullMotorSimConfig mMotorSim;
    void Print(std::ostream& aStream, const std::string& aIndent = "") override;
};

struct SimulatorConfigV1
{
    std::vector<BasicModuleConfig> mAccelerometers;
    std::vector<BasicModuleConfig> mAnalogIn;
    std::vector<BasicModuleConfig> mAnalogOut;
    std::vector<BasicModuleConfig> mDigitalIO;
    std::vector<BasicModuleConfig> mGyros;
    std::vector<BasicModuleConfig> mRelays;
    std::vector<BasicModuleConfig> mSolenoids;
    std::vector<EncoderConfig> mEncoders;
    std::vector<PwmConfig> mPwm;
    std::map<int, std::string> mDefaultI2CWrappers;
    std::map<int, std::string> mDefaultSpiWrappers;

    virtual void Print(std::ostream& aStream);
};
