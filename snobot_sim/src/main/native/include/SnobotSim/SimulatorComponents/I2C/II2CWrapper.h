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
};

class EXPORT_ NullI2CWrapper : public II2CWrapper
{

};


#endif /* II2CWRAPPER_H_ */
