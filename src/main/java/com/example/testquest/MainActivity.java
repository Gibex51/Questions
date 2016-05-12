package com.example.testquest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.content.DialogInterface.*;


public class MainActivity extends ActionBarActivity {

    Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();

            MyAppTQ QApp = (MyAppTQ)this.getApplication();
            QApp.Init(activity);
        }else{
            MyAppTQ QApp = (MyAppTQ)this.getApplication();
            QApp.Init(activity);
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

    public void Reset(){
        MyAppTQ QApp = (MyAppTQ)this.getApplication();
        QApp.SetInfo((TextView) findViewById(R.id.InfoText));
        QApp.SetStarted(true);
        QApp.ClearQuests();
        if (QApp.GetNQ() > 0){
            CheckBox unlimbox = (CheckBox) findViewById(R.id.checkBox);
            CQuestionDesc CurrQuest;
            CurrQuest = QApp.GetRandQuest();
            QApp.FillQuestUI((LinearLayout) findViewById(R.id.QuestLayout), CurrQuest);
        }

        QApp.ClearData();
        QApp.UpdateInfo();
    }

    public void Next(){
        MyAppTQ QApp = (MyAppTQ)this.getApplication();
        QApp.SetInfo((TextView) findViewById(R.id.InfoText));
        if ((QApp.NextBegin()) && (QApp.GetNQ() > 0)){
            CheckBox unlimbox = (CheckBox) findViewById(R.id.checkBox);
            CQuestionDesc CurrQuest;
            CurrQuest = QApp.GetRandQuest();
            if (CurrQuest.isvalid){
                QApp.MixAnswers(CurrQuest);
                QApp.FillQuestUI((LinearLayout) findViewById(R.id.QuestLayout), CurrQuest);
                QApp.NextEnd();
            }else{
                QApp.SetStarted(false);
                MessageBox(String.format("Победа! Вы набрали %1$d баллов", QApp.GetPoints()));
            }
        }
        QApp.UpdateInfo();
    }

    public void ResetButton_Click(View v){
        Button RBut = (Button) findViewById(R.id.button3);
        RBut.setEnabled(false);
        Reset();
        RBut.setEnabled(true);
    }

    public void NextButton_Click(View v){
        Button NBut = (Button) findViewById(R.id.button);
        NBut.setEnabled(false);
        Next();
        NBut.setEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
