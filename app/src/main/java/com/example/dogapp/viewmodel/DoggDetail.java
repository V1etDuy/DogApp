package com.example.dogapp.viewmodel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.dogapp.R;
import com.example.dogapp.databinding.ActivityDoggDetailBinding;
import com.example.dogapp.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;

public class DoggDetail extends AppCompatActivity {
    private String url;
    private String name;
    private String lifeSpan;
    private String origin;
    private ActivityDoggDetailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoggDetailBinding.inflate(getLayoutInflater());
        View viewRoot = binding.getRoot();
        setContentView(viewRoot);

        Intent getIntent = getIntent();
        url = getIntent.getStringExtra("url");
        name = getIntent.getStringExtra("name");
        lifeSpan =getIntent.getStringExtra("lifeSpan");
        origin =getIntent.getStringExtra("origin");

        setUp();
    }
    private void setUp(){
        Picasso.get().
                load(url).
                into(binding.ivPhoto);
        binding.edtName.setText(name);
        binding.edtLifeSpan.setText(lifeSpan);
        binding.edtOrigin.setText(origin);
    }
}