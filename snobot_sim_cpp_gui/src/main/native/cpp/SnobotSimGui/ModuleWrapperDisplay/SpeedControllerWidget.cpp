

#include "SnobotSimGui/ModuleWrapperDisplay/SpeedControllerWidget.h"

#include <imgui.h>

#include <iostream>

#include "SnobotSim/MotorFactory/VexMotorFactory.h"
#include "SnobotSim/MotorSim/GravityLoadDcMotorSim.h"
#include "SnobotSim/MotorSim/RotationalLoadDcMotorSim.h"
#include "SnobotSim/MotorSim/SimpleMotorSimulator.h"
#include "SnobotSim/MotorSim/StaticLoadDcMotorSim.h"
#include "SnobotSim/SensorActuatorRegistry.h"
#include "SnobotSim/SimulatorComponents/SmartSC/BaseCanSmartSpeedController.h"
#include "SnobotSimGui/Utilities/ColorFormatters.h"
#include "SnobotSimGui/Utilities/IndicatorDrawer.h"

namespace
{

std::map<ISpeedControllerWrapper::Type, std::string> gSCTypeNameLookup = {
    { ISpeedControllerWrapper::Type::WPI, "WPI" },
    { ISpeedControllerWrapper::Type::CTRE, "CTRE" },
    { ISpeedControllerWrapper::Type::REV, "REV" },
    { ISpeedControllerWrapper::Type::UNKNOWN, "Unknown" },
};
std::map<BaseCanSmartSpeedController::ControlType, std::string> sCanScControlTypeLookup = {
    { BaseCanSmartSpeedController::ControlType::Raw, "Raw" },
    { BaseCanSmartSpeedController::ControlType::Position, "Position" },
    { BaseCanSmartSpeedController::ControlType::Speed, "Speed" },
    { BaseCanSmartSpeedController::ControlType::MotionMagic, "MotionMagic" },
    { BaseCanSmartSpeedController::ControlType::MotionProfile, "MotionProfile" },
};
std::map<BaseCanSmartSpeedController::FeedbackDevice, std::string> sCanScFeedbackDeviceLookup = {
    { BaseCanSmartSpeedController::FeedbackDevice::None, "None" },
    { BaseCanSmartSpeedController::FeedbackDevice::QuadEncoder, "QuadEncoder" },
    { BaseCanSmartSpeedController::FeedbackDevice::Encoder, "Encoder" },
    { BaseCanSmartSpeedController::FeedbackDevice::Analog, "Analog" },
};

std::map<int, FullMotorSimConfig> gMotorConfig;
std::map<FullMotorSimConfig::MotorSimConfigType, std::string> gMotorSimToString;

FullMotorSimConfig CreateMotorSimConfig(const std::shared_ptr<ISpeedControllerWrapper>& wrapper)
{
    FullMotorSimConfig output;
    auto motorSimulator = wrapper->GetMotorSimulator();
    bool hasMotorModel = false;
    if (motorSimulator)
    {
        std::string simType = motorSimulator->GetSimulatorType();
        if (simType == SimpleMotorSimulator::GetType())
        {
            output.mMotorSimConfigType = FullMotorSimConfig::Simple;
            output.mSimple.mMaxSpeed = std::static_pointer_cast<SimpleMotorSimulator>(motorSimulator)->GetMaxSpeed();
        }
        else if (simType == StaticLoadDcMotorSim::GetType())
        {
            hasMotorModel = true;
            auto castSim = std::static_pointer_cast<StaticLoadDcMotorSim>(motorSimulator);

            output.mMotorSimConfigType = FullMotorSimConfig::Static;
            output.mStatic.mLoad = castSim->GetLoad();
            output.mStatic.mConversionFactor = castSim->GetConversionFactor();
        }
        else if (simType == GravityLoadDcMotorSim::GetType())
        {
            hasMotorModel = true;
            auto castSim = std::static_pointer_cast<GravityLoadDcMotorSim>(motorSimulator);

            output.mMotorSimConfigType = FullMotorSimConfig::Gravity;
            output.mGravity.mLoad = castSim->GetLoad();
        }
        else if (simType == RotationalLoadDcMotorSim::GetType())
        {
            hasMotorModel = true;
            auto castSim = std::static_pointer_cast<RotationalLoadDcMotorSim>(motorSimulator);

            output.mMotorSimConfigType = FullMotorSimConfig::Rotational;
            output.mRotational.mArmCenterOfMass = castSim->GetArmCenterOfMass();
            output.mRotational.mArmMass = castSim->GetArmMass();
            output.mRotational.mConstantAssistTorque = 0;
            output.mRotational.mOverCenterAssistTorque = 0;
        }
        else if (simType == "Null")
        {
            output.mMotorSimConfigType = FullMotorSimConfig::None;
        }
        else
        {
            SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Unknown type " << simType);
        }
    }

    if (hasMotorModel)
    {
        auto castSim = std::static_pointer_cast<BaseDcMotorSimulator>(motorSimulator);
        const DcMotorModel& config = castSim->GetMotorModel();
        output.mMotorModelConfig.mFactoryParams.mGearReduction = config.GetModelConfig().mFactoryParams.mGearReduction;
        output.mMotorModelConfig.mFactoryParams.mTransmissionEfficiency = config.GetModelConfig().mFactoryParams.mTransmissionEfficiency;
        output.mMotorModelConfig.mHasBrake = config.GetModelConfig().mHasBrake;
        output.mMotorModelConfig.mInverted = config.GetModelConfig().mInverted;
        output.mMotorModelConfig.mFactoryParams.mMotorName = config.GetModelConfig().mFactoryParams.mMotorName;
        output.mMotorModelConfig.mFactoryParams.mNumMotors = config.GetModelConfig().mFactoryParams.mNumMotors;
    }

    return output;
}

DcMotorModel GetMotorModel(const DcMotorModelConfig::FactoryParams& factoryParams, bool ahasBrake, bool aInverted)
{
    DcMotorModelConfig motorModelConfig = VexMotorFactory::MakeTransmission(
            VexMotorFactory::CreateMotor(factoryParams.mMotorName),
            factoryParams.mNumMotors, factoryParams.mGearReduction, factoryParams.mTransmissionEfficiency);

    motorModelConfig.mHasBrake = ahasBrake;
    motorModelConfig.mInverted = aInverted;

    DcMotorModel motorModel(motorModelConfig);

    return motorModel;
}

} // namespace

SpeedControllerWidget::SpeedControllerWidget(SaveCallback callback) :
        mSaveCallback(callback)
{
    gMotorSimToString[FullMotorSimConfig::None] = "None";
    gMotorSimToString[FullMotorSimConfig::Simple] = "Simple";
    gMotorSimToString[FullMotorSimConfig::Static] = "Static";
    gMotorSimToString[FullMotorSimConfig::Gravity] = "Gravity";
    gMotorSimToString[FullMotorSimConfig::Rotational] = "Rotational";
}

void SpeedControllerWidget::updateDisplay()
{
    if (gMotorConfig.empty())
    {
        for (const auto& pair : SensorActuatorRegistry::Get().GetISpeedControllerWrapperMap())
        {
            gMotorConfig[pair.first] = CreateMotorSimConfig(pair.second);
        }
    }

    ImGui::Begin("Speed Controllers");

    ImGui::PushItemWidth(ImGui::GetFontSize() * 8);
    for (const auto& pair : SensorActuatorRegistry::Get().GetISpeedControllerWrapperMap())
    {
        auto wrapper = pair.second;
        bool open = ImGui::CollapsingHeader(
                wrapper->GetName().c_str(), true ? ImGuiTreeNodeFlags_DefaultOpen : 0);

        AddIndicator(GetClampedColor(wrapper->GetVoltagePercentage(), -1, 1));

        if (open)
        {
            ImGui::PushID(pair.first);

            ImGui::LabelText("Type", gSCTypeNameLookup[wrapper->GetSpeedControllerType()].c_str());
            ImGui::LabelText("Voltage Percentage", "%0.3f", wrapper->GetVoltagePercentage());
            ImGui::Separator();

            ImGui::LabelText("Position", "%0.3f", wrapper->GetPosition());
            ImGui::LabelText("Velocity", "%0.3f", wrapper->GetVelocity());
            ImGui::LabelText("Acceleration", "%0.3f", wrapper->GetAcceleration());
            ImGui::LabelText("Current", "%0.3f", wrapper->GetCurrent());
            ImGui::Separator();

            ImGui::Text("Motor Config");
            ImGui::SameLine();

            bool open_popup = ImGui::Button(wrapper->GetMotorSimulator()->GetDisplaySimulatorType().c_str());
            if (open_popup)
            {
                ImGui::OpenPopup("mypicker");
            }

            if (ImGui::BeginPopup("mypicker"))
            {
                RenderMotorConfigPopup(wrapper, gMotorConfig[pair.first]);
                // ImGui::Text("MY CUSTOM COLOR PICKER WITH AN AMAZING PALETTE!");
                // ImGui::Text("MY CUSTOM COLOR PICKER WITH AN AMAZING PALETTE!");
                // ImGui::Text("MY CUSTOM COLOR PICKER WITH AN AMAZING PALETTE!");
                ImGui::EndPopup();
            }

            if (wrapper->GetSpeedControllerType() == ISpeedControllerWrapper::Type::CTRE || wrapper->GetSpeedControllerType() == ISpeedControllerWrapper::Type::REV)
            {
                ImGui::Separator();
                auto castSim = std::static_pointer_cast<BaseCanSmartSpeedController>(wrapper);
                ImGui::LabelText("Control Type", sCanScControlTypeLookup[castSim->GetControlType()].c_str());
                ImGui::LabelText("Feedback Device", sCanScFeedbackDeviceLookup[castSim->GetFeedbackDevice()].c_str());
                ImGui::LabelText("Control Goal", "%0.3f", castSim->GetControlGoal());
            }

            ImGui::PopID();
        }
        // std::cout << "  SC: " << pair.first << ", " << pair.second << std::endl;
    }
    ImGui::PopItemWidth();

    ImGui::End();
}

void SpeedControllerWidget::RenderMotorConfigPopup(std::shared_ptr<ISpeedControllerWrapper>& wrapper, FullMotorSimConfig& motorConfig)
{
    bool changed = false;

    std::string currentModel = gMotorSimToString[motorConfig.mMotorSimConfigType];
    if (ImGui::BeginCombo("Simulator Type", currentModel.c_str()))
    {
        for (auto scPair : gMotorSimToString)
        {
            bool is_selected = currentModel == scPair.second;
            if (ImGui::Selectable(scPair.second.c_str(), is_selected))
            {
                motorConfig.mMotorSimConfigType = scPair.first;
                std::cout << "Changed motor type to " << motorConfig.mMotorSimConfigType << std::endl;
            }
        }
        ImGui::EndCombo();
    }
    ImGui::Text("MY CUSTOM COLOR PICKER WITH AN AMAZING PALETTE!");
    //        changed |= ImGui::InputString("Motor Type", &motorConfig.mMotorModelConfig.mFactoryParams.mMotorType);
    changed |= ImGui::InputInt("Num Motors", &motorConfig.mMotorModelConfig.mFactoryParams.mNumMotors);
    changed |= ImGui::InputDouble("Gear Reduction", &motorConfig.mMotorModelConfig.mFactoryParams.mGearReduction);
    changed |= ImGui::InputDouble("Efficiency", &motorConfig.mMotorModelConfig.mFactoryParams.mTransmissionEfficiency);

    if (changed)
    {
        std::cout << "Motor controller change" << std::endl;
        switch (motorConfig.mMotorSimConfigType)
        {
        case FullMotorSimConfig::Simple:
        {
            wrapper->SetMotorSimulator(std::shared_ptr<IMotorSimulator>(new SimpleMotorSimulator(
                    motorConfig.mSimple)));
            break;
        }
        case FullMotorSimConfig::Static:
        {
            wrapper->SetMotorSimulator(std::shared_ptr<IMotorSimulator>(new StaticLoadDcMotorSim(
                    GetMotorModel(motorConfig.mMotorModelConfig.mFactoryParams, motorConfig.mMotorModelConfig.mHasBrake, motorConfig.mMotorModelConfig.mInverted),
                    motorConfig.mStatic)));
            break;
        }
        case FullMotorSimConfig::Gravity:
        {
            wrapper->SetMotorSimulator(std::shared_ptr<IMotorSimulator>(new GravityLoadDcMotorSim(
                    GetMotorModel(motorConfig.mMotorModelConfig.mFactoryParams, motorConfig.mMotorModelConfig.mHasBrake, motorConfig.mMotorModelConfig.mInverted),
                    motorConfig.mGravity)));
            break;
        }
        case FullMotorSimConfig::Rotational:
        {
            wrapper->SetMotorSimulator(std::shared_ptr<IMotorSimulator>(new RotationalLoadDcMotorSim(
                    GetMotorModel(motorConfig.mMotorModelConfig.mFactoryParams, motorConfig.mMotorModelConfig.mHasBrake, motorConfig.mMotorModelConfig.mInverted),
                    wrapper,
                    motorConfig.mRotational)));
            break;
        }
        case FullMotorSimConfig::None:
            wrapper->SetMotorSimulator(std::shared_ptr<IMotorSimulator>(new NullMotorSimulator));
        default:
            break;
        }
        mSaveCallback();
    }
}
