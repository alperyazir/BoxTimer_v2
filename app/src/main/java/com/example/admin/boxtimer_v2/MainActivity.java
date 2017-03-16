package com.example.admin.boxtimer_v2;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button buttonReset;
    private Button buttonSettings;
    private Button buttonPlayPause;
    private TextView textRounds;
    private TextView textTimer;
    private TextView textWork;
    private TextView textBrake;


    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonReset = (Button) findViewById(R.id.buttonReset);
        buttonSettings = (Button) findViewById(R.id.buttonSettings);
        buttonPlayPause = (Button) findViewById(R.id.buttonPlayPause);

        textRounds = (TextView) findViewById(R.id.textRounds);
        textTimer = (TextView) findViewById(R.id.textTimer);
        textWork = (TextView) findViewById(R.id.textWorkout);
        textBrake = (TextView) findViewById(R.id.textBrake);
    }

    public void buttonResetClicked(View view){

    }

    public void buttonSettingsClicked(View view){
        startActivity(new Intent(this,SettingsActivity.class));
    }

    public void buttonPlayPauseClicked(View view){
    }


    public void manageTimer(int seconds) {
        countDownTimer = new CountDownTimer(seconds * 1000 + 100, 1000) {
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                textTimer.setText(String.format("%02d", minutes) + " " + String.format("%02d", seconds));
                Log.d("time",""+seconds);
            }

            public void onFinish() {


            }
        }.start();
    }

}
