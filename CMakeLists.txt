cmake_minimum_required (VERSION 2.8.12)

project ("star-json")

if (CMAKE_HOST_WIN32)
  set(WINDOWS 1)
  set(EXT_NAME ".dll")
  set(PLATFORM_PATH "/win32")
elseif(CMAKE_HOST_APPLE)
  set(MACOS 1)
  set(EXT_NAME ".dylib")
  set(PLATFORM_PATH "/darwin")
else()
  set(LINUX 1)
  set(EXT_NAME ".so")
  set(PLATFORM_PATH "/linux")
endif()

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
  "${PROJECT_BINARY_DIR}/$<$<CONFIG:Release>:Release>$<$<CONFIG:Debug>:Debug>/${PROJECT_NAME}${EXT_NAME}"
  "${PROJECT_SOURCE_DIR}/src/main/resources/native/starjson${PLATFORM_PATH}"
)
