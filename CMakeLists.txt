cmake_minimum_required (VERSION 3.11)

project ("starword-java" LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 11)

if(MSVC)
  add_compile_options("/utf-8")
endif()

if (CMAKE_HOST_WIN32)
  set(OS_KEY "win32")
elseif(CMAKE_HOST_APPLE)
  set(OS_KEY "darwin")
  list(APPEND CMAKE_CXX_FLAGS "-std=c++11")
else()
  set(OS_KEY "linux")
  list(APPEND CMAKE_CXX_FLAGS "-static-libstdc++")
endif()
string(TOLOWER "${CMAKE_HOST_SYSTEM_PROCESSOR}" ARCH_KEY)
string(REPLACE "amd64" "x86_64" ARCH_KEY "${ARCH_KEY}")

if (CMAKE_HOST_WIN32)
  set(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} /MT /DNDEBUG")
else()
  set(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} -dNDEBUG")
endif()

find_package (JNI REQUIRED)
include_directories(${JNI_INCLUDE_DIRS})

aux_source_directory(jni/core/src CORE_SOURCES)

add_library(
  ${PROJECT_NAME}
  SHARED
  jni/star-keyword-jni.cc ${CORE_SOURCES}
)
target_link_libraries(
  ${PROJECT_NAME}
  PRIVATE
  ${JNI_LIBRARIES}
)

set(NATIVE_RESOURCES_DIR "${PROJECT_SOURCE_DIR}/src/main/resources/jni/${OS_KEY}/${ARCH_KEY}")
add_custom_command(
  TARGET ${PROJECT_NAME} POST_BUILD
  COMMAND ${CMAKE_COMMAND} -E make_directory "${NATIVE_RESOURCES_DIR}"
  COMMAND ${CMAKE_COMMAND} -E copy "$<TARGET_FILE:${PROJECT_NAME}>" "${NATIVE_RESOURCES_DIR}"
)
