/* Copyright (c) 2014, 2015 Qualcomm Technologies Inc

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

package com.qualcomm.ftccommon;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.network.DeviceNameManager;
import org.firstinspires.ftc.robotcore.internal.ui.ThemedActivity;

public class FtcRobotControllerSettingsActivity extends ThemedActivity {

    @Override
    public String getTag() {
        return this.getClass().getSimpleName();
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the settings from an XML resource
            addPreferencesFromResource(R.xml.app_settings);

            // Modified for OpenFTC: Add a preference for Line Wrap
            addLineWrapPref();

            Preference prefViewLogs = findPreference(getString(R.string.pref_launch_viewlogs));
            prefViewLogs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent viewLogsIntent = new Intent(AppUtil.getDefContext(), ViewLogsActivity.class);
                    viewLogsIntent.putExtra(LaunchActivityConstantsList.VIEW_LOGS_ACTIVITY_FILENAME, RobotLog.getLogFilename(getActivity()));
                    startActivity(viewLogsIntent);
                    return true;
                }
            });
        }

        @Override
        public void onActivityResult(int request, int result, Intent intent) {
            // We test both for historical reasons only
            if (request == LaunchActivityConstantsList.RequestCode.CONFIGURE_ROBOT_CONTROLLER.ordinal() ||
                    request == LaunchActivityConstantsList.RequestCode.SETTINGS_ROBOT_CONTROLLER.ordinal()) {
                if (result == RESULT_OK) {
                    getActivity().setResult(RESULT_OK, intent);
                }
            }
        }

        private void addLineWrapPref() {
            String lwPreferenceKey = getString(R.string.pref_line_wrap_log_key);
            String logCategoryKey = getString(R.string.prefcat_logging_key);
            PreferenceCategory logCategory = (PreferenceCategory) findPreference(logCategoryKey);
            SwitchPreference lwPreference = new SwitchPreference(getActivity());
            lwPreference.setTitle(R.string.pref_line_wrap_log_title);
            lwPreference.setSummary(R.string.pref_line_wrap_log_summary);
            lwPreference.setKey(lwPreferenceKey);
            lwPreference.setDefaultValue(false);
            lwPreference.setOrder(0);
            logCategory.addPreference(lwPreference);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Always make sure we have a real device name before we launch
        DeviceNameManager.getInstance().initializeDeviceNameIfNecessary();

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
