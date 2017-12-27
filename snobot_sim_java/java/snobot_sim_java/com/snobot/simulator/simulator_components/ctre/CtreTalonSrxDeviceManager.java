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

        sLOGGER.log(Level.TRACE,
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

        sLOGGER.log(Level.TRACE, "@ReceiveMessage: MID: " + Integer.toHexString(aCanMessageId));

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
        sLOGGER.log(Level.TRACE, "Reading stream session " + messagesToRead);
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
        byte feedbackDevice = (byte) ((commandSection & 0x1E) >> 1);
        byte overrideLimitSwitchesRaw = (byte) ((commandSection & 0xE0) >> 5);
        wrapper.setCanFeedbackDevice(feedbackDevice);
        wrapper.setCurrentProfile(profileSelect);

        if (overrideLimitSwitchesRaw != 0x1)
        {
            boolean overrideFwdLimitSwitch = (overrideLimitSwitchesRaw & 0x2) == 0x2;
            boolean overrideRevLimitSwitch = (overrideLimitSwitchesRaw & 0x1) == 0x1;

            wrapper.setLimitSwitchOverride(overrideFwdLimitSwitch, overrideRevLimitSwitch);
        }

        byte commandType = (byte) ((aBuffer.get(6) >> 4) & 0xF);
        int demand = (aBuffer.getInt(2)) >> 8;
        sLOGGER.log(Level.TRACE, String.format("handleTx1: Demand: %d, Command: %d, Profile: %d, Feedback Device: %d, Limit Switches: %02X",
                demand, commandType, profileSelect, feedbackDevice, overrideLimitSwitchesRaw));

        if (commandType == 0x00)
        {
            double appliedVoltageDemand = demand / 1023.0;
            wrapper.set(appliedVoltageDemand);
            sLOGGER.log(Level.TRACE, " Setting by applied throttle.. " + appliedVoltageDemand);
        }
        else if (commandType == (byte) 0x01)
        {
            wrapper.setPositionGoal(demand);
            sLOGGER.log(Level.TRACE, "  Setting by position." + demand);
        }
        else if (commandType == (byte) 0x02)
        {
            double speed = demand;
            wrapper.setSpeedGoal(speed);
            sLOGGER.log(Level.TRACE, " Setting by speed. " + speed);
        }
        else if (commandType == (byte) 0x03)
        {
            sLOGGER.log(Level.WARN, "  Setting by current (" + demand + ") is not supported in the simulator");
        }
        else if (commandType == (byte) 0x04)
        {
            double voltageDemand = demand / 256.0;
            wrapper.set(voltageDemand / 12.0);
            sLOGGER.log(Level.TRACE, "  Setting by voltage. " + voltageDemand);
        }
        else if (commandType == (byte) 0x05)
        {
            CtreTalonSrxSpeedControllerSim leadTalon = getWrapperHelper(demand);
            leadTalon.addFollower(wrapper);
            sLOGGER.log(Level.TRACE, "  Setting by FOLLOWER." + demand);
        }
        else if (commandType == (byte) 0x06)
        {
            wrapper.setMotionProfilingCommand(demand);
            sLOGGER.log(Level.TRACE, "  Setting by Motion Profile " + demand);
        }
        else if (commandType == (byte) 0x07)
        {
            wrapper.setMotionMagicGoal(demand);
            sLOGGER.log(Level.TRACE, "  Setting by Motion Magic (" + demand + ")");
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

        sLOGGER.log(Level.TRACE, String.format("handleTx6: " +
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
        
        switch(commandType)
        {

        /////////////////////////
        // PID - Slot 0
        /////////////////////////
        case 1:
            wrapper.setPGain(0, floatValue);
            break;
        case 2:
            wrapper.setIGain(0, floatValue);
            break;
        case 3:
            wrapper.setDGain(0, floatValue);
            break;
        case 4:
            wrapper.setFGain(0, floatValue);
            break;
        case 5:
            wrapper.setIZone(0, rawValue);
            break;
        case 6:
            logUnsupportedSetParam(commandType, "setCloseLoopRampRate");
            break;
        case 11:
            wrapper.setPGain(1, floatValue);
            break;
        case 12:
            wrapper.setIGain(1, floatValue);
            break;
        case 13:
            wrapper.setDGain(1, floatValue);
            break;
        case 14:
            wrapper.setFGain(1, floatValue);
            break;
        case 15:
            wrapper.setIZone(1, rawValue);
            break;
        case 16:
            logUnsupportedSetParam(commandType, "setCloseLoopRampRate");
            break;
        case 21:
            logUnsupportedSetParam(commandType, "setForwardSoftLimit");
            break;
        case 22:
            logUnsupportedSetParam(commandType, "setReverseSoftLimit");
            break;
        case 23:
            logUnsupportedSetParam(commandType, "enableForwardSoftLimit");
            break;
        case 24:
            logUnsupportedSetParam(commandType, "enableReverseSoftLimit");
            break;
        case 32:
            logUnsupportedSetParam(commandType, "ConfigFwdLimitSwitchNormallyOpen");
            break;
        case 33:
            logUnsupportedSetParam(commandType, "ConfigRevLimitSwitchNormallyOpen");
            break;
        case 44:
            logUnsupportedSetParam(commandType, "setForwardSoftLimit");
            break;
        case 73:
            double position = rawValue / 256;
            wrapper.reset(position, wrapper.getVelocity(), wrapper.getCurrent());
            break;
        case 77:
            wrapper.reset(rawValue, wrapper.getVelocity(), wrapper.getCurrent());
            break;
        case 93:
            logUnsupportedSetParam(commandType, "clearIAccum");
            break;
        case 94:
        case 95:
        case 96:
        case 97:
        case 109:
            logUnsupportedSetParam(commandType, "setStatusFrameRateMs");
            break;
        case 105:
        case 107:
            logUnsupportedSetParam(commandType, "configNominalOutputVoltage");
            break;
        case 104:
        case 106:
            logUnsupportedSetParam(commandType, "configPeakOutputVoltage");
            break;
        case 108:
        case 100:
            logUnsupportedSetParam(commandType, "enableZeroSensorPositionOnIndex");
            break;
        case 111:
            logUnsupportedSetParam(commandType, "setAllowableClosedLoopErr");
            break;
        case 112:
            logUnsupportedSetParam(commandType, "configPotentiometerTurns");
            break;
        case 113:
            logUnsupportedSetParam(commandType, "configEncoderCodesPerRev");
            break;
        case 114:
            logUnsupportedSetParam(commandType, "setPulseWidthPosition");
            break;
        case 115:
            logUnsupportedSetParam(commandType, "setAnalogPosition");
            break;
        case 116:
            logUnsupportedSetParam(commandType, "setVoltageCompensationRampRate");
            break;
        case 119:
            logUnsupportedSetParam(commandType, "clearMotionProfileHasUnderrun");
            break;
        case 122:
            wrapper.setMotionMagicMaxAcceleration(rawValue);
            break;
        case 123:
            wrapper.setMotionMagicMaxVelocity(rawValue);
            break;
        case 125:
            logUnsupportedSetParam(commandType, "setCurrentLimit");
            break;
        case -112:
            logUnsupportedSetParam(commandType, "setCurrentLimit");
            break;
        case -111:
            logUnsupportedSetParam(commandType, "enableZeroSensorPositionOnReverseLimit");
            break;
        default:
            logUnsupportedSetParam(commandType);
            break;
        }
    }

    private void logUnsupportedSetParam(int commandType)
    {
        sLOGGER.log(Level.ERROR, "************* Unknown SetParam command: " + commandType + " ************* "
                + "This is an unexpected call, please make an issue at https://github.com/pjreiniger/SnobotSim");
    }

    private void logUnsupportedSetParam(int commandType, String aFunctionName)
    {
        sLOGGER.log(Level.WARN, aFunctionName + " (command=" + commandType + ") is not supported."
                + "  If this is *crucial* to simulating your robot, please make an issue at https://github.com/pjreiniger/SnobotSim");
    }

    private void handleParamRequest(ByteBuffer aBuffer, int aPort)
    {

        CtreTalonSrxSpeedControllerSim wrapper = getWrapperHelper(aPort);
        aBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byte commandType = aBuffer.get(0);
        sLOGGER.log(Level.TRACE, "Getting parameters... " + commandType);

        double floatValue = 0;
        boolean isFloat = true;
        
        switch(commandType)
        {
        case 1:
            floatValue = wrapper.getPidConstants(0).mP;
            break;
        case 2:
            floatValue = wrapper.getPidConstants(0).mI;
            break;
        case 3:
            floatValue = wrapper.getPidConstants(0).mD;
            break;
        case 4:
            floatValue = wrapper.getPidConstants(0).mF;
            break;
        case 5:
            floatValue = wrapper.getPidConstants(0).mIZone;
            isFloat = false;
            break;
        case 6:
            sLOGGER.log(Level.INFO, "getCloseLoopRampRate is not supported");
            break;
        case 11:
            floatValue = wrapper.getPidConstants(1).mP;
            break;
        case 12:
            floatValue = wrapper.getPidConstants(1).mI;
            break;
        case 13:
            floatValue = wrapper.getPidConstants(1).mD;
            break;
        case 14:
            floatValue = wrapper.getPidConstants(1).mF;
            break;
        case 15:
            floatValue = wrapper.getPidConstants(1).mIZone;
            isFloat = false;
            break;
        case 93:
            sLOGGER.log(Level.INFO, "GetIaccum is not supported");
            break;
        default:
            sLOGGER.log(Level.ERROR, "************* Unknown GetParam command: " + commandType + " ************* "
                    + "If this is *crucial* to simulating your robot, please make an issue at https://github.com/pjreiniger/SnobotSim");
            break;
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
        sLOGGER.log(Level.TRACE, " Getting STATUS1 " + wrapper.get());

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
        int binnedPosition = wrapper.getBinnedPosition();
        int binnedVelocity = wrapper.getBinnedVelocity();
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
        int binnedVelocity = (int) (wrapper.getVelocity() * 54.1343283582);

        putNumber(aData, binnedPosition, 3);
        aData.putShort((short) binnedVelocity);
    }

    private void populateStatus4(int aPort, ByteBuffer aData)
    {
        sLOGGER.log(Level.TRACE, "POPULATE STATUS 4");
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
        sLOGGER.log(Level.TRACE, "POPULATE STATUS 9");
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
        sLOGGER.log(Level.TRACE, "POPULATE STATUS 4 " + point);
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
            sLOGGER.log(Level.TRACE, "Creating " + aPort);
            return output;
        }
        
        return (CtreTalonSrxSpeedControllerSim) rawWrapper;
    }
}
