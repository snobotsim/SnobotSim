

# [0.7.0](https://github.com/pjreiniger/SnobotSim/releases/tag/0.7.0)
First release of the 2018 Season.  Anything releases before this were experimental and not recommended for usage.

## Known Issues
### CTRE
* Voltage and AppliedThrottle control modes are in beta support.  This means that that they *should* work as expected
* MotionMagic, Position, and Velocity control modes are in alpha support.  This means that they *may, sort of, sometimes* work as expected.  They are tested against, but may not anywhere near the same as the real world
* Everything else is unsupported.  Attempting to use other functions will simply log a warning.  For the average team, these features will not hinder the majority of your pre-robot testing

### Simulator
* Tank drive simulator, I2C, and SPI built in simulations do not have GUI support, so you must update the config file manually