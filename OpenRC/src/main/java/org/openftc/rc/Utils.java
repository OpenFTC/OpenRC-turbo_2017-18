package org.openftc.rc;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class Utils {
    private static String openRcPreferencesFilename = AppUtil.getDefContext().getString(R.string.openRcPreferencesFile);
    private static SharedPreferences openRcPrefs = AppUtil.getDefContext().getSharedPreferences(openRcPreferencesFilename, Context.MODE_PRIVATE);

    public static boolean isFtcDriverStationInstalled(PackageManager packageManager) {
        try {
            packageManager.getPackageInfo("com.qualcomm.ftcdriverstation", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static boolean hasAcknowledgedLegalityStatus() {
        return openRcPrefs.getBoolean(AppUtil.getDefContext().getString(R.string.acknowledgedLegalityPrefKey), false);
    }

    public static void setLegalityAcknowledgementStatus(boolean legalityAcknowledged) {
        openRcPrefs.edit().
                putBoolean(AppUtil.getDefContext().getString(R.string.acknowledgedLegalityPrefKey), legalityAcknowledged)
                .apply();
    }
}
