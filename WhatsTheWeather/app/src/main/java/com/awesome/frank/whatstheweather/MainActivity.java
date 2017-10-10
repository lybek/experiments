package com.awesome.frank.whatstheweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    EditText cityName;
    TextView results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = (EditText) findViewById(R.id.editText);
        results = (TextView) findViewById(R.id.results);
    }

    public void findWeather(View view) {

        String encodedCityName;
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(cityName.getWindowToken(), 0);

        if (cityName.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter a city name!", Toast.LENGTH_LONG).show();
            return;
        }

        DownloadTask task = new DownloadTask();
        try {
            encodedCityName = URLEncoder.encode(cityName.getText().toString(), "UTF-8");
            task.execute("http://api.openweathermap.org/data/2.5/weather?q="
                    + encodedCityName + "&APPID=844ff3d954b76850c2c17b042f0fde1d").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            URL url;
            HttpURLConnection connection = null;

            try {
                url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                System.out.println("weather: " + result);

                return result;

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Weather not found", Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);

                String message = "";

                String weatherInfo = jsonObject.getString("weather");
                JSONArray json = new JSONArray(weatherInfo);

                for (int i = 0; i < json.length(); i++) {
                    JSONObject jsonPart = (JSONObject) json.get(i);

                    String main = "";
                    String description = "";

                    main = jsonPart.getString("main");
                    description = jsonPart.getString("description");

                    if (main != "" && description != "") {
                        message += main + ": " + description + "\r\n";
                    }
                }

                if (!message.isEmpty()) {
                    results.setText(message);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
