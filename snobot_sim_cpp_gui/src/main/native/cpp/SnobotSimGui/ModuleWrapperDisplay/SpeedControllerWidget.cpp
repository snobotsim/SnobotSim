

#include "SnobotSimGui/ModuleWrapperDisplay/SpeedControllerWidget.h"
#include "SnobotSim/SensorActuatorRegistry.h"

#include <imgui.h>

void SpeedControllerWidget::updateDisplay()
{
    ImGui::Begin("Speed Controllers");

    ImGui::PushItemWidth(ImGui::GetFontSize() * 8);
    for(const auto& pair : SensorActuatorRegistry::Get().GetISpeedControllerWrapperMap())
    {
        auto wrapper = pair.second;
        bool open = ImGui::CollapsingHeader(
            wrapper->GetName().c_str(), true ? ImGuiTreeNodeFlags_DefaultOpen : 0);
            
        if(open)
        {
            ImGui::PushID(pair.first);

            ImGui::LabelText("Voltage Percentage", "%0.3f", wrapper->GetVoltagePercentage());
            ImGui::Separator();
            
            ImGui::LabelText("Position", "%0.3f", wrapper->GetPosition());
            ImGui::LabelText("Velocity", "%0.3f", wrapper->GetVelocity());
            ImGui::LabelText("Acceleration", "%0.3f", wrapper->GetAcceleration());
            ImGui::LabelText("Current", "%0.3f", wrapper->GetCurrent());

            ImGui::PopID();
        }
        // std::cout << "  SC: " << pair.first << ", " << pair.second << std::endl;
    }
    ImGui::PopItemWidth();

    ImGui::End();
}