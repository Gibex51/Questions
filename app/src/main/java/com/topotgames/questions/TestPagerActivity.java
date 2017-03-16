package com.topotgames.questions;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

import java.util.List;
import java.util.UUID;

public class TestPagerActivity extends CommonQuestionPagerActivity {

    public static Intent newIntent(Context packageContext, UUID question_id) {
        Intent intent = new Intent(packageContext, TestPagerActivity.class);
        intent.putExtra(EXTRA_QUESTION_ID, question_id);
        return intent;
    }

    @Override
    protected Fragment createFragment(UUID questionId) {
        return TestQuestionFragment.newInstance(questionId);
    }

    @Override
    protected List<Question> getQuestions() {
        return QuestionDB.getInstance(this).getFilteredQuestions();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.question_actionbar_title));
        actionBar.show();
    }
}
