# syntax=docker/dockerfile:1.4

ARG UBUNTU_VERSION=22.04
ARG OSXCROSS_VERSION=14.5
ARG DOCKCROSS_VERSION=latest

FROM crazymax/osxcross:${OSXCROSS_VERSION}-ubuntu AS osxcross

FROM ubuntu:${UBUNTU_VERSION} AS base
WORKDIR /work
RUN sed -i_bak 's/archive.ubuntu.com/mirrors.tuna.tsinghua.edu.cn/g' /etc/apt/sources.list
RUN sed -i_bak 's/security.ubuntu.com/mirrors.tuna.tsinghua.edu.cn/g' /etc/apt/sources.list
RUN apt-get update && apt-get install -y cmake clang lld libc6-dev

FROM base as build-linux
COPY . /work
RUN <<EOF
    cd /work
    cmake -B build .
    cmake --build build --config Release
EOF

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

FROM dockcross/windows-static-x64:${DOCKCROSS_VERSION} as build-windows
COPY . /work
RUN <<EOF
    cd /work
    cmake -B build .
    cmake --build build --config Release
EOF

FROM ubuntu:${UBUNTU_VERSION} AS release
WORKDIR /work
COPY --from=build-drawin /work/src/main/resources/native /work/
COPY --from=build-windows /work/src/main/resources/native /work/
COPY --from=build-linux /work/src/main/resources/native /work/

