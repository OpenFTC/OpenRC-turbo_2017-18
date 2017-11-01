package org.openftc;

import android.content.pm.PackageManager;

public class Utils
{
    public static boolean isFtcDriverStationInstalled(PackageManager packageManager)
    {
        try
        {
            packageManager.getPackageInfo("com.qualcomm.ftcdriverstation", 0);
            return true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }
}
