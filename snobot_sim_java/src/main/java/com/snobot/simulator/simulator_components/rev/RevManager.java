package com.snobot.simulator.simulator_components.rev;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.simulator_components.smart_sc.BaseCanSmartSpeedController;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;

@SuppressWarnings({"PMD.ExcessiveMethodLength", "PMD.NcssCount", "PMD.CyclomaticComplexity"})
public class RevManager
{
    private static final Logger sLOGGER = LogManager.getLogger(RevManager.class);

    public void handleMessage(String aCallback, int aCanPort, ByteBuffer aData)
    {
        aData.order(ByteOrder.LITTLE_ENDIAN);

        Set<String> unsupportedFunctions = new HashSet<>();
        unsupportedFunctions.add("GetFault");

        switch (aCallback)
        {
        case "Create":
            createSim(aCanPort);
            break;
        case "SetpointCommand":
        {
            RevSpeedControllerSimWrapper wrapper = getMotorControllerWrapper(aCanPort);
            wrapper.handleSetSetpointCommand();
            break;
        }
        case "SetFollow":
        {
            int followerID = aData.getInt();
            int leadId = followerID & 0x3F;

            BaseCanSmartSpeedController leadWrapper = getMotorControllerWrapper(leadId);
            RevSpeedControllerSimWrapper follower = getMotorControllerWrapper(aCanPort);

            sLOGGER.log(Level.INFO, "Setting SparkMax " + aCanPort + " to follow " + leadId);

            leadWrapper.addFollower(follower);

            break;
        }
        case "SetSensorType":
        {
            RevSpeedControllerSimWrapper wrapper = getMotorControllerWrapper(aCanPort);
            wrapper.handleSetSensorType();
            break;
        }
        case "SetFeedbackDevice":
        {
            RevSpeedControllerSimWrapper wrapper = getMotorControllerWrapper(aCanPort);
            wrapper.handleSetFeedbackDevice();
            break;
        }
        case "SetP":
        {
            RevSpeedControllerSimWrapper wrapper = getMotorControllerWrapper(aCanPort);
            int slot = aData.getInt();
            wrapper.handleSetPGain(slot);
            break;
        }
        case "SetI":
        {
            RevSpeedControllerSimWrapper wrapper = getMotorControllerWrapper(aCanPort);
            int slot = aData.getInt();
            wrapper.handleSetIGain(slot);
            break;
        }
        case "SetD":
        {
            RevSpeedControllerSimWrapper wrapper = getMotorControllerWrapper(aCanPort);
            int slot = aData.getInt();
            wrapper.handleSetDGain(slot);
            break;
        }
        case "SetFF":
        {
            RevSpeedControllerSimWrapper wrapper = getMotorControllerWrapper(aCanPort);
            int slot = aData.getInt();
            wrapper.handleSetFFGain(slot);
            break;
        }
//        case "SetSmartMotionMaxVelocity":
//        {
//            RevSpeedControllerSimWrapper wrapper = getMotorControllerWrapper(aCanPort);
//            aData.getInt();
//            double value = aData.getFloat();
//            wrapper.setMotionMagicMaxVelocity((int) value);
//            break;
//        }
//        case "SetSmartMotionMaxAccel":
//        {
//            RevSpeedControllerSimWrapper wrapper = getMotorControllerWrapper(aCanPort);
//            aData.getInt();
//            double value = aData.getFloat();
//            wrapper.setMotionMagicMaxAcceleration((int) value);
//            break;
//        }
//        case "SetEncoderPosition":
//        {
//            int position = aData.getInt();
//            RevSpeedControllerSimWrapper wrapper = getMotorControllerWrapper(aCanPort);
//            wrapper.reset(position, wrapper.getVelocity(), wrapper.getCurrent());
//            break;
//        }
//
//        ////////////////////////
//        // Getters
//        ////////////////////////
        case "GetAppliedOutput":
        {
            RevSpeedControllerSimWrapper wrapper = getMotorControllerWrapper(aCanPort);
            wrapper.handleGetAppliedOutput();
            break;
        }
        case "GetEncoderPosition":
        {
            RevSpeedControllerSimWrapper wrapper = getMotorControllerWrapper(aCanPort);
            wrapper.handleGetEncoderPosition();
            break;
        }
        case "GetEncoderVelocity":
        {
            RevSpeedControllerSimWrapper wrapper = getMotorControllerWrapper(aCanPort);
            wrapper.handleGetEncoderVelocity();
            break;
        }
        default:
            if (unsupportedFunctions.contains(aCallback))
            {
                sLOGGER.log(Level.WARN, "Unsupported option " + aCallback + "(" + aData.limit() + " bytes)");
            }
            else
            {
                sLOGGER.log(Level.WARN, "Unsupported option " + aCallback + "(" + aData.limit() + " bytes)");
            }
            break;
        }
    }

    protected void createSim(int aCanPort)
    {
        int simPort = aCanPort + BaseCanSmartSpeedController.sCAN_SC_OFFSET;
        if (!DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().contains(simPort))
        {
            sLOGGER.log(Level.WARN, "REV Motor Controller is being created dynamically instead of in the config file for port " + aCanPort);

            DataAccessorFactory.getInstance().getSpeedControllerAccessor().createSimulator(simPort, RevSpeedControllerSimWrapper.class.getName());
        }
        else if (!(SensorActuatorRegistry.get().getSpeedControllers().get(simPort) instanceof RevSpeedControllerSimWrapper))
        {
            sLOGGER.log(Level.FATAL, "A pre-registered motor controller of type " + SensorActuatorRegistry.get().getSpeedControllers().get(simPort).getClass()
                + " is the wrong type on port " + aCanPort);
            SensorActuatorRegistry.get().getSpeedControllers().remove(simPort);
            createSim(aCanPort);
            return;
        }
        SensorActuatorRegistry.get().getSpeedControllers().get(simPort).setInitialized(true);
    }

    public void reset()
    {

    }

    protected <WrapperType> WrapperType getMotorControllerWrapper(int aCanPort)
    {
        return (WrapperType) SensorActuatorRegistry.get().getSpeedControllers().get(aCanPort + 100);
    }
}
