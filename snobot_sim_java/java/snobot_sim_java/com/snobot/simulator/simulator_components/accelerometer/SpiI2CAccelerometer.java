package com.snobot.simulator.simulator_components.accelerometer;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.jni.SpiI2CSimulatorHal;

public class SpiI2CAccelerometer
{
    public SpiI2CAccelerometer(String aType, long aNativePointer, int aBasePort)
    {
        if (aNativePointer == -1)
        {
            throw new IllegalArgumentException("Native pointer not set up correctly");
        }

        AccelerometerWrapper xWrapper = new AccelerometerWrapper(aType)
        {
            @Override
            public void setAcceleration(double aAcceleration)
            {
                SpiI2CSimulatorHal.setSpiI2cAccelerometerX(aType, aNativePointer, aAcceleration);
            }

            @Override
            public double getAcceleration()
            {
                return SpiI2CSimulatorHal.getSpiI2cAccelerometerX(aType, aNativePointer);
            }
        };

        AccelerometerWrapper yWrapper = new AccelerometerWrapper("")
        {
            @Override
            public void setAcceleration(double aAcceleration)
            {
                SpiI2CSimulatorHal.setSpiI2cAccelerometerY(aType, aNativePointer, aAcceleration);
            }

            @Override
            public double getAcceleration()
            {
                return SpiI2CSimulatorHal.getSpiI2cAccelerometerY(aType, aNativePointer);
            }
        };

        AccelerometerWrapper zWrapper = new AccelerometerWrapper("")
        {
            @Override
            public void setAcceleration(double aAcceleration)
            {
                SpiI2CSimulatorHal.setSpiI2cAccelerometerZ(aType, aNativePointer, aAcceleration);
            }

            @Override
            public double getAcceleration()
            {
                return SpiI2CSimulatorHal.getSpiI2cAccelerometerZ(aType, aNativePointer);
            }
        };

        SensorActuatorRegistry.get().register(xWrapper, aBasePort + 0);
        SensorActuatorRegistry.get().register(yWrapper, aBasePort + 1);
        SensorActuatorRegistry.get().register(zWrapper, aBasePort + 2);
    }
    

//    public double 
}
