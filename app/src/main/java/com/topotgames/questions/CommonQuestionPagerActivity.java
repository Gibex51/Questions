package com.topotgames.questions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

public abstract class CommonQuestionPagerActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private List<Question> questions;

    protected static final String EXTRA_QUESTION_ID = "com.topotgames.questions.question_id";

    protected abstract Fragment createFragment(UUID questionId);

    protected abstract List<Question> getQuestions();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_pager);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        UUID questionId = (UUID) getIntent().getSerializableExtra(EXTRA_QUESTION_ID);

        viewPager = (ViewPager) findViewById(R.id.question_view_pager);

        questions = getQuestions();
        FragmentManager fragmentManager = getSupportFragmentManager();

        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Question question = questions.get(position);
                return createFragment(question.getId());
            }

            @Override
            public int getCount() {
                return questions.size();
            }
        });

        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).getId().equals(questionId)) {
                viewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
