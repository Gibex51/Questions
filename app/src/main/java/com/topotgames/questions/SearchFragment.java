package com.topotgames.questions;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {

    private RecyclerView recyclerView;
    private SearchListAdapter searchListAdapter;

    private class SearchListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView questionTextView;
        private Question question;

        public SearchListHolder(View itemView) {
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
            Intent intent = SearchPagerActivity.newIntent(getActivity(), question.getId());
            startActivity(intent);
        }
    }

    private class SearchListAdapter extends RecyclerView.Adapter<SearchListHolder> {
        private List<Question> questions;

        public SearchListAdapter(List<Question> questions) {
            this.questions = new ArrayList<Question>(questions);
        }

        @Override
        public SearchListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.item_simple_text, parent, false);
            return new SearchListHolder(view);
        }

        @Override
        public void onBindViewHolder(SearchListHolder holder, int position) {
            Question question = questions.get(position);
            holder.bindQuestion(question);
        }

        @Override
        public int getItemCount() {
            return questions.size();
        }

        public Question removeItem(int position) {
            final Question removedQuestion = questions.remove(position);
            notifyItemRemoved(position);
            return removedQuestion;
        }

        public void addItem(int position, Question question) {
            questions.add(position, question);
            notifyItemInserted(position);
        }

        public void moveItem(int fromPosition, int toPosition) {
            final Question question = questions.remove(fromPosition);
            questions.add(toPosition, question);
            notifyItemMoved(fromPosition, toPosition);
        }

        public void animateTo(List<Question> questions) {
            applyAndAnimateRemovals(questions);
            applyAndAnimateAdditions(questions);
            applyAndAnimateMovedItems(questions);
        }

        private void applyAndAnimateRemovals(List<Question> newQuestions) {
            for (int i = questions.size() - 1; i >= 0; i--) {
                final Question question = questions.get(i);
                if (!newQuestions.contains(question)) {
                    removeItem(i);
                }
            }
        }

        private void applyAndAnimateAdditions(List<Question> newQuestions) {
            for (int i = 0, count = newQuestions.size(); i < count; i++) {
                final Question question = newQuestions.get(i);
                if (!questions.contains(question)) {
                    addItem(i, question);
                }
            }
        }

        private void applyAndAnimateMovedItems(List<Question> newQuestions) {
            for (int toPosition = newQuestions.size() - 1; toPosition >= 0; toPosition--) {
                final Question question = newQuestions.get(toPosition);
                final int fromPosition = questions.indexOf(question);
                if (fromPosition >= 0 && fromPosition != toPosition) {
                    moveItem(fromPosition, toPosition);
                }
            }
        }
    }

    public SearchFragment() {

    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
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
        qDB.resetFilter();
        if (searchListAdapter == null) {
            searchListAdapter = new SearchListAdapter(qDB.getQuestions());
            recyclerView.setAdapter(searchListAdapter);
        } else {
            searchListAdapter.notifyDataSetChanged();
        }
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_list_questions, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setIconifiedByDefault(false);
    }

    @Override
    public boolean onQueryTextChange(String query) {
        QuestionDB qDB = QuestionDB.getInstance(getActivity());
        qDB.setFilter(query, false);
        final List<Question> filteredModelList = qDB.getFilteredQuestions();
        searchListAdapter.animateTo(filteredModelList);
        recyclerView.scrollToPosition(0);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

}
