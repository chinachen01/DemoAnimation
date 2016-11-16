package com.example.chenyong.demoanimation;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.chenyong.demoanimation.databinding.ActivityPointBinding;

/**
 * Created by focus on 16/11/16.
 */

public class PointActivity extends AppCompatActivity {
    private ActivityPointBinding mBinding;
    private final String TAG = this.getClass().getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_point);
        mBinding.setPresenter(new Presenter());
    }

    public class Presenter {
        public void onClick(final View view) {
            Log.d(TAG, "translationX===>" + view.getTranslationX());
            Log.d(TAG, "X===>" + view.getX());
            Log.d(TAG, "left===>" + view.getLeft());
            Log.d(TAG, "padding===>" + view.getPaddingLeft());
//            AnimatorSet animatorSet = new AnimatorSet();
//            ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", 300);
//            animator.addListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    super.onAnimationEnd(animation);
//                    Log.d(TAG, "translationX===>" + view.getTranslationX());
//                    Log.d(TAG, "X===>" + view.getX());
//                    Log.d(TAG, "left===>" + view.getLeft());
//                    Log.d(TAG, "padding===>" + view.getPaddingLeft());
//                }
//            });
//            ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "translationY", 300);
//            animatorSet.play(animator).with(animator1);
//            animatorSet.setDuration(3000);
//            animatorSet.start();
            view.animate().setDuration(2000)
                    .rotationXBy(180)
                    .start();
//            view.animate().setDuration(2000)
//                    .translationX(300)
//                    .translationY(300)
//                    .setInterpolator(new BounceInterpolator())
//                    .scaleX(2.0f)
//                    .scaleY(0f)
//                    .setListener(new AnimatorListenerAdapter() {
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            super.onAnimationEnd(animation);
//                        }
//                    })
//                    .start();
        }
    }
}
