package org.openftc.rc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class UiUtils {
    public static void showDsAppInstalledDialog(final Activity activity) {
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

    public static void showOpenRcSummary(final Activity activity) {
        final LayoutInflater inflater = LayoutInflater.from(activity);
        final View view = inflater.inflate(R.layout.about_openrc_layout, null);

        final TextView textView = (TextView)view.findViewById(R.id.about_openrc_textview);
        final SpannableString aboutText = new SpannableString(activity.getText(R.string.openRcSummary));
        Linkify.addLinks(aboutText, Linkify.WEB_URLS);

        textView.setText(aboutText);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        new AlertDialog.Builder(activity)
                .setCancelable(false)
                .setTitle("About OpenRC")
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(R.drawable.ic_info_outline)
                .show();

    }

    public static void showLegalityAcknowlegementDialog(final Activity activity) {
        final SpannableString dialogText = new SpannableString(activity.getText(R.string.openRcLegalityWarning));
        Linkify.addLinks(dialogText, Linkify.WEB_URLS);

        final AlertDialog legalityDialog = new AlertDialog.Builder(activity)
                .setMessage(dialogText)
                .setPositiveButton("Acknowledged.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Utils.setLegalityAcknowledgementStatus(true);
                    }
                })
                .setNegativeButton("Tell me again.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Utils.setLegalityAcknowledgementStatus(false);
                    }
                })
                .create();
        legalityDialog.show();

        ((TextView)legalityDialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
    }
}
