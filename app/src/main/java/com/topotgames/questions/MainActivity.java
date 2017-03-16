package com.topotgames.questions;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.View;

public class MainActivity extends CommonFragmentActivity {

    protected Fragment createFragment() {
        return MainMenuFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        QuestionDB.initInstance(this);
    }
}
