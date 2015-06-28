package com.example.myapp.activity;

import android.app.Activity;
import android.os.Bundle;

import com.example.myapp.common.api.FileIO;

public class BaseActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public FileIO getFileIO() {
        return null;
    }
}