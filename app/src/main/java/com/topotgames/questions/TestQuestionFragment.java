package com.topotgames.questions;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestQuestionFragment extends Fragment {

    private RecyclerView recyclerView;
    private TestQuestionListAdapter testQuestionListAdapter;
    private Question question;

    private static final String DRAWABLE_UNCHECKED = "@drawable/abc_btn_check_to_on_mtrl_000";
    private static final String DRAWABLE_CHECKED = "@drawable/abc_btn_check_to_on_mtrl_015";

    private static final String ARG_QUESTION_ID = "question_id";

    private class TestQuestionListHolder extends RecyclerView.ViewHolder{
        private RadioButton answerRadioButton;
        private Question question;
        private int answer_index;

        private void updateItemUI(TestQuestionListHolder holder, int itemIndex) {
            if (holder.answerRadioButton != null)
                holder.answerRadioButton.setChecked(question.answerIsChecked(itemIndex));
        }

        public TestQuestionListHolder(View itemView) {
            super(itemView);
            answerRadioButton = (RadioButton) itemView.findViewById(R.id.answer_radio_button);
            answerRadioButton.setChecked(false);
            answerRadioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int prevCheckedIndex = question.getCheckedAnswer();
                    question.checkAnswer(answer_index);
                    if (prevCheckedIndex >= 0) {
                        TestQuestionListHolder holder = (TestQuestionListHolder) recyclerView.findViewHolderForAdapterPosition(prevCheckedIndex);
                        if (holder != null)
                            updateItemUI(holder, prevCheckedIndex);
                    }
                }
            });
        }

        public void bindAnswer(Question question, int answer_index) {
            this.question = question;
            this.answer_index = answer_index;
            answerRadioButton.setText(question.getAnswers().get(answer_index));
            updateItemUI(this, answer_index);
        }

        public void updateAnswer() {
            updateItemUI(this, answer_index);
        }

    }

    private class TestQuestionListAdapter extends RecyclerView.Adapter<TestQuestionListHolder> {
        private Question question;

        public TestQuestionListAdapter(Question question) {
            this.question = question;
        }

        @Override
        public TestQuestionListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.item_simple_radiobutton, parent, false);
            return new TestQuestionListHolder(view);
        }

        @Override
        public void onBindViewHolder(TestQuestionListHolder holder, int position) {
            holder.bindAnswer(question, position);
        }

        @Override
        public void onViewAttachedToWindow(TestQuestionListHolder holder) {
            holder.updateAnswer();
        }

        @Override
        public int getItemCount() {
            return question.getAnswers().size();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID questionId = (UUID) getArguments().getSerializable(ARG_QUESTION_ID);
        question = QuestionDB.getInstance(getActivity()).getQuestionById(questionId);
        question.resetChecksAnswers();
    }

    public static TestQuestionFragment newInstance(UUID questionId) {
        TestQuestionFragment fragment = new TestQuestionFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_QUESTION_ID, questionId);
        fragment.setArguments(args);
        return fragment;
    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_test_question, container, false);
        setHasOptionsMenu(true);

        final TextView questionText = (TextView) v.findViewById(R.id.test_question_text);
        questionText.setText(question.getQuestion());

        recyclerView = (RecyclerView)v.findViewById(R.id.test_answers_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (testQuestionListAdapter == null) {
            testQuestionListAdapter = new TestQuestionListAdapter(question);
            recyclerView.setAdapter(testQuestionListAdapter);
        } else {
            testQuestionListAdapter.notifyDataSetChanged();
        }

        Button answerButton = (Button) v.findViewById(R.id.test_answer_button);
        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int checked = question.getCheckedAnswer();
                if (checked >= 0) {
                    if (checked == question.getRightAnswer()) {
                        showToast(getString(R.string.message_right_answer));
                    } else {
                        question.setMark(true);
                        showToast(getString(R.string.message_wrong_answer, question.getRightAnswer() + 1));
                    }
                } else {
                    showToast(getString(R.string.message_no_answer));
                }
            }
        });

        return v;
    }

    private void setItemChecked(MenuItem menuItem, boolean isChecked) {
        menuItem.setChecked(isChecked);
        if (isChecked == true)
            menuItem.setIcon(android.R.drawable.checkbox_on_background);
        else
            menuItem.setIcon(android.R.drawable.checkbox_off_background);
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_test_question, menu);

        final MenuItem item = menu.findItem(R.id.action_mark_question);
        setItemChecked(item, question.isMarked());

        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                boolean checkState = !menuItem.isChecked();
                setItemChecked(menuItem, checkState);
                question.setMark(checkState);
                return true;
            }
        });
    }
}
