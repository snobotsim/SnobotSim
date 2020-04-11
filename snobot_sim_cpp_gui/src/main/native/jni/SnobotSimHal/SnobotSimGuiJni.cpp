
#include <jni.h>

#include <cassert>
#include <iostream>

#include "SnobotSimGui/SnobotSimGui.h"
#include "com_snobot_simulator_jni_SnobotSimGuiJni.h"
#include "wpi/jni_util.h"

using namespace wpi::java;

namespace SnobotSim
{
SnobotSimGui* gGui = nullptr;
std::thread gThread;
} // namespace SnobotSim

extern "C" {

JNIEXPORT void JNICALL JNI_OnUnload(JavaVM* vm, void* reserved)
{
    std::cout << "Stopping gui" << std::endl;
    if (SnobotSim::gGui)
    {
        SnobotSim::gGui->Stop();
        SnobotSim::gThread.join();
    }

    std::cout << "Stopped" << std::endl;
}

/*
 * Class:     com_snobot_simulator_jni_SnobotSimGuiJni
 * Method:    initializeGui
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_com_snobot_simulator_jni_SnobotSimGuiJni_initializeGui
  (JNIEnv*, jclass)
{
    std::cout << "Initializing C++ GUI" << std::endl;
    SnobotSim::gGui = new SnobotSim::SnobotSimGui();
    SnobotSim::gThread = std::move(std::thread([] { SnobotSim::gGui->StartThread(); }));
}

} // extern "C"
