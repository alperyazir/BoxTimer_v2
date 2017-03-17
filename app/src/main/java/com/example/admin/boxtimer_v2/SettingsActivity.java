package com.example.admin.boxtimer_v2;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity implements EditTimeFragmentDialog.EditNameDialogListener {

    public boolean roundClicked=false;
    public boolean workoutClicked=false;
    public boolean brakeClicked=false;
    public Button buttonRound;
    public Button buttonWorkout;
    public Button buttonBrake;
    public TextView textRound;
    public TextView textWorkout;
    public TextView textBrake;
    public String incomingInterval="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_settings);
        buttonRound     = (Button) findViewById(R.id.buttonRound);
        buttonWorkout   = (Button) findViewById(R.id.buttonWorkout);
        buttonBrake     = (Button) findViewById(R.id.buttonBrake);
        textRound       = (TextView) findViewById(R.id.textViewRound);
        textWorkout     = (TextView) findViewById(R.id.textViewWorkout);
        textBrake       = (TextView) findViewById(R.id.textViewBrake);

    }

    public void buttonRoundClicked(View view){
        roundClicked = true;
        workoutClicked=false;
        brakeClicked=false;
        FragmentManager fm = getSupportFragmentManager();
        EditTimeFragmentDialog alertDialog = EditTimeFragmentDialog.newInstance("Rounds");
        alertDialog.setVal(14);
        alertDialog.show(fm, "fragment_alert");

    }

    public void buttonWorkoutClicked(View view){
        workoutClicked = true;
        roundClicked=false;
        brakeClicked=false;
        FragmentManager fm = getSupportFragmentManager();
        EditTimeFragmentDialog alertDialog = EditTimeFragmentDialog.newInstance("Workout");
//        alertDialog.setVal(5);
        alertDialog.show(fm, "fragment_alert");
    }

    public void buttonBrakeClicked(View view){
        brakeClicked = true;
        roundClicked=false;
        workoutClicked=false;

        FragmentManager fm = getSupportFragmentManager();
        EditTimeFragmentDialog alertDialog = EditTimeFragmentDialog.newInstance("Brake");
//        alertDialog.setVal(2);
        alertDialog.show(fm, "fragment_alert");
    }

    @Override
    public void onFinishEditDialog(String inputText) {
        incomingInterval = inputText;
        if(roundClicked){
            textRound.setText(incomingInterval);
        }
        else if(workoutClicked){
            textWorkout.setText(incomingInterval);
        }
        else if(brakeClicked){
            textBrake.setText(incomingInterval);
        }
    }

}
