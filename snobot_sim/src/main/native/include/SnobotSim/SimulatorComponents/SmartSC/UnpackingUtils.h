
#include "SnobotSim/Logging/SnobotLogger.h"

namespace SnobotSim
{


template <typename Type>
Type Extract(uint8_t* buffer, size_t& aBufferPos)
{
    Type output;
    std::memcpy(&output, &buffer[aBufferPos], sizeof(Type));
    aBufferPos += sizeof(Type);

    return output;
}

template <typename Type>
void Write(uint8_t* buffer, size_t& aBufferPos, const Type& value)
{
    std::memcpy(&buffer[aBufferPos], &value, sizeof(Type));
    aBufferPos += sizeof(Type);
}

// TODO make more elegant
template <typename T0>
void WriteData(const std::string& name, uint8_t* aBuffer, int aLength, const T0& v0)
{
    constexpr int expected_size = sizeof(T0);
    if (expected_size != aLength)
    {
        SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Incorrect size, for '" << name << "' expected " << expected_size << ", got " << aLength);
    }

    size_t bufferPos = 0;
    Write(aBuffer, bufferPos, v0);
}

template <typename T0, typename T1>
void WriteData(const std::string& name, uint8_t* aBuffer, int aLength, const T0& v0, const T1& v1)
{
    constexpr int expected_size = sizeof(T0) + sizeof(T1);
    if (expected_size != aLength)
    {
        SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Incorrect size, for '" << name << "' expected " << expected_size << ", got " << aLength);
    }

    size_t bufferPos = 0;
    Write(aBuffer, bufferPos, v0);
    Write(aBuffer, bufferPos, v1);
}

template <typename T0, typename T1, typename T2>
void WriteData(const std::string& name, uint8_t* aBuffer, int aLength, const T0& v0, const T1& v1, const T2& v2)
{
    constexpr int expected_size = sizeof(T0) + sizeof(T1) + sizeof(T2);
    if (expected_size != aLength)
    {
        SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Incorrect size, for '" << name << "' expected " << expected_size << ", got " << aLength);
    }

    size_t bufferPos = 0;
    Write(aBuffer, bufferPos, v0);
    Write(aBuffer, bufferPos, v1);
    Write(aBuffer, bufferPos, v2);
}

// TODO make more elegant
template <typename T0>
std::tuple<T0> ExtractData(uint8_t* buffer, int aLength)
{
    constexpr int expected_size = sizeof(T0);
    if (expected_size != aLength)
    {
        SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Incorrect size, expected " << expected_size << ", got " << aLength);
    }

    size_t bufferPos = 0;
    T0 t0 = Extract<T0>(buffer, bufferPos);

    return std::make_tuple(t0);
}

template <typename T0, typename T1>
std::tuple<T0, T1> ExtractData(uint8_t* buffer, int aLength)
{
    constexpr int expected_size = sizeof(T0) + sizeof(T1);
    if (expected_size != aLength)
    {
        SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Incorrect size, expected " << expected_size << ", got " << aLength);
    }

    size_t bufferPos = 0;
    T0 t0 = Extract<T0>(buffer, bufferPos);
    T1 t1 = Extract<T1>(buffer, bufferPos);

    return std::make_tuple(t0, t1);
}

template <typename T0, typename T1, typename T2>
std::tuple<T0, T1, T2> ExtractData(uint8_t* buffer, int aLength)
{
    constexpr int expected_size = sizeof(T0) + sizeof(T1) + sizeof(T2);
    if (expected_size != aLength)
    {
        SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Incorrect size, expected " << expected_size << ", got " << aLength);
    }

    size_t bufferPos = 0;
    T0 t0 = Extract<T0>(buffer, bufferPos);
    T1 t1 = Extract<T1>(buffer, bufferPos);
    T2 t2 = Extract<T2>(buffer, bufferPos);

    return std::make_tuple(t0, t1, t2);
}

template <typename T0, typename T1, typename T2, typename T3>
std::tuple<T0, T1, T2, T3> ExtractData(uint8_t* buffer, int aLength)
{
    constexpr int expected_size = sizeof(T0) + sizeof(T1) + sizeof(T2) + sizeof(T3);
    if (expected_size != aLength)
    {
        SNOBOT_LOG(SnobotLogging::LOG_LEVEL_CRITICAL, "Incorrect size, expected " << expected_size << ", got " << aLength);
    }

    size_t bufferPos = 0;
    T0 t0 = Extract<T0>(buffer, bufferPos);
    T1 t1 = Extract<T1>(buffer, bufferPos);
    T2 t2 = Extract<T2>(buffer, bufferPos);
    T3 t3 = Extract<T3>(buffer, bufferPos);

    return std::make_tuple(t0, t1, t2, t3);
}

}