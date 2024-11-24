# syntax=docker/dockerfile:1.4

ARG UBUNTU_VERSION=22.04
ARG OSXCROSS_VERSION=14.5

FROM crazymax/osxcross:${OSXCROSS_VERSION}-ubuntu AS osxcross

FROM ubuntu:${UBUNTU_VERSION} AS base
RUN sed -i_bak 's/archive.ubuntu.com/mirrors.tuna.tsinghua.edu.cn/g' /etc/apt/sources.list
RUN sed -i_bak 's/security.ubuntu.com/mirrors.tuna.tsinghua.edu.cn/g' /etc/apt/sources.list
RUN apt-get update && apt-get install -y cmake clang lld libc6-dev
WORKDIR /work

FROM base as build-drawin
COPY . /work
ENV PATH="/osxcross/bin:$PATH"
ENV LD_LIBRARY_PATH="/osxcross/lib:$LD_LIBRARY_PATH"
RUN --mount=type=bind,from=osxcross,source=/osxcross,target=/osxcross \
    <<EOF
    cd /work
    x86_64-apple-darwin23.6-cmake -B build .
    x86_64-apple-darwin23.6-cmake --build build --config Release
    rm -fr /work/build
    aarch64-apple-darwin23.6-cmake -B build .
    aarch64-apple-darwin23.6-cmake --build build --config Release
    rm -fr /work/build
EOF
