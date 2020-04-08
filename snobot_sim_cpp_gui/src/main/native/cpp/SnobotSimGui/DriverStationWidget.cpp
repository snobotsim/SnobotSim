
#include "SnobotSimGui/DriverStationWidget.h"
#include <mockdata/DriverStationData.h>
#include <hal/HALBase.h>
#include <mockdata/MockHooks.h>
#include <mockdata/NotifierData.h>

#include <GLFW/glfw3.h>
#include <imgui.h>
#include <cstring>
#include <string>
#include <vector>


void DriverStationWidget::RenderRobotState()
{
    bool isEnabled = HALSIM_GetDriverStationEnabled();
    bool isAuto = HALSIM_GetDriverStationAutonomous();
    bool isTest = HALSIM_GetDriverStationTest();
    
    if (ImGui::Selectable("Disabled", !isEnabled))
    {
      HALSIM_SetDriverStationEnabled(false);
    }
    if (ImGui::Selectable("Autonomous", isEnabled && isAuto && !isTest)) 
    {
      HALSIM_SetDriverStationAutonomous(true);
      HALSIM_SetDriverStationTest(false);
      HALSIM_SetDriverStationEnabled(true);
    }
    if (ImGui::Selectable("Teleoperated", isEnabled && !isAuto && !isTest)) 
    {
      HALSIM_SetDriverStationAutonomous(false);
      HALSIM_SetDriverStationTest(false);
      HALSIM_SetDriverStationEnabled(true);
    }
    if (ImGui::Selectable("Test", isEnabled && isTest)) 
    {
      HALSIM_SetDriverStationAutonomous(false);
      HALSIM_SetDriverStationTest(true);
      HALSIM_SetDriverStationEnabled(true);
    }
}

void DriverStationWidget::RenderTiming()
{
  int32_t status = 0;
  uint64_t curTime = HAL_GetFPGATime(&status);

  if (ImGui::Button("Run")) 
  {
      HALSIM_ResumeTiming();
  }
  ImGui::SameLine();
  if (ImGui::Button("Pause")) 
  {
      HALSIM_PauseTiming();
  }
  ImGui::SameLine();
  ImGui::PushButtonRepeat(true);

  if (ImGui::Button("Step")) 
  {
    HALSIM_PauseTiming();
    uint64_t nextTimeout = HALSIM_GetNextNotifierTimeout();
    if (nextTimeout != UINT64_MAX) 
    {
        HALSIM_StepTiming(nextTimeout - curTime);
    }
  }
  ImGui::PopButtonRepeat();
  ImGui::PushItemWidth(ImGui::GetFontSize() * 4);
  ImGui::LabelText("FPGA Time", "%.3f", curTime / 1000000.0);
  ImGui::PopItemWidth();

  static std::vector<HALSIM_NotifierInfo> notifiers;
  int32_t num = HALSIM_GetNotifierInfo(notifiers.data(), notifiers.size());
  if (static_cast<uint32_t>(num) > notifiers.size()) 
  {
    notifiers.resize(num);
    HALSIM_GetNotifierInfo(notifiers.data(), notifiers.size());
  }
  if (num > 0) 
  {
      ImGui::Separator();
  }
  ImGui::PushItemWidth(ImGui::GetFontSize() * 4);
  for (int32_t i = 0; i < num; ++i)
  {
    ImGui::LabelText(notifiers[i].name, "%.3f",
                     notifiers[i].timeout / 1000000.0);
  }
  ImGui::PopItemWidth();
}

void DriverStationWidget::RenderDSState()
{
    double curTime = glfwGetTime();
    bool fmsAttached = HALSIM_GetDriverStationFmsAttached();
    bool dsAttached = HALSIM_GetDriverStationDsAttached();

    if (ImGui::Checkbox("FMS Attached", &fmsAttached))
    {
        HALSIM_SetDriverStationFmsAttached(fmsAttached);
    }
    if (ImGui::Checkbox("DS Attached", &dsAttached)) 
    {
        HALSIM_SetDriverStationDsAttached(dsAttached);
    }

    // Alliance Station
    static const char* stations[] = {"Red 1",  "Red 2",  "Red 3",
                                    "Blue 1", "Blue 2", "Blue 3"};
    int allianceStationId = HALSIM_GetDriverStationAllianceStationId();
    ImGui::SetNextItemWidth(ImGui::GetFontSize() * 8);
    if (ImGui::Combo("Alliance Station", &allianceStationId, stations, 6))
    {
        HALSIM_SetDriverStationAllianceStationId(
            static_cast<HAL_AllianceStationID>(allianceStationId));
    }

    // Match Time
    static bool matchTimeEnabled = true;
    ImGui::Checkbox("Match Time Enabled", &matchTimeEnabled);

    static double startMatchTime = 0.0;
    double matchTime = HALSIM_GetDriverStationMatchTime();
    ImGui::SetNextItemWidth(ImGui::GetFontSize() * 8);
    if (ImGui::InputDouble("Match Time", &matchTime, 0, 0, "%.1f",
                            ImGuiInputTextFlags_EnterReturnsTrue)) 
    {
        HALSIM_SetDriverStationMatchTime(matchTime);
        startMatchTime = curTime - matchTime;
    } 
    else if (!HALSIM_GetDriverStationEnabled() || HALSIM_IsTimingPaused()) 
    {
        startMatchTime = curTime - matchTime;
    } 
    else if (matchTimeEnabled) 
    {
        HALSIM_SetDriverStationMatchTime(curTime - startMatchTime);
    }

    ImGui::SameLine();
    if (ImGui::Button("Reset"))
    {
        HALSIM_SetDriverStationMatchTime(0.0);
        startMatchTime = curTime;
    }

    // Game Specific Message
    static HAL_MatchInfo matchInfo;
    ImGui::SetNextItemWidth(ImGui::GetFontSize() * 8);
    if (ImGui::InputText("Game Specific",
                        reinterpret_cast<char*>(matchInfo.gameSpecificMessage),
                        sizeof(matchInfo.gameSpecificMessage),
                        ImGuiInputTextFlags_EnterReturnsTrue)) 
    {
        matchInfo.gameSpecificMessageSize =
            std::strlen(reinterpret_cast<char*>(matchInfo.gameSpecificMessage));
        HALSIM_SetMatchInfo(&matchInfo);
    }
}

void DriverStationWidget::updateDisplay()
{

    ImGui::Begin("Robot State", nullptr, ImGuiWindowFlags_AlwaysAutoResize);

    RenderRobotState();
    ImGui::Separator();
    RenderTiming();
    ImGui::Separator();
    RenderDSState();

    ImGui::End();
}