

#include "SnobotSimGui/ModuleWrapperDisplay/EncoderWidget.h"

#include <imgui.h>

#include <iostream>

#include "SnobotSim/SensorActuatorRegistry.h"

void EncoderWidget::updateDisplay()
{
    ImGui::Begin("Encoders");

    ImGui::PushItemWidth(ImGui::GetFontSize() * 16);
    for (const auto& pair : SensorActuatorRegistry::Get().GetIEncoderWrapperMap())
    {
        auto wrapper = pair.second;
        bool open = ImGui::CollapsingHeader(wrapper->GetName().c_str(), true ? ImGuiTreeNodeFlags_DefaultOpen : 0);

        if (open)
        {
            ImGui::LabelText("Position", "%0.3f", wrapper->GetDistance());
            ImGui::LabelText("Velocity", "%0.3f", wrapper->GetVelocity());
            ImGui::LabelText("Is Hooked Up", "%s", wrapper->IsHookedUp() ? "yes" : "no");

            auto connectedSc = wrapper->GetSpeedController();
            const char* currentSc = connectedSc ? connectedSc->GetName().c_str() : nullptr;

            ImGui::PushID(pair.first);
            if (ImGui::BeginCombo("Connected Speed Controller", currentSc))
            {
                for (auto scPair : SensorActuatorRegistry::Get().GetISpeedControllerWrapperMap())
                {
                    bool is_selected = connectedSc == scPair.second;
                    if (ImGui::Selectable(scPair.second->GetName().c_str(), is_selected))
                    {
                        wrapper->SetSpeedController(scPair.second);
                        mSaveCallback();
                        std::cout << "Selected? " << scPair.second->GetName() << std::endl;
                    }
                }
                ImGui::EndCombo();
            }
            ImGui::PopID();
        }

        // std::cout << "  SC: " << pair.first << ", " << pair.second << std::endl;
    }
    ImGui::PopItemWidth();

    ImGui::End();
}
