package com.snobot.simulator.motor_sim.motor_factory;

import java.util.HashMap;
import java.util.Map;

import com.snobot.simulator.motor_sim.DcMotorModel;

public class VexMotorFactory
{
    private final static double NOMINAL_VOLTAGE = 12;

    public static class MotorParameters
    {
        public final double FREE_SPEED_RPM;
        public final double FREE_CURRENT;
        public final double STALL_TORQUE;
        public final double STALL_CURRENT;
        
        public MotorParameters(double aFreeSpeedRpm, double aFreeCurrent, double aStallTorque, double aCurrent)
        {
            FREE_SPEED_RPM = aFreeSpeedRpm;
            FREE_CURRENT = aFreeCurrent;
            STALL_TORQUE = aStallTorque;
            STALL_CURRENT = aCurrent;
        }
    }

    protected static final Map<String, MotorParameters> sMOTOR_PARAMS;

    protected static final MotorParameters MOTOR_PARAMS_CIM = new MotorParameters(5330, 2.7, 2.41, 131);
    protected static final MotorParameters MOTOR_PARAMS_MINI_CIM = new MotorParameters(5840, 3, 1.41, 89);
    protected static final MotorParameters MOTOR_PARAMS_BAG = new MotorParameters(13180, 1.8, .43, 53);
    protected static final MotorParameters MOTOR_PARAMS_775PRO = new MotorParameters(18730, .7, .71, 134);
    protected static final MotorParameters MOTOR_PARAMS_AM_775_125 = new MotorParameters(5800, 1.6, .28, 18);
    protected static final MotorParameters MOTOR_PARAMS_BB_RS775 = new MotorParameters(13050, 2.7, .72, 97);
    protected static final MotorParameters MOTOR_PARAMS_AM_9015 = new MotorParameters(14270, 3.7, .36, 71);
    protected static final MotorParameters MOTOR_PARAMS_BB_RS550 = new MotorParameters(19000, .4, .38, 84);

    static
    {
        sMOTOR_PARAMS = new HashMap<>();
        sMOTOR_PARAMS.put("CIM", MOTOR_PARAMS_CIM);
        sMOTOR_PARAMS.put("Mini CIM", MOTOR_PARAMS_MINI_CIM);
        sMOTOR_PARAMS.put("Bag", MOTOR_PARAMS_BAG);
        sMOTOR_PARAMS.put("775 Pro", MOTOR_PARAMS_775PRO);
        sMOTOR_PARAMS.put("Andymark RS 775-125", MOTOR_PARAMS_AM_775_125);
        sMOTOR_PARAMS.put("Banebots RS 775", MOTOR_PARAMS_BB_RS775);
        sMOTOR_PARAMS.put("Andymark 9015", MOTOR_PARAMS_AM_9015);
        sMOTOR_PARAMS.put("Banebots RS 550", MOTOR_PARAMS_BB_RS550);
    }

    public static DcMotorModel createMotor(String aMotorName)
    {
        MotorParameters motorParams = sMOTOR_PARAMS.get(aMotorName);
        if (motorParams != null)
        {
            return new DcMotorModel(NOMINAL_VOLTAGE, motorParams.FREE_SPEED_RPM, motorParams.FREE_CURRENT, motorParams.STALL_TORQUE,
                    motorParams.STALL_CURRENT, 0);
        }
        return new DcMotorModel(NOMINAL_VOLTAGE, 0, 0, 0, 0, 0);
    }
}
