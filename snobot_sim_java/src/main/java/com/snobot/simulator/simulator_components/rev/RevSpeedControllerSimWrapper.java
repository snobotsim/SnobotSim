package com.snobot.simulator.simulator_components.rev;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.SimDeviceDumpHelper;
import com.snobot.simulator.SimDeviceHelper;
import com.snobot.simulator.simulator_components.smart_sc.SmartScAnalogIn;
import com.snobot.simulator.simulator_components.smart_sc.SmartScEncoder;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;

import edu.wpi.first.hal.SimDouble;
import edu.wpi.first.hal.sim.SimDeviceSim;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.snobot.simulator.simulator_components.smart_sc.BaseCanSmartSpeedController;

public class RevSpeedControllerSimWrapper extends BaseCanSmartSpeedController
{
    private static final Logger sLOGGER = LogManager.getLogger(RevSpeedControllerSimWrapper.class);

    private SimDouble  mSetpointCommandCtrl;
    private SimDouble  mSetpointCommandValue;
    private SimDouble  mAppliedOutputAppliedOutput;
    private SimDouble  mSensorTypeSensorType;
    private SimDouble  mFeedbackDeviceSensorID;
    private SimDouble  mEncoderPosition;
    private SimDouble  mEncoderVelocity;



    private static final class SlottedVariables
    {
        private SimDouble mPGain;
        private SimDouble mIGain;
        private SimDouble mDGain;
        private SimDouble mFFGain;
    }
    
    private static final int NUM_SLOTS = 6;
    private final SlottedVariables[] mSlottedVariables = new SlottedVariables[NUM_SLOTS];
    
    public RevSpeedControllerSimWrapper(int aCanHandle)
    {
        super(aCanHandle, "Rev", NUM_SLOTS);
    }

    @Override
    public void setInitialized(boolean aInitialized)
    {
        super.setInitialized(aInitialized);


        String deviceName = "RevCANSparkMaxDriverWrapper " + mCanHandle + "[" + mCanHandle + "]";
        System.out.println("----------------- Initializing " + deviceName);
        SimDeviceSim simDevice = new SimDeviceSim(deviceName);
        SimDeviceDumpHelper.dumpSimDevices();
        mSetpointCommandCtrl = SimDeviceHelper.getSimDouble(simDevice, "pointCommand_ctrl");
        mSetpointCommandValue = SimDeviceHelper.getSimDouble(simDevice, "pointCommand_value");
        mAppliedOutputAppliedOutput = SimDeviceHelper.getSimDouble(simDevice, "AppliedOutput_appliedOutput");
        mSensorTypeSensorType = SimDeviceHelper.getSimDouble(simDevice, "SensorType_sensorType");
        mFeedbackDeviceSensorID = SimDeviceHelper.getSimDouble(simDevice, "FeedbackDevice_sensorID");
        mEncoderPosition = SimDeviceHelper.getSimDouble(simDevice, "EncoderPosition_position");
        mEncoderVelocity = SimDeviceHelper.getSimDouble(simDevice, "EncoderVelocity_velocity");
        System.out.println(deviceName  + ", " + simDevice + ", " +  mSetpointCommandCtrl + ", " + mSetpointCommandValue);

        for (int i = 0; i < NUM_SLOTS; ++i)
        {
            mSlottedVariables[i] = new SlottedVariables();
            mSlottedVariables[i].mPGain = SimDeviceHelper.getSimDouble(simDevice, "P_gain[" + i + "]");
            mSlottedVariables[i].mIGain = SimDeviceHelper.getSimDouble(simDevice, "I_gain[" + i + "]");
            mSlottedVariables[i].mDGain = SimDeviceHelper.getSimDouble(simDevice, "D_gain[" + i + "]");
            mSlottedVariables[i].mFFGain = SimDeviceHelper.getSimDouble(simDevice, "FF_gain[" + i + "]");
        }
    }

    @Override
    protected void registerFeedbackSensor()
    {
        switch (mFeedbackDevice)
        {
        case Encoder:
            if (!DataAccessorFactory.getInstance().getEncoderAccessor().getPortList().contains(mHandle))
            {
                DataAccessorFactory.getInstance().getEncoderAccessor().createSimulator(mHandle, SmartScEncoder.class.getName());
                DataAccessorFactory.getInstance().getEncoderAccessor().connectSpeedController(getHandle(), getHandle());
                sLOGGER.log(Level.WARN, "REV Encoder on port " + mCanHandle + " was not registerd before starting the robot");
            }
            SensorActuatorRegistry.get().getEncoders().get(getHandle()).setInitialized(true);
            break;
        case Analog:
            if (!DataAccessorFactory.getInstance().getAnalogInAccessor().getPortList().contains(mHandle))
            {
                DataAccessorFactory.getInstance().getAnalogInAccessor().createSimulator(mHandle, SmartScAnalogIn.class.getName());
                sLOGGER.log(Level.WARN, "REV Analog on port " + mCanHandle + " was not registerd before starting the robot");
            }
            SensorActuatorRegistry.get().getAnalogIn().get(getHandle()).setInitialized(true);
            break;
        default:
            sLOGGER.log(Level.ERROR, "Unsupported feedback device " + mFeedbackDevice);
            break;
        }
    }

    @Override
    protected double getPositionUnitConversion()
    {
        return 1;
    }

    @Override
    protected double getMotionMagicAccelerationUnitConversion()
    {
        return 1;
    }

    @Override
    protected double getMotionMagicVelocityUnitConversion()
    {
        return 1;
    }

    @Override
    protected double calculateMotionProfileOutput(double aCurrentPosition, double aCurrentVelocity, int aModeType)
    {
        throw new IllegalStateException("Not supported");
    }

    @SuppressWarnings("PMD.AvoidLiteralsInIfCondition")
    public void setCanFeedbackDevice(int aFeedbackDevice)
    {
        FeedbackDevice newDevice = null;
        if (aFeedbackDevice == 1 || aFeedbackDevice == 2)
        {
            newDevice = FeedbackDevice.Encoder;
        }
        else
        {
            sLOGGER.log(Level.WARN, "Unsupported feedback device " + aFeedbackDevice);
        }

        setCanFeedbackDevice(newDevice);
    }
    
    public void handleSetSetpointCommand()
    {
        float value = (float) mSetpointCommandValue.get();
        int ctrl = (int) mSetpointCommandCtrl.get();
        sLOGGER.log(Level.DEBUG, "SetpointCommand " + value + ", " + ctrl);

        switch (ctrl)
        {
        // Throttle
        case 0:
            setRawGoal(value);
            break;
        // Velocity
        case 1:
            setSpeedGoal(value);
            break;
        // Position
        case 3:
            setPositionGoal(value);
            break;
        // SmartMotion
        case 4:
            setMotionMagicGoal(value);
            break;
        default:
            sLOGGER.log(Level.ERROR, String.format("Unknown demand mode %d", ctrl));
            break;
        }
    }
    
    public void handleSetSensorType()
    {
        int type = (int) mSensorTypeSensorType.get();
        setCanFeedbackDevice(type);
    }
    
    public void handleSetFeedbackDevice()
    {
        int type = (int) mFeedbackDeviceSensorID.get();
        setCanFeedbackDevice(type);
    }
    
    public void handleSetPGain(int aSlot)
    {
        double value = mSlottedVariables[aSlot].mPGain.get();
        setPGain(aSlot, value);
    }
    
    public void handleSetIGain(int aSlot)
    {
        double value = mSlottedVariables[aSlot].mIGain.get();
        setIGain(aSlot, value);
    }
    
    public void handleSetDGain(int aSlot)
    {
        double value = mSlottedVariables[aSlot].mDGain.get();
        setDGain(aSlot, value);
    }
    
    public void handleSetFFGain(int aSlot)
    {
        double value = mSlottedVariables[aSlot].mFFGain.get();
        setFGain(aSlot, value);
    }

    
    public void handleGetAppliedOutput()
    {
        mAppliedOutputAppliedOutput.set(get());
    }
    
    public void handleGetEncoderPosition()
    {
        mEncoderPosition.set(getPosition());
    }
    
    public void handleGetEncoderVelocity()
    {
        mEncoderVelocity.set(getVelocity());
    }
}
