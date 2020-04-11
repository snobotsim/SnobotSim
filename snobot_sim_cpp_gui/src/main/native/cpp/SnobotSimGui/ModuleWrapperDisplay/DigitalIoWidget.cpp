

#include "SnobotSimGui/ModuleWrapperDisplay/DigitalIoWidget.h"

#include <hal/Ports.h>
#include <imgui.h>
#include <mockdata/DIOData.h>
#include <mockdata/EncoderData.h>

#include <iostream>
#include <map>

#include "SnobotSim/SensorActuatorRegistry.h"
#include "SnobotSimGui/Utilities/IndicatorDrawer.h"

using WpiEncoderMap = std::map<int, std::pair<int, int>>;

namespace
{
WpiEncoderMap GetWpiEncoderPorts()
{
    static int numEncoder = HAL_GetNumEncoders();
    static int numDIO = HAL_GetNumDigitalChannels();

    WpiEncoderMap output;

    for (int i = 0; i < numEncoder; ++i)
    {
        if (HALSIM_GetEncoderInitialized(i))
        {
            int channelA = HALSIM_GetEncoderDigitalChannelA(i);
            if (channelA < 0 || channelA >= numDIO)
            {
                channelA = -1;
            }

            int channelB = HALSIM_GetEncoderDigitalChannelB(i);
            if (channelB < 0 || channelB >= numDIO)
            {
                channelB = -1;
            }

            if (channelA >= 0)
            {
                output[channelA] = std::make_pair(channelA, channelB);
            }
            if (channelB >= 0)
            {
                output[channelB] = std::make_pair(channelA, channelB);
            }
        }
    }

    return output;
}
} // namespace

void DigitalIoWidget::updateDisplay()
{
    static int numDIO = HAL_GetNumDigitalChannels();

    ImGui::Begin("Digital IO");

    ImGui::PushItemWidth(ImGui::GetFontSize() * 8);

    WpiEncoderMap wpiEncoders = GetWpiEncoderPorts();
    for (const auto& pair : SensorActuatorRegistry::Get().GetIDigitalIoWrapperMap())
    {
        auto wrapper = pair.second;
        const char* name = wrapper->GetName().c_str();
        // bool open = ImGui::CollapsingHeader(
        //     wrapper->GetName().c_str(), true ? ImGuiTreeNodeFlags_DefaultOpen : 0);

        WpiEncoderMap::iterator wpiIter = wpiEncoders.find(pair.first);
        if (wpiIter != wpiEncoders.end())
        {
            ImGui::LabelText(name, "Encoder[%d,%d]", wpiIter->second.first, wpiIter->second.second);
        }
        else if (pair.first < numDIO && !HALSIM_GetDIOIsInput(pair.first))
        {
            ImGui::LabelText(name, "");
            AddIndicator(wrapper->Get() ? IM_COL32(0, 255, 0, 255) : IM_COL32(255, 0, 0, 255));
        }
        else
        {
            bool isSet = wrapper->Get();
            ImGui::PushStyleColor(ImGuiCol_Text, isSet ? IM_COL32(0, 255, 0, 255) : IM_COL32(255, 0, 0, 255));
            if (ImGui::Button(name))
            {
                std::cout << "Button flipped" << std::endl;
                wrapper->Set(!isSet);
            }
            ImGui::PopStyleColor();

            // ImGui::LabelText(name, "Input[%d]", wrapper->Get());
        }

        // std::cout << "  SC: " << pair.first << ", " << pair.second << std::endl;
    }
    ImGui::PopItemWidth();

    ImGui::End();
}
