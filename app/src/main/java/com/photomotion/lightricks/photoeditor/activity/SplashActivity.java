package com.photomotion.lightricks.photoeditor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.photomotion.lightricks.photoeditor.R;


public class SplashActivity extends AppCompatActivity {

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                startMain();
            }
        }, 3000);
    }



    public void startMain() {
        startActivity(new Intent(SplashActivity.this, StartActivity.class));
        finish();
    }

}
