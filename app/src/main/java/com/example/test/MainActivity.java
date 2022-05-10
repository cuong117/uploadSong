package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.test.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private Button uploadbt;
    private Button listen;
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        uploadbt = binding.upload;
        listen = binding.listen;
        setContentView(binding.getRoot());

        uploadbt.setOnClickListener(view -> {
                startActivity(new Intent(this, Upload.class));
        });

        listen.setOnClickListener(view -> startActivity(new Intent(this, player.class)));
    }


}