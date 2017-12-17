package org.firstinspires.ftc.robotcontroller.internal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, FtcRobotControllerActivity.class);
        startActivity(intent);
        finish();
    }
}
