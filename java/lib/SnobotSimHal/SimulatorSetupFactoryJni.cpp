
#include <assert.h>
#include <jni.h>
#include "com_snobot_simulator_module_wrapper_AnalogSourceWrapperJni.h"
#include "SnobotSim/SensorActuatorRegistry.h"
#include "SnobotSim/MotorSim/SimpleMotorSimulator.h"

extern "C"
{

/*
 * Class:     com_snobot_simulator_SimulatorSetupFactoryJni
 * Method:    setupSimulation
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_SimulatorSetupFactoryJni_setupSimulation(JNIEnv *, jclass)
{
    int32_t leftEncoderHandle = 1029;
    int32_t rightEncoderHandle = 258;
    int32_t leftSCHandle = 0;
    int32_t rightSCHandle = 1;

    std::shared_ptr<SpeedControllerWrapper> leftSc = SensorActuatorRegistry::Get().GetSpeedControllerWrapper(leftSCHandle);
    std::shared_ptr<SpeedControllerWrapper> rightSc = SensorActuatorRegistry::Get().GetSpeedControllerWrapper(rightSCHandle);

    leftSc->SetMotorSimulator(std::shared_ptr < SimpleMotorSimulator > (new SimpleMotorSimulator(12)));
    rightSc->SetMotorSimulator(std::shared_ptr < SimpleMotorSimulator > (new SimpleMotorSimulator(24)));

    SensorActuatorRegistry::Get().GetEncoderWrapper(leftEncoderHandle)->SetSpeedController(leftSc);
    SensorActuatorRegistry::Get().GetEncoderWrapper(rightEncoderHandle)->SetSpeedController(rightSc);
}

}
