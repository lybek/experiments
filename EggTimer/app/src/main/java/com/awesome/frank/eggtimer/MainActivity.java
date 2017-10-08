package com.awesome.frank.eggtimer;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    SeekBar timerBar;
    TextView timer;
    Button button;
    CountDownTimer countDownTimer;
    Boolean isRunning = false;
    ImageView image;
    TextView warning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timer = (TextView) findViewById(R.id.timer);
        timerBar = (SeekBar) findViewById(R.id.seekBar);
        image = (ImageView) findViewById(R.id.imageView);
        warning = (TextView) findViewById(R.id.warning);

        timerBar.setMax(600);
        timerBar.setProgress(420);

        updateTimer(timerBar.getProgress());

        timerBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                image.setImageResource(R.drawable.egg);
                warning.setVisibility(View.INVISIBLE);
                timer.setVisibility(View.VISIBLE);
                updateTimer(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void toggleTimer(View view) {

        timer.setVisibility(View.VISIBLE);
        warning.setVisibility(View.INVISIBLE);
        toggleSeekBarEnabled(timerBar);
        if (isRunning) {
            countDownTimer.cancel();
            updateTimer(timerBar.getProgress());
            button.setText("Start");
            //timerBar.setEnabled(true);
            isRunning = false;
            return;
        }

        image.setImageResource(R.drawable.egg);

        button = (Button)findViewById(R.id.goButton);
        //toggleSeekBarEnabled(timerBar);
        button.setText("Stop");
        isRunning = true;

        countDownTimer = new CountDownTimer(timerBar.getProgress() * 1000 + 100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                updateTimer((int) millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                isRunning = false;
                timerBar.setProgress(420);
                updateTimer(timerBar.getProgress());
                timerBar.setEnabled(true);
                MediaPlayer mplayer = MediaPlayer.create(getApplicationContext(), R.raw.airhorn);
                mplayer.start();
                button.setText("Start");
                image.setImageResource(R.drawable.rooster);
                warning.setVisibility(View.VISIBLE);
                warning.setText("Your eggs are done. \nBetter not forget them!");
                timer.setVisibility(View.INVISIBLE);

            }
        }.start();
    }

    public void updateTimer(Integer secondsLeft) {
        Integer minutes = secondsLeft / 60;
        Integer seconds = secondsLeft - minutes * 60;
        timerBar.setProgress(secondsLeft);

        timer.setText((minutes < 10 ? "0" + minutes : minutes) + ":" + (seconds < 10 ? "0" + seconds : seconds));
    }

    public void toggleSeekBarEnabled(SeekBar seekbar) {
        if (isRunning) {
            seekbar.setOnTouchListener(null);
        } else {
            seekbar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }
    }
}
