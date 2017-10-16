/*
 * Copyright (c) 2016 Craig MacFarlane
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * (subject to the limitations in the disclaimer below) provided that the following conditions are
 * met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 * and the following disclaimer in the documentation and/or other materials provided with the
 * distribution.
 *
 * Neither the name of Craig MacFarlane nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS LICENSE. THIS
 * SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.robotcore.internal.opmode;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.qualcomm.robotcore.util.RobotLog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dalvik.system.DexFile;
@SuppressWarnings("WeakerAccess")
public class InstantRunDexHelper {

    public static final String TAG = "InstantRunDexHelper";

    private static final String INSTANT_RUN_LOCATION = "files" + File.separator + "instant-run" + File.separator + "dex";

    /**
     * Find all the classes in the base apk AND any instant run dex files (for Marshmallow+ users)
     *
     * @param context the application context
     * @return A list of class names
     */
    public static List<String> getAllClassNames(Context context)
    {
        ApplicationInfo applicationInfo = null;
        List<String> classNames = new ArrayList<String>();

        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            RobotLog.ee(TAG, e, "Could not obtain application info for class scanning");
            return classNames;
        }

        File[] instantRunDexes = new File(applicationInfo.dataDir, INSTANT_RUN_LOCATION).listFiles();
        if (instantRunDexes != null) {
            for (File f : instantRunDexes) {
                try {
                    /*
                     * We're not certain that *only* dex files are stored in this directory,
                     * but try them all, anyway, just in case.
                     */
                    DexFile dexFile = new DexFile(f.getAbsolutePath());
                    try {
                        classNames.addAll(Collections.list(dexFile.entries()));
                    } finally {
                        dexFile.close();
                    }
                /*
                 * Catch, log, but don't rethrow as we might as well not short-circuit the list of paths.
                 */
                } catch (IOException e) {
                    RobotLog.ee(TAG, e, "Error loading dex file: " + f.toString() + ", IOException: " + e.toString());
                }
            }
        }
        return classNames;
    }
}
