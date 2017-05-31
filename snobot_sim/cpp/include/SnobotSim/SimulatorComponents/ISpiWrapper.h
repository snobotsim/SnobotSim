/*
 * ISpiWrapper.hpp
 *
 *  Created on: May 7, 2017
 *      Author: PJ
 */

#ifndef ISPIWRAPPER_HPP_
#define ISPIWRAPPER_HPP_


class ISpiWrapper
{
public:

    virtual double GetAccumulatorValue() = 0;
    virtual void ResetAccumulatorValue() = 0;
};



#endif /* ISPIWRAPPER_HPP_ */
