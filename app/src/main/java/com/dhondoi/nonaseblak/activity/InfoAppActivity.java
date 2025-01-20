package com.dhondoi.nonaseblak.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.dhondoi.nonaseblak.R;

public class InfoAppActivity extends BaseActivity {

    @Override
    protected int initLayout() {
        return R.layout.activity_info_app;
    }

    @Override
    protected void initButtons() {
        findViewById(R.id.imageButtonLinkedin).setOnClickListener(v -> openLink("https://www.linkedin.com/in/doni-firmansyah-bba977217/"));
        findViewById(R.id.imageButtonInstagram).setOnClickListener(v -> openLink("https://www.instagram.com/dhondoii"));
        findViewById(R.id.imageButtonGithub).setOnClickListener(v -> openLink("https://github.com/dhondoi"));
        findViewById(R.id.imageButtonYouTube).setOnClickListener(v -> openLink("https://www.youtube.com/@dielci33"));
        findViewById(R.id.imageButtonSoloLearn).setOnClickListener(v -> openLink("https://www.sololearn.com/profile/997006"));
    }

    private void openLink(String link) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
    }
}