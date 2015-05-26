package com.example.myapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.myapp.R;

public class MyActivity extends Activity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);

        findViewById(R.id.button_last).setOnClickListener(this);
//        findViewById(R.id.button_list).setOnClickListener(this);
//        findViewById(R.id.button_category).setOnClickListener(this);
//        findViewById(R.id.button_search).setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_last: {
                Intent intent = new Intent(this, GameActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.button_list: {
//                Intent intent = new Intent(this, GridListActivity.class);
//                startActivity(intent);
                break;
            }
            case R.id.button_category: {
                break;
            }
            case R.id.button_search: {
//				Intent intent = new Intent(this, CategoryActivity.class);
//				startActivity(intent);
                break;
            }
        }
    }
}
