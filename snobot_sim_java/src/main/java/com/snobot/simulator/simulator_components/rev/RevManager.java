package com.snobot.simulator.simulator_components.rev;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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

    private final Map<String, Consumer<RevSpeedControllerSimWrapper>> mMotorControllerNormalCallbacks;
    private final Map<String, BiConsumer<RevSpeedControllerSimWrapper, Integer>> mMotorControllerSlottedCallbacks;


    public RevManager()
    {
        mMotorControllerNormalCallbacks = new HashMap<>();
        mMotorControllerNormalCallbacks.put("SetpointCommand", RevSpeedControllerSimWrapper::handleSetSetpointCommand);
        mMotorControllerNormalCallbacks.put("SetSensorType", RevSpeedControllerSimWrapper::handleSetSensorType);
        mMotorControllerNormalCallbacks.put("SetFeedbackDevice", RevSpeedControllerSimWrapper::handleSetFeedbackDevice);
        mMotorControllerNormalCallbacks.put("GetAppliedOutput", RevSpeedControllerSimWrapper::handleGetAppliedOutput);
        mMotorControllerNormalCallbacks.put("GetEncoderPosition", RevSpeedControllerSimWrapper::handleGetEncoderPosition);
        mMotorControllerNormalCallbacks.put("GetEncoderVelocity", RevSpeedControllerSimWrapper::handleGetEncoderVelocity);

        mMotorControllerSlottedCallbacks = new HashMap<>();
        mMotorControllerSlottedCallbacks.put("SetP", RevSpeedControllerSimWrapper::handleSetPGain);
        mMotorControllerSlottedCallbacks.put("SetI", RevSpeedControllerSimWrapper::handleSetIGain);
        mMotorControllerSlottedCallbacks.put("SetD", RevSpeedControllerSimWrapper::handleSetDGain);
        mMotorControllerSlottedCallbacks.put("SetFF", RevSpeedControllerSimWrapper::handleSetFFGain);
    }

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
        default:
            if (mMotorControllerNormalCallbacks.containsKey(aCallback))
            {
                RevSpeedControllerSimWrapper wrapper = getMotorControllerWrapper(aCanPort);
                mMotorControllerNormalCallbacks.get(aCallback).accept(wrapper);
            }
            else if (mMotorControllerSlottedCallbacks.containsKey(aCallback))
            {
                RevSpeedControllerSimWrapper wrapper = getMotorControllerWrapper(aCanPort);

                int slot = aData.getInt();
                mMotorControllerSlottedCallbacks.get(aCallback).accept(wrapper, slot);
            }
            else if (unsupportedFunctions.contains(aCallback))
            {
                sLOGGER.log(Level.DEBUG, "Unsupported option " + aCallback + "(" + aData.limit() + " bytes)");
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
