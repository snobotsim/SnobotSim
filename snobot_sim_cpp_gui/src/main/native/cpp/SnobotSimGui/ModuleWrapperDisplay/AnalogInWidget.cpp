

#include "SnobotSimGui/ModuleWrapperDisplay/AnalogInWidget.h"

#include <hal/Ports.h>
#include <imgui.h>
#include <mockdata/AnalogGyroData.h>
#include <mockdata/AnalogInData.h>

#include <iostream>

#include "SnobotSim/SensorActuatorRegistry.h"

void AnalogInWidget::updateDisplay()
{
    static int numAccum = HAL_GetNumAccumulators();

    ImGui::Begin("Analog In");

    ImGui::PushItemWidth(ImGui::GetFontSize() * 8);
    for (const auto& pair : SensorActuatorRegistry::Get().GetIAnalogInWrapperMap())
    {
        auto wrapper = pair.second;
        const char* name = wrapper->GetName().c_str();

        if (pair.first < numAccum && HALSIM_GetAnalogGyroInitialized(pair.first))
        {
            ImGui::LabelText(name, "AnalogGyro[%d]", pair.first);
        }
        else
        {
            float voltage = static_cast<float>(wrapper->GetVoltage());
            if (ImGui::SliderFloat(name, &voltage, 0.0, 5.0))
            {
                std::cout << "Voltage changed from " << wrapper->GetVoltage() << " to " << voltage << std::endl;
                wrapper->SetVoltage(voltage);
            }
        }
    }
    ImGui::PopItemWidth();

    ImGui::End();
}
