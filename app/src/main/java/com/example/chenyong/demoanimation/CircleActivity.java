package com.example.chenyong.demoanimation;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.chenyong.demoanimation.databinding.ActivityCircleBinding;

/**
 * Created by chenyong on 16-11-15.
 */

public class CircleActivity extends AppCompatActivity {
    private ActivityCircleBinding mBinding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_circle);
    }
}
