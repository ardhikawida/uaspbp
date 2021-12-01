package com.ssd.ssd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.ssd.ssd.databinding.ActivityInsertFoodBinding;

public class InsertFoodActivity extends AppCompatActivity {

    ActivityInsertFoodBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_insert_food);
        binding.getLifecycleOwner();


    }
}