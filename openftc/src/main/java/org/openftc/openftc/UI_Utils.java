package org.openftc.openftc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class UI_Utils
{
    public static void showDsAppNotInstalledDialog (final Activity activity)
    {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);

        String test = "<font color='#FF0000'>This device has the FTC Driver Station app installed.</font><br><br>Either:<br>1) You accidentally deployed the RC to your DS and you need uninstall the RC; or<br><br>2) You need to uninstall the DS";

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
}
