package com.awesome.frank.guessthecelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    Button button1;
    Button button2;
    Button button3;
    Button button4;
    ImageView celebImage;
    ArrayList<String> celebrityUrls = new ArrayList<>();
    ArrayList<String> celebrityNames = new ArrayList<>();
    Integer chosenCelebrity = 0;
    Integer locationOfCorrectAnswer = 0;
    String[] answers = new String[4];
    String[] splitResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = (Button)findViewById(R.id.button1);
        button2 = (Button)findViewById(R.id.button2);
        button3 = (Button)findViewById(R.id.button3);
        button4 = (Button)findViewById(R.id.button4);
        celebImage = (ImageView) findViewById(R.id.imageView);

        DownloadTask task = new DownloadTask();
        String result = null;
        try {
            result = task.execute("http://www.posh24.se/kandisar").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        splitResult = result.split("<div class=\"sidebarContainer\"");

        getQuestion(splitResult);
    }

    public void getQuestion(String[] splitResult) {

        Pattern p = Pattern.compile("<img src=\"(.*?)\"");
        Matcher m = p.matcher(splitResult[0]);

        while(m.find()) {
            celebrityUrls.add(m.group(1));
        }

        p = Pattern.compile("alt=\"(.*?)\"");
        m = p.matcher(splitResult[0]);

        while (m.find()) {
            celebrityNames.add(m.group(1));
        }

        Random random = new Random();
        chosenCelebrity = random.nextInt(celebrityUrls.size());

        ImageDownloader imageDownloader = new ImageDownloader();
        Bitmap celebImageBitMap = null;
        try {
            celebImageBitMap = imageDownloader.execute(celebrityUrls.get(chosenCelebrity)).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        celebImage.setImageBitmap(celebImageBitMap);

        locationOfCorrectAnswer = random.nextInt(4);

        for (int i = 0; i < 4; i++) {

            if (i == locationOfCorrectAnswer) {
                answers[i] = celebrityNames.get(chosenCelebrity);
            } else {
                Integer incorrect = random.nextInt(celebrityUrls.size());
                while (incorrect == chosenCelebrity) {
                    incorrect = random.nextInt(celebrityUrls.size());
                }
                answers[i] = celebrityNames.get(incorrect);
            }
        }

        button1.setText(answers[0]);
        button2.setText(answers[1]);
        button3.setText(answers[2]);
        button4.setText(answers[3]);

    }

    public void chooseAnswer(View view) {

        if (view.getTag().toString().equalsIgnoreCase(String.valueOf(locationOfCorrectAnswer))) {
            Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getApplicationContext(), "Wrong! It was " + celebrityNames.get(locationOfCorrectAnswer), Toast.LENGTH_LONG).show();
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        getQuestion(splitResult);
    }

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {

            try {

                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream in = connection.getInputStream();
                Bitmap bitMap = BitmapFactory.decodeStream(in);

                return bitMap;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }


    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection connection;

            try {
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
