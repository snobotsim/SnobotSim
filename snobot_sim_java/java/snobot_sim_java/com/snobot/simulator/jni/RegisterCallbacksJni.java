package com.snobot.simulator.jni;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.module_wrapper.AnalogWrapper;
import com.snobot.simulator.module_wrapper.AnalogWrapper.VoltageSetterHelper;
import com.snobot.simulator.module_wrapper.DigitalSourceWrapper;
import com.snobot.simulator.module_wrapper.DigitalSourceWrapper.StateSetterHelper;
import com.snobot.simulator.module_wrapper.EncoderWrapper;
import com.snobot.simulator.module_wrapper.EncoderWrapper.DistanceSetterHelper;
import com.snobot.simulator.module_wrapper.PwmWrapper;
import com.snobot.simulator.module_wrapper.RelayWrapper;
import com.snobot.simulator.module_wrapper.SolenoidWrapper;
import com.snobot.simulator.simulator_components.gyro.GyroWrapper;
import com.snobot.simulator.simulator_components.gyro.GyroWrapper.AngleSetterHelper;

public class RegisterCallbacksJni extends BaseSnobotJni
{
    public static native void reset();

    public static native void registerAnalogCallback(String functionName);

    public static native void registerAnalogGyroCallback(String functionName);

    public static native void registerDigitalCallback(String functionName);

    public static native void registerEncoderCallback(String functionName);

    public static native void registerPcmCallback(String functionName);

    public static native void registerPdpCallback(String functionName);

    public static native void registerPwmCallback(String functionName);

    public static native void registerRelayCallback(String functionName);

    public static void registerAllCallbacks()
    {
        registerAnalogCallback();
        registerAnalogGyroCallback();
        registerDigitalCallback();
        registerEncoderCallback();
        registerPcmCallback();
        registerPdpCallback();
        registerPwmCallback();
        registerRelayCallback();
    }

    public static void registerAnalogCallback()
    {
        registerAnalogCallback("analogCallback");
    }

    public static void registerAnalogGyroCallback()
    {
        registerAnalogGyroCallback("analogGyroCallback");
    }

    public static void registerDigitalCallback()
    {
        registerDigitalCallback("digitalCallback");
    }

    public static void registerEncoderCallback()
    {
        registerEncoderCallback("encoderCallback");
    }

    public static void registerPcmCallback()
    {
        registerPcmCallback("pcmCallback");
    }

    public static void registerPdpCallback()
    {
        registerPdpCallback("pdpCallback");
    }

    public static void registerPwmCallback()
    {
        registerPwmCallback("pwmCallback");
    }

    public static void registerRelayCallback()
    {
        registerRelayCallback("relayCallback");
    }

    public static void analogCallback(String callbackType, int port, HalCallbackValue halValue)
    {
        if ("Initialized".equals(callbackType))
        {
            SensorActuatorRegistry.get().register(new AnalogWrapper(port, new VoltageSetterHelper()
            {

                @Override
                public void setVoltage(double aVoltage)
                {
                    SensorFeedbackJni.setAnalogVoltage(port, aVoltage);
                }
            }), port);
        }
        else
        {
            System.out.println("Unknown Analog callback " + callbackType);
        }
    }

    public static void analogGyroCallback(String callbackType, int port, HalCallbackValue halValue)
    {
        if ("Initialized".equals(callbackType))
        {
            GyroWrapper wrapper = new GyroWrapper("Analog Gyro", new AngleSetterHelper()
            {
                @Override
                public void setAngle(double aAngle)
                {
                    SensorFeedbackJni.setAnalogGyroAngle(port, aAngle);
                }
            });
            SensorActuatorRegistry.get().register(wrapper, port);
        }
        else if ("Angle".equals(callbackType))
        {
            SensorActuatorRegistry.get().getGyros().get(port).setAngle(halValue.mDouble);
        }
        else
        {
            System.out.println("Unknown AnalogGyro callback " + callbackType);
        }
    }

    public static void digitalCallback(String callbackType, int port, HalCallbackValue halValue)
    {
        if ("Initialized".equals(callbackType))
        {
            SensorActuatorRegistry.get().register(new DigitalSourceWrapper(port, new StateSetterHelper()
            {

                @Override
                public void setState(boolean aState)
                {
                    System.out.println("Trying to set");
                    SensorFeedbackJni.setDigitalInput(port, aState);
                }
            }), port);
        }
        else if ("Value".equals(callbackType))
        {
            SensorActuatorRegistry.get().getDigitalSources().get(port).set(halValue.mBoolean);
        }
        else
        {
            System.out.println("Unknown Digital callback " + callbackType);
        }
    }

    public static void encoderCallback(String callbackType, int port, HalCallbackValue halValue)
    {
        if ("Initialized".equals(callbackType))
        {
            SensorActuatorRegistry.get().register(new EncoderWrapper(port, new DistanceSetterHelper()
            {

                @Override
                public void setDistance(double aDistance)
                {
                    SensorFeedbackJni.setEncoderDistance(port, aDistance);
                }
            }), port);
        }
        else
        {
            System.out.println("Unknown Encoder callback " + callbackType);
        }
    }

    public static void pcmCallback(String callbackType, int port, HalCallbackValue halValue)
    {
        if ("SolenoidInitialized".equals(callbackType))
        {
            SensorActuatorRegistry.get().register(new SolenoidWrapper(port), port);
        }
        else if ("SolenoidOutput".equals(callbackType))
        {
            SensorActuatorRegistry.get().getSolenoids().get(port).set(halValue.mBoolean);
        }
        else
        {
            System.out.println("Unknown PCM callback " + callbackType);
        }
    }

    public static void pdpCallback(String callbackType, int port, HalCallbackValue halValue)
    {
        if ("Initialized".equals(callbackType))
        {

        }
        else
        {
            System.out.println("Unknown PDP callback " + callbackType);
        }
    }

    public static void pwmCallback(String callbackType, int port, HalCallbackValue halValue)
    {
        if ("Initialized".equals(callbackType))
        {
            SensorActuatorRegistry.get().register(new PwmWrapper(port), port);
        }
        else if ("Speed".equals(callbackType))
        {
            SensorActuatorRegistry.get().getSpeedControllers().get(port).set(halValue.mDouble);
        }
        else
        {
            System.out.println("Unknown PWM callback " + callbackType);
        }
    }

    public static void relayCallback(String callbackType, int port, HalCallbackValue halValue)
    {
        if ("InitializedForward".equals(callbackType))
        {
            SensorActuatorRegistry.get().register(new RelayWrapper(port), port);
        }
        else if ("InitializedReverse".equals(callbackType))
        {
            // Nothing to do, assume it was initialized in forwards call
        }
        else if ("Forward".equals(callbackType))
        {
            SensorActuatorRegistry.get().getRelays().get(port).setRelayForwards(halValue.mBoolean);
        }
        else if ("Reverse".equals(callbackType))
        {
            SensorActuatorRegistry.get().getRelays().get(port).setRelayReverse(halValue.mBoolean);
        }
        else
        {
            System.out.println("Unknown Relay callback " + callbackType);
        }
    }
}
