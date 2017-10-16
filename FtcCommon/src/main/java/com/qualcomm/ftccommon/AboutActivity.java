/* Copyright (c) 2014. 2015 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

/* Copyright (c) 2015 Jonathan Berling

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Jonathan Berling nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.ftccommon;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.qualcomm.robotcore.robocol.RobocolConfig;
import com.qualcomm.robotcore.util.Network;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.Version;
import com.qualcomm.robotcore.wifi.NetworkConnection;
import com.qualcomm.robotcore.wifi.NetworkConnectionFactory;
import com.qualcomm.robotcore.wifi.NetworkType;

import org.firstinspires.ftc.robotcore.internal.ui.ThemedActivity;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class AboutActivity extends ThemedActivity {

  @Override public String getTag() { return this.getClass().getSimpleName(); }

  NetworkConnection networkConnection = null;
  NetworkType networkType;

  @Override
  protected void onStart() {
    super.onStart();
    setContentView(R.layout.activity_about);

    Intent intent = getIntent();
    Serializable extra = intent.getSerializableExtra(LaunchActivityConstantsList.ABOUT_ACTIVITY_CONNECTION_TYPE);
    if(extra != null) {
      networkType = (NetworkType) extra;
    }


    ListView aboutList = (ListView)findViewById(R.id.aboutList);

    try {
      networkConnection = NetworkConnectionFactory.getNetworkConnection(networkType, null);
      networkConnection.enable();
    } catch (NullPointerException e) {
      RobotLog.e("Cannot start Network Connection of type: " + networkType);
      networkConnection = null;
    }

    ArrayAdapter<Item> adapter =  new ArrayAdapter<Item>(this, android.R.layout.simple_list_item_2, android.R.id.text1) {
      @Override
      public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView topLine = (TextView) view.findViewById(android.R.id.text1);
        TextView bottomLine = (TextView) view.findViewById(android.R.id.text2);

        Item item = getItem(position);
        topLine.setText(item.title);
        bottomLine.setText(item.info);

        return view;
      }

      @Override
      public int getCount()
      {
        return 5;
      }

      @Override
      public Item getItem(int pos) {
        switch (pos) {
          case 0: return getAppVersion();
          case 1: return getLibVersion();
          case 2: return getNetworkProtocolVersion();
          case 3: return getConnectionInfo();
          case 4: return getBuildTimeInfo();
        }
        return new Item();
      }

      private Item getAppVersion() {
        Item i = new Item();
        i.title = getString(R.string.about_app_version);
        try {
          i.info = AboutActivity.this.getPackageManager().getPackageInfo(AboutActivity.this.getPackageName(), 0).versionName;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
          i.info = e.getMessage();
        }
        return i;
      }

      private Item getLibVersion() {
        Item i = new Item();
        i.title = getString(R.string.about_library_version);
        i.info = Version.getLibraryVersion();
        return i;
      }

      private Item getNetworkProtocolVersion() {
        Item i = new Item();
        i.title = getString(R.string.about_network_protocol_version);
        i.info = String.format("v%d", RobocolConfig.ROBOCOL_VERSION);
        return i;
      }

      private Item getConnectionInfo() {
        Item i = new Item();
        i.title = getString(R.string.about_network_connection_info);
        i.info = networkConnection.getInfo();

        return i;
      }

      private Item getBuildTimeInfo() {
        Item i = new Item();
        i.title = getString(R.string.about_build_time);
        i.info = getBuildTime();
        return i;
      }
    };

    aboutList.setAdapter(adapter);
  }

  protected void onStop() {
    super.onStop();

    if (networkConnection != null) {
      networkConnection.disable();
    }
  }

  protected String getLocalIpAddressesAsString() {
    ArrayList<InetAddress> addrs;

    addrs = Network.getLocalIpAddresses();
    addrs = Network.removeLoopbackAddresses(addrs);
    addrs = Network.removeIPv6Addresses(addrs);

    if (addrs.size() < 1) return "unavailable";

    StringBuilder sb = new StringBuilder();
    sb.append(addrs.get(0).getHostAddress());
    for (int i = 1; i < addrs.size(); i++) {
      sb.append(", ").append(addrs.get(i).getHostAddress());
    }
    return sb.toString();
  }

  /** https://code.google.com/p/android/issues/detail?id=220039 */
  protected String getBuildTime() {

    String buildTime = "unavailable";
    try {
      ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), 0);

      ZipFile zf = new ZipFile(ai.sourceDir);
      ZipEntry ze = zf.getEntry("classes.dex");
      zf.close();

      long time = ze.getTime();
      buildTime = SimpleDateFormat.getInstance().format(new java.util.Date(time));
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return buildTime;
  }

  public static class Item {
    public String title = "";
    public String info = "";
  }
}

