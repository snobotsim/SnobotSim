/*
 * AHRS.h
 *
 *  Created on: Jul 30, 2015
 *      Author: Scott
 */

#ifndef SRC_AHRS_H_
#define SRC_AHRS_H_

#include "WPILib.h"


class AHRS : public SensorBase,
             public LiveWindowSendable,
             public PIDSource  {
public:

    AHRS(SPI::Port spi_port_id);
    AHRS(I2C::Port i2c_port_id);
    AHRS(SerialPort::Port serial_port_id);

    AHRS(SPI::Port spi_port_id, uint8_t update_rate_hz);
    AHRS(SPI::Port spi_port_id, uint32_t spi_bitrate, uint8_t update_rate_hz);

    AHRS(I2C::Port i2c_port_id, uint8_t update_rate_hz);

    float  GetPitch();
    float  GetRoll();
    float  GetYaw();
    float  GetCompassHeading();
    void   ZeroYaw();
    bool   IsCalibrating();
    bool   IsConnected();
    double GetByteCount();
    double GetUpdateCount();
    long   GetLastSensorTimestamp();
    float  GetWorldLinearAccelX();
    float  GetWorldLinearAccelY();
    float  GetWorldLinearAccelZ();
    bool   IsMoving();
    bool   IsRotating();
    float  GetBarometricPressure();
    float  GetAltitude();
    bool   IsAltitudeValid();
    float  GetFusedHeading();
    bool   IsMagneticDisturbance();
    bool   IsMagnetometerCalibrated();
    float  GetQuaternionW();
    float  GetQuaternionX();
    float  GetQuaternionY();
    float  GetQuaternionZ();
    void   ResetDisplacement();
    void   UpdateDisplacement( float accel_x_g, float accel_y_g,
                               int update_rate_hz, bool is_moving );
    float  GetVelocityX();
    float  GetVelocityY();
    float  GetVelocityZ();
    float  GetDisplacementX();
    float  GetDisplacementY();
    float  GetDisplacementZ();
    double GetAngle();
    double GetRate();
    void   SetAngleAdjustment(double angle);
    double GetAngleAdjustment();
    void   Reset();
    float  GetRawGyroX();
    float  GetRawGyroY();
    float  GetRawGyroZ();
    float  GetRawAccelX();
    float  GetRawAccelY();
    float  GetRawAccelZ();
    float  GetRawMagX();
    float  GetRawMagY();
    float  GetRawMagZ();
    float  GetPressure();
    float  GetTempC();
    std::string GetFirmwareVersion();

    int GetActualUpdateRate();
    int GetRequestedUpdateRate();

    void EnableLogging(bool enable);

private:

    /* LiveWindowSendable implementation */
    void InitTable(std::shared_ptr<ITable> subtable);
    std::shared_ptr<ITable> GetTable() const;
    std::string GetSmartDashboardType() const;
    void UpdateTable();
    void StartLiveWindowMode();
    void StopLiveWindowMode();

    /* PIDSource implementation */
    double PIDGet();

};

#endif /* SRC_AHRS_H_ */
