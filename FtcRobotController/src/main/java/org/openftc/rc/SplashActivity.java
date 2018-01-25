
/*
 * Copyright (c) 2017 FTC team 4634 FROGbots
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.openftc.rc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.cyanogenmod.updater.utils.MD5;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.openftc.rc.exceptions.VuforiaCorruptedException;
import org.openftc.rc.exceptions.VuforiaNotFoundException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class SplashActivity extends Activity
{
    File libInProtectedStorage;
    File protectedExtraFolder;
    File libOnSdcard;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            /*
             * Attempt to set up the Vuforai library for loading in
             * the next statement
             */
            setupVuforiaFilesForLaunch();

            /*
             * We've been given the go-ahead! Load up libVuforiaReal.so
             */
            System.load(libInProtectedStorage.getAbsolutePath());

            /*
             * Start loading the main activity. After it's loaded, close
             * down the splash screen
             */
            Intent intent = new Intent(this, FtcRobotControllerActivity.class);
            startActivity(intent);
            finish();
        }
        catch (VuforiaNotFoundException e)
        {
            e.printStackTrace();
            showLibNotOnSdcardDialog();
        }
        catch (VuforiaCorruptedException e)
        {
            e.printStackTrace();
            showLibCorruptedDialog();
        }
    }

    private void setupVuforiaFilesForLaunch() throws VuforiaNotFoundException, VuforiaCorruptedException
    {
        libInProtectedStorage = new File(getFilesDir() + "/extra/libVuforiaReal.so");
        protectedExtraFolder = new File(getFilesDir() + "/extra/");
        libOnSdcard = new File(Environment.getExternalStorageDirectory() + "/FIRST/libVuforia.so");

        /*
         * First, check to see if it exists in the protected storage.
         * No need to check the MD5 hash since it would be have been
         * checked prior to being copied in here
         */
        if(!libInProtectedStorage.exists())
        {
            /*
             * Ok, so it's not in the protected storage. Check if it exists
             * in the FIRST folder on the SDcard
             */
            if(libOnSdcard.exists())
            {
                /*
                 * Yup, it exists, but we need to verify the integrity of the file
                 * with the MD5 hash before we copy it, otherwise bad things might
                 * happen when we try to load a corrupted lib...
                 */
                if(MD5.checkMD5("0f79da7f3a0c10c68978f823e02bffdf", libOnSdcard))
                {
                    /*
                     * Alright, everything checks out, so copy it to the protected
                     * storage and continue with the app launch!
                     */
                    copyLibFromSdcardToProtectedStorage();
                }
                else
                {
                    /*
                     * Oooh, not good - it's corrupted.
                     * Show the user a dialog explaining the situation
                     */
                    throw new VuforiaCorruptedException();
                }
            }
            else
            {
                /*
                 * Welp, it doesn't exist on the SDcard either :(
                 * Show the user a dialog explaining the situation
                 */
                throw new VuforiaNotFoundException();
            }
        }
    }

    private void showLibNotOnSdcardDialog()
    {
        String msg = "libVuforia.so should have been copied to the phone the first time you deployed " +
                "this app from Android Studio.<br><br>" +

                "Re-deploying the app through Android Studio should fix the issue. Make sure to only " +
                "have a single Android device connected, or the file will fail to copy.<br><br>" +

                "If that doesn't work or isn't an option, you can copy libVuforia.so to the FIRST folder" +
                " on the phone's storage yourself.<br><br>" +

                "<a href='https://github.com/OpenFTC/OpenFTC-app-turbo/blob/5b706b920252ed810038849e4c52c7c5c89e56ef/doc/libVuforia.so?raw=true'>" +
                "You can find it in the 'doc' folder of the OpenRC repository</a>.";

        AlertDialog dialog = new AlertDialog.Builder(this)
        .setTitle("libVuforia.so not found!")
        .setMessage(Html.fromHtml(msg))
        .setCancelable(false)
        .setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                finish();
            }
        }).create();
        dialog.show();
        ((TextView)dialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void showLibCorruptedDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("libVuforia.so corrupted!");
        builder.setMessage("libVuforia.so is present in the FIRST folder. However, the MD5 " +
                "checksum does not match what is expected. Delete and re-download the file.");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                finish();
            }
        });
        builder.show();
    }

    private void copyLibFromSdcardToProtectedStorage()
    {
        /*
         * Check if the 'extra' folder exists. If it doesn't,
         * then create it now or else the copy code will crash
         */
        if(!protectedExtraFolder.exists())
        {
            protectedExtraFolder.mkdir();
        }

        try
        {
            /*
             * Copy the file with a 1MiB buffer
             */
            InputStream is = new FileInputStream(libOnSdcard);
            OutputStream os = new FileOutputStream(libInProtectedStorage);
            byte[] buff = new byte[1024];
            int len;
            while ((len = is.read(buff)) > 0)
            {
                os.write(buff, 0, len);
            }
            is.close();
            os.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
