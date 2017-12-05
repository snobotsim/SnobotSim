package com.snobot.simulator.simulator_components.ctre;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Collection;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.module_wrapper.PwmWrapper;

public class TalonSrxDeviceManager implements ICanDeviceManager
{
    private static final Logger sLOGGER = Logger.getLogger(TalonSrxDeviceManager.class);
    private static final int sCAN_OFFSET = 100;

    private ByteBuffer mSetParamBuffer = ByteBuffer.allocateDirect(40);

    public TalonSrxDeviceManager()
    {
        mSetParamBuffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    public static Collection<Integer> getSupportedMessageIds()
    {
        return Arrays.asList(
                // Stream
                0,

                // Send
                0x02041400, 0x02041440, 0x02041480, 0x020414C0,

                // Recv
                0x02040000, 0x02041880, 0x02041800,

                0x02040140, 0x02040080,

                0x020415C0);
    }

    @Override
    public void handleSend(int aCanMessageId, int aCanPort, ByteBuffer aData, int aDataSize)
    {
        String bufferPrintout;

        if (aDataSize >= 8)
        {
            bufferPrintout = "(0x" + String.format("%016X", aData.getLong(0)) + ")";
        }
        else
        {
            bufferPrintout = "(too small)";
        }

        sLOGGER.log(Level.DEBUG,
                "@SendingMessage: MID: " + Integer.toHexString(aCanMessageId) + ", size: " + aDataSize + ", buffer: " + bufferPrintout);

        if (aCanMessageId == 0x02040000)
        {
            handleTx1(aData, aCanPort);
        }
        else if (aCanMessageId == 0x02041880)
        {
            handleSetParamCommand(aData, aCanPort);
        }
        else if (aCanMessageId == 0x02041800)
        {
            handleParamRequest(aData, aCanPort);
        }
        else
        {
            sLOGGER.log(Level.ERROR, "TX Request " + aCanMessageId + " is not supported.");
        }
    }

    @Override
    public int handleReceive(int aCanMessageId, int aCanPort, ByteBuffer aData)
    {
        boolean success = true;

        sLOGGER.log(Level.DEBUG, "@ReceiveMessage: MID: " + Integer.toHexString(aCanMessageId));

        if (aCanMessageId == 0x02041400)
        {
            populateStatus1(aCanPort, aData);
        }
        else if (aCanMessageId == 0x02041440)
        {
            populateStatus2(aCanPort, aData);
        }
        else if (aCanMessageId == 0x02041480)
        {
            populateStatus3(aCanPort, aData);
        }
        else if (aCanMessageId == 0x020414C0)
        {
            populateStatus4(aCanPort, aData);
        }
        else
        {
            success = false;
            sLOGGER.log(Level.ERROR, "Status request " + aCanMessageId + " is not supported.");
        }

        return success ? 8 : 0;
    }

    @Override
    public void readStreamSession(ByteBuffer[] messages, int messagesToRead)
    {
        ByteBuffer buffer = messages[0];
        buffer.rewind();
        mSetParamBuffer.rewind();
        for (int i = 0; i < 17; ++i)
        {
            buffer.put(mSetParamBuffer.get(i));
        }
    }

    private void handleTx1(ByteBuffer aBuffer, int aPort)
    {
        CanTalonSpeedControllerSim wrapper = getWrapperHelper(aPort);

        byte commandSection = (byte) (aBuffer.get(5));
        byte profileSelect = (byte) (commandSection & 0x01);
        byte feedbackDevice = (byte) (commandSection & 0x0E);
        byte overrideLimitSwitches = (byte) (commandSection & 0xE0);

        byte commandType = (byte) ((aBuffer.get(6) >> 4) & 0xF);
        int demand = (aBuffer.getInt(2)) >> 8;
        sLOGGER.log(Level.DEBUG, String.format("handleTx1: Demand: %d, Command: %d, Profile: %d, Feedback Device: %d, Limit Switches: %d", demand,
                commandType, profileSelect, feedbackDevice, overrideLimitSwitches));

        if (commandType == 0x00)
        {
            double appliedVoltageDemand = demand / 1023.0;
            wrapper.set(appliedVoltageDemand);
            sLOGGER.log(Level.DEBUG, " Setting by applied throttle.. " + appliedVoltageDemand);
        }
        else if (commandType == (byte) 0x01)
        {
            double position = demand / 4096.0;
            wrapper.setPositionGoal(position);
            sLOGGER.log(Level.DEBUG, "  Setting by position." + position);
        }
        else if (commandType == (byte) 0x02)
        {
            double speed = demand * 600.0 / 4096.0;
            wrapper.setSpeedGoal(speed);
            sLOGGER.log(Level.DEBUG, " Setting by speed. " + speed);
        }
        else if (commandType == (byte) 0x03)
        {
            sLOGGER.log(Level.DEBUG, "  Setting by current." + demand);
        }
        else if (commandType == (byte) 0x04)
        {
            double voltageDemand = demand / 256.0;
            wrapper.set(voltageDemand / 12.0);
            sLOGGER.log(Level.DEBUG, "  Setting by voltage. " + voltageDemand);
        }
        else if (commandType == (byte) 0x05)
        {
            sLOGGER.log(Level.DEBUG, "  Setting by FOLLOWER.");
        }
        else if (commandType == (byte) 0x06)
        {
            sLOGGER.log(Level.DEBUG, "  Setting by Motion Profile.");
        }
        else if (commandType == (byte) 0x07)
        {
            sLOGGER.log(Level.DEBUG, "  Setting by Motion Magic.");
        }
        else if (commandType == (byte) 0x0F)
        {
            // Nothing to do, but don't print error
        }
        else
        {
            sLOGGER.log(Level.ERROR, String.format("Unknown set command type 0x%02X", commandType));
        }
    }

    private void handleSetParamCommand(ByteBuffer aBuffer, int aPort)
    {
        CanTalonSpeedControllerSim wrapper = getWrapperHelper(aPort);
        aBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byte commandType = aBuffer.get(0);

        int rawValue = aBuffer.getInt(1);
        double floatValue = rawValue * 0.0000002384185791015625;

        // P gain
        if (commandType == 1)
        {
            wrapper.setPGain(floatValue);
        }
        // I gain
        else if (commandType == 2)
        {
            wrapper.setIGain(floatValue);
        }
        // D gain
        else if (commandType == 3)
        {
            wrapper.setDGain(floatValue);
        }
        // F gain
        else if (commandType == 4)
        {
            wrapper.setFGain(floatValue);
        }
        // I Zone
        else if (commandType == 5)
        {
            wrapper.setIZone(rawValue);
        }

        else if (commandType == 6)
        {
            sLOGGER.log(Level.INFO, "setCloseLoopRampRate is not supported");
        }
        else if (commandType == 6)
        {
            sLOGGER.log(Level.INFO, "setForwardSoftLimit is not supported");
        }
        else if (commandType == 21)
        {
            sLOGGER.log(Level.INFO, "setForwardSoftLimit is not supported");
        }
        else if (commandType == 22)
        {
            sLOGGER.log(Level.INFO, "setReverseSoftLimit is not supported");
        }
        else if (commandType == 23)
        {
            sLOGGER.log(Level.INFO, "enableForwardSoftLimit is not supported");
        }
        else if (commandType == 32)
        {
            sLOGGER.log(Level.INFO, "ConfigFwdLimitSwitchNormallyOpen is not supported");
        }
        else if (commandType == 33)
        {
            sLOGGER.log(Level.INFO, "ConfigRevLimitSwitchNormallyOpen is not supported");
        }
        else if (commandType == 44)
        {
            sLOGGER.log(Level.INFO, "setForwardSoftLimit is not supported");
        }
        else if (commandType == 73)
        {
            sLOGGER.log(Level.INFO, "setPosition is not supported");
        }
        else if (commandType == 77)
        {
            wrapper.reset(rawValue, wrapper.getVelocity(), wrapper.getCurrent());
        }
        else if (commandType == 93)
        {
            sLOGGER.log(Level.INFO, "clearIAccum is not supported");
        }
        else if (commandType == 105 || commandType == 107)
        {
            sLOGGER.log(Level.INFO, "configNominalOutputVoltage is not supported, but it probably shouldn't matter ");
        }
        else if (commandType == 104 || commandType == 106)
        {
            sLOGGER.log(Level.INFO, "configPeakOutputVoltage is not supported, but it probably shouldn't matter ");
        }
        else if (commandType == 108 || commandType == 100)
        {
            sLOGGER.log(Level.INFO, "enableZeroSensorPositionOnIndex is not supported, but it probably shouldn't matter ");
        }
        else if (commandType == 111)
        {
            sLOGGER.log(Level.INFO, "setAllowableClosedLoopErr is not supported");
        }
        else if (commandType == 112)
        {
            sLOGGER.log(Level.INFO, "configPotentiometerTurns is not supported");
        }
        else if (commandType == 113)
        {
            sLOGGER.log(Level.INFO, "configEncoderCodesPerRev is not supported");
        }
        else if (commandType == 114)
        {
            sLOGGER.log(Level.INFO, "setPulseWidthPosition is not supported");
        }
        else if (commandType == 115)
        {
            sLOGGER.log(Level.INFO, "setAnalogPosition is not supported");
        }
        else if (commandType == 119)
        {
            sLOGGER.log(Level.INFO, "clearMotionProfileHasUnderrun is not supported");
        }

        else
        {
            sLOGGER.log(Level.ERROR, "Unknown SetParam command: " + commandType);
        }
    }

    private void handleParamRequest(ByteBuffer aBuffer, int aPort)
    {
        sLOGGER.log(Level.DEBUG, "Getting parameters...");

        CanTalonSpeedControllerSim wrapper = getWrapperHelper(aPort);
        aBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byte commandType = aBuffer.get(0);

        double floatValue = 0;
        boolean isFloat = true;

        // P gain
        if (commandType == 1)
        {
            floatValue = wrapper.getPidConstants().mP;
        }
        // I gain
        else if (commandType == 2)
        {
            floatValue = wrapper.getPidConstants().mI;
        }
        // D gain
        else if (commandType == 3)
        {
            floatValue = wrapper.getPidConstants().mD;
        }
        // F gain
        else if (commandType == 4)
        {
            floatValue = wrapper.getPidConstants().mF;
        }
        // I Zone
        else if (commandType == 5)
        {
            floatValue = wrapper.getPidConstants().mIZone;
            isFloat = false;
        }
        else
        {
            sLOGGER.log(Level.ERROR, "Unknown GetParam command: " + commandType);
        }
        int rawValue;
        if(isFloat)
        {
            rawValue = (int) (floatValue / 0.0000002384185791015625);
        }
        else
        {
            rawValue = (int) floatValue;
        }
        
        int messageId = 0x2041840;
        messageId |= aPort;

        mSetParamBuffer.rewind();
        mSetParamBuffer.putInt(messageId);
        mSetParamBuffer.putInt(0xDEADBEEF); // Timestamp
        mSetParamBuffer.put(commandType);
        mSetParamBuffer.putInt(rawValue);
    }

    private void populateStatus1(int aPort, ByteBuffer aData)
    {
        CanTalonSpeedControllerSim wrapper = getWrapperHelper(aPort);
        sLOGGER.log(Level.DEBUG, " Getting STATUS1 " + wrapper.get());

        aData.putShort(3, (short) (wrapper.get() * 1023));
    }

    private void populateStatus2(int aPort, ByteBuffer aData)
    {
        CanTalonSpeedControllerSim wrapper = getWrapperHelper(aPort);
        int binnedPosition = (int) (wrapper.getPosition() * 4096);
        int binnedVelocity = (int) (wrapper.getVelocity() * 6.9);

        putNumber(aData, binnedPosition, 3);
        aData.putShort((short) binnedVelocity);
    }

    private void populateStatus3(int aPort, ByteBuffer aData)
    {
        CanTalonSpeedControllerSim wrapper = getWrapperHelper(aPort);
        int binnedPosition = (int) wrapper.getPosition();
        int binnedVelocity = (int) wrapper.getVelocity();

        putNumber(aData, binnedPosition, 3);
        aData.putShort((short) binnedVelocity);
    }

    private void populateStatus4(int aPort, ByteBuffer aData)
    {
        sLOGGER.log(Level.DEBUG, "POPULATE STATUS 4");
        double temperature = 30;
        double batteryVoltage = 12;
        CanTalonSpeedControllerSim wrapper = getWrapperHelper(aPort);
        int binnedPosition = (int) wrapper.getPosition();
        int binnedVelocity = (int) wrapper.getVelocity();

        putNumber(aData, binnedPosition, 3);
        aData.putShort((short) binnedVelocity);
        aData.put((byte) ((temperature + 50) / 0.6451612903));
        aData.put((byte) ((batteryVoltage - 4) / 0.05));
    }

    private void putNumber(ByteBuffer aOutput, int aNumber, int aBytes)
    {
        for (int i = aBytes; i > 0; --i)
        {
            int shift = 8 * (i - 1);
            int mask = 0xFF << shift;
            aOutput.put((byte) ((aNumber & mask) >> shift));
        }
    }

    private CanTalonSpeedControllerSim getWrapperHelper(int aPort)
    {
        PwmWrapper rawWrapper = SensorActuatorRegistry.get().getSpeedControllers().get(aPort + sCAN_OFFSET);

        if (rawWrapper == null)
        {
            CanTalonSpeedControllerSim output = new CanTalonSpeedControllerSim(aPort);
            SensorActuatorRegistry.get().register(output, aPort + sCAN_OFFSET);
            sLOGGER.log(Level.DEBUG, "Creating " + aPort);
            return output;
        }
        
        return (CanTalonSpeedControllerSim) rawWrapper;
    }
}
