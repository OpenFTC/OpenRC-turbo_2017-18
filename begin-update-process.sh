#!/bin/bash

# FUNCTION DEFINITIONS

pause () {
	read -p "Press Enter to continue"
}

extract_source () {
	rm -rf $1/src/main/java/*
	unzip -q libs/$1-release-sources.jar -x "META-INF/*" -d $1/src/main/java/
}

extract_aar () {
	unzip -q libs/$1-release.aar -d $1-tmp
	cd $1-tmp

	mv -f AndroidManifest.xml ../$1/src/main/AndroidManifest.xml
	rm -rf ../$1/src/main/aidl
	mv aidl ../$1/src/main

	rm -rf ../$1/src/main/assets
	mv assets ../$1/src/main

	# Needs more testing
	rm -rf ../$1/src/main/jniLibs
	mv jni/* ../$1/src/main/jniLibs

	rm -rf ../$1/libs
	mv libs ../$1/

	rm -rf ../$1/src/main/res
	mv res ../$1/src/main

	cd ..
	rm -rf $1-tmp
}

extract () {
	extract_source $1
	extract_aar $1
	git add $1/*
}

# SCRIPT EXECUTION
echo
echo "This script is intended to be used by the primary maintainer of OpenRC ONLY."
echo "Press Control-C to exit if you are just an OpenRC user."
echo
echo "This script expects a remote to be set up called \"first\" that points to the official repo."
echo
echo "Since Android Studio can always read Unix line endings, I recommend setting Auto CRLF to input mode,"
echo "regardless of your operating system:"
echo "git config --local core.autocrlf input"
echo
echo "However, make sure that you don't commit Unix line endings for the gradlew.bat file."
echo
pause

# Switch to script location
cd "${0%/*}" # https://stackoverflow.com/a/16349776/4651874

git pull first beta -X theirs

extract Blocks

extract FtcCommon

extract Hardware

extract Inspection

extract RobotCore

# Needs more testing
cp -f libs/armeabi-v7a/libVuforia.so doc/
git add doc/libVuforia.so
mv -f libs/armeabi-v7a/libVuforia.so FtcRobotController/src/stock/jniLibs/armeabi-v7a/
git add FtcRobotController/src/stock/jniLibs/armeabi-v7a/libVuforia.so

git rm -f doc/apk/FtcRobotController-release.apk
echo
echo "==============================================================================="
echo "              Checklist of stuff to do after this script finishes.             "
echo "==============================================================================="
echo
echo "1. Remove the version code and name for each library from its manifest"
echo "2. Update the version code and name for each library in its build.gradle file"
echo "3. Run Android Studio's auto-formatting tool on all .java and .xml files \(use the filter\)"
echo