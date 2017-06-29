//
//#include "AHRS.h"
//#include "SnobotSim/GetSensorActuatorHelper.h"
//#include "SnobotSim/Logging/SnobotLogger.h"
//#include "SnobotSim/SensorActuatorRegistry.h"
//#include "SnobotSim/SimulatorComponents/Gyro/GyroWrapper.h"
//
//void SetupAHRS(int aHandle)
//{
//	std::cout << "Registering AHRS on " + aHandle;
//	SensorActuatorRegistry::Get().Register(aHandle + 0, std::shared_ptr<GyroWrapper>(new GyroWrapper("NavX Yaw")));
//	SensorActuatorRegistry::Get().Register(aHandle + 1, std::shared_ptr<GyroWrapper>(new GyroWrapper("NavX Pitch")));
//	SensorActuatorRegistry::Get().Register(aHandle + 2, std::shared_ptr<GyroWrapper>(new GyroWrapper("NavX Roll")));
//}
//
//AHRS::AHRS(SPI::Port spi_port_id)
//{
//	SetupAHRS(500);
//}
//
//AHRS::AHRS(SPI::Port spi_port_id, uint8_t update_rate_hz)
//{
//	SetupAHRS(500);
//}
//
//AHRS::AHRS(SPI::Port spi_port_id, uint32_t spi_bitrate, uint8_t update_rate_hz)
//{
//	SetupAHRS(500);
//}
//
//AHRS::AHRS(I2C::Port i2c_port_id)
//{
//	SetupAHRS(500);
//}
//AHRS::AHRS(I2C::Port i2c_port_id, uint8_t update_rate_hz)
//{
//	SetupAHRS(500);
//}
//
//AHRS::AHRS(SerialPort::Port serial_port_id)
//{
//	SetupAHRS(500);
//}
//
//
//float  AHRS::GetPitch()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//float  AHRS::GetRoll()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//float  AHRS::GetYaw()
//{
//    std::shared_ptr<GyroWrapper> wrapper = GetSensorActuatorHelper::GetGyroWrapper(500);
//	return wrapper->GetAngle();
//}
//float  AHRS::GetCompassHeading()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//void   AHRS::ZeroYaw()
//{
//	LOG_UNSUPPORTED();
//}
//bool   AHRS::IsCalibrating()
//{
//	return false;
//}
//bool   AHRS::IsConnected()
//{
//	return true;
//}
//double AHRS::GetByteCount()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//double AHRS::GetUpdateCount()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//long   AHRS::GetLastSensorTimestamp()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//float  AHRS::GetWorldLinearAccelX()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//float  AHRS::GetWorldLinearAccelY()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//float  AHRS::GetWorldLinearAccelZ()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//bool   AHRS::IsMoving()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//bool   AHRS::IsRotating()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//float  AHRS::GetBarometricPressure()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//float  AHRS::GetAltitude()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//bool   AHRS::IsAltitudeValid()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//float  AHRS::GetFusedHeading()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//bool   AHRS::IsMagneticDisturbance()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//bool   AHRS::IsMagnetometerCalibrated()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//float  AHRS::GetQuaternionW()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//float  AHRS::GetQuaternionX()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//float  AHRS::GetQuaternionY()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//float  AHRS::GetQuaternionZ()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//void   AHRS::ResetDisplacement()
//{
//	LOG_UNSUPPORTED();
//}
//void   AHRS::UpdateDisplacement( float accel_x_g, float accel_y_g,
//						   int update_rate_hz, bool is_moving )
//{
//	LOG_UNSUPPORTED();
//}
//float  AHRS::GetVelocityX()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//float  AHRS::GetVelocityY()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//float  AHRS::GetVelocityZ()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//float  AHRS::GetDisplacementX()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//float  AHRS::GetDisplacementY()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//float  AHRS::GetDisplacementZ()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//double AHRS::GetAngle()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//double AHRS::GetRate()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//void   AHRS::SetAngleAdjustment(double angle)
//{
//	LOG_UNSUPPORTED();
//}
//double AHRS::GetAngleAdjustment()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//void   AHRS::Reset()
//{
//	LOG_UNSUPPORTED();
//}
//float  AHRS::GetRawGyroX()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//float  AHRS::GetRawGyroY()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//float  AHRS::GetRawGyroZ()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//float  AHRS::GetRawAccelX()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//float  AHRS::GetRawAccelY()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//float  AHRS::GetRawAccelZ()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//float  AHRS::GetRawMagX()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//float  AHRS::GetRawMagY()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//float  AHRS::GetRawMagZ()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//float  AHRS::GetPressure()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//float  AHRS::GetTempC()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//std::string AHRS::GetFirmwareVersion()
//{
//	LOG_UNSUPPORTED();
//	return "";
//}
//
//int AHRS::GetActualUpdateRate()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//int AHRS::GetRequestedUpdateRate()
//{
//	LOG_UNSUPPORTED();
//	return 0;
//}
//
//void AHRS::EnableLogging(bool enable)
//{
//	LOG_UNSUPPORTED();
//}
//
//
///* LiveWindowSendable implementation */
//void AHRS::InitTable(std::shared_ptr<ITable> subtable)
//{
//
//}
//std::shared_ptr<ITable> AHRS::GetTable() const
//{
//	return std::shared_ptr<ITable>();
//}
//std::string AHRS::GetSmartDashboardType() const
//{
//	return "";
//}
//void AHRS::UpdateTable()
//{
//
//}
//void AHRS::StartLiveWindowMode()
//{
//
//}
//void AHRS::StopLiveWindowMode()
//{
//
//}
//
///* PIDSource implementation */
//double AHRS::PIDGet()
//{
//	return 0;
//}
