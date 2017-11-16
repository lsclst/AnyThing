package com.lsc.anything.module.about;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.lsc.anything.R;
import com.lsc.anything.utils.ShareUtil;
import com.lsc.anything.utils.ToastUtil;

/**
 * Created by lsc on 2017/11/3 0003.
 *
 * @author lsc
 */

public class AboutDetailFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.about_preference_fragment);
        findPreference("follow_me").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(preference.getSummary().toString()));
                getContext().startActivity(i);
                return false;
            }
        });
        findPreference("feedback").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                try {
                    ShareUtil.sendMail(getContext(), getString(R.string.sendto), getString(R.string.feedback_tip), getString(R.string.device_model) + Build.MODEL + "\n" + getString(R.string.sdk_version) + Build.VERSION.RELEASE + "\n" + getString(R.string.app_version));
                } catch (ActivityNotFoundException e) {
                    ToastUtil.showErrorMsg("没有邮件App");
                }
                return false;
            }
        });

    }
}
