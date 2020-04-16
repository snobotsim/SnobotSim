

#include "SnobotSimGui/ModuleWrapperDisplay/SpeedControllerWidget.h"
#include "SnobotSimGui/EditNamePopup.h"

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

std::vector<std::string> gMotorTypes = {
    "CIM",
    "Mini CIM",
    "Bag",
    "775 Pro",
    "Andymark RS 775-125",
    "Banebots RS 775",
    "Andymark 9015",
    "Banebots RS 550",
    "RS775",
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
                // wrapper->GetName().c_str(), true ? ImGuiTreeNodeFlags_DefaultOpen : 0);
                wrapper->GetName().c_str());
        if(PopupEditName(pair.first, wrapper))
        {
            mSaveCallback();
        }

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

bool RenderMotorModelConfig(std::shared_ptr<ISpeedControllerWrapper>& wrapper, DcMotorModelConfigConfig& motorConfig)
{
    bool changed = false;

    ImGui::Separator();

    auto motorSim = std::dynamic_pointer_cast<BaseDcMotorSimulator>(wrapper->GetMotorSimulator());
    if(!motorSim)
    {
        return false;
    }

    const DcMotorModel& motorModel = motorSim->GetMotorModel();
    const DcMotorModelConfig& motorModelConfig = motorModel.GetModelConfig();

    const char* currentMotorName = motorModelConfig.mFactoryParams.mMotorName.c_str();

    ImGui::PushID(wrapper->GetId());
    if (ImGui::BeginCombo("Motor Name", currentMotorName))
    {
        for (auto name : gMotorTypes)
        {
            bool is_selected = name == currentMotorName;
            if (ImGui::Selectable(name.c_str(), is_selected))
            {
                motorConfig.mFactoryParams.mMotorName = name;
                changed = true;
                // wrapper->SetSpeedController(scPair.second);
                // mSaveCallback();
                std::cout << "Selected motor name " << name << std::endl;
            }
        }
        ImGui::EndCombo();
    }
    ImGui::PopID();
    SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "XXXX ");

    changed |= ImGui::InputInt("Num Motors", &motorConfig.mFactoryParams.mNumMotors);
    changed |= ImGui::InputDouble("Gear Reduction", &motorConfig.mFactoryParams.mGearReduction);
    changed |= ImGui::InputDouble("Efficiency", &motorConfig.mFactoryParams.mTransmissionEfficiency);

    ImGui::Separator();
    ImGui::LabelText("Free Speed RPM", "%0.3f", motorModelConfig.FREE_SPEED_RPM);
    ImGui::LabelText("Free Current", "%0.3f", motorModelConfig.FREE_CURRENT);
    ImGui::LabelText("Stall Torque", "%0.3f", motorModelConfig.STALL_TORQUE);
    ImGui::LabelText("Stall Current", "%0.3f", motorModelConfig.STALL_CURRENT);

    return changed;
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
                changed = true;
            }
        }
        ImGui::EndCombo();
    }
    ImGui::Text("MY CUSTOM COLOR PICKER WITH AN AMAZING PALETTE!");

    switch(motorConfig.mMotorSimConfigType)
    {
        case FullMotorSimConfig::Simple:
        {
            changed |= ImGui::InputDouble("Max Speed", &motorConfig.mSimple.mMaxSpeed);
            break;
        }
        case FullMotorSimConfig::Static:
        {
            changed |= ImGui::InputDouble("Load", &motorConfig.mStatic.mLoad);
            changed |= ImGui::InputDouble("Conversion Factor", &motorConfig.mStatic.mConversionFactor);
            changed |= RenderMotorModelConfig(wrapper, motorConfig.mMotorModelConfig);
            break;
        }
        case FullMotorSimConfig::Gravity:
        {
            changed |= ImGui::InputDouble("Load", &motorConfig.mGravity.mLoad);
            changed |= RenderMotorModelConfig(wrapper, motorConfig.mMotorModelConfig);
            break;
        }
        case FullMotorSimConfig::Rotational:
        {
            changed |= ImGui::InputDouble("Arm Centor of Mass", &motorConfig.mRotational.mArmCenterOfMass);
            changed |= ImGui::InputDouble("Arm Mass", &motorConfig.mRotational.mArmMass);
            changed |= ImGui::InputDouble("Constant Assist Torque", &motorConfig.mRotational.mConstantAssistTorque);
            changed |= ImGui::InputDouble("Over Center Assist Torque", &motorConfig.mRotational.mOverCenterAssistTorque);
            changed |= RenderMotorModelConfig(wrapper, motorConfig.mMotorModelConfig);
            break;
        }
        case FullMotorSimConfig::None:
        {
            break;
        }
    }

    //        changed |= ImGui::InputString("Motor Type", &motorConfig.mMotorModelConfig.mFactoryParams.mMotorType);

    if (changed)
    {
        std::cout << "Motor controller change" << std::endl;
        motorConfig.CreateSimulator(wrapper);
        mSaveCallback();
    }
}
