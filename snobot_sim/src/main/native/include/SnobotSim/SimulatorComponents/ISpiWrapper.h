/*
 * ISpiWrapper.hpp
 *
 *  Created on: May 7, 2017
 *      Author: PJ
 */

#ifndef ISPIWRAPPER_HPP_
#define ISPIWRAPPER_HPP_

#include <stdint.h>
#include "SnobotSim/ExportHelper.h"

class ISpiWrapper
{
public:

    virtual double GetAccumulatorValue() = 0;
    virtual void ResetAccumulatorValue() = 0;

    virtual int32_t Read(uint8_t* buffer, int32_t count) = 0;
    virtual void Write(uint8_t* dataToSend, int32_t sendSize) = 0;
};


class EXPORT_ NullSpiWrapper : public ISpiWrapper
{
public:

    virtual double GetAccumulatorValue() override;
    virtual void ResetAccumulatorValue() override;

    virtual int32_t Read(uint8_t* buffer, int32_t count) override;
    virtual void Write(uint8_t* dataToSend, int32_t sendSize) override;
};


#endif /* ISPIWRAPPER_HPP_ */
