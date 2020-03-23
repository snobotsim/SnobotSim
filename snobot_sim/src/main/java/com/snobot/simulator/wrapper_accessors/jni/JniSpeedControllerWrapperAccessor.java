
package com.snobot.simulator.wrapper_accessors.jni;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.snobot.simulator.jni.LocalDcMotorModelConfig;
import com.snobot.simulator.jni.module_wrapper.SpeedControllerWrapperJni;
import com.snobot.simulator.module_wrapper.interfaces.IMotorFeedbackSensor;
import com.snobot.simulator.module_wrapper.interfaces.IMotorSimulator;
import com.snobot.simulator.module_wrapper.interfaces.IPwmWrapper;
import com.snobot.simulator.motor_sim.DcMotorModelConfig;
import com.snobot.simulator.motor_sim.GravityLoadMotorSimulationConfig;
import com.snobot.simulator.motor_sim.RotationalLoadMotorSimulationConfig;
import com.snobot.simulator.motor_sim.SimpleMotorSimulationConfig;
import com.snobot.simulator.motor_sim.StaticLoadMotorSimulationConfig;
import com.snobot.simulator.wrapper_accessors.SpeedControllerWrapperAccessor;

public class JniSpeedControllerWrapperAccessor implements SpeedControllerWrapperAccessor {
    private static class SpeedControllerWrapper implements IPwmWrapper {
        private final int mHandle;

        private SpeedControllerWrapper(int aHandle, String aType) {
            SpeedControllerWrapperJni.createSimulator(aHandle, aType);
            mHandle = aHandle;
        }

        @Override
        public boolean isInitialized() {
            return SpeedControllerWrapperJni.isInitialized(mHandle);
        }

        @Override
        public void setInitialized(boolean aInitialized) {
            // TODO Auto-generated method stub

        }

        @Override
        public String getName() {
            return SpeedControllerWrapperJni.getName(mHandle);
        }

        @Override
        public void setName(String aName) {
            SpeedControllerWrapperJni.setName(mHandle, aName);
        }

        @Override
        public boolean getWantsHidden() {
            return SpeedControllerWrapperJni.getWantsHidden(mHandle);
        }

        @Override
        public void setWantsHidden(boolean aVisible) {
            // TODO Auto-generated method stub

        }

        @Override
        public void close() throws Exception {
            // TODO Auto-generated method stub

        }

        @Override
        public IMotorSimulator getMotorSimulator() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void setMotorSimulator(IMotorSimulator aSimulator) {
            // TODO Auto-generated method stub

        }

        @Override
        public void reset(double aPosition, double aVelocity, double aCurrent) {
            SpeedControllerWrapperJni.reset(mHandle, aPosition, aVelocity, aCurrent);
        }

        @Override
        public void reset() {
            reset(0, 0, 0);
        }

        @Override
        public double getPosition() {
            return SpeedControllerWrapperJni.getPosition(mHandle);
        }

        @Override
        public double getVelocity() {
            return SpeedControllerWrapperJni.getVelocity(mHandle);
        }

        @Override
        public double getAcceleration() {
            return SpeedControllerWrapperJni.getAcceleration(mHandle);
        }

        @Override
        public double getCurrent() {
            return SpeedControllerWrapperJni.getCurrent(mHandle);
        }

        @Override
        public void set(double aSpeed) {
            // TODO Auto-generated method stub

        }

        @Override
        public double get() {
            return SpeedControllerWrapperJni.getVoltagePercentage(mHandle);
        }

        @Override
        public void update(double aWaitTime) {
            // TODO Auto-generated method stub

        }

        @Override
        public void setFeedbackSensor(IMotorFeedbackSensor aFeedbackSensor) {
            // TODO Auto-generated method stub

        }

        @Override
        public IMotorFeedbackSensor getFeedbackSensor() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public int getHandle() {
            // TODO Auto-generated method stub
            return 0;
        }
    }

    @Override
    public double getVoltagePercentage(int aPort)
    {
        return SpeedControllerWrapperJni.getVoltagePercentage(aPort);
    }

    @Override
    public List<Integer> getPortList()
    {
        return IntStream.of(SpeedControllerWrapperJni.getPortList()).boxed().collect(Collectors.toList());
    }

    @Override
    public DcMotorModelConfig getMotorConfig(int aPort)
    {
        LocalDcMotorModelConfig config = SpeedControllerWrapperJni.getMotorConfig(aPort);
        return config == null ? null : config.getConfig();
    }

    @Override
    public SimpleMotorSimulationConfig getMotorSimSimpleModelConfig(int aPort)
    {
        double maxSpeed = SpeedControllerWrapperJni.getMotorSimSimpleModelConfig(aPort);
        return new SimpleMotorSimulationConfig(maxSpeed);
    }

    @Override
    public StaticLoadMotorSimulationConfig getMotorSimStaticModelConfig(int aPort)
    {
        double load = SpeedControllerWrapperJni.getMotorSimStaticModelConfig_load(aPort);
        double conversionFactor = SpeedControllerWrapperJni.getMotorSimStaticModelConfig_conversionFactor(aPort);
        return new StaticLoadMotorSimulationConfig(load, conversionFactor);
    }

    @Override
    public GravityLoadMotorSimulationConfig getMotorSimGravitationalModelConfig(int aPort)
    {
        double load = SpeedControllerWrapperJni.getMotorSimGravitationalModelConfig(aPort);
        return new GravityLoadMotorSimulationConfig(load);
    }

    @Override
    public RotationalLoadMotorSimulationConfig getMotorSimRotationalModelConfig(int aPort)
    {
        double armCOM = SpeedControllerWrapperJni.getMotorSimRotationalModelConfig_armCenterOfMass(aPort);
        double armMass = SpeedControllerWrapperJni.getMotorSimRotationalModelConfig_armMass(aPort);
        return new RotationalLoadMotorSimulationConfig(armCOM, armMass);
    }

    @Override
    public MotorSimType getMotorSimType(int aHandle)
    {
        int rawType = SpeedControllerWrapperJni.getMotorSimTypeNative(aHandle);
        return MotorSimType.values()[rawType];
    }

    @Override
    public double getPosition(int aHandle)
    {
        return SpeedControllerWrapperJni.getPosition(aHandle);
    }

    @Override
    public double getVelocity(int aHandle)
    {
        return SpeedControllerWrapperJni.getVelocity(aHandle);
    }

    @Override
    public double getCurrent(int aHandle)
    {
        return SpeedControllerWrapperJni.getCurrent(aHandle);
    }

    @Override
    public double getAcceleration(int aHandle)
    {
        return SpeedControllerWrapperJni.getAcceleration(aHandle);
    }

    @Override
    public void reset(int aHandle, double aPosition, double aVelocity, double aCurrent)
    {
        SpeedControllerWrapperJni.reset(aHandle, aPosition, aVelocity, aCurrent);
    }

    @Override
    public String getType(int aPort)
    {
        return null;
    }

    @Override
    public IPwmWrapper createSimulator(int aPort, String aType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IPwmWrapper getWrapper(int aHandle) {
        // TODO Auto-generated method stub
        return null;
    }
}
