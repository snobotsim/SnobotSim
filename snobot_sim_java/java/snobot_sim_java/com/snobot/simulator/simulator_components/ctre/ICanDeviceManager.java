package com.snobot.simulator.simulator_components.ctre;

public interface ICanDeviceManager
{

    void handleSend(int aMessageId);

    void handleReceive(int aMessageId);

    void openStreamSession(int aMessageId);

    void readStreamSession(int aMessageId);

}
