package com.example.musik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private MediaPlayer mediaPlayer;
    private Button playButton;
    private Button pauseButton;
    private Button stopButton;
    private ListView songListView;
    private List<String> songTitles = new ArrayList<>();
    private List<Integer> tracks = new ArrayList<>();
    private int currentTrackIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playButton = findViewById(R.id.play_button);
        pauseButton = findViewById(R.id.pause_button);
        stopButton = findViewById(R.id.stop_button);
        songListView = findViewById(R.id.song_list);

        playButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);

        songListView.setOnItemClickListener(this);

        loadTracks();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, songTitles);
        songListView.setAdapter(adapter);

        mediaPlayer = MediaPlayer.create(this, tracks.get(currentTrackIndex));
    }

    private void loadTracks() {
        Field[] rawFields = R.raw.class.getFields();
        Resources resources = getResources();

        for (Field field : rawFields) {
            try {
                int resourceId = resources.getIdentifier(field.getName(), "raw", getPackageName());
                tracks.add(resourceId);
                String songTitle = field.getName(); // Assume the resource file name is the song title
                songTitles.add(songTitle);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.play_button) {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
        } else if (v.getId() == R.id.pause_button) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
        } else if (v.getId() == R.id.stop_button) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.prepareAsync(); // Prepare the MediaPlayer for future playback
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        currentTrackIndex = position;
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(this, tracks.get(currentTrackIndex));
        mediaPlayer.start();
    }
}