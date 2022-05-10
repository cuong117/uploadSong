package com.example.test;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaMetadata;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.test.databinding.ActivityUploadBinding;
import com.example.test.model.Song;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class Upload extends AppCompatActivity {
    private ActivityUploadBinding binding;
    private Uri audioUri;
    private Song uploadSong;
    private Button file;
    private Button up;
    private StorageReference store;
    private StorageTask uploadTask;
    private DatabaseReference db;

    ActivityResultLauncher<String> getContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            audioUri = result;
            uploadSong = getData(result);
            file.setText(uploadSong.getTitle());
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        file = binding.inputFile;
        up = binding.uploadBtn;
        db = FirebaseDatabase.getInstance().getReference().child("songs");
        store = FirebaseStorage.getInstance().getReference().child("songs");

        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContent.launch("audio/*");
                Toast.makeText(Upload.this, "clicked", Toast.LENGTH_SHORT).show();
            }
        });

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StorageReference storageReference = store.child(System.currentTimeMillis() + "");
                uploadTask = storageReference.putFile(audioUri);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                uploadSong.setLink(uri.toString());
                                String id = db.push().getKey();
                                db.child(id).setValue(uploadSong);
                                Toast.makeText(Upload.this, uri.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
            }
        });
    }

    public Song getData(Uri audioUri){
        Toast.makeText(Upload.this, audioUri.getPath(), Toast.LENGTH_SHORT).show();
        ContentResolver contentResolver = getContentResolver();
        Song s = new Song();
        Cursor cursor = contentResolver.query(audioUri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()){
            int songTitle = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
//            int songPath = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            s = new Song(cursor.getString(songTitle), cursor.getString(songArtist));
        }
        return s;
    }

}