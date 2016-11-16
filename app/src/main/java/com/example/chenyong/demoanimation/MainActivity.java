package com.example.chenyong.demoanimation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.chenyong.demoanimation.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mBinding;
    private final String TAG = this.getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        Presenter presenter = new Presenter();
        mBinding.setPresenter(presenter);
    }
    private void simpleAlpha() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (float) animation.getAnimatedValue();
                Log.d(TAG, "animation value:" + currentValue);
            }
        });
        animator.start();
    }

    private void simpleAlpha1() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mBinding.textAlphaDemo, "alpha", 1.0f, 0f, 1f);
        animator.setDuration(3000);
        animator.setRepeatCount(10);
        animator.start();
    }

    private void simpleRotation() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mBinding.textAlphaDemo, "rotation", 0f, 360f);
        animator.setDuration(5000);
        animator.start();
    }

    private void simpleTranslationX() {
        float currentX = mBinding.textAlphaDemo.getTranslationX();
        int offsetX = (int) mBinding.textAlphaDemo.getX();
        int offset = mBinding.textAlphaDemo.getMeasuredWidth() + offsetX;
        ObjectAnimator animator = ObjectAnimator.ofFloat(mBinding.textAlphaDemo, "translationX", currentX, -offset, currentX);
        animator.setDuration(3000);
        animator.start();
    }

    private void composeAnimation() {
        float currentX = mBinding.textAlphaDemo.getTranslationX();
        int offsetX = (int) mBinding.textAlphaDemo.getX();
        int offset = mBinding.textAlphaDemo.getMeasuredWidth() + offsetX;
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(mBinding.textAlphaDemo, "translationX", currentX, -offset, currentX);
        translationAnimator.setDuration(3000);
        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(mBinding.textAlphaDemo, "rotation", 0f, 360f);
        rotationAnimator.setDuration(3000);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mBinding.textAlphaDemo, "alpha", 1f, 0f, 1f);
        alphaAnimator.setDuration(3000);
        alphaAnimator.setRepeatCount(3);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(translationAnimator).with(rotationAnimator).before(alphaAnimator);
        animatorSet.start();
    }

    private void simpleAnimatorObject() {
        Point point1 = new Point(0, 0);
        Point point2 = new Point(300, 300);
        ValueAnimator animator = ValueAnimator.ofObject(new PointEvaluator(), point1, point2);
        animator.setDuration(3000);
        animator.start();
    }
    public class Presenter {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.button_simple_alpha:
                    simpleAlpha();
                    break;
                case R.id.button_simple_alpha1:
                    simpleAlpha1();
                    break;
                case R.id.button_simple_rotation:
                    simpleRotation();
                    break;
                case R.id.button_simple_translationX:
                    simpleTranslationX();
                    break;
                case R.id.button_simple_compose:
                    composeAnimation();
                    break;
                case R.id.button_simple_object:
                    simpleAnimatorObject();
                    break;
                case R.id.button_next:
                    Intent intent = new Intent(MainActivity.this, CircleActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }
}
