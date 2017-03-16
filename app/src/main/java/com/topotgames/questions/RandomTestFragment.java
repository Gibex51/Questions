package com.topotgames.questions;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RandomTestFragment extends Fragment {

    private RecyclerView recyclerView;
    private TestListAdapter testListAdapter;

    private static final int RANDOM_QUESTIONS_COUNT = 15;

    private class TestListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView questionTextView;
        private Question question;

        public TestListHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            questionTextView = (TextView) itemView.findViewById(R.id.question_text);
        }

        public void bindQuestion(Question question) {
            this.question = question;
            questionTextView.setText(question.getQuestion());
        }

        @Override
        public void onClick(View v) {
            Intent intent = TestRandomPagerActivity.newIntent(getActivity(), question.getId());
            startActivity(intent);
        }
    }

    private class TestListAdapter extends RecyclerView.Adapter<TestListHolder> {
        private List<Question> questions;

        public TestListAdapter(List<Question> questions) {
            this.questions = new ArrayList<Question>(questions);
        }

        @Override
        public TestListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.item_simple_text_with_mark, parent, false);
            return new TestListHolder(view);
        }

        @Override
        public void onBindViewHolder(TestListHolder holder, int position) {
            Question question = questions.get(position);
            holder.bindQuestion(question);
        }

        @Override
        public int getItemCount() {
            return questions.size();
        }
    }

    public RandomTestFragment() {

    }

    public static RandomTestFragment newInstance() {
        RandomTestFragment fragment = new RandomTestFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_simple_list, container, false);
        setHasOptionsMenu(true);

        recyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        QuestionDB qDB = QuestionDB.getInstance(getActivity());
        if (testListAdapter == null) {
            qDB.makeRandomQuestions(RANDOM_QUESTIONS_COUNT);
            testListAdapter = new TestListAdapter(qDB.getRandomQuestions());
            recyclerView.setAdapter(testListAdapter);
        } else {
            testListAdapter.notifyDataSetChanged();
        }
        return v;
    }

}
