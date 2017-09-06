package com.snobot.simulator.motor_sim;

import com.snobot.simulator.module_wrapper.SpeedControllerWrapper;

public class RotationalLoadDcMotorSim extends BaseDcMotorSimulator
{
    protected final static double sGRAVITY = 9.8;

    protected final SpeedControllerWrapper mSpeedController;
    protected final double mArmInertia;
    protected final double mGravityBasedTorqueFactor;
    
    //Helper springs
    protected final double mConstantAssistTorque;
    protected final double mOverCenterAssistTorque;

    /**
     * 
     * @param aModel
     *            The motor model
     * @param aSpeedController
     *            The speed controller that is wrapped
     * @param aArmCenterOfMass
     *            The location of the center of mass, in meters
     * @param aArmMass
     *            The mass of the arm, in kg
     */
    public RotationalLoadDcMotorSim(
            DcMotorModel aModel,
            SpeedControllerWrapper aSpeedController,
            double aArmCenterOfMass,
            double aArmMass)
    {
        this(aModel, aSpeedController, aArmCenterOfMass, aArmMass, 0, 0);
    }

    /**
     * 
     * @param aModel
     *            The motor model
     * @param aSpeedController
     *            The speed controller that is wrapped
     * @param aArmCenterOfMass
     *            The location of the center of mass, in meters
     * @param aArmMass
     *            The mass of the arm, in kg
     * @param aConstantAssistTorque
     *            torque provided constantly over the entire range of motion, in
     *            N*m
     * @param aOverCenterAssistTorque
     *            torque provided varying with sin of angle (same effect as
     *            gravity) due to over-center, in N*m
     */
    public RotationalLoadDcMotorSim(
            DcMotorModel aModel,
            SpeedControllerWrapper aSpeedController,
            double aArmCenterOfMass,
            double aArmMass,
            double aConstantAssistTorque,
            double aOverCenterAssistTorque)
    {
        super(aModel);

        mSpeedController = aSpeedController;
        mArmInertia = aArmMass * aArmCenterOfMass * aArmCenterOfMass;
        mGravityBasedTorqueFactor = aArmMass * aArmCenterOfMass * sGRAVITY;
        mConstantAssistTorque = aConstantAssistTorque;
        mOverCenterAssistTorque = aOverCenterAssistTorque;
    }


    @Override
    public void update(double cycleTime)
    {
        double position = mSpeedController.getPosition();
        double gravityTorque = mGravityBasedTorqueFactor * Math.sin(position);
        gravityTorque += mConstantAssistTorque;
        gravityTorque += mOverCenterAssistTorque * Math.sin(position);

        double inVolts = mVoltagePercentage * 12;

        mMotorModel.step(inVolts, mArmInertia, gravityTorque, cycleTime);
    }
}
