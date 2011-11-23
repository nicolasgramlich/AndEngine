#! /bin/sh

NDK_DIRECTORY="/Users/ngramlich/SDKs/Android/ndk/r6b/"
PROJECT_DIRECTORY="/Users/ngramlich/Workspace/ZooMobileAndroidClient/AndEngine/"

# Run build:
cd ${PROJECT_DIRECTORY}
${NDK_DIRECTORY}ndk-build

# Clean temporary files:
rm -rf ${PROJECT_DIRECTORY}obj
find . -name gdbserver -print0 | xargs -0 rm -rf
find . -name gdb.setup -print0 | xargs -0 rm -rf
