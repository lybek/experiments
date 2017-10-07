package com.awesome.frank.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.stream.IntStream;

public class MainActivity extends AppCompatActivity {

    int activePlayer = 0;
    int[] board = {2,2,2,2,2,2,2,2,2};
    int[][] winningPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};
    boolean gameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void dropIn(View view) {

        //Arrays.stream(board).noneMatch(s -> s == 2);

        if (gameOver) {
            return;
        }
        ImageView counter = (ImageView) view;

        int tag = Integer.parseInt(counter.getTag().toString());

        if (board[tag] == 2) {
            if (activePlayer == 0) {

                counter.setImageResource(R.drawable.yellow);
                board[tag] = activePlayer;
                activePlayer = 1;
            } else {
                counter.setImageResource(R.drawable.red);
                board[tag] = activePlayer;
                activePlayer = 0;
            }

            for(int[] winningPosition : winningPositions) {
                if (board[winningPosition[0]] == board[winningPosition[1]] && board[winningPosition[0]] == board[winningPosition[2]] &&
                        board[winningPosition[0]] != 2) {
                    Toast.makeText(this.getApplicationContext(), "Player " + board[winningPosition[0]] + " has won!", Toast.LENGTH_LONG).show();
                    gameOver = true;
                    Button restartButton = (Button) findViewById(R.id.restart);
                    restartButton.setVisibility(View.VISIBLE);
                }
            }
        }

        for(int i = 0; i < board.length; i++) {
            if (board[i] != 2) break;

            if(board[i] != 2 && i == board.length -1) {
                gameOver = true;
                Toast.makeText(this.getApplicationContext(), "Draw!", Toast.LENGTH_LONG).show();
                Button restert = (Button) findViewById(R.id.restart);
                restert.setVisibility(View.VISIBLE);
            }
        }

        counter.animate().alpha(0f);
        counter.animate().alpha(1f).setDuration(4000);
    }

    public void restart(View view) {
        gameOver = false;

        for (int i = 0; i < board.length; i++) {

            board[i] = 2;

        }
        ImageView img1 =  (ImageView) findViewById(R.id.imageView1);
        img1.setImageResource(0);
        ImageView img2 =  (ImageView) findViewById(R.id.imageView10);
        img2.setImageResource(0);
        ImageView img3 =  (ImageView) findViewById(R.id.imageView3);
        img3.setImageResource(0);
        ImageView img4 =  (ImageView) findViewById(R.id.imageView4);
        img4.setImageResource(0);
        ImageView img5 =  (ImageView) findViewById(R.id.imageView5);
        img5.setImageResource(0);
        ImageView img6 =  (ImageView) findViewById(R.id.imageView6);
        img6.setImageResource(0);
        ImageView img7 =  (ImageView) findViewById(R.id.imageView7);
        img7.setImageResource(0);
        ImageView img8 =  (ImageView) findViewById(R.id.imageView8);
        img8.setImageResource(0);
        ImageView img9 =  (ImageView) findViewById(R.id.imageView9);
        img9.setImageResource(0);
        Button restertButton = (Button) findViewById(R.id.restart);
        restertButton.setVisibility(View.INVISIBLE);
    }
}
