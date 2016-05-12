package com.example.testquest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.util.Pair;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import static android.content.DialogInterface.BUTTON_NEUTRAL;
import static java.lang.Math.abs;
import static java.lang.Math.random;

/**
 * Created by Yaroslav on 04.12.13.
 */

class CQuestion{
   int     qindex;
   boolean used;
   boolean answered;
}

class CQuestionDesc{
    boolean  used;
    boolean  answered;
    String   quest;
    String[] ans;
    int      rightans;
    boolean  isvalid;
}

public class MyAppTQ extends Application {

    private ImageView QImg;
    private ImageView[] AnsImg;
    private TextView Divider;
    private TextView QText;
    private CheckBox[] AnsCheck;

    private int nQuests;
    private ArrayList<CQuestion> Quests;

    private CQuestionDesc CurrQuestsDesc;
    private int CurrQuestNum;
    private int CurrQuest;
    private int Points;
    private boolean Answered;
    private boolean QStarted = false;

    private Random qrand;

    Activity activity;
    TextView infobox;

    public int GetNQ(){
        return nQuests;
    }

    public int GetPoints(){
        return Points;
    }

    public CQuestionDesc GetRandQuest(){
        CQuestionDesc tmpQuest = new CQuestionDesc();
        boolean haveQuest = false;

        for (CQuestion quest : Quests) {
            if (quest.used == true) continue;
            haveQuest = true;
            break;
        }

        if (haveQuest){
            CurrQuest = abs(qrand.nextInt() % nQuests);
            while (Quests.get(CurrQuest).used)
                CurrQuest = abs(qrand.nextInt()%nQuests);

            tmpQuest.isvalid = true;
            tmpQuest.answered = Quests.get(CurrQuest).answered;
            tmpQuest.used = Quests.get(CurrQuest).used;

            int strId = getResources().getIdentifier(String.format("q%1$d",Quests.get(CurrQuest).qindex), "string", getPackageName());
            if (strId != 0)
                tmpQuest.quest = getString(strId);
            else
                tmpQuest.quest = "<Ошибка>";
            tmpQuest.ans = new String[5];
            for (int answerInd = 0; answerInd < 5; answerInd++){
                strId = getResources().getIdentifier(String.format("q%1$da%2$d",Quests.get(CurrQuest).qindex, answerInd+1), "string", getPackageName());
                if (strId != 0)
                    tmpQuest.ans[answerInd] = getString(strId);
                else
                    tmpQuest.ans[answerInd] = "<Ошибка>";
            }
            strId = getResources().getIdentifier(String.format("q%1$dr",Quests.get(CurrQuest).qindex), "string", getPackageName());
            tmpQuest.rightans = Integer.parseInt(getString(strId));
        }
        else{
            CurrQuest = -1;
            tmpQuest.isvalid = false;
        }
        CurrQuestsDesc = tmpQuest;
        return tmpQuest;
    }

    public void MixAnswers(CQuestionDesc QDesc){
        if (QDesc.isvalid){
            int randi[] = new int[5];
            String rands[] = new String[5];
            for (int i = 0; i < 5; i++){
                boolean res = false;
                while (!res){
                    randi[i] = abs(qrand.nextInt())%5;
                    res = true;
                    for (int k = 0; k < i; k++)
                        if (randi[k] == randi[i])
                            res = false;
                }
                rands[randi[i]] = QDesc.ans[i];
            }
            for (int i = 0; i < 5; i++)
                QDesc.ans[i] = rands[i];
            QDesc.rightans = randi[QDesc.rightans-1] + 1;
        }
    }

    public void ClearQuests(){
        for (CQuestion quest : Quests)
        {
            quest.answered = false;
            quest.used = false;
        }
        CurrQuest = -1;
    }

    public void ClearData(){
        Points = 0;
        CurrQuestNum = 1;
        Answered = false;
    }

    public boolean NextBegin(){
        if (CurrQuest > -1)
            Quests.get(CurrQuest).used = true;
        if (nQuests <= 0)
            CurrQuest = -1;
        return QStarted;
    }

    public void NextEnd(){
        CurrQuestNum++;
        Answered = false;
    }

    public boolean CanAnswer(){
        return ((Answered == false) && (CurrQuest >= 0));
    }

    public boolean Answer(int ans_id){
        if ((ans_id <= 0) || (ans_id > 5) || (!CanAnswer()))
            return false;

        if (ans_id == CurrQuestsDesc.rightans){
            Quests.get(CurrQuest).answered = true;
            Points++;
        }

        if (Quests.get(CurrQuest).answered){
            MessageBox("Правильно");
        } else {
            MessageBox(String.format("Неправильно. Правильный ответ %1$d", CurrQuestsDesc.rightans));
        }

        Answered = true;
        Quests.get(CurrQuest).used = true;

        UpdateInfo();

        return Quests.get(CurrQuest).answered;
    }

    public void FillQuestUI(LinearLayout QLayout, CQuestionDesc QDesc){
        if (QDesc.isvalid){
            QLayout.removeAllViews();

            if (QDesc.quest.toCharArray()[0] == '%'){
                String vName = QDesc.quest.substring(1,QDesc.quest.length());
                int res_id = getResources().getIdentifier(vName, "drawable", getPackageName());
                if (res_id != 0){
                  QImg.setImageResource(res_id);
                  QLayout.addView(QImg);
                }else{
                  QText.setText("<Ошибка загрузки изображения>");
                  QLayout.addView(QText);
                }
            }else{
                QText.setText(QDesc.quest);
                QLayout.addView(QText);
            }
            QLayout.addView(Divider);

            for (int i = 0; i < 5; i++){
                AnsCheck[i].setChecked(false);
                QLayout.addView(AnsCheck[i]);
                if (QDesc.ans[i].toCharArray()[0] == '%'){
                    AnsCheck[i].setText("Ответ " + String.valueOf(i+1));
                    String vName = QDesc.ans[i].substring(1,QDesc.ans[i].length());
                    AnsImg[i].setImageResource(getResources().getIdentifier(vName, "drawable", getPackageName()));
                    QLayout.addView(AnsImg[i]);
                }else{
                    AnsCheck[i].setText(QDesc.ans[i]);
                }
            }
        }
    }

    public void MessageBox(String MsgText){
        AlertDialog dialog = new AlertDialog.Builder(activity).create();
        dialog.setTitle(MsgText);
        dialog.setButton(BUTTON_NEUTRAL,"OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog,
                                int id) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void UpdateInfo(){
        if (infobox != null)
            infobox.setText(String.format(getString(R.string.QuestStr2), CurrQuestNum, Points));
    }

    public void SetInfo(TextView _infobox){
        infobox = _infobox;
    }

    public void SetActivity(Activity activ){
        activity = activ;
    }

    public void SetStarted(boolean Started){
        QStarted = Started;
    }

    public void Init(Activity activ){
        activity = activ;
        QStarted = false;
        //Init Random
        Calendar calendar = Calendar.getInstance();
        qrand = new Random();
        qrand.setSeed(calendar.get(Calendar.SECOND)*
                      calendar.get(Calendar.MINUTE)*
                      calendar.get(Calendar.HOUR_OF_DAY));
        //Init Questions
        nQuests = Integer.parseInt(getString(R.string.nquest));
        Quests = new ArrayList<CQuestion>();
        for (int i = 0; i < nQuests; i++)
        {
            CQuestion quest = new CQuestion();
            quest.answered = false;
            quest.used = false;
            quest.qindex = i+1;
            Quests.add(quest);
        }
        ClearData();
        //Init UI
        QText = new TextView(activity);
        QImg = new ImageView(activity);
        Divider = new TextView(activity);
        Divider.setText("_____________________");
        AnsImg = new ImageView[5];
        AnsCheck = new CheckBox[5];
        for (int i = 0; i < 5; i++){
            AnsImg[i] = new ImageView(activity);
            AnsCheck[i] = new CheckBox(activity);
        }

        AnsCheck[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {Answer(1);}
        });
        AnsCheck[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {Answer(2);}
        });
        AnsCheck[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {Answer(3);}
        });
        AnsCheck[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {Answer(4);}
        });
        AnsCheck[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {Answer(5);}
        });
    }
}
