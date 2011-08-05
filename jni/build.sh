#! /bin/sh

NDK_DIRECTORY="/Users/ngramlich/SDKs/Android/ndk/r6/"
PROJECT_DIRECTORY="/Users/ngramlich/Workspace/AndEngine/"

# Run build:
cd ${PROJECT_DIRECTORY}
${NDK_DIRECTORY}ndk-build

# Clean temporary files:
rm -rf ${PROJECT_DIRECTORY}obj