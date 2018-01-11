#!/bin/bash

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
	
	# Currently untested
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

echo "If you are running this from Windows (WSL), make sure to turn on Auto CRLF:"
echo "git config --global core.autocrlf true"
echo
echo "This script expects a remote to be set up called \"first\""

pause

# Switch to script location
cd "${0%/*}" # https://stackoverflow.com/a/16349776/4651874

git pull first beta

extract Blocks

extract FtcCommon

extract Hardware

extract Inspection

extract RobotCore

# Currently untested
cp -f libs/armeabi-v7a/libVuforia.so doc/
mv -f libs/armeabi-v7a/libVuforia.so FtcRobotController/src/stock/jniLibs/armeabi-v7a/

git rm doc/apk/FtcRobotController-release.apk

echo "==============================================================================="
echo "Don't forget to move the version for every library from its manifest to Gradle."
echo "==============================================================================="