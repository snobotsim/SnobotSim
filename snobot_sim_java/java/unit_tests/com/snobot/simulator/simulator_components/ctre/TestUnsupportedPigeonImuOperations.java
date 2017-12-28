package com.snobot.simulator.simulator_components.ctre;

import org.junit.Test;

import com.ctre.phoenix.Sensors.PigeonIMU;
import com.ctre.phoenix.Sensors.PigeonIMU.CalibrationMode;
import com.ctre.phoenix.Sensors.PigeonIMU.ParamEnum;
import com.ctre.phoenix.Sensors.PigeonIMU.StatusFrameRate;
import com.snobot.test.utilities.BaseSimulatorTest;

public class TestUnsupportedPigeonImuOperations extends BaseSimulatorTest
{
    @Test
    public void testAllFunctions()
    {
        PigeonIMU imu = new PigeonIMU(0);
        imu.configSetParameter(ParamEnum.AccumZ, 0);
        imu.setStatusFrameRateMs(StatusFrameRate.BiasedStatus_2_Gyro, 0);
        imu.setYaw(0);
        imu.addYaw(0);
        imu.setYawToCompass();
        imu.setFusedHeading(0);
        imu.addFusedHeading(0);
        imu.setFusedHeadingToCompass();
        imu.setAccumZAngle(0);
        imu.enableTemperatureCompensation(false);
        imu.setCompassDeclination(0);
        imu.setCompassAngle(0);
        imu.enterCalibrationMode(CalibrationMode.Accelerometer);
        imu.getGeneralStatus();
        imu.getLastError();
        imu.get6dQuaternion();
        imu.getYawPitchRoll();
        imu.getAccumGyro();
        imu.getAbsoluteCompassHeading();
        imu.getCompassHeading();
        imu.getCompassFieldStrength();
        imu.getTemp();
        imu.getState(0, 0);
        imu.getState();
        imu.getUpTime();
        imu.getRawMagnetometer();
        imu.getBiasedMagnetometer();
        imu.getBiasedAccelerometer();
        imu.getRawGyro();
        imu.getAccelerometerAngles();
        imu.getFusedHeading();
        imu.getResetCount();
        imu.getResetFlags();
        imu.getFirmVers();
        imu.hasResetOccured();
    }

}
