/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2016-2017. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

#pragma once

// Allows usage with std::lock_guard without including <mutex> separately
#include <mutex>

// We do not want to use pthreads if in the simulator; however, in the
// simulator, we do not care about priority inversion.
typedef std::mutex priority_mutex;
typedef std::recursive_mutex priority_recursive_mutex;
