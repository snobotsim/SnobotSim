package com.snobot.simulator.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SimulatorConfig
{
    private List<BasicModuleConfig> mAccelerometers;
    private List<BasicModuleConfig> mAnalogIO;
    private List<BasicModuleConfig> mDigitalIO;
    private List<BasicModuleConfig> mGyros;
    private List<BasicModuleConfig> mRelays;
    private List<BasicModuleConfig> mSolenoids;
    private List<EncoderConfig> mEncoders;
    private List<PwmConfig> mPwm;
    private Map<Integer, String> mDefaultI2CWrappers;
    private Map<Integer, String> mDefaultSpiWrappers;
    private List<Object> mSimulatorComponents;

    public SimulatorConfig()
    {
        mAccelerometers = new ArrayList<>();
        mAnalogIO = new ArrayList<>();
        mDigitalIO = new ArrayList<>();
        mGyros = new ArrayList<>();
        mRelays = new ArrayList<>();
        mSolenoids = new ArrayList<>();
        mEncoders = new ArrayList<>();
        mPwm = new ArrayList<>();

        mDefaultI2CWrappers = new LinkedHashMap<>();
        mDefaultSpiWrappers = new LinkedHashMap<>();
        mSimulatorComponents = new ArrayList<>();
    }

    public List<BasicModuleConfig> getmDigitalIO()
    {
        return mDigitalIO;
    }

    public void setmDigitalIO(List<BasicModuleConfig> mDigitalIO)
    {
        this.mDigitalIO = mDigitalIO;
    }

    public List<BasicModuleConfig> getmAnalogIO()
    {
        return mAnalogIO;
    }

    public void setmAnalogIO(List<BasicModuleConfig> mAnalogIO)
    {
        this.mAnalogIO = mAnalogIO;
    }

    public List<BasicModuleConfig> getmRelays()
    {
        return mRelays;
    }

    public void setmRelays(List<BasicModuleConfig> mRelays)
    {
        this.mRelays = mRelays;
    }

    public List<BasicModuleConfig> getmSolenoids()
    {
        return mSolenoids;
    }

    public void setmSolenoids(List<BasicModuleConfig> mSolenoids)
    {
        this.mSolenoids = mSolenoids;
    }

    public List<EncoderConfig> getmEncoders()
    {
        return mEncoders;
    }

    public void setmEncoders(List<EncoderConfig> mEncoders)
    {
        this.mEncoders = mEncoders;
    }

    public List<PwmConfig> getmPwm()
    {
        return mPwm;
    }

    public void setmPwm(List<PwmConfig> mPwm)
    {
        this.mPwm = mPwm;
    }

    public Map<Integer, String> getmDefaultI2CWrappers()
    {
        return mDefaultI2CWrappers;
    }

    public void setmDefaultI2CWrappers(Map<Integer, String> mDefaultI2CWrappers)
    {
        this.mDefaultI2CWrappers = mDefaultI2CWrappers;
    }

    public Map<Integer, String> getmDefaultSpiWrappers()
    {
        return mDefaultSpiWrappers;
    }

    public void setmDefaultSpiWrappers(Map<Integer, String> mDefaultSpiWrappers)
    {
        this.mDefaultSpiWrappers = mDefaultSpiWrappers;
    }

    public List<BasicModuleConfig> getmAccelerometers()
    {
        return mAccelerometers;
    }

    public void setmAccelerometers(List<BasicModuleConfig> mAccelerometers)
    {
        this.mAccelerometers = mAccelerometers;
    }

    public List<BasicModuleConfig> getmGyros()
    {
        return mGyros;
    }

    public void setmGyros(List<BasicModuleConfig> mGyros)
    {
        this.mGyros = mGyros;
    }

    public List<Object> getmSimulatorComponents()
    {
        return mSimulatorComponents;
    }

    public void setmSimulatorComponents(List<Object> mSimulatorComponents)
    {
        this.mSimulatorComponents = mSimulatorComponents;
    }

}
