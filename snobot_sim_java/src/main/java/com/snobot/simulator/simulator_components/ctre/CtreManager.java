package com.snobot.simulator.simulator_components.ctre;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.snobot.simulator.simulator_components.smart_sc.BaseCanSmartSpeedController;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;

@SuppressWarnings({"PMD.NcssCount", "PMD.CyclomaticComplexity"})
public class CtreManager
{
    private static final Logger sLOGGER = LogManager.getLogger(CtreManager.class);

    private final Map<Integer, CtrePigeonImuSim> mPigeonMap;

    private final Map<String, Consumer<CtreTalonSrxSpeedControllerSim>> mMotorControllerNormalCallbacks;
    private final Map<String, BiConsumer<CtreTalonSrxSpeedControllerSim, Integer>> mMotorControllerSlottedCallbacks;

    private final Map<String, Consumer<CtrePigeonImuSim>> mPigeonNormalCallbacks;

    public CtreManager()
    {
        mPigeonMap = new HashMap<>();
        
        mMotorControllerNormalCallbacks = new HashMap<>();
        mMotorControllerNormalCallbacks.put("SetDemand", CtreTalonSrxSpeedControllerSim::handleSetDemand);
        mMotorControllerNormalCallbacks.put("Set_4", CtreTalonSrxSpeedControllerSim::handleSet4);
        mMotorControllerNormalCallbacks.put("ConfigSelectedFeedbackSensor", CtreTalonSrxSpeedControllerSim::handleConfigSelectedFeedbackSensor);
        mMotorControllerNormalCallbacks.put("ConfigMotionCruiseVelocity", CtreTalonSrxSpeedControllerSim::handleSetMotionCruiseVelocity);
        mMotorControllerNormalCallbacks.put("ConfigMotionAcceleration", CtreTalonSrxSpeedControllerSim::handleSetMotionAcceleration);
        mMotorControllerNormalCallbacks.put("PushMotionProfileTrajectory", CtreTalonSrxSpeedControllerSim::handlePushMotionProfileTrajectory);
        mMotorControllerNormalCallbacks.put("PushMotionProfileTrajectory_2", CtreTalonSrxSpeedControllerSim::handlePushMotionProfileTrajectory_2);
        mMotorControllerNormalCallbacks.put("SetSelectedSensorPosition", CtreTalonSrxSpeedControllerSim::handleSetSelectedSensorPosition);
        mMotorControllerNormalCallbacks.put("SetInverted_2", CtreTalonSrxSpeedControllerSim::handleSetInverted_2);

        mMotorControllerNormalCallbacks.put("GetMotorOutputPercent", CtreTalonSrxSpeedControllerSim::handleGetMotorOutputPercent);
        mMotorControllerNormalCallbacks.put("GetSelectedSensorPosition", CtreTalonSrxSpeedControllerSim::handleGetSelectedSensorPosition);
        mMotorControllerNormalCallbacks.put("GetSelectedSensorVelocity", CtreTalonSrxSpeedControllerSim::handleGetSelectedSensorVelocity);
        mMotorControllerNormalCallbacks.put("GetClosedLoopError", CtreTalonSrxSpeedControllerSim::handleGetClosedLoopError);
        mMotorControllerNormalCallbacks.put("GetMotionProfileStatus", CtreTalonSrxSpeedControllerSim::handleGetMotionProfileStatus);
        mMotorControllerNormalCallbacks.put("GetActiveTrajectoryPosition", CtreTalonSrxSpeedControllerSim::handleGetActiveTrajectoryPosition);
        mMotorControllerNormalCallbacks.put("GetActiveTrajectoryVelocity", CtreTalonSrxSpeedControllerSim::handleGetActiveTrajectoryVelocity);
        mMotorControllerNormalCallbacks.put("GetQuadraturePosition", CtreTalonSrxSpeedControllerSim::handleGetQuadraturePosition);
        mMotorControllerNormalCallbacks.put("GetQuadratureVelocity", CtreTalonSrxSpeedControllerSim::handleGetQuadratureVelocity);

        mMotorControllerSlottedCallbacks = new HashMap<>();
        mMotorControllerSlottedCallbacks.put("Config_kP", CtreTalonSrxSpeedControllerSim::handleSetKP);
        mMotorControllerSlottedCallbacks.put("Config_kI", CtreTalonSrxSpeedControllerSim::handleSetKI);
        mMotorControllerSlottedCallbacks.put("Config_kD", CtreTalonSrxSpeedControllerSim::handleSetKD);
        mMotorControllerSlottedCallbacks.put("Config_kF", CtreTalonSrxSpeedControllerSim::handleSetKF);
        mMotorControllerSlottedCallbacks.put("Config_IntegralZone", CtreTalonSrxSpeedControllerSim::handleSetIntegralZone);

        mPigeonNormalCallbacks = new HashMap<>();
        mPigeonNormalCallbacks.put("SetYaw", CtrePigeonImuSim::handleSetYaw);
        mPigeonNormalCallbacks.put("GetRawGyro", CtrePigeonImuSim::handleGetRawGyro);
        mPigeonNormalCallbacks.put("GetYawPitchRoll", CtrePigeonImuSim::handleGetYawPitchRoll);
        mPigeonNormalCallbacks.put("GetFusedHeading", CtrePigeonImuSim::handleGetFusedHeading);
        mPigeonNormalCallbacks.put("GetFusedHeading1", CtrePigeonImuSim::handleGetFusedHeading);
    }

    public void reset()
    {
        mPigeonMap.clear();
    }

    private CtreTalonSrxSpeedControllerSim getMotorControllerWrapper(int aCanPort)
    {
        return CtreTalonSrxSpeedControllerSim.getMotorControllerWrapper(aCanPort);
    }

    private CtrePigeonImuSim getPigeonWrapper(int aCanPort)
    {
        return mPigeonMap.get(aCanPort);
    }

    @SuppressWarnings("PMD.ExcessiveMethodLength")
    public void handleMotorControllerMessage(String aCallback, int aCanPort, ByteBuffer aData)
    {
        aData.order(ByteOrder.LITTLE_ENDIAN);

        sLOGGER.log(Level.INFO, "Handling motor controller message " + aCallback + ", " + aCanPort);

        Set<String> unsupportedFunctions = new HashSet<>();

        if ("Create".equals(aCallback))
        {
            createMotorController(aCanPort);
        }
        else if (mMotorControllerNormalCallbacks.containsKey(aCallback))
        {
            CtreTalonSrxSpeedControllerSim wrapper = getMotorControllerWrapper(aCanPort);
            mMotorControllerNormalCallbacks.get(aCallback).accept(wrapper);
        }
        else if (mMotorControllerSlottedCallbacks.containsKey(aCallback))
        {
            CtreTalonSrxSpeedControllerSim wrapper = getMotorControllerWrapper(aCanPort);

            int slot = aData.getInt();
            mMotorControllerSlottedCallbacks.get(aCallback).accept(wrapper, slot);
        }
        ///////////////////////////////////
        // Unsupported, but not important
        ///////////////////////////////////
        else if (unsupportedFunctions.contains(aCallback))
        {
            sLOGGER.log(Level.DEBUG, aCallback + " not supported");
        }
        else
        {
            sLOGGER.log(Level.ERROR, "Unknown motor callback: " + aCallback);
        }
    }

    public CtrePigeonImuSim createPigeon(int aPort)
    {
        if (mPigeonMap.containsKey(aPort))
        {
            sLOGGER.log(Level.WARN, "Pigeon already registerd on " + aPort);
            return null;
        }
        else
        {
            CtrePigeonImuSim sim = new CtrePigeonImuSim(aPort, CtrePigeonImuSim.sCTRE_OFFSET + aPort * 3);
            mPigeonMap.put(aPort, sim);
            return sim;
        }

    }

    public void createMotorController(int aCanPort)
    {
        int simPort = aCanPort + BaseCanSmartSpeedController.sCAN_SC_OFFSET;
        if (!DataAccessorFactory.getInstance().getSpeedControllerAccessor().getPortList().contains(simPort))
        {
            sLOGGER.log(Level.WARN, "CTRE Motor Controller is being created dynamically instead of in the config file for port " + aCanPort);

            DataAccessorFactory.getInstance().getSpeedControllerAccessor().createSimulator(simPort, CtreTalonSrxSpeedControllerSim.class.getName());
        }
        else if (!(SensorActuatorRegistry.get().getSpeedControllers().get(simPort) instanceof CtreTalonSrxSpeedControllerSim))
        {
            sLOGGER.log(Level.FATAL, "A pre-registered motor controller of type " + SensorActuatorRegistry.get().getSpeedControllers().get(simPort).getClass()
                + " is the wrong type on port " + aCanPort);
            SensorActuatorRegistry.get().getSpeedControllers().remove(simPort);
            createMotorController(aCanPort);
            return;
        }
        SensorActuatorRegistry.get().getSpeedControllers().get(aCanPort + CtreTalonSrxSpeedControllerSim.sCAN_SC_OFFSET).setInitialized(true);
    }

    public void handlePigeonMessage(String aName, int aPort, ByteBuffer aData)
    {
        aData.order(ByteOrder.LITTLE_ENDIAN);

        sLOGGER.log(Level.TRACE, "Handling pigeon message " + aName + ", " + aPort);

        if ("Create".equals(aName))
        {
            CtrePigeonImuSim pigeon = mPigeonMap.get(aPort);
            if (pigeon == null)
            {
                sLOGGER.log(Level.WARN, "CTRE Pigeon is being created dynamically instead of in the config file for port " + aPort);
                pigeon = createPigeon(aPort);
            }

            pigeon.setInitialized(true);
        }
        else if (mPigeonNormalCallbacks.containsKey(aName))
        {
            CtrePigeonImuSim wrapper = getPigeonWrapper(aPort);
            mPigeonNormalCallbacks.get(aName).accept(wrapper);
        }
        else
        {
            sLOGGER.log(Level.ERROR, "Unsupported option " + aName + "(" + aData.limit() + " bytes)");
        }
    }

    public void handleCanifierMessage(String aName, int aDeviceId, ByteBuffer aBuffer)
    {
        sLOGGER.log(Level.ERROR, "Unknown canifier callback: " + aName);
    }

    public void handleBuffTrajPointStreamMessage(String aName, int aDeviceId, ByteBuffer aBuffer)
    {
        sLOGGER.log(Level.ERROR, "Unknown buff traj callback: " + aName);
    }
}
