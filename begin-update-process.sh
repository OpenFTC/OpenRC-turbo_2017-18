#!/bin/bash
echo "If you are running this from Windows (WSL), make sure to turn on Auto CRLF:"
echo "git config --global core.autocrlf true"
echo
echo "This script expects a remote to be set up called \"first\""

pause () {
	read -p "Press Enter to continue"
}

pause

git pull first master

extract_source () {
	rm -rf $1/src/main/java/*
	unzip -q libs/$1-release-sources.jar -x "META-INF/*" -d $1/src/main/java/
	git rm libs/$1-release-sources.jar
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
	mv -f jni/* ../libs/armeabi-v7a
	
	rm -rf ../$1/libs
	mv libs ../$1/
	
	rm -rf ../$1/src/main/res
	mv res ../$1/src/main
	
	cd ..
	rm -rf $1-tmp
	git rm libs/$1-release.aar
}

extract () {
	extract_source $1
	extract_aar $1
	git add $1/*
}

extract Blocks

extract FtcCommon

extract Hardware

extract Inspection

extract RobotCore

git rm doc/apk/FtcRobotController-release.apk

echo "==============================================================================="
echo "Don't forget to move the version for every library from its manifest to Gradle."
echo "==============================================================================="