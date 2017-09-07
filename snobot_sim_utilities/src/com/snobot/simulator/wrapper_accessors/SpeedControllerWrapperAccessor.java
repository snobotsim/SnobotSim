
package com.snobot.simulator.wrapper_accessors;

import java.util.List;

import com.snobot.simulator.DcMotorModelConfig;

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
    
    public void updateAllSpeedControllers(double aUpdatePeriod);
    
    public List<Integer> getPortList();

    public DcMotorModelConfig getMotorConfig(int aPort);

    public double getMotorSimSimpleModelConfig(int aPort);

    public double getMotorSimStaticModelConfig(int aPort);

    public double getMotorSimGravitationalModelConfig(int aPort);

    public MotorSimType getMotorSimType(int aHandle);

    public double getPosition(int i);

    public double getVelocity(int i);

    public double getCurrent(int i);
}
