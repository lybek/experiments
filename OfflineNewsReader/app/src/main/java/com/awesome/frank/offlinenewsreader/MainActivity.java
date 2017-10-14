package com.awesome.frank.offlinenewsreader;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> titles = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    SQLiteDatabase articlesDB;
    ArrayList<String> content = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.listView);

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titles);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
                intent.putExtra("content", content.get(position));

                startActivity(intent);
            }
        });

        articlesDB = this.openOrCreateDatabase("articles", MODE_PRIVATE, null);

        articlesDB.execSQL("CREATE TABLE IF NOT EXISTS articles (id INTEGER PRIMARY KEY, articleId INT, title VARCHAR, content VARCHAR)");


        updateListView();

        DownloadTask task = new DownloadTask();

        try {

            task.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty");

        } catch(Exception e) {

            e.printStackTrace();

        }


    }


    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String result = "";
            URL url;
            HttpURLConnection connection;

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

                JSONArray jsonArray = new JSONArray(result);

                int numberOfItems = 20;

                if (jsonArray.length() < 20) {
                    numberOfItems = jsonArray.length();
                }

                articlesDB.execSQL("DELETE FROM articles");

                for (int i = 0; i < numberOfItems; i++) {

                    String articleId = jsonArray.getString(i);

                    url = new URL("https://hacker-news.firebaseio.com/v0/item/" + jsonArray.getString(i) + ".json?print=pretty");

                    connection = (HttpURLConnection) url.openConnection();
                    in = connection.getInputStream();

                    reader = new InputStreamReader(in);

                    String articleInfo = "";

                    data = reader.read();

                    while(data != -1) {

                        char current = (char) data;
                        articleInfo += current;
                        data = reader.read();

                    }

                    JSONObject jsonObject = new JSONObject(articleInfo);

                    String title;
                    String articleUrl;
                    if (!jsonObject.isNull("title") && !jsonObject.isNull("url")) {
                        title = jsonObject.getString("title");
                        articleUrl = jsonObject.getString("url");
                        url = new URL(articleUrl);

                        connection = (HttpURLConnection) url.openConnection();
                        in = connection.getInputStream();

                        reader = new InputStreamReader(in);

                        String articleContent = "";

                        data = reader.read();

                        while(data != -1) {

                            char current = (char) data;
                            articleContent += current;
                            data = reader.read();

                        }

                        System.out.println(articleContent);

                        String sql = "INSERT INTO articles (articleId, title, content) VALUES (?, ?, ?)";

                        SQLiteStatement statement = articlesDB.compileStatement(sql);

                        statement.bindString(1, articleId);
                        statement.bindString(2, title);
                        statement.bindString(3, articleContent);

                        statement.execute();
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            updateListView();
        }
    }

    public void updateListView() {

        Cursor c = articlesDB.rawQuery("SELECT * FROM articles", null);

        int contentIndex = c.getColumnIndex("content");
        int titleIndex = c.getColumnIndex("title");

        if (c.moveToFirst()) {
            titles.clear();
            content.clear();

            do {
                titles.add(c.getString(titleIndex));
                content.add(c.getString(contentIndex));
            } while(c.moveToNext());

            arrayAdapter.notifyDataSetChanged();
        }
    }


}
