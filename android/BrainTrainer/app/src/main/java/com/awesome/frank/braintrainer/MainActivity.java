package com.awesome.frank.braintrainer;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button startButton;
    Button button0;
    Button button1;
    Button button2;
    Button button3;
    Button playAgain;
    TextView sum;
    TextView result;
    TextView points;
    TextView timer;
    List<Integer> answers = new ArrayList<>();
    Integer locationOfCorrectAnswer;
    Integer score = 0;
    Integer numberOfAnswers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = (Button) findViewById(R.id.button);
        sum = (TextView) findViewById(R.id.sum);
        result = (TextView) findViewById(R.id.result);
        points = (TextView) findViewById(R.id.points);
        timer = (TextView) findViewById(R.id.timer);
        button0 = (Button) findViewById(R.id.button1);
        button1 = (Button) findViewById(R.id.button2);
        button2 = (Button) findViewById(R.id.button3);
        button3 = (Button) findViewById(R.id.button4);
        playAgain = (Button) findViewById(R.id.playAgain);
    }

    public void generateQuestion() {
        Random random = new Random();
        answers.clear();

        Integer a = random.nextInt(100);
        Integer b = random.nextInt(100);

        sum.setText(a + " + " + b);

        locationOfCorrectAnswer = random.nextInt(4);

        Integer incorrectAnswer;

        for (int i = 0; i < 4; i++) {
            if (i == locationOfCorrectAnswer) {
                answers.add(a + b);
            } else {
                incorrectAnswer = random.nextInt(200);

                while(incorrectAnswer == a + b) {
                    incorrectAnswer = random.nextInt(200);
                }
                answers.add(random.nextInt(200));
            }
        }
        button0.setText(String.valueOf(answers.get(0)));
        button1.setText(String.valueOf(answers.get(1)));
        button2.setText(String.valueOf(answers.get(2)));
        button3.setText(String.valueOf(answers.get(3)));
    }

    public void start(View view) {
        startButton.setVisibility(View.INVISIBLE);
        generateQuestion();
        playAgain(view);
        displayElements();
    }

    public void chooseAnswer(View view) {
        numberOfAnswers++;
        if (answers.get(locationOfCorrectAnswer) == answers.get(Integer.valueOf((view.getTag().toString())))) {
            score++;
            result.setText("Correct!");
        } else {
            result.setText("Wrong!");
        }
        points.setText(score + "/" + numberOfAnswers);

        generateQuestion();
    }

    public void playAgain(View view) {
        points.setText("0/0");
        timer.setText("30s");
        result.setText("");
        playAgain.setVisibility(View.INVISIBLE);


        new CountDownTimer(30000 + 100, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText(String.valueOf(millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                timer.setText("0s");
                result.setText("Your score: " + points.getText().toString());
                playAgain.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    public void displayElements() {
        points.setVisibility(View.VISIBLE);
        timer.setVisibility(View.VISIBLE);
        sum.setVisibility(View.VISIBLE);
        button0.setVisibility(View.VISIBLE);
        button1.setVisibility(View.VISIBLE);
        button2.setVisibility(View.VISIBLE);
        button3.setVisibility(View.VISIBLE);
    }
}
