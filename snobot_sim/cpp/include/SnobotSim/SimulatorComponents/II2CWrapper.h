/*
 * II2CWrapper.h
 *
 *  Created on: Jun 22, 2017
 *      Author: preiniger
 */

#ifndef II2CWRAPPER_H_
#define II2CWRAPPER_H_

#include <stdint.h>
#include "SnobotSim/ExportHelper.h"

class II2CWrapper
{
public:

    virtual ~II2CWrapper() {}

    virtual int32_t Transaction(
            uint8_t* dataToSend, int32_t sendSize,
            uint8_t* dataReceived, int32_t receiveSize) = 0;
};

class EXPORT_ NullI2CWrapper : public II2CWrapper
{
public:
    virtual int Transaction(
            uint8_t* dataToSend, int32_t sendSize,
            uint8_t* dataReceived, int32_t receiveSize);
};



#endif /* II2CWRAPPER_H_ */
