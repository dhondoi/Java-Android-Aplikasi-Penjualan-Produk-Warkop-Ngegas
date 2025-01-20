package com.dhondoi.nonaseblak.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.dhondoi.nonaseblak.R;

public class SplashScreenActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        progressBar = findViewById(R.id.progressBar);

        new Thread(() -> {
            try {
                int progressStatus = 0;
                while (progressStatus < 100) {
                    Thread.sleep(10);
                    progressBar.setProgress(progressStatus++);
                }
                if (progressStatus == 100) {
                    startActivity(new Intent(SplashScreenActivity.this, MainMenuActivity.class));
                    finish();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

}