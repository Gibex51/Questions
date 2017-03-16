package com.topotgames.questions;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class MainMenuFragment extends Fragment {

    public MainMenuFragment() {

    }

    public static MainMenuFragment newInstance() {
        MainMenuFragment fragment = new MainMenuFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_menu, container, false);

        Button buttonQuestSearch = (Button) v.findViewById(R.id.button_question_search);
        buttonQuestSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(SearchActivity.newIntent(getActivity()));
            }
        });

        Button buttonStartTest = (Button) v.findViewById(R.id.button_start_test);
        buttonStartTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(TestActivity.newIntent(getActivity(), false));
            }
        });

        Button buttonCheckedTest = (Button) v.findViewById(R.id.button_checked_test);
        buttonCheckedTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuestionDB qDB = QuestionDB.getInstance(getActivity());
                if (qDB.haveMarked()) {
                    startActivity(TestActivity.newIntent(getActivity(), true));
                } else {
                    Toast.makeText(getActivity(), "Нет отмеченых вопросов", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button buttonRandomTest = (Button) v.findViewById(R.id.button_random_test);
        buttonRandomTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(RandomTestActivity.newIntent(getActivity()));
            }
        });

        return v;
    }
}
