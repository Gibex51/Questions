package com.topotgames.questions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Question {
    private String question;
    private List<String> answers;
    private boolean[] checksAnswers;
    private int rightAnswer;
    private UUID id;
    private boolean mark;
    private int number;

    private static int number_counter = 0;

    public Question(String question, List<String> answers, int rightAnswer) {
        this.question = question;
        this.answers = new ArrayList<String>(answers);
        this.checksAnswers = new boolean[answers.size()];
        this.rightAnswer = rightAnswer;
        this.id = UUID.randomUUID();
        this.mark = false;
        this.number = ++number_counter;
    }
    
    public int getNumber() {
        return number;
    }

    public void resetChecksAnswers() {
        for (int i = 0; i < checksAnswers.length; i++) {
            checksAnswers[i] = false;
        }
    }

    public int getCheckedAnswer() {
        for (int i = 0; i < checksAnswers.length; i++)
            if (checksAnswers[i]) return i;
        return -1;
    }

    public void checkAnswer(int index) {
        if ((index < 0) || (index >= checksAnswers.length)) return;
        resetChecksAnswers();
        checksAnswers[index] = true;
    }

    public boolean answerIsChecked(int index) {
        if ((index < 0) || (index > checksAnswers.length)) return false;
        return checksAnswers[index];
    }

    public UUID getId() {
        return id;
    }

    public boolean isMarked() {
        return mark;
    }

    public void setMark(boolean mark) {
        this.mark = mark;
    }

    public String getQuestion() {
        return question;
    }

    public int getRightAnswer() {
        return rightAnswer;
    }

    public List<String> getAnswers() {
        return new ArrayList<String>(answers);
    }
}
