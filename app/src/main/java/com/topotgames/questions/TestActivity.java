package com.topotgames.questions;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

public class TestActivity extends CommonFragmentActivity {

    private static final String EXTRA_IS_CHECKED_TEST = "com.topotgames.questions.is_checked_test";

    public static Intent newIntent(Context packageContext, boolean isCheckedTest) {
        Intent intent = new Intent(packageContext, TestActivity.class);
        intent.putExtra(EXTRA_IS_CHECKED_TEST, isCheckedTest);
        return intent;
    }

    protected Fragment createFragment() {
        Intent intent = getIntent();
        return TestFragment.newInstance(intent.getBooleanExtra(EXTRA_IS_CHECKED_TEST, false));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.test_question_list));
    }
}
