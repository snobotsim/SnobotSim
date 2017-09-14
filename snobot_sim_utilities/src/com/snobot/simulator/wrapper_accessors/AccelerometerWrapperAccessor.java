
package com.snobot.simulator.wrapper_accessors;

public interface AccelerometerWrapperAccessor extends IBasicSensorActuatorWrapperAccessor
{
    public void register(int aPort, String aName);
    
    public double getAcceleration(int aPort);
    
    public void setAcceleration(int aPort, double aAcceleration);

}
