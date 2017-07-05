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
};


class EXPORT_ NullSpiWrapper : public ISpiWrapper
{
public:

    virtual double GetAccumulatorValue();
    virtual void ResetAccumulatorValue();

    virtual int32_t Read(uint8_t* buffer, int32_t count);
};


#endif /* ISPIWRAPPER_HPP_ */
