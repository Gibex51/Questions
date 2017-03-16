package com.topotgames.questions;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.UUID;

public class QuestionFragment extends Fragment {
    private static final String ARG_QUESTION_ID = "question_id";
    private Question question;

    public QuestionFragment() {

    }

    public static QuestionFragment newInstance(UUID questionId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_QUESTION_ID, questionId);
        QuestionFragment fragment = new QuestionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID questionId = (UUID) getArguments().getSerializable(ARG_QUESTION_ID);
        question = QuestionDB.getInstance(getActivity()).getQuestionById(questionId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_question, container, false);
        setHasOptionsMenu(true);

        TextView questionTextView = (TextView) v.findViewById(R.id.question_text);
        questionTextView.setMovementMethod(new ScrollingMovementMethod());
        StringBuilder resultText = new StringBuilder();
        resultText.append(question.getQuestion());
        resultText.append("<br><br><br>");

        List<String> answers = question.getAnswers();

        for (int ansInd = 0; ansInd < answers.size(); ansInd++) {
            if (ansInd != question.getRightAnswer()) {
                resultText.append(String.format("%d. %s <br><br>", ansInd + 1, answers.get(ansInd)));
            } else {
                resultText.append(String.format("<strong>%d. %s</strong><br><br>", ansInd + 1, answers.get(ansInd)));
            }
        }

        questionTextView.setText(Html.fromHtml(resultText.toString()));

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_question, menu);
        final MenuItem item = menu.findItem(R.id.ic_home);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ic_home:
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
