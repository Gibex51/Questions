package com.topotgames.questions;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;


public class TestFragment extends Fragment implements SearchView.OnQueryTextListener{

    private RecyclerView recyclerView;
    private TestListAdapter testListAdapter;

    private static final String ARG_IS_CHECKED_TEST = "is_checked_test";

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
            questionTextView.setText(String.valueOf(question.getNumber()) + ". " + question.getQuestion());
        }

        @Override
        public void onClick(View v) {
            Intent intent = TestPagerActivity.newIntent(getActivity(), question.getId());
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
            View view = layoutInflater.inflate(R.layout.item_simple_text, parent, false);
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

    public TestFragment() {

    }

    public static TestFragment newInstance(boolean isCheckedTest) {
        TestFragment fragment = new TestFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_CHECKED_TEST, isCheckedTest);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_simple_list, container, false);
        setHasOptionsMenu(true);

        recyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Bundle args = getArguments();
        QuestionDB qDB = QuestionDB.getInstance(getActivity());
        qDB.setFilter("", args.getBoolean(ARG_IS_CHECKED_TEST, false));
        if (testListAdapter == null) {
            testListAdapter = new TestListAdapter(qDB.getFilteredQuestions());
            recyclerView.setAdapter(testListAdapter);
        } else {
            testListAdapter.notifyDataSetChanged();
        }
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_test_list_questions, menu);
        final MenuItem item = menu.findItem(R.id.action_search_question_number);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.goto_hint));
        searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    @Override
    public boolean onQueryTextChange(String query) {
        QuestionDB qDB = QuestionDB.getInstance(getActivity());
        List<Question> questions = qDB.getFilteredQuestions();
        int query_number = 0;
        int position = 0;
        try {
            query_number = Integer.parseInt(query);
        } catch (Exception e) {
            query_number = 0;
        } finally {
            for (int qIndex = 0; qIndex < questions.size(); qIndex++)
                if (questions.get(qIndex).getNumber() == query_number) {
                    position = qIndex;
                    break;
                }
            recyclerView.scrollToPosition(position);
        }
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
}
