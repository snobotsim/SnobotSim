package com.snobot.simulator.motor_sim;

public interface IMotorSimulator
{

    void setVoltagePercentage(double aSpeed);

    double getVoltagePercentage();

    double getAcceleration();

    double getVelocity();

    double getPosition();

    void reset();

    void reset(double aPosition, double aVelocity, double aCurrent);

    void update(double aCycleTime);

    public class NullMotorSimulator implements IMotorSimulator
    {
        private double mSpeed;

        @Override
        public void setVoltagePercentage(double aSpeed)
        {
            mSpeed = aSpeed;
        }

        @Override
        public double getVoltagePercentage()
        {
            return mSpeed;
        }

        @Override
        public double getAcceleration()
        {
            return 0;
        }

        @Override
        public double getVelocity()
        {
            return 0;
        }

        @Override
        public double getPosition()
        {
            return 0;
        }

        @Override
        public void reset()
        {
        }

        @Override
        public void reset(double aPosition, double aVelocity, double aCurrent)
        {

        }

        @Override
        public void update(double cycleTime)
        {

        }

    }
}
