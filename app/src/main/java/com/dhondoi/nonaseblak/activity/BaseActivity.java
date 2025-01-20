package com.dhondoi.nonaseblak.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

// root to make Activity this module
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set file Layout
        setContentView(initLayout());
        // init necessary
        initViews();
        // init button in layout
        initButtons();
        // init list
        initLists();
        // set necesssary
        initNecessary();
    }

    // get Layout
    protected abstract int initLayout();

    protected void initViews() {

    }
    // init button in layout
    protected abstract void initButtons();

    // init list
    protected void initLists() {
    }

    protected void initNecessary() {
    }
}
