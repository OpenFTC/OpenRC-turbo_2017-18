[![Discord](https://img.shields.io/discord/377144270034829324.svg?style=for-the-badge)](https://discord.gg/ameFTnC)

# OpenFTC-app-turbo is to be used at your own risk.
##### While every change is made with care not to break anything, FIRST has NOT tested our modifications.
##### Volunteers at competitions may be less willing to assist you if you use OpenFTC. Our community is very helpful though, so please create an issue or talk to us on Discord if you encounter any problems.

[Sign up for our mailing list](https://goo.gl/forms/xFBIx0Ptk3Br7ZOD2) for notifications about updates and upcoming new projects. 

[Join us on Discord](https://discord.gg/Q3CgrxU) for real-time support or to ask questions.

OpenFTC-app is currently based on version 3.5 of the SDK. Do not attempt to manually update beyond that yourself. OpenFTC will be updated within a week of new official app releases.

The OpenFTC documentation is still under construction.

---

OpenFTC-app-turbo removes the OnBotJava and Blocks programming systems for teams that don't use them, speeding up deploy time drastically. If you use these systems, please see [OpenFTC-app](https://github.com/OpenFTC/OpenFTC-app).

The OpenFTC family of Robot Controller apps are based on the official
[FTC SDK](https://github.com/ftctechnh/ftc_app) (Software Development Kit), but the AAR files it uses have been converted to modules in the Android Studio project. This makes it easy to see and modify almost the entirety of the SDK's source code. In addition, the history in Git shows all changes that have been made to the core code since OpenFTC's inception. This is a very useful supplement to the changelogs that FIRST provides - teams can see exactly what code has been changed and how it will affect them.

This system allows pull requests and enhancements to the code of the entire SDK, and can allow teams to understand the structure and functionality of the whole system. Most enhancements will likely be accepted as long as they _do not force teams to change their workflow._ Changes made in the OpenFTC SDK should allow teams to move from the official SDK to OpenFTC-app with no code changes required.

To request a new feature, you can open an issue on this repository. If there's a large enough call for the feature, it's very likely to be added to the list for a future release. 

# Release Notes
To see the release notes for FIRST's releases of this SDK, see [doc/FIRST_CHANGELOG.md](doc/FIRST_CHANGELOG.md)

## 1.1
* Updated the app to be based on version 3.5 of the official SDK.

## Pre-1.1
* Extracted the aar files and associated source jars into full, easily changed source code modules.
* Warn user if the driver station app is installed on the device
* Fixed bug with log viewer colorization
* Add option that allows the user to choose whether lines should wrap in the log viewer (they don't by default now)
* Changed the icon to make the app more easily distinguishable from the official one
* Added the OpenFTC version to the main screen
* Added "About OpenFTC" button to the about screen
* Allowed Gradle to use more RAM (up to 2GB) for faster build times
##### &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Turbo-specific changes
* Initial Turbo release that removes the Blocks and OnBotJava features


## Welcome!
This GitHub repository contains the source code that is used to build an Android app to control a *FIRST* Tech Challenge competition robot.  To use this SDK, download/clone the entire project to your local computer.

If you are new to the *FIRST* Tech Challenge software and control system, you should visit the online wiki to learn how to install, configure, and use the software and control system:

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;https://github.com/ftctechnh/ftc_app/wiki

Note that the wiki is an "evergreen" document that is constantly being updated and edited.  It contains the most current information about the *FIRST* Tech Challenge software and control system.

## Downloading the Project
It is important to note that this repository is large and can take a long time and use a lot of space to download. If you would like to save time and space, there are some options that you can choose to download only the most current version of the Android project folder:

* If you are a git user, *FIRST* recommends that you use the --depth command line argument to only clone the most current version of the repository:

<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;git clone --depth=1 https://github.com/ftctechnh/ftc_app.git</p>

* Or, if you prefer, you can use the "Download Zip" button available through the main repository page.  Downloading the project as a .ZIP file will keep the size of the download manageable.

* You can also download the project folder (as a .zip or .tar.gz archive file) from the Downloads subsection of the Releases page for this repository.

Once you have downloaded and uncompressed (if needed) your folder, you can use Android Studio to import the folder  ("Import project (Eclipse ADT, Gradle, etc.)").

## Getting Help
### User Documentation and Tutorials
*FIRST* maintains an online wiki with information and tutorials on how to use the *FIRST* Tech Challenge software and robot control system.  You can access the wiki at the following address:

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;https://github.com/ftctechnh/ftc_app/wiki

### Javadoc Reference Material
The Javadoc reference documentation for the FTC SDK is now available online.  Visit the following URL to view the FTC SDK documentation as a live website:

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;http://ftctechnh.github.io/ftc_app/doc/javadoc/index.html    

Documentation for the FTC SDK is also included with this repository.  There is a subfolder called "doc" which contains several subfolders:

 * The folder "apk" contains the .apk files for the FTC Driver Station and FTC Robot Controller apps.
 * The folder "javadoc" contains the JavaDoc user documentation for the FTC SDK.

### Online User Forum
For technical questions regarding the SDK, please visit the FTC Technology forum:

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;http://ftcforum.usfirst.org/forumdisplay.php?156-FTC-Technology
