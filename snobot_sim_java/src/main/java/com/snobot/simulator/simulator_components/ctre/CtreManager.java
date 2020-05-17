package com.snobot.simulator.simulator_components.ctre;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import com.snobot.simulator.simulator_components.smart_sc.BaseCanSmartSpeedController;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.simulator_components.ctre.CtreTalonSrxSpeedControllerSim.MotionProfilePoint;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;

@SuppressWarnings({"PMD.NcssCount", "PMD.CyclomaticComplexity"})
public class CtreManager
{
    private static final Logger sLOGGER = LogManager.getLogger(CtreManager.class);

    private final Map<Integer, CtrePigeonImuSim> mPigeonMap;
    
    private final Map<String, Consumer<CtreTalonSrxSpeedControllerSim>> mNormalMoterControllerCallbacks;

    public CtreManager()
    {
        mPigeonMap = new HashMap<>();
        
        mNormalMoterControllerCallbacks = new HashMap<>();
        mNormalMoterControllerCallbacks.put("", (wrapper) -> wrapper.handleSetDemand());
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
        
        else if ("SetDemand".equals(aCallback))
        {
            CtreTalonSrxSpeedControllerSim wrapper = getMotorControllerWrapper(aCanPort);
            wrapper.handleSetDemand();
        }
        else if ("Set_4".equals(aCallback))
        {
            CtreTalonSrxSpeedControllerSim wrapper = getMotorControllerWrapper(aCanPort);
            wrapper.handleSet4();
        }
        else if ("ConfigSelectedFeedbackSensor".equals(aCallback))
        {
            CtreTalonSrxSpeedControllerSim wrapper = getMotorControllerWrapper(aCanPort);
            wrapper.handleConfigSelectedFeedbackSensor();
        }
        else if ("Config_kP".equals(aCallback))
        {
            CtreTalonSrxSpeedControllerSim wrapper = getMotorControllerWrapper(aCanPort);

            int slot = aData.getInt();
            wrapper.handleSetKP(slot);
        }
        else if ("Config_kI".equals(aCallback))
        {
            CtreTalonSrxSpeedControllerSim wrapper = getMotorControllerWrapper(aCanPort);

            int slot = aData.getInt();
            wrapper.handleSetKI(slot);
        }
        else if ("Config_kD".equals(aCallback))
        {
            CtreTalonSrxSpeedControllerSim wrapper = getMotorControllerWrapper(aCanPort);

            int slot = aData.getInt();
            wrapper.handleSetKD(slot);
        }
        else if ("Config_kF".equals(aCallback))
        {
            CtreTalonSrxSpeedControllerSim wrapper = getMotorControllerWrapper(aCanPort);

            int slot = aData.getInt();
            wrapper.handleSetKF(slot);
        }
        else if ("Config_IntegralZone".equals(aCallback))
        {
            CtreTalonSrxSpeedControllerSim wrapper = getMotorControllerWrapper(aCanPort);

            int slot = aData.getInt();
            wrapper.handleSetIntegralZone(slot);
        }
        else if ("ConfigMotionCruiseVelocity".equals(aCallback))
        {
            CtreTalonSrxSpeedControllerSim wrapper = getMotorControllerWrapper(aCanPort);
            wrapper.handleSetMotionCruiseVelocity();
        }
        else if ("ConfigMotionAcceleration".equals(aCallback))
        {
            CtreTalonSrxSpeedControllerSim wrapper = getMotorControllerWrapper(aCanPort);
            wrapper.handleSetMotionAcceleration();
        }
        else if ("PushMotionProfileTrajectory".equals(aCallback))
        {
            CtreTalonSrxSpeedControllerSim wrapper = getMotorControllerWrapper(aCanPort);
            wrapper.handlePushMotionProfileTrajectory();
        }
        else if ("PushMotionProfileTrajectory_2".equals(aCallback))
        {
            CtreTalonSrxSpeedControllerSim wrapper = getMotorControllerWrapper(aCanPort);
            wrapper.handlePushMotionProfileTrajectory_2();
        }
        else if ("SetSelectedSensorPosition".equals(aCallback))
        {
            CtreTalonSrxSpeedControllerSim wrapper = getMotorControllerWrapper(aCanPort);
            wrapper.handleSetSelectedSensorPosition();
        }
        else if ("SetInverted_2".equals(aCallback))
        {
            CtreTalonSrxSpeedControllerSim wrapper = getMotorControllerWrapper(aCanPort);
            wrapper.handleSetInverted_2();
        }
        else if ("ProcessMotionProfileBuffer".equals(aCallback))
        { // NOPMD
            // Nothing to do
        }

        ////////////////////////
        //
        ////////////////////////
        else if ("GetMotorOutputPercent".equals(aCallback))
        {
            CtreTalonSrxSpeedControllerSim wrapper = getMotorControllerWrapper(aCanPort);
            wrapper.handleGetMotorOutputPercent();

        }
        else if ("GetSelectedSensorPosition".equals(aCallback))
        {
            CtreTalonSrxSpeedControllerSim wrapper = getMotorControllerWrapper(aCanPort);
            wrapper.handleGetSelectedSensorPosition();

        }
        else if ("GetSelectedSensorVelocity".equals(aCallback))
        {
            CtreTalonSrxSpeedControllerSim wrapper = getMotorControllerWrapper(aCanPort);
            wrapper.handleGetSelectedSensorVelocity();

        }
        else if ("GetClosedLoopError".equals(aCallback))
        {
            CtreTalonSrxSpeedControllerSim wrapper = getMotorControllerWrapper(aCanPort);
            wrapper.handleGetClosedLoopError();

        }
        else if ("GetMotionProfileStatus".equals(aCallback))
        {
            CtreTalonSrxSpeedControllerSim wrapper = getMotorControllerWrapper(aCanPort);
            wrapper.handleGetMotionProfileStatus();
        }
        else if ("GetActiveTrajectoryPosition".equals(aCallback))
        {
            CtreTalonSrxSpeedControllerSim wrapper = getMotorControllerWrapper(aCanPort);
            wrapper.handleGetActiveTrajectoryPosition();
        }
        else if ("GetActiveTrajectoryVelocity".equals(aCallback))
        {
            CtreTalonSrxSpeedControllerSim wrapper = getMotorControllerWrapper(aCanPort);
            wrapper.handleGetActiveTrajectoryVelocity();
        }
        else if ("GetQuadraturePosition".equals(aCallback))
        {
            CtreTalonSrxSpeedControllerSim wrapper = getMotorControllerWrapper(aCanPort);
            wrapper.handleGetQuadraturePosition();
        }
        else if ("GetQuadratureVelocity".equals(aCallback))
        {
            CtreTalonSrxSpeedControllerSim wrapper = getMotorControllerWrapper(aCanPort);
            wrapper.handleGetQuadratureVelocity();
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
        else if ("SetYaw".equals(aName))
        {
            CtrePigeonImuSim wrapper = getPigeonWrapper(aPort);
            wrapper.handleSetYaw();
        }

        //////////////////////////
        //
        //////////////////////////
        else if ("GetRawGyro".equals(aName))
        {
            CtrePigeonImuSim wrapper = getPigeonWrapper(aPort);
            wrapper.handleGetRawGyro();
        }
        else if ("GetYawPitchRoll".equals(aName)) //NOPMD.AvoidLiteralsInIfCondition
        {
            CtrePigeonImuSim wrapper = getPigeonWrapper(aPort);
            wrapper.handleGetYawPitchRoll();
        }
        else if ("GetFusedHeading".equals(aName) || "GetFusedHeading1".equals(aName)) //NOPMD.AvoidLiteralsInIfCondition
        {
            CtrePigeonImuSim wrapper = getPigeonWrapper(aPort);
            wrapper.handleGetFusedHeading();
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
