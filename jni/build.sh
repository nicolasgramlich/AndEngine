#!/bin/bash

NDK_DIRECTORY="/Users/ngramlich/Applications/android/ndk/r8e/"

SCRIPT_DIR="$( cd "$( dirname "${0}" )" && pwd )"

PROJECT_DIRECTORY=${SCRIPT_DIR}/../

# Run build:
pushd ${PROJECT_DIRECTORY} > /dev/null
${NDK_DIRECTORY}ndk-build -j8

# Clean temporary files:
rm -rf ${PROJECT_DIRECTORY}obj
find . -name gdbserver -print0 | xargs -0 rm -rf
find . -name gdb.setup -print0 | xargs -0 rm -rf

popd > /dev/null