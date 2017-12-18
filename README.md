

[![Appveyor Build status](https://ci.appveyor.com/api/projects/status/wsc0xo7ix749ibuo?svg=true)](https://ci.appveyor.com/project/pjreiniger/snobotsim)
[![Travis Build Status](https://travis-ci.org/pjreiniger/SnobotSim.svg?branch=master)](https://travis-ci.org/pjreiniger/SnobotSim)
[![codecov](https://codecov.io/gh/pjreiniger/SnobotSim/branch/master/graph/badge.svg)](https://codecov.io/gh/pjreiniger/SnobotSim)

# SnobotSim
SnobotSim is a java-based simulator that can simulate FRC Robots written in Java, with incubating support for C++ and Python.

## Features
* Simple physics based motor simulations
* Feedback Sensor simulation (AnalogIO, DigitalIO, Encoders, Gyros)
* [NavX Simulation](https://www.kauailabs.com/store/index.php?route=product/product&product_id=56) for the 3-axis Gyro and 3-axis Accelerometers
* [CTRE CanTalon](http://www.ctr-electronics.com/talon-srx.html) and [Pigeon IMU](http://www.ctr-electronics.com/gadgeteer-imu-module-pigeon.html) simulations (Currently in Beta.  Not all features are supported, and the changes they are making for 2018 might break this)
* Open to extension for custom I2C, SPI, Potentiometer, Motors servo'd by limit switches, etc.  Hopefully anything you need!

## Contributing
If you need any help with simulator, check the [](wiki).  If that doesn't help, DM me on ChiefDelphi, `pjreiniger`
If something doesn't seem like its working, create an issue here and I will try to make a patch for you
If you find a bug you can fix, don't like how I did something, or want to add an improvment, create a Pull Request

## Documentation
Check out the [wiki](https://github.com/pjreiniger/SnobotSim/wiki) for how to set up and use the simulator.