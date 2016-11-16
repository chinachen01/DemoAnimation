package com.example.chenyong.demoanimation;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.chenyong.demoanimation.databinding.ActivityTransitionToBinding;

/**
 * Created by focus on 16/11/16.
 */

public class TransitionToActivity extends AppCompatActivity {
    private ActivityTransitionToBinding mBinding;
    private static final String EXTRA_IMAGE = "com.demo.extraImage";
    private static final String EXTRA_TITLE = "com.demo.extraTitle";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_transition_to);
        ViewCompat.setTransitionName(mBinding.image, EXTRA_IMAGE);
        supportPostponeEnterTransition();
        mBinding.image.setImageURI(Uri.parse(getIntent().getStringExtra(EXTRA_IMAGE)));
        handler.sendEmptyMessageDelayed(1, 2000);
    }

    public static void startActivity(AppCompatActivity activity, View transitionImage, ViewModel viewModel) {
        Intent intent = new Intent(activity, TransitionToActivity.class);
        intent.putExtra(EXTRA_IMAGE, viewModel.getImage());
        intent.putExtra(EXTRA_TITLE, viewModel.getText());

        //设置动画view
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, EXTRA_IMAGE);
        ActivityCompat.startActivity(activity, intent, options.toBundle());

    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            supportStartPostponedEnterTransition();
        }
    };

    public void onClick(View view) {
        Uri uri = Uri.parse(getIntent().getStringExtra(EXTRA_IMAGE));
        mBinding.image2.setImageURI(uri);
    }
}
