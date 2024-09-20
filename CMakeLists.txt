cmake_minimum_required (VERSION 2.8.12)

project ("starjson")

if (CMAKE_HOST_WIN32)
  set(PLATFORM_KEY "win32")
elseif(CMAKE_HOST_APPLE)
  set(PLATFORM_KEY "darwin")
else()
  set(PLATFORM_KEY "linux")
endif()

if (NOT CMAKE_HOST_WIN32)
  list(APPEND CMAKE_CXX_FLAGS "-std=c++11")
endif()

find_package (JNI REQUIRED)
include_directories(${JNI_INCLUDE_DIRS})

aux_source_directory(src/native/core/src CORE_SOURCES)

add_library(
  ${PROJECT_NAME}
  SHARED
  src/native/star-json-jni.cc ${CORE_SOURCES}
)
target_link_libraries(
  ${PROJECT_NAME}
  PRIVATE
  ${JNI_LIBRARIES}
)

set(NATIVE_RESOURCES_DIR "${PROJECT_SOURCE_DIR}/src/main/resources/native/starjson/${PLATFORM_KEY}/")
add_custom_command(
  TARGET ${PROJECT_NAME} POST_BUILD
  COMMAND ${CMAKE_COMMAND} -E make_directory "${NATIVE_RESOURCES_DIR}"
  COMMAND ${CMAKE_COMMAND} -E copy "$<TARGET_FILE:${PROJECT_NAME}>" "${NATIVE_RESOURCES_DIR}"
)
