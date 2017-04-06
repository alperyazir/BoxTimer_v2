package com.example.admin.boxtimer_v2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce = false;

    private ImageButton buttonReset;
    private ImageButton buttonSettings;
    private ImageButton buttonPlayPause;
    private TextView textRounds;
    private TextView textTimer;
    private TextView textWork;
    private TextView textBrake;

    String round="3";
    String workout="30";
    String brake="15";
    String warning="10";
    String ready="5";

    boolean brakeMode;
    boolean readyMode;
    boolean playMode = true;
    int roundCounter = 1;


    private CountDownTimer countDownTimer = null;
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

        buttonReset = (ImageButton) findViewById(R.id.buttonReset);
        buttonSettings = (ImageButton) findViewById(R.id.buttonSettings);
        buttonPlayPause = (ImageButton) findViewById(R.id.buttonPlayPause);

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
        new AlertDialog.Builder(view.getContext(),1)
                .setTitle("Reset")
                .setMessage("Are you sure you reset timer?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        roundCounter = 1;
                        textRounds.setText("" + roundCounter + "/" + String.format("%01d", Integer.parseInt(round)) );
                        textTimer.setText(""+String.format("%02d", Integer.parseInt(workout)/60) +" "+ String.format("%02d", Integer.parseInt(workout)%60));
                        root.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                        if(countDownTimer != null) countDownTimer.cancel();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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

        if(playMode){
        manageTimer(Integer.parseInt(ready));
        readyMode = true;
        brakeMode = false;
        countDownTimer.start();
        root.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
        buttonPlayPause.setImageResource(R.drawable.pause);
            playMode = false;
        }else{
            buttonPlayPause.setImageResource(R.drawable.play);
        }
    }

    public void manageTimer(int seconds) {
        countDownTimer = new CountDownTimer(seconds * 1000 + 100, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                textTimer.setText(String.format("%02d", minutes) + " " + String.format("%02d", seconds));
                if(brakeMode){ // Workout Interval
                    if(seconds==10){
                        playGong();
                        root.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                    }
                }
                else if(readyMode){
                    textToSpeech.speak(Integer.toString(seconds), TextToSpeech.QUEUE_FLUSH, null);
                }
                else { // In brake Interval
                    if(seconds==10){
                        playGong();
                    }
                }
            }

            public void onFinish() {

                if(brakeMode){
                    root.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                    playGong();
                    manageTimer(Integer.parseInt(brake));
                    brakeMode = false;
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
                        textRounds.setText("Round " + roundCounter + "/" +String.format("%01d", Integer.parseInt(round)));
                    }
                    else if(roundCounter == Integer.parseInt(round)){
                        playGong();
                        toSpeak = "Final Round ";
                        brakeMode = false;
                        manageTimer(Integer.parseInt(workout));
                        countDownTimer.start();
                        textRounds.setText("Round " + roundCounter + "/" +String.format("%01d", Integer.parseInt(round)));
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
