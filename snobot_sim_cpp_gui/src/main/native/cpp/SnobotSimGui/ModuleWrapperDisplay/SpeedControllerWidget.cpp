

#include "SnobotSimGui/ModuleWrapperDisplay/SpeedControllerWidget.h"
#include "SnobotSim/SensorActuatorRegistry.h"
#include "SnobotSimGui/Utilities/IndicatorDrawer.h"
#include "SnobotSimGui/Utilities/ColorFormatters.h"
#include "SnobotSim/MotorSim/GravityLoadDcMotorSim.h"
#include "SnobotSim/MotorSim/RotationalLoadDcMotorSim.h"
#include "SnobotSim/MotorSim/SimpleMotorSimulator.h"
#include "SnobotSim/MotorSim/StaticLoadDcMotorSim.h"

#include <imgui.h>
#include <iostream>


namespace
{

std::map<int, FullMotorSimConfig> gMotorConfig;
std::map<FullMotorSimConfig::MotorSimConfigType, std::string> gMotorSimToString;

    FullMotorSimConfig CreateMotorSimConfig(const std::shared_ptr<ISpeedControllerWrapper>& wrapper)
    {
        FullMotorSimConfig output;
        auto motorSimulator = wrapper->GetMotorSimulator();
        bool hasMotorModel = false;
        if(motorSimulator)
        {
            std::string simType = motorSimulator->GetSimulatorType();
            if(simType == SimpleMotorSimulator::GetType())
            {
                output.mMotorSimConfigType = FullMotorSimConfig::Simple;
                output.mMotorSimConfig.mSimple.mMaxSpeed = std::static_pointer_cast<SimpleMotorSimulator>(motorSimulator)->GetMaxSpeed();

            }
            else if(simType == StaticLoadDcMotorSim::GetType())
            {
                hasMotorModel = true;
                auto castSim = std::static_pointer_cast<StaticLoadDcMotorSim>(motorSimulator);

                output.mMotorSimConfigType = FullMotorSimConfig::Static;
                output.mMotorSimConfig.mStatic.mLoad = castSim->GetLoad();
                output.mMotorSimConfig.mStatic.mConversionFactor = castSim->GetConversionFactor();
            }
            else if(simType == GravityLoadDcMotorSim::GetType())
            {
                hasMotorModel = true;
                auto castSim = std::static_pointer_cast<GravityLoadDcMotorSim>(motorSimulator);

                output.mMotorSimConfigType = FullMotorSimConfig::Gravity;
                output.mMotorSimConfig.mGravity.mLoad = castSim->GetLoad();
            }
            else if(simType == RotationalLoadDcMotorSim::GetType())
            {
                hasMotorModel = true;
                auto castSim = std::static_pointer_cast<RotationalLoadDcMotorSim>(motorSimulator);

                output.mMotorSimConfigType = FullMotorSimConfig::Rotational;
                output.mMotorSimConfig.mRotational.mArmCenterOfMass = castSim->GetArmCenterOfMass();
                output.mMotorSimConfig.mRotational.mArmMass = castSim->GetArmMass();
                output.mMotorSimConfig.mRotational.mConstantAssistTorque = 0;
                output.mMotorSimConfig.mRotational.mOverCenterAssistTorque = 0;
            }
            else if(simType == "Null")
            {
                output.mMotorSimConfigType = FullMotorSimConfig::None;
            }
            else
            {
                SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Unknown type " << simType);
            }
        }

        if(hasMotorModel)
        {
            auto castSim = std::static_pointer_cast<BaseDcMotorSimulator>(motorSimulator);
            const DcMotorModel& config = castSim->GetMotorModel();
            output.mMotorModelConfig.mFactoryParams.mGearReduction =  config.GetModelConfig().mFactoryParams.mGearReduction;
            output.mMotorModelConfig.mFactoryParams.mGearboxEfficiency = config.GetModelConfig().mFactoryParams.mTransmissionEfficiency;
            output.mMotorModelConfig.mFactoryParams.mHasBrake = config.GetModelConfig().mHasBrake;
            output.mMotorModelConfig.mFactoryParams.mInverted = config.GetModelConfig().mInverted;
            output.mMotorModelConfig.mFactoryParams.mMotorType = config.GetModelConfig().mFactoryParams.mMotorName;
            output.mMotorModelConfig.mFactoryParams.mNumMotors = config.GetModelConfig().mFactoryParams.mNumMotors;
        }
        

        return output;

    }

    
}


SpeedControllerWidget::SpeedControllerWidget(SaveCallback callback) : mSaveCallback(callback) 
{
    gMotorSimToString[FullMotorSimConfig::None] = "None";
    gMotorSimToString[FullMotorSimConfig::Simple] = "Simple";
    gMotorSimToString[FullMotorSimConfig::Static] = "Static";
    gMotorSimToString[FullMotorSimConfig::Gravity] = "Gravity";
    gMotorSimToString[FullMotorSimConfig::Rotational] = "Rotational";
}

void SpeedControllerWidget::updateDisplay()
{
    if(gMotorConfig.empty())
    {
        for(const auto& pair : SensorActuatorRegistry::Get().GetISpeedControllerWrapperMap())
        {
            gMotorConfig[pair.first] = CreateMotorSimConfig(pair.second);
        }
    }

    ImGui::Begin("Speed Controllers");

    ImGui::PushItemWidth(ImGui::GetFontSize() * 8);
    for(const auto& pair : SensorActuatorRegistry::Get().GetISpeedControllerWrapperMap())
    {
        auto wrapper = pair.second;
        bool open = ImGui::CollapsingHeader(
            wrapper->GetName().c_str(), true ? ImGuiTreeNodeFlags_DefaultOpen : 0);
            
        AddIndicator(GetClampedColor(wrapper->GetVoltagePercentage(), -1, 1));
            
        if(open)
        {
            ImGui::PushID(pair.first);

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
            for(auto scPair : gMotorSimToString)
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
        changed |= ImGui::InputDouble("Efficiency", &motorConfig.mMotorModelConfig.mFactoryParams.mGearboxEfficiency);

        if(changed)
        {
            std::cout << "Motor controller change" << std::endl;
            mSaveCallback();
            // switch (motorConfig.mMotorSimConfigType)
            // {
            // case FullMotorSimConfig::Simple:
            // {
            //     speedController->SetMotorSimulator(std::shared_ptr<IMotorSimulator>(new SimpleMotorSimulator(
            //             motorConfig.mMotorSimConfig.mSimple.mMaxSpeed)));
            //     break;
            // }
            // case FullMotorSimConfig::Static:
            // {
            //     speedController->SetMotorSimulator(std::shared_ptr<IMotorSimulator>(new StaticLoadDcMotorSim(
            //             GetMotorModle(motorConfig.mMotorModelConfig.mFactoryParams),
            //             motorConfig.mMotorSimConfig.mStatic.mLoad,
            //             motorConfig.mMotorSimConfig.mStatic.mConversionFactor)));
            //     break;
            // }
            // case FullMotorSimConfig::Gravity:
            // {
            //     speedController->SetMotorSimulator(std::shared_ptr<IMotorSimulator>(new GravityLoadDcMotorSim(
            //             GetMotorModle(motorConfig.mMotorModelConfig.mFactoryParams),
            //             motorConfig.mMotorSimConfig.mGravity.mLoad)));
            //     break;
            // }
            // case FullMotorSimConfig::Rotational:
            // {
            //     speedController->SetMotorSimulator(std::shared_ptr<IMotorSimulator>(new RotationalLoadDcMotorSim(
            //             GetMotorModle(motorConfig.mMotorModelConfig.mFactoryParams),
            //             speedController,
            //             motorConfig.mMotorSimConfig.mRotational.mArmCenterOfMass,
            //             motorConfig.mMotorSimConfig.mRotational.mArmMass,
            //             motorConfig.mMotorSimConfig.mRotational.mConstantAssistTorque,
            //             motorConfig.mMotorSimConfig.mRotational.mOverCenterAssistTorque)));
            //     break;
            // }
            // case FullMotorSimConfig::None:
            // default:
            //     break;
            // }
        }
    }