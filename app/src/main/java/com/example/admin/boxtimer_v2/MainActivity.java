package com.example.admin.boxtimer_v2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import com.example.admin.boxtimer_v2.ReadyAnimation.CountDownListener;

public class MainActivity extends AppCompatActivity implements CountDownListener{

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
    String ready="3";

    boolean brakeMode;
    boolean readyMode;
    boolean playMode = true;
    int roundCounter = 1;
    int pauseTime;
    boolean isPaused = false;


    private CountDownTimer countDownTimer = null;
    private TextToSpeech textToSpeech;
    View root ;

    private ReadyAnimation readyAni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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

        textRounds.setText("Round " + roundCounter + "/" + String.format("%01d", Integer.parseInt(round)) );
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
                        playMode = true;
                        textRounds.setText("" + roundCounter + "/" + String.format("%01d", Integer.parseInt(round)) );
                        textTimer.setText(""+String.format("%02d", Integer.parseInt(workout)/60) +" "+ String.format("%02d", Integer.parseInt(workout)%60));
                        root.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                        buttonPlayPause.setImageResource(R.drawable.play);
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
            initCountDownAnimation();
            startCountDownAnimation(2);
           //manageTimer(Integer.parseInt(ready));
           playMode = false;
           readyMode = true;
           brakeMode = false;
           //countDownTimer.start();
           if(Integer.parseInt(ready) > 0)
               root.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
           buttonPlayPause.setImageResource(R.drawable.pause);

        }else{
            isPaused = true;
            playMode = true;
            String str = textTimer.getText().toString();
            pauseTime = Integer.parseInt(""+ str.charAt(0)+str.charAt(1))*60 + Integer.parseInt(""+ str.charAt(3)+str.charAt(4));
            buttonPlayPause.setImageResource(R.drawable.play);
            countDownTimer.cancel();
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
                        playGong(1);
                        root.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                    }
                }
                else if(readyMode){
                    textToSpeech.speak(Integer.toString(seconds), TextToSpeech.QUEUE_FLUSH, null);
                }
                else { // In brake Interval
                    if(seconds==10){
                        playGong(1);
                    }
                }
            }

            public void onFinish() {

                if(Integer.parseInt(brake) > 0 && brakeMode){
                    brakeMode = false;
                    root.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                    playGong(0);
                    manageTimer(Integer.parseInt(brake));
                    countDownTimer.start();

                }
                else if(readyMode){
                    cancelCountDownAnimation();

                }
                else{   // WorkoutMode

                    String toSpeak = null;
                    textRounds.setText("Round " + roundCounter + "/" +String.format("%01d", Integer.parseInt(round)));
                    readyMode = false;

                    if(roundCounter < Integer.parseInt(round)){
                        playGong(0);
                        toSpeak = "Round " + Integer.toString(roundCounter);
                        brakeMode = true;
                        if(isPaused){
                            manageTimer(pauseTime);
                            isPaused = false;
                        }
                        else{
                            manageTimer(Integer.parseInt(workout));
                            textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                            roundCounter++;
                        }
                        countDownTimer.start();
                    }
                    else if(roundCounter == Integer.parseInt(round)){
                        playGong(0);
                        toSpeak = "Final Round ";
                        textRounds.setText("Round " + roundCounter + "/" +String.format("%01d", Integer.parseInt(round)));
                        brakeMode = true;
                        if(isPaused){
                            manageTimer(pauseTime);
                            isPaused = false;
                        }
                        else{
                            manageTimer(Integer.parseInt(workout));
                            textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                            roundCounter++;
                        }
                        countDownTimer.start();


                    }
                    else {
                        countDownTimer.cancel();
                    }
                    root.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));

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

    public void playGong(int i) {
        MediaPlayer mp = null;
        if(i == 0) {
            mp = MediaPlayer.create(this, R.raw.gong);
        }else if(i == 1) {
            mp = MediaPlayer.create(this, R.raw.warning);
        }
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

    private void initCountDownAnimation() {
        readyAni = new ReadyAnimation(textRounds, Integer.parseInt(ready));
        readyAni.setCountDownListener(this);
    }

    private void startCountDownAnimation(int i) {
        // Customizable animation
        if (i == 1) { // Scale
            // Use scale animation
            Animation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f,
                    0.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            readyAni.setAnimation(scaleAnimation);
        } else if (i == 2) { // Set (Scale +
            // Alpha)
            // Use a set of animations
            Animation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f,
                    0.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            Animation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
            AnimationSet animationSet = new AnimationSet(false);
            animationSet.addAnimation(scaleAnimation);
            animationSet.addAnimation(alphaAnimation);
            readyAni.setAnimation(animationSet);
        }

        // Customizable start count
        readyAni.setStartCount(Integer.parseInt(ready));

        readyAni.start();
    }

    private void cancelCountDownAnimation() {
        readyAni.cancel();
    }

    /**
     * Notifies the end of the count down animation.
     *
     * @param animation The count down animation which reached its end.
     */
    @Override
    public void onCountDownEnd(ReadyAnimation animation) {

        manageTimer(Integer.parseInt(workout));
        countDownTimer.start();
        readyMode = false;
    }
}
