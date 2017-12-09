package com.snobot.simulator.simulator_components.ctre;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Collection;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.module_wrapper.PwmWrapper;
import com.snobot.simulator.simulator_components.ctre.CtreTalonSrxSpeedControllerSim.MotionProfilePoint;

public class CtreTalonSrxDeviceManager implements ICanDeviceManager
{
    private static final Logger sLOGGER = Logger.getLogger(CtreTalonSrxDeviceManager.class);
    private static final int sCAN_OFFSET = 100;

    private ByteBuffer mSetParamBuffer = ByteBuffer.allocateDirect(40);

    public CtreTalonSrxDeviceManager()
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

                0x020415C0, 0x02041600);
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
        else if (aCanMessageId == 0x02040080)
        {
            handleTx3(aData, aCanPort);
        }
        else if (aCanMessageId == 0x02040140)
        {
            handleTx6(aData, aCanPort);
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
            sLOGGER.log(Level.ERROR, "TX Request " + String.format("0x%08X", aCanMessageId) + " is not supported.");
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
        else if (aCanMessageId == 0x020415C0)
        {
            sLOGGER.log(Level.ERROR, "Status #6 is not supported.");
        }
        else if (aCanMessageId == 0x02041600)
        {
            populateStatus9(aCanPort, aData);
        }
        else
        {
            success = false;
            sLOGGER.log(Level.ERROR, "Status request " + String.format("0x%08X", aCanMessageId) + " is not supported.");
        }

        return success ? 8 : 0;
    }

    @Override
    public int readStreamSession(ByteBuffer[] messages, int messagesToRead)
    {
        sLOGGER.log(Level.DEBUG, "Reading stream session " + messagesToRead);
        ByteBuffer buffer = messages[0];
        buffer.rewind();
        mSetParamBuffer.rewind();
        for (int i = 0; i < 17; ++i)
        {
            buffer.put(mSetParamBuffer.get(i));
        }

        int messageId = mSetParamBuffer.getInt(0);
        int timestamp = 0xDEADBEEF;

        ByteBuffer forwardSoftLimit = messages[1];
        forwardSoftLimit.order(ByteOrder.LITTLE_ENDIAN);
        forwardSoftLimit.putInt(messageId);
        forwardSoftLimit.putInt(timestamp);
        forwardSoftLimit.put((byte) 21);

        ByteBuffer reverseSoftLimit = messages[2];
        reverseSoftLimit.order(ByteOrder.LITTLE_ENDIAN);
        reverseSoftLimit.putInt(messageId);
        reverseSoftLimit.putInt(timestamp);
        reverseSoftLimit.put((byte) 22);

        ByteBuffer forwardLimitSwitchEnabled = messages[3];
        forwardLimitSwitchEnabled.order(ByteOrder.LITTLE_ENDIAN);
        forwardLimitSwitchEnabled.putInt(messageId);
        forwardLimitSwitchEnabled.putInt(timestamp);
        forwardLimitSwitchEnabled.put((byte) 23);

        ByteBuffer reverseLimitSwitchEnabled = messages[4];
        reverseLimitSwitchEnabled.order(ByteOrder.LITTLE_ENDIAN);
        reverseLimitSwitchEnabled.putInt(messageId);
        reverseLimitSwitchEnabled.putInt(timestamp);
        reverseLimitSwitchEnabled.put((byte) 24);

        return 5;
    }

    private void handleTx1(ByteBuffer aBuffer, int aPort)
    {
        CtreTalonSrxSpeedControllerSim wrapper = getWrapperHelper(aPort);

        byte commandSection = (byte) (aBuffer.get(5));
        byte profileSelect = (byte) (commandSection & 0x01);
        byte feedbackDevice = (byte) (commandSection & 0x0E);
        byte overrideLimitSwitchesRaw = (byte) ((commandSection & 0xE0) >> 5);

        if (overrideLimitSwitchesRaw != 0x1)
        {
            boolean overrideFwdLimitSwitch = (overrideLimitSwitchesRaw & 0x2) == 0x2;
            boolean overrideRevLimitSwitch = (overrideLimitSwitchesRaw & 0x1) == 0x1;

            wrapper.setLimitSwitchOverride(overrideFwdLimitSwitch, overrideRevLimitSwitch);
        }

        byte commandType = (byte) ((aBuffer.get(6) >> 4) & 0xF);
        int demand = (aBuffer.getInt(2)) >> 8;
        sLOGGER.log(Level.DEBUG, String.format("handleTx1: Demand: %d, Command: %d, Profile: %d, Feedback Device: %d, Limit Switches: %02X",
                demand, commandType, profileSelect, feedbackDevice, overrideLimitSwitchesRaw));

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
            sLOGGER.log(Level.WARN, "  Setting by current (" + demand + ") is not supported in the simulator");
        }
        else if (commandType == (byte) 0x04)
        {
            double voltageDemand = demand / 256.0;
            wrapper.set(voltageDemand / 12.0);
            sLOGGER.log(Level.DEBUG, "  Setting by voltage. " + voltageDemand);
        }
        else if (commandType == (byte) 0x05)
        {
            CtreTalonSrxSpeedControllerSim leadTalon = getWrapperHelper(demand);
            leadTalon.addFollower(wrapper);
            sLOGGER.log(Level.DEBUG, "  Setting by FOLLOWER." + demand);
        }
        else if (commandType == (byte) 0x06)
        {
            wrapper.setMotionProfilingCommand(demand);
            sLOGGER.log(Level.DEBUG, "  Setting by Motion Profile " + demand);
        }
        else if (commandType == (byte) 0x07)
        {
            wrapper.setMotionMagicGoal(demand);
            sLOGGER.log(Level.DEBUG, "  Setting by Motion Magic (" + demand + ")");
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

    private void handleTx3(ByteBuffer aData, int aCanPort)
    {
        sLOGGER.log(Level.ERROR, "TX #3 is not supported.");
    }

    private void handleTx6(ByteBuffer aData, int aCanPort)
    {
        boolean isZeroPosition = (aData.get(0) & 0x40) == 0x40;
        boolean isLastPosition = (aData.get(0) & 0x08) == 0x08;
        boolean velOnly = (aData.get(0) & 0x04) == 0x04;
        int huffCode = (aData.get(0) & 0x30) >> 4;
        int position = (aData.getInt(4)) & 0xFFFFFF;
        int velocity = ((aData.getInt(3)) & 0xFFFF00) >> 16;
        int index = aData.get(1);

        CtreTalonSrxSpeedControllerSim wrapper = getWrapperHelper(aCanPort);
        wrapper.addMotionProfilePoint(new CtreTalonSrxSpeedControllerSim.MotionProfilePoint(index, position, velocity));

        sLOGGER.log(Level.DEBUG, String.format("handleTx6: " + 
                "index: " + index + ", " + 
                "isZeroPosition: " + isZeroPosition + ", " + 
                "isLastPosition: " + isLastPosition + ", " + 
                "velOnly: " + velOnly + ", " + 
                "HuffCode: " + huffCode + ", " + 
                "Position: " + position + ", " + 
                "Velocity: " + velocity));
    }

    private void handleSetParamCommand(ByteBuffer aBuffer, int aPort)
    {
        CtreTalonSrxSpeedControllerSim wrapper = getWrapperHelper(aPort);
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
            double position = rawValue / 256;
            wrapper.reset(position, wrapper.getVelocity(), wrapper.getCurrent());
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
        else if (commandType == 122)
        {
            wrapper.setMotionMagicMaxAcceleration(rawValue);
        }
        else if (commandType == 123)
        {
            wrapper.setMotionMagicMaxVelocity(rawValue);
        }

        else
        {
            sLOGGER.log(Level.ERROR, "Unknown SetParam command: " + commandType);
        }
    }

    private void handleParamRequest(ByteBuffer aBuffer, int aPort)
    {
        sLOGGER.log(Level.DEBUG, "Getting parameters...");

        CtreTalonSrxSpeedControllerSim wrapper = getWrapperHelper(aPort);
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
        // getCloseLoopRampRate
        else if (commandType == 6)
        {
            sLOGGER.log(Level.INFO, "getCloseLoopRampRate is not supported");
        }
        // GetIaccum
        else if (commandType == 93)
        {
            sLOGGER.log(Level.INFO, "GetIaccum is not supported");
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
        CtreTalonSrxSpeedControllerSim wrapper = getWrapperHelper(aPort);
        sLOGGER.log(Level.DEBUG, " Getting STATUS1 " + wrapper.get());

        int closedLoopErrorRaw = (int) wrapper.getLastClosedLoopError();

        aData.putShort(3, (short) (wrapper.get() * 1023));

        // byte pos0 = (byte) ((closedLoopErrorRaw & 0x0000FF) >> 0);
        // byte pos1 = (byte) ((closedLoopErrorRaw & 0x00FF00) >> 8);
        // byte pos2 = (byte) ((closedLoopErrorRaw & 0xFF0000) >> 16);
        // aData.put((byte) 0);
        // aData.put(pos2);
        // aData.put(pos1);
        // aData.put(pos0);

        putNumber(aData, closedLoopErrorRaw, 3);
    }

    private void populateStatus2(int aPort, ByteBuffer aData)
    {
        CtreTalonSrxSpeedControllerSim wrapper = getWrapperHelper(aPort);
        int binnedPosition = (int) (wrapper.getPosition() * 4096);
        int binnedVelocity = (int) (wrapper.getVelocity() * 6.9);
        int binnedCurrent = 4;
        byte stickyFaults = 0;
        byte lastByte = 0;

        putNumber(aData, binnedPosition, 3);
        aData.putShort((short) binnedVelocity);
        aData.put((byte) binnedCurrent);
        aData.put(stickyFaults);
        aData.put(lastByte);
    }

    private void populateStatus3(int aPort, ByteBuffer aData)
    {
        CtreTalonSrxSpeedControllerSim wrapper = getWrapperHelper(aPort);
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

        CtreTalonSrxSpeedControllerSim wrapper = getWrapperHelper(aPort);
        int binnedPosition = (int) wrapper.getPosition();
        int binnedVelocity = (int) wrapper.getVelocity();

        putNumber(aData, binnedPosition, 3);
        aData.putShort((short) binnedVelocity);
        aData.put((byte) ((temperature + 50) / 0.6451612903));
        aData.put((byte) ((batteryVoltage - 4) / 0.05));
    }

    private void populateStatus9(int aCanPort, ByteBuffer aData)
    {
        sLOGGER.log(Level.DEBUG, "POPULATE STATUS 9");
        CtreTalonSrxSpeedControllerSim wrapper = getWrapperHelper(aCanPort);
        MotionProfilePoint point = wrapper.getMotionProfilePoint();

        int rawPosition = 0;
        int rawVelocity = 0;
        byte index = 0;

        if (point != null)
        {
            rawPosition = (int) point.mPosition;
            rawVelocity = (int) point.mVelocity;
            index = (byte) point.mIndex;
        }

        byte statusByte = 0;
        statusByte |= point == null ? 0x00 : 0x01;

        byte pos0 = (byte) ((rawPosition & 0x0000FF) >> 0);
        byte pos1 = (byte) ((rawPosition & 0x00FF00) >> 8);
        byte pos2 = (byte) ((rawPosition & 0xFF0000) >> 16);
        byte vel0 = (byte) ((rawVelocity & 0x0000FF) >> 0);
        byte vel1 = (byte) ((rawVelocity & 0x00FF00) >> 8);

        // aData.put(infoByte);
        aData.put(statusByte);
        aData.put(index);
        aData.put((byte) wrapper.getMotionProfileSize());
        aData.put(vel1);
        aData.put(vel0);
        aData.put(pos2);
        aData.put(pos1);
        aData.put(pos0);
        
        aData.order(ByteOrder.LITTLE_ENDIAN);
        sLOGGER.log(Level.DEBUG, "POPULATE STATUS 4 " + point);
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

    private CtreTalonSrxSpeedControllerSim getWrapperHelper(int aPort)
    {
        PwmWrapper rawWrapper = SensorActuatorRegistry.get().getSpeedControllers().get(aPort + sCAN_OFFSET);

        if (rawWrapper == null)
        {
            CtreTalonSrxSpeedControllerSim output = new CtreTalonSrxSpeedControllerSim(aPort);
            SensorActuatorRegistry.get().register(output, aPort + sCAN_OFFSET);
            sLOGGER.log(Level.DEBUG, "Creating " + aPort);
            return output;
        }
        
        return (CtreTalonSrxSpeedControllerSim) rawWrapper;
    }
}
