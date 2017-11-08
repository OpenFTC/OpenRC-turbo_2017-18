package org.openftc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class UiUtils
{
    public static void showDsAppInstalledDialog (final Activity activity)
    {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);

        String test = "<font color='#FF0000'>This device has the FTC Driver Station app installed.</font>" +
            "<br> <br>You need to uninstall either this Robot Controller (RC) app or the Driver Station (DS) app.";

        builder.setTitle("Ruh-roh!")
                .setMessage(Html.fromHtml(test))
                .setPositiveButton("Uninstall DS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Uri packageUri = Uri.parse("package:com.qualcomm.ftcdriverstation");
                        Intent uninstallIntent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE, packageUri);
                        activity.finish();
                        activity.startActivity(uninstallIntent);
                    }
                })
                .setNegativeButton("Uninstall RC", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Uri packageUri = Uri.parse("package:com.qualcomm.ftcrobotcontroller");
                        Intent uninstallIntent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE, packageUri);
                        activity.finish();
                        activity.startActivity(uninstallIntent);
                    }
                })
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .show();
    }

    public static void showOpenFtcSummary(final Activity activity)
    {
        LayoutInflater inflater= LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.about_openftc_layout, null);

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);

        builder.setTitle("About OpenFTC")
                .setView(view)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(R.drawable.ic_info_outline)
                .show();
    }
}
