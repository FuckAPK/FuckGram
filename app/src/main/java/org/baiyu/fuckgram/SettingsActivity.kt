package org.baiyu.fuckgram;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import de.robv.android.xposed.XposedBridge;

public class SettingsActivity extends AppCompatActivity {
    /** @noinspection deprecation*/
    @SuppressLint("WorldReadableFiles")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        try {
            getSharedPreferences(BuildConfig.APPLICATION_ID + "_preferences", MODE_WORLD_READABLE);
        } catch (Exception e) {
            XposedBridge.log(e);
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new MySettingsFragment())
                .commit();
    }

    private static class MySettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);
        }
    }
}
