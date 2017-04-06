package com.example.admin.boxtimer_v2;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce = false;

    private Button buttonReset;
    private Button buttonSettings;
    private Button buttonPlayPause;
    private TextView textRounds;
    private TextView textTimer;
    private TextView textWork;
    private TextView textBrake;

    String round="3";
    String workout="20";
    String brake="15";
    String warning="10";
    String ready="5";

    boolean brakeMode = false ;
    boolean readyMode;
    int roundCounter = 1;


    private CountDownTimer countDownTimer;
    private TextToSpeech textToSpeech;
    View root ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View someView = findViewById(R.id.activity_main);
        root = someView.getRootView();someView.getRootView();

        buttonReset = (Button) findViewById(R.id.buttonReset);
        buttonSettings = (Button) findViewById(R.id.buttonSettings);
        buttonPlayPause = (Button) findViewById(R.id.buttonPlayPause);

        textRounds = (TextView) findViewById(R.id.textRounds);
        textTimer = (TextView) findViewById(R.id.textTimer);
        textWork = (TextView) findViewById(R.id.textWorkout);
        textBrake = (TextView) findViewById(R.id.textBrake);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            round   = extras.getString("round");
            workout = extras.getString("workout");
            brake   = extras.getString("brake");
            warning = extras.getString("warning");
            ready   = extras.getString("ready");
        }
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });

        textRounds.setText("" + roundCounter + "/" + String.format("%01d", Integer.parseInt(round)) );
        textWork.setText("Work " + String.format("%02d", Integer.parseInt(workout)/60) +":"+ String.format("%02d", Integer.parseInt(workout)%60));
        textBrake.setText("Brake "+String.format("%02d", Integer.parseInt(brake)/60) +":"+ String.format("%02d", Integer.parseInt(brake)%60));
        textTimer.setText(""+String.format("%02d", Integer.parseInt(workout)/60) +" "+ String.format("%02d", Integer.parseInt(workout)%60));

        //manageTimer(Integer.parseInt(workout));
    }

    public void buttonResetClicked(View view){
    }

    public void buttonSettingsClicked(View view){
        Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
        intent.putExtra("round",   round);
        intent.putExtra("workout", workout);
        intent.putExtra("brake",   brake);
        intent.putExtra("warning", warning);
        intent.putExtra("ready",   ready);
        startActivity(intent);
    }

    public void buttonPlayPauseClicked(View view){

        manageTimer(Integer.parseInt(ready));
        readyMode = true;
        countDownTimer.start();
        root.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
    }

    public void manageTimer(int seconds) {
        countDownTimer = new CountDownTimer(seconds * 1000 + 100, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                textTimer.setText(String.format("%02d", minutes) + " " + String.format("%02d", seconds));
                if(brakeMode){
                    if(seconds<10){
                        playGong();
                    }
                }
                else if(readyMode){
                    textToSpeech.speak(Integer.toString(seconds), TextToSpeech.QUEUE_FLUSH, null);
                }
                else{ // WorkoutMode
                    if(seconds<10){
                        playGong();
                        root.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    }
                }
            }

            public void onFinish() {

                if(brakeMode){
                    root.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                    playGong();
                    brakeMode = false;
                    manageTimer(Integer.parseInt(brake));
                    countDownTimer.start();
                }
                else{   // WorkoutMode

                    String toSpeak = null;
                    readyMode = false;
                    root.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));

                    if(roundCounter < Integer.parseInt(round)){
                        playGong();
                        toSpeak = "Round " + Integer.toString(roundCounter);
                        brakeMode = true;
                        manageTimer(Integer.parseInt(workout));
                        countDownTimer.start();
                        textRounds.setText("" + roundCounter + "/" +String.format("%01d", Integer.parseInt(round)));
                    }
                    else if(roundCounter == Integer.parseInt(round)){
                        playGong();
                        toSpeak = "Final Round ";
                        brakeMode = false;
                        manageTimer(Integer.parseInt(workout));
                        countDownTimer.start();
                        textRounds.setText("Round" + roundCounter + "/" +String.format("%01d", Integer.parseInt(round)));
                    }
                    else {
                        countDownTimer.cancel();
                    }

                    textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                    roundCounter++;
                }
            }
//                playGong();
//                if (brakeMode) {
//                    manageTimer(Integer.parseInt(brake));
//                    countDownTimer.start();
//                    brakeMode = false;
//                    //root.setBackgroundColor(Color.RED);
//                } else {
//                    roundCounter++;
//                    String toSpeak;
//                    if (roundCounter == Integer.parseInt(round)) {
//                        toSpeak= "Round Final";
//                    }
//                    else {
//                         toSpeak = "Round " + Integer.toString(roundCounter);
//                    }
//
//
//                    if (roundCounter > Integer.parseInt(round)) {
//                        countDownTimer.cancel();
//                    } else {
//
//                        manageTimer(Integer.parseInt(workout));
//                        countDownTimer.start();
//                        //root.setBackgroundColor(Color.GREEN);
//                    }
//
//                    brakeMode = true;
//                    textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
//                    textRounds.setText("" + roundCounter + "/" +String.format("%01d", Integer.parseInt(round)));
//                }
//            }

        };
    }
    public void playGong() {
        MediaPlayer mp = MediaPlayer.create(this,R.raw.gong );
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
