# syntax=docker/dockerfile:1.4

FROM crazymax/osxcross:14.5-ubuntu AS osxcross

FROM ubuntu:20.04 as build-linux
WORKDIR /work
COPY . /work
RUN sed -i_bak 's/archive.ubuntu.com/mirrors.tuna.tsinghua.edu.cn/g' /etc/apt/sources.list
RUN sed -i_bak 's/security.ubuntu.com/mirrors.tuna.tsinghua.edu.cn/g' /etc/apt/sources.list
RUN apt-get update && apt-get install -y cmake clang lld libc6-dev
RUN <<EOF
    cd /work
    cmake -B build .
    cmake --build build --config Release
EOF

FROM ubuntu:22.04 as build-drawin
WORKDIR /work
COPY . /work
ENV PATH="/osxcross/bin:$PATH"
ENV LD_LIBRARY_PATH="/osxcross/lib:$LD_LIBRARY_PATH"
RUN sed -i_bak 's/archive.ubuntu.com/mirrors.tuna.tsinghua.edu.cn/g' /etc/apt/sources.list
RUN sed -i_bak 's/security.ubuntu.com/mirrors.tuna.tsinghua.edu.cn/g' /etc/apt/sources.list
RUN apt-get update && apt-get install -y cmake clang lld
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

FROM dockcross/windows-static-x64 as build-windows
WORKDIR /work
COPY . /work
RUN <<EOF
    cd /work
    cmake -B build .
    cmake --build build --config Release
EOF

FROM ubuntu:20.04
WORKDIR /native
COPY --from=build-drawin /work/src/main/resources/native/ /native/
COPY --from=build-windows /work/src/main/resources/native/ /native/
COPY --from=build-linux /work/src/main/resources/native/ /native/
