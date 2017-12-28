package com.snobot.simulator.simulator_components.ctre;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.snobot.simulator.SensorActuatorRegistry;

public class CtreManager
{
    private static final Logger sLOGGER = Logger.getLogger(CtreManager.class);

    private final Map<Integer, CtrePigeonImuSim> mPigeonMap;

    public CtreManager()
    {
        mPigeonMap = new HashMap<>();
    }

    private CtreTalonSrxSpeedControllerSim getMotorControllerWrapper(int aCanPort)
    {
        return (CtreTalonSrxSpeedControllerSim) SensorActuatorRegistry.get().getSpeedControllers().get(aCanPort + 100);
    }

    private CtrePigeonImuSim getPigeonWrapper(int aCanPort)
    {
        return mPigeonMap.get(aCanPort);
    }

    public void handleMotorControllerMessage(String aCallback, int aCanPort, ByteBuffer aData)
    {
        aData.order(ByteOrder.LITTLE_ENDIAN);

        if ("Create".equals(aCallback))
        {
            CtreTalonSrxSpeedControllerSim output = new CtreTalonSrxSpeedControllerSim(aCanPort);
            SensorActuatorRegistry.get().register(output, aCanPort + 100);
        }
        else if ("SetDemand".equals(aCallback))
        {
            CtreTalonSrxSpeedControllerSim wrapper = getMotorControllerWrapper(aCanPort);

            int mode = aData.getInt();
            int param0 = aData.getInt();
            int param1 = aData.getInt();
            sLOGGER.log(Level.DEBUG, "Setting demand " + mode + ", " + param0 + ", " + param1);

            switch (mode)
            {
            case 0:
                wrapper.set(param0 / 1023.0);
                break;
            case 1:
                wrapper.setPositionGoal(param0);
                break;
            case 2:
                wrapper.setSpeedGoal(param0);
                break;
            case 5:
                int followerPort = param0 & 0xFF;
                CtreTalonSrxSpeedControllerSim leadTalon = getMotorControllerWrapper(followerPort);
                leadTalon.addFollower(wrapper);
                break;
            case 7:
                wrapper.setMotionMagicGoal(param0);
                break;
            default:
                sLOGGER.log(Level.ERROR, String.format("Unknown demand mode %d", mode));

            }

        }
        else if ("ConfigSelectedFeedbackSensor".equals(aCallback))
        {
            CtreTalonSrxSpeedControllerSim wrapper = getMotorControllerWrapper(aCanPort);

            int feedbackDevice = aData.getInt();
            wrapper.setCanFeedbackDevice((byte) feedbackDevice);

        }
        else if ("Config_kP".equals(aCallback))
        {
            CtreTalonSrxSpeedControllerSim wrapper = getMotorControllerWrapper(aCanPort);

            int slot = aData.getInt();
            float value = aData.getFloat();

            wrapper.setPGain(slot, value);
        }
        else if ("Config_kI".equals(aCallback))
        {
            CtreTalonSrxSpeedControllerSim wrapper = getMotorControllerWrapper(aCanPort);

            int slot = aData.getInt();
            float value = aData.getFloat();

            wrapper.setIGain(slot, value);
        }
        else if ("Config_kD".equals(aCallback))
        {
            CtreTalonSrxSpeedControllerSim wrapper = getMotorControllerWrapper(aCanPort);

            int slot = aData.getInt();
            float value = aData.getFloat();

            wrapper.setDGain(slot, value);
        }
        else if ("Config_kF".equals(aCallback))
        {
            CtreTalonSrxSpeedControllerSim wrapper = getMotorControllerWrapper(aCanPort);

            int slot = aData.getInt();
            float value = aData.getFloat();

            wrapper.setFGain(slot, value);
        }
        else if ("ConfigMotionCruiseVelocity".equals(aCallback))
        {
            CtreTalonSrxSpeedControllerSim wrapper = getMotorControllerWrapper(aCanPort);

            int sensorUnitsPer100ms = aData.getInt();

            wrapper.setMotionMagicMaxVelocity(sensorUnitsPer100ms);
        }
        else if ("ConfigMotionAcceleration".equals(aCallback))
        {
            CtreTalonSrxSpeedControllerSim wrapper = getMotorControllerWrapper(aCanPort);

            int sensorUnitsPer100msPerSec = aData.getInt();

            wrapper.setMotionMagicMaxAcceleration(sensorUnitsPer100msPerSec);
        }

        ////////////////////////
        //
        ////////////////////////
        else if ("GetMotorOutputPercent".equals(aCallback))
        {
            CtreTalonSrxSpeedControllerSim wrapper = getMotorControllerWrapper(aCanPort);

            float speed = (float) wrapper.get();
            aData.putFloat(0, speed);

        }
        else if ("GetSelectedSensorPosition".equals(aCallback))
        {
            CtreTalonSrxSpeedControllerSim wrapper = getMotorControllerWrapper(aCanPort);

            int speed = (int) Math.ceil(wrapper.getPosition());
            aData.putInt(0, speed);

        }
        else if ("GetSelectedSensorVelocity".equals(aCallback))
        {
            CtreTalonSrxSpeedControllerSim wrapper = getMotorControllerWrapper(aCanPort);

            int speed = (int) Math.ceil(wrapper.getVelocity());
            aData.putInt(0, speed);

        }
        else if ("GetClosedLoopError".equals(aCallback))
        {
            CtreTalonSrxSpeedControllerSim wrapper = getMotorControllerWrapper(aCanPort);

            int speed = (int) Math.ceil(wrapper.getLastClosedLoopError());
            aData.putInt(0, speed);

        }
        else
        {
            sLOGGER.log(Level.ERROR, "Unknown motor callback: " + aCallback);
        }
    }

    public void handlePigeonMessage(String aName, int aPort, ByteBuffer aData)
    {
        aData.order(ByteOrder.LITTLE_ENDIAN);

        if ("Create".equals(aName))
        {
            CtrePigeonImuSim sim = new CtrePigeonImuSim(400 + aPort * 3);
            mPigeonMap.put(aPort, sim);
        }

        //////////////////////////
        //
        //////////////////////////
        else if ("GetRawGyro".equals(aName))
        {
            CtrePigeonImuSim wrapper = getPigeonWrapper(aPort);

            aData.putDouble(wrapper.getYawWrapper().getAngle());
            aData.putDouble(wrapper.getPitchWrapper().getAngle());
            aData.putDouble(wrapper.getRollWrapper().getAngle());
        }
        else if ("GetFusedHeading".equals(aName))
        {
            CtrePigeonImuSim wrapper = getPigeonWrapper(aPort);

            aData.putDouble(wrapper.getYawWrapper().getAngle());
        }
        else
        {
            sLOGGER.log(Level.ERROR, "Unknown pigeon callback: " + aName);
        }
    }
}
