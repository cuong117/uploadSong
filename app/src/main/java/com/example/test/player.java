package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.test.databinding.ActivityPlayerBinding;
import com.example.test.model.Song;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

public class player extends AppCompatActivity {
    private ActivityPlayerBinding binding;
    private Button play;
    private ArrayList<Song> songs = new ArrayList<Song>();
    private DatabaseReference db;
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        play = binding.play;
        db = FirebaseDatabase.getInstance().getReference().child("songs");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dt: snapshot.getChildren()){
                    Song s = dt.getValue(Song.class);
                    songs.add(s);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer = new MediaPlayer();
                try {
                    if(songs.isEmpty()){
                        Toast.makeText(player.this, "loading list", Toast.LENGTH_SHORT).show();
                    }
                    else if (songs.get(0).getLink() == null){
                        Toast.makeText(player.this, "empty link", Toast.LENGTH_SHORT).show();
                    }
                    else{mediaPlayer.setDataSource(songs.get(0).getLink());
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mediaPlayer.start();
                            }
                        });
                        mediaPlayer.prepare();}
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}