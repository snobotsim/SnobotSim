/*
 * CompressorWrapper.h
 *
 *  Created on: Jun 22, 2017
 *      Author: preiniger
 */

#ifndef COMPRESSORWRAPPER_H_
#define COMPRESSORWRAPPER_H_

#include "SnobotSim/ExportHelper.h"

class EXPORT_ CompressorWrapper
{
public:
    CompressorWrapper();
    virtual ~CompressorWrapper();

    bool IsPressureSwitchFull();
};

#endif /* COMPRESSORWRAPPER_H_ */
