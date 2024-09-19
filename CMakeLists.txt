cmake_minimum_required (VERSION 2.8.12)

project ("starjson")

if (CMAKE_HOST_WIN32)
  set(WINDOWS 1)
  set(PLATFORM_PATH "/win32")
  set(NATIVE_BINARY_FILE "${PROJECT_NAME}.dll")
elseif(CMAKE_HOST_APPLE)
  set(MACOS 1)
  set(PLATFORM_PATH "/darwin")
  set(NATIVE_BINARY_FILE "lib${PROJECT_NAME}.dylib")
else()
  set(LINUX 1)
  set(PLATFORM_PATH "/linux")
  set(NATIVE_BINARY_FILE "lib${PROJECT_NAME}.so")
endif()

set(JNI_FIND_COMPONENTS JVM)
find_package (JNI REQUIRED)
include_directories(${JNI_INCLUDE_DIRS})

aux_source_directory(src/main/cpp/core CORE_SOURCES)

add_library(
  ${PROJECT_NAME}
  SHARED
  src/main/cpp/star-json-jni.cpp ${CORE_SOURCES}
)
target_link_libraries(
  ${PROJECT_NAME}
  PRIVATE
  ${JNI_LIBRARIES}
)

add_custom_command(
  TARGET ${PROJECT_NAME} POST_BUILD
  COMMAND ${CMAKE_COMMAND} -E copy_if_different
  "${PROJECT_BINARY_DIR}/$<$<CONFIG:Release>:Release/>$<$<CONFIG:Debug>:Debug/>${NATIVE_BINARY_FILE}"
  "${PROJECT_SOURCE_DIR}/src/main/resources/native/starjson${PLATFORM_PATH}/"
)
