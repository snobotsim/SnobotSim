package com.snobot.simulator.simulator_components;

public interface IMotorFeedbackSensor
{
    public void setPosition(double aPosition);
    public double getPosition();

    public static class NullFeedbackSensor implements IMotorFeedbackSensor
    {
        @Override
        public double getPosition()
        {
            return 0;
        }

        @Override
        public void setPosition(double aPosition)
        {

        }
    }

}
