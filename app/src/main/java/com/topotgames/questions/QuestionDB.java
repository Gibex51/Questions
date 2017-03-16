package com.topotgames.questions;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QuestionDB {
    private static QuestionDB questionDB;
    private List<Question> questions;
    private String filter = "";
    private boolean filterOnlyMarked = false;

    private List<Question> randomQuestions;

    @NonNull
    private String getQuestionRes(Resources res, Context context, int questionIndex) {
        String resName = res.getString(R.string.question_pattern, questionIndex);
        return res.getString(res.getIdentifier(resName, "string", context.getPackageName()));
    }

    private List<String> getAnswersRes(Resources res, Context context, int questionIndex) {
        List<String> answers = new ArrayList<String>();
        int answerIndex = 0;
        while (true) {
            String resName = res.getString(R.string.answer_pattern, questionIndex, answerIndex);
            int resId = res.getIdentifier(resName, "string", context.getPackageName());
            if (resId == 0) break;
            answers.add(res.getString(resId));
            answerIndex++;
        }
        return answers;
    }

    private int getRightAnswerRes(Resources res, Context context, int questionIndex) {
        String resName = res.getString(R.string.right_answer_pattern, questionIndex);
        return Integer.parseInt(res.getString(res.getIdentifier(resName, "string", context.getPackageName())));
    }

    private QuestionDB(Context context) {
        Resources res = context.getResources();
        int nQuestions = res.getInteger(R.integer.nquest);
        questions = new ArrayList<Question>(nQuestions);

        for (int qIndex = 0; qIndex < nQuestions; qIndex++){
            String questionText = getQuestionRes(res, context, qIndex);
            List<String> answers = getAnswersRes(res, context, qIndex);
            int rightAnswer = getRightAnswerRes(res, context, qIndex);
            questions.add(new Question(questionText, answers, rightAnswer));
        }
    }

    public static QuestionDB getInstance(Context context) {
        if (questionDB == null) {
            questionDB = new QuestionDB(context);
        }
        return questionDB;
    }

    public static void initInstance(Context context) {
        if (questionDB == null) {
            questionDB = new QuestionDB(context);
        }
    }

    public Question getQuestion(int questionIndex) {
        if ((questionIndex < 0) || (questionIndex >= questions.size())) return null;
        return questions.get(questionIndex);
    }

    public Question getQuestionById(UUID id) {
        for (Question question : questions) {
            if (question.getId().equals(id)) {
                return question;
            }
        }
        return null;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public List<Question> getFilteredQuestions() {
        String query = filter.toLowerCase();

        final List<Question> filteredQuestionList = new ArrayList<>();
        for (Question question : questions) {
            final String text = question.getQuestion().toLowerCase();
            if (text.contains(query) && ((filterOnlyMarked == false) || (question.isMarked()))) {
                filteredQuestionList.add(question);
            }
        }
        return filteredQuestionList;
    }

    public void makeRandomQuestions(int count) {
        if (count > questions.size()) count = questions.size();
        randomQuestions = new ArrayList<>(count);

        while (true) {
            int randomQuestionNumber = (int) Math.round(Math.random() * questions.size());
            Question randQuestion = questions.get(randomQuestionNumber);
            boolean exists = false;
            for (Question question: randomQuestions)
                if (question.equals(randQuestion)) {
                    exists = true;
                    break;
                }
            if (exists) continue;
            randomQuestions.add(randQuestion);

            count--;
            if (count <= 0) break;
        }
    }

    public List<Question> getRandomQuestions() {
        return randomQuestions;
    }

    public void resetFilter() {
        this.filter = "";
        this.filterOnlyMarked = false;
    }

    public void setFilter(String filter, boolean onlyMarked) {
        this.filter = filter;
        this.filterOnlyMarked = onlyMarked;
    }

    public boolean haveMarked() {
        for (Question question: questions) {
            if (question.isMarked())
                return true;
        }
        return false;
    }
}
