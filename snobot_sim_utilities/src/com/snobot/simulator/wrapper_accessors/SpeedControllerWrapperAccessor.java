
package com.snobot.simulator.wrapper_accessors;

import java.util.List;

import com.snobot.simulator.motor_sim.DcMotorModelConfig;
import com.snobot.simulator.motor_sim.GravityLoadMotorSimulationConfig;
import com.snobot.simulator.motor_sim.RotationalLoadMotorSimulationConfig;
import com.snobot.simulator.motor_sim.SimpleMotorSimulationConfig;
import com.snobot.simulator.motor_sim.StaticLoadMotorSimulationConfig;

public interface SpeedControllerWrapperAccessor
{
    public enum MotorSimType
    {
        None("None"), Simple("Simple"), StaticLoad("Static Load"), RotationalLoad("Rotational Load"), GravitationalLoad("Gravitational Load");

        private String mDisplayName;

        MotorSimType(String aDisplayName)
        {
            mDisplayName = aDisplayName;
        }

        public String toString()
        {
            return mDisplayName;
        }
    }
    
    public void setName(int aPort, String aName);
    
    public String getName(int aPort);

    public boolean getWantsHidden(int aPort);
    
    public double getVoltagePercentage(int aPort);
    
    public List<Integer> getPortList();

    public DcMotorModelConfig getMotorConfig(int aPort);

    public SimpleMotorSimulationConfig getMotorSimSimpleModelConfig(int aPort);

    public StaticLoadMotorSimulationConfig getMotorSimStaticModelConfig(int aPort);

    public GravityLoadMotorSimulationConfig getMotorSimGravitationalModelConfig(int aPort);

    public RotationalLoadMotorSimulationConfig getMotorSimRotationalModelConfig(int aPort);

    public MotorSimType getMotorSimType(int aHandle);

    public double getPosition(int i);

    public double getVelocity(int i);

    public double getCurrent(int i);

    public double getAcceleration(int i);
}
