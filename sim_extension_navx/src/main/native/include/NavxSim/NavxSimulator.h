/*
 * NavxSimulator.h
 *
 *  Created on: Jul 8, 2017
 *      Author: PJ
 */

#ifndef NAVXSIMULATOR_H_
#define NAVXSIMULATOR_H_

#include <atomic>
#include <memory>
#include <cstring>

#include <support/mutex.h>

#include "MockData/NotifyListenerVector.h"

class NavxSimulator
{
public:
    NavxSimulator();
    virtual ~NavxSimulator();

    virtual void ResetData();

    int32_t RegisterXCallback(HAL_NotifyCallback callback, void* param,
                              HAL_Bool initialNotify);
    void CancelXCallback(int32_t uid);
    void InvokeXCallback(HAL_Value value);
    double GetX();
    void SetX(double x);

    int32_t RegisterYCallback(HAL_NotifyCallback callback, void* param,
                              HAL_Bool initialNotify);
    void CancelYCallback(int32_t uid);
    void InvokeYCallback(HAL_Value value);
    double GetY();
    void SetY(double y);

    int32_t RegisterZCallback(HAL_NotifyCallback callback, void* param,
                              HAL_Bool initialNotify);
    void CancelZCallback(int32_t uid);
    void InvokeZCallback(HAL_Value value);
    double GetZ();
    void SetZ(double z);

    int32_t RegisterYawCallback(HAL_NotifyCallback callback, void* param,
                              HAL_Bool initialNotify);
    void CancelYawCallback(int32_t uid);
    void InvokeYawCallback(HAL_Value value);
    double GetYaw();
    void SetYaw(double yaw);

    int32_t RegisterPitchCallback(HAL_NotifyCallback callback, void* param,
                              HAL_Bool initialNotify);
    void CancelPitchCallback(int32_t uid);
    void InvokePitchCallback(HAL_Value value);
    double GetPitch();
    void SetPitch(double pitch);

    int32_t RegisterRollCallback(HAL_NotifyCallback callback, void* param,
                              HAL_Bool initialNotify);
    void CancelRollCallback(int32_t uid);
    void InvokeRollCallback(HAL_Value value);
    double GetRoll();
    void SetRoll(double roll);

protected:

    template <typename Value>
    void PutTheValue(uint8_t* aBuffer, int aPosition, const Value& value, int aSize)
    {
        std::memcpy(&aBuffer[aPosition], &value, aSize);
    }

    template <typename Type>
    Type BoundBetweenNeg180Pos180(Type input)
    {
        Type output = input;
        while (output > 180)
        {
            output -= 360;
        }
        while (output < -180)
        {
            output += 360;
        }

        return output;
    }

    void GetCurrentData(uint8_t* aBuffer, int aFirstAddress);
    void GetWriteConfig(uint8_t* aBuffer);

    std::mutex m_registerMutex;

    std::atomic<double> mX{0.0};
    std::shared_ptr<hal::NotifyListenerVector> mXCallbacks = nullptr;
    std::atomic<double> mY{0.0};
    std::shared_ptr<hal::NotifyListenerVector> mYCallbacks = nullptr;
    std::atomic<double> mZ{0.0};
    std::shared_ptr<hal::NotifyListenerVector> mZCallbacks = nullptr;

    std::atomic<double> mYaw{0.0};
    std::shared_ptr<hal::NotifyListenerVector> mYawCallbacks = nullptr;
    std::atomic<double> mPitch{0.0};
    std::shared_ptr<hal::NotifyListenerVector> mPitchCallbacks = nullptr;
    std::atomic<double> mRoll{0.0};
    std::shared_ptr<hal::NotifyListenerVector> mRollCallbacks = nullptr;
};

#endif /* NAVXSIMULATOR_H_ */
