package com.example.admin.boxtimer_v2;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity implements EditTimeFragmentDialog.EditNameDialogListener {

    public boolean roundClicked=false;
    public boolean workoutClicked=false;
    public boolean brakeClicked=false;
    public boolean warningClicked=false;
    public boolean readyClicked=false;

    public Button buttonRound;
    public ImageButton buttonWorkout;
    public ImageButton buttonBrake;
    public ImageButton buttonWarning;
    public ImageButton buttonReady;

    public TextView textRound;
    public TextView textWorkout;
    public TextView textBrake;
    public TextView textWarning;
    public TextView textReady;

    public String incomingInterval="";

    public int minute = 0;
    public int second = 0;

    FragmentManager fm = getSupportFragmentManager();
    EditTimeFragmentDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_settings);




      //  buttonRound     = (Button) findViewById(R.id.buttonRound);
        buttonWorkout   = (ImageButton) findViewById(R.id.buttonWorkout);
        buttonBrake     = (ImageButton) findViewById(R.id.buttonBrake);
        buttonWarning   = (ImageButton) findViewById(R.id.buttonWarning);
        buttonReady     = (ImageButton) findViewById(R.id.buttonReady);

        textRound       = (TextView) findViewById(R.id.textViewRound);
        textWorkout     = (TextView) findViewById(R.id.textViewWorkout);
        textBrake       = (TextView) findViewById(R.id.textViewBrake);
        textWarning     = (TextView) findViewById(R.id.textViewWarning);
        textReady       = (TextView) findViewById(R.id.textViewReady);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            textRound.setText(extras.getString("round"));
            textWorkout.setText(String.format("%02d", Integer.parseInt(extras.getString("workout"))/60) + ":" + String.format("%02d", Integer.parseInt(extras.getString("workout"))%60 ));
            textBrake.setText(String.format("%02d", Integer.parseInt(extras.getString("brake"))/60) + ":" + String.format("%02d", Integer.parseInt(extras.getString("brake"))%60 ));
            textWarning.setText(String.format("%02d", Integer.parseInt(extras.getString("warning"))/60) + ":" + String.format("%02d", Integer.parseInt(extras.getString("warning"))%60));
            textReady.setText(String.format("%02d", Integer.parseInt(extras.getString("ready"))/60) + ":" + String.format("%02d", Integer.parseInt(extras.getString("ready"))%60 ));
        }

    }

    public void buttonRoundClicked(View view){
        roundClicked = true;
        workoutClicked=false;
        brakeClicked=false;
        warningClicked=false;
        readyClicked=false;
        alertDialog = EditTimeFragmentDialog.newInstance("Rounds");
        alertDialog.isRound(true);

        alertDialog.setTime(Integer.parseInt(textRound.getText().toString()));
        alertDialog.show(fm, "fragment_alert");

    }
    public void buttonRoundDecreaseClicked(View view){
        int round = Integer.parseInt(textRound.getText().toString()) -1;
        if(round < 0)
            round  = 0;

        textRound.setText(String.format("%02d", round));
    }
    public void buttonRoundIncreaseClicked(View view){
        int round = Integer.parseInt(textRound.getText().toString()) +1;
        textRound.setText(String.format("%02d", round));
    }
    public void buttonWorkoutDecreaseClicked(View view){
        int workout = getTimeAsSeconds(textWorkout.getText().toString())-10;
        if(workout < 0)
            workout  = 0;

        textWorkout.setText(String.format("%02d", (workout)/60) + ":" + String.format("%02d", (workout)%60) );
    }
    public void buttonWorkoutIncreaseClicked(View view){
        int workout = getTimeAsSeconds(textWorkout.getText().toString())+10;

        textWorkout.setText(String.format("%02d", (workout)/60) + ":" + String.format("%02d", (workout)%60) );
    }
    public void buttonBrakeDecreaseClicked(View view){
        int brake = getTimeAsSeconds(textBrake.getText().toString())-10;
        if(brake < 0)
            brake  = 0;

        textBrake.setText(String.format("%02d", (brake)/60) + ":" + String.format("%02d", (brake)%60) );
    }
    public void buttonBrakeIncreaseClicked(View view){
        int brake = getTimeAsSeconds(textBrake.getText().toString())+10;

        textBrake.setText(String.format("%02d", (brake)/60) + ":" + String.format("%02d", (brake)%60));
    }
    public void buttonWarningDecreaseClicked(View view){
        int warning = getTimeAsSeconds(textWarning.getText().toString())-10;
        if(warning < 0)
            warning  = 0;

        textWarning.setText(String.format("%02d", (warning)/60) + ":" + String.format("%02d", (warning)%60) );
    }
    public void buttonWarningIncreaseClicked(View view){
        int brake = getTimeAsSeconds(textWarning.getText().toString())+10;

        textWarning.setText(String.format("%02d", (brake)/60) + ":" + String.format("%02d", (brake)%60));
    }
    public void buttonReadyDecreaseClicked(View view){
        int ready = getTimeAsSeconds(textReady.getText().toString())-5;
        if(ready < 0)
            ready  = 0;

        textReady.setText(String.format("%02d", (ready)/60) + ":" + String.format("%02d", (ready)%60) );
    }

    public void buttonReadyIncreaseClicked(View view){
        int brake = getTimeAsSeconds(textReady.getText().toString())+5;

        textReady.setText(String.format("%02d", (brake)/60) + ":" + String.format("%02d", (brake)%60));
    }

    public void buttonWorkoutClicked(View view){
        workoutClicked = true;
        roundClicked=false;
        brakeClicked=false;
        warningClicked=false;
        readyClicked=false;

        alertDialog = EditTimeFragmentDialog.newInstance("Workout");
        alertDialog.setTime(getTimeAsSeconds(textWorkout.getText().toString())/60,getTimeAsSeconds(textWorkout.getText().toString())%60);
        alertDialog.show(fm, "fragment_alert");
    }

    public void buttonBrakeClicked(View view){
        brakeClicked = true;
        roundClicked=false;
        workoutClicked=false;
        warningClicked=false;
        readyClicked=false;

        alertDialog = EditTimeFragmentDialog.newInstance("Brake");
        alertDialog.setTime(getTimeAsSeconds(textBrake.getText().toString())/60,getTimeAsSeconds(textBrake.getText().toString())%60);
        alertDialog.show(fm, "fragment_alert");
    }

    public void buttonWarningClicked(View view){
        brakeClicked = false;
        roundClicked=false;
        workoutClicked=false;
        warningClicked=true;
        readyClicked=false;

        alertDialog = EditTimeFragmentDialog.newInstance("Warning");
        alertDialog.setTime(getTimeAsSeconds(textWarning.getText().toString())/60,getTimeAsSeconds(textWarning.getText().toString())%60);
        alertDialog.show(fm, "fragment_alert");
    }

    public void buttonReadyClicked(View view){
        brakeClicked = false;
        roundClicked=false;
        workoutClicked=false;
        warningClicked=false;
        readyClicked=true;

        alertDialog = EditTimeFragmentDialog.newInstance("Ready");
        alertDialog.setTime(getTimeAsSeconds(textReady.getText().toString())/60,getTimeAsSeconds(textReady.getText().toString())%60);
        alertDialog.show(fm, "fragment_alert");
    }

    public void buttonSaveClicked(View view){
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.putExtra("round",   (textRound.getText().toString()));
        intent.putExtra("workout", ""+getTimeAsSeconds(textWorkout.getText().toString()));
        intent.putExtra("brake",   ""+getTimeAsSeconds(textBrake.getText().toString()));
        intent.putExtra("warning", ""+getTimeAsSeconds(textWarning.getText().toString()));
        intent.putExtra("ready",   ""+getTimeAsSeconds(textReady.getText().toString()));
        startActivity(intent);
        finish();


    }
    @Override
    public void onFinishEditDialog(String inputText) {
        incomingInterval = inputText;
        minute = Integer.parseInt(incomingInterval)/60;
        second = Integer.parseInt(incomingInterval)%60;

        if(roundClicked){
            textRound.setText(String.format("%02d",minute));
        }
        else if(workoutClicked){

            textWorkout.setText(String.format("%02d",  minute) + ":" + String.format("%02d", second));
        }
        else if(brakeClicked){
            textBrake.setText(String.format("%02d", minute) + ":" + String.format("%02d", second));
        }
        else if(warningClicked){
            textWarning.setText(String.format("%02d", minute) + ":" + String.format("%02d", second));
        }
        else if(readyClicked){
            textReady.setText(String.format("%02d", minute) + ":" + String.format("%02d", second));
        }
    }

    public int getTimeAsSeconds(String str){

       return Integer.parseInt(""+ str.charAt(0)+str.charAt(1))*60 + Integer.parseInt(""+ str.charAt(3)+str.charAt(4));
    }
}





