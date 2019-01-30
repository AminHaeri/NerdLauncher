package com.example.nerdlauncher;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class NerdLauncherActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return NerdLauncherFragment.newInstance();
    }

    @Override
    public void onBackPressed() {
    }
}
