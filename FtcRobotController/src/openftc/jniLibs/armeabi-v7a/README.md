# OpenFTC native libraries

### libVuforia.so

This is NOT the real Vuforia library. It's just a tiny placeholder to
give the proprietary Vuforia jar something to load. We'll load the real
libVuforia.so (located in doc/libVuforia.so) from the device's storage.
It will be pushed to the SD card during deployment if it's not there
already, and from there it will be copied to the app's protected storage
 on first launch.