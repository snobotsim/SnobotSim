
#pragma once

#include <functional>
#include <map>
#include <memory>
#include <string>

class RevpeedControllerSim;

class RevManager
{
public:
    RevManager();

    void Reset();

    void handleMessage(const std::string& aCallback, int aCanPort, uint8_t* aBuffer, int aLength);

protected:

    using NormalMotorCallback = std::function<void(std::shared_ptr<RevpeedControllerSim>)>;
    using SlottedMotorCallback = std::function<void(std::shared_ptr<RevpeedControllerSim>, int)>;
    std::map<std::string, NormalMotorCallback> mMotorControllerNormalCallbacks;
    std::map<std::string, SlottedMotorCallback> mMotorControllerSlottedCallbacks;
};
