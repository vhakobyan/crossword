package com.example.myapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.myapp.R;
import com.example.myapp.util.Settings;

public class SplashActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash);

        Settings.load(getFileIO());

        int SPLASH_DISPLAY_LENGTH = 1000 * 3;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, MyActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}