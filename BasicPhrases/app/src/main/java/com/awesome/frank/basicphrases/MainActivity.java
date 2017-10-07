package com.awesome.frank.basicphrases;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buttonTapped(View view) {

        String id = view.getResources().getResourceEntryName(view.getId());

        int soundResourceID = getResources().getIdentifier(id, "raw", "com.awesome.frank.basicphrases");

        MediaPlayer mplayer = MediaPlayer.create(this, soundResourceID);
        mplayer.start();
    }
}
