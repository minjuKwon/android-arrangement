package com.example.contentprovider;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.contentprovider.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity{

    private ActivityMainBinding binding;
    private NotesRecyclerviewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter=new NotesRecyclerviewAdapter(this);
        binding.javaList.setLayoutManager(new LinearLayoutManager(this));
        binding.kotlinList.setLayoutManager(new LinearLayoutManager(this));
        binding.javaList.setAdapter(adapter);
        binding.kotlinList.setAdapter(adapter);

        binding.btnDelete.setOnClickListener(v->deleteData());
        binding.btnInsert.setOnClickListener(v->insertData());
        binding.btnUpdate.setOnClickListener(v->updateData());
    }

    public void deleteData(){

    }

    public void insertData(){

    }

    public void updateData(){

    }

}