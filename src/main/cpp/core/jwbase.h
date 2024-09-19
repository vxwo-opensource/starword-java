#pragma once

#include <cstdint>

#if defined(_MSC_VER)
#include <BaseTsd.h>
typedef SSIZE_T ssize_t;
#else
#include <sys/types.h>
#endif

typedef uint16_t JWChar;
typedef JWChar *JWCharBuffer;

#ifdef NDEBUG
#define debug_output(...)
#else
extern "C" void debug_output(const char *format, ...);
#endif
