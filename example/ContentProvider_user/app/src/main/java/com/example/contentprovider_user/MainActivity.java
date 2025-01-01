package com.example.contentprovider_user;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.contentprovider_user.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnDelete.setOnClickListener(v->deleteAllData());
        binding.btnQuery.setOnClickListener(v->queryData());
    }

    public void deleteAllData(){

    }

    public void queryData(){

    }

}