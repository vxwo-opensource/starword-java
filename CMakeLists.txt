cmake_minimum_required (VERSION 3.11)

project ("starword-java" LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 11)

if(MSVC)
  add_compile_options("/utf-8")
endif()

if (CMAKE_SYSTEM_NAME MATCHES "Windows")
  set(OS_KEY "win32")
elseif(CMAKE_SYSTEM_NAME MATCHES "Darwin")
  set(OS_KEY "darwin")
else()
  set(OS_KEY "linux")
  list(APPEND CMAKE_CXX_FLAGS "-static-libstdc++")
endif()
string(TOLOWER "${CMAKE_SYSTEM_PROCESSOR}" ARCH_KEY)
string(REPLACE "amd64" "x86_64" ARCH_KEY "${ARCH_KEY}")

if (MSVC)
  set(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} /MT")
endif()

include_directories(
  src/native/include
  src/native/include/${OS_KEY}
)
aux_source_directory(src/native/core/src CORE_SOURCES)

add_library(
  ${PROJECT_NAME}
  SHARED
  src/native/starword-jni.cc ${CORE_SOURCES}
)
target_link_libraries(
  ${PROJECT_NAME}
  PRIVATE
  ${JNI_LIBRARIES}
)

set(NATIVE_RESOURCES_DIR "${PROJECT_SOURCE_DIR}/src/main/resources/native/${OS_KEY}-${ARCH_KEY}")
add_custom_command(
  TARGET ${PROJECT_NAME} POST_BUILD
  COMMAND ${CMAKE_COMMAND} -E make_directory "${NATIVE_RESOURCES_DIR}"
  COMMAND ${CMAKE_COMMAND} -E copy "$<TARGET_FILE:${PROJECT_NAME}>" "${NATIVE_RESOURCES_DIR}"
)
