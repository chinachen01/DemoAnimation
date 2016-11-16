package com.example.chenyong.demoanimation;

import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by focus on 16/11/16.
 */

public class RocketActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener{
    private static final String TAG = "MainActivity";
    private static final long SCALE_ANIM_TIME = 2000;
    private static final int FIX_Y_HEIGHT = 30;
    private static final long SLIDE_TO_BOUNDARY_TIME = 500;

    // 用于控制震动的节奏
    private static final long[] VIBRATOR_PATTERN = {
            10, 30, 20, 30, 30, 30, 40, 30
    };
    private static final int VIRBRATE_WIDTH = 280;
    private static final int VIRBRATE_HEIGHT = 180;
    private int mVirbrateWidth, mVirbrateHeight;

    private int mFixYHeight;
    private View mValueAnimatorButton;
    private View mObjectAnimatorButton;

    private View mTestImage;
    private View mFloatWindow;
    private WindowManager mWindowManager;
    private LayoutParams mIconLayoutParams;

    private int mScreenWidth;
    private int mScreenHeight;
    private int mIconWidth, mIconHeight;

    private ImageView mIconImage;
    private View mIconContent;
    private int mStatusBarHeight;
    private AnimationDrawable mFLoatRocketDrabable;
    private Drawable mCircleDrawable;

    // 振动器
    private Vibrator mVibrator;
    private LayoutParams mRocketLayoutParams;
    private View mRocketContent;

    private boolean mIsIconContentAdded, mIsRocketContentAdded;
    private View mRocketPad;
    private ImageView mRocketFire;
    private AnimationDrawable mFireDrawable;
    // 火箭底座
    private View mSmokePad;
    // 烟雾线
    private View mSmoketop;
    // 起飞的火箭
    private View mBottomRocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rocket);
        initViews();
        mFixYHeight = UiUtils.dipToPx(getApplicationContext(), FIX_Y_HEIGHT);
        mVirbrateWidth = UiUtils.dipToPx(getApplicationContext(), VIRBRATE_WIDTH);
        mVirbrateHeight = UiUtils.dipToPx(getApplicationContext(), VIRBRATE_HEIGHT);

        mFLoatRocketDrabable = (AnimationDrawable) getResources().getDrawable(
                R.drawable.float_rocket);
        mCircleDrawable = getResources().getDrawable(R.drawable.circle);
    }

    private void initViews() {
        mValueAnimatorButton = findViewById(R.id.value_animator);
        mValueAnimatorButton.setOnClickListener(this);

        mObjectAnimatorButton = findViewById(R.id.object_animator);
        mObjectAnimatorButton.setOnClickListener(this);

        mTestImage = findViewById(R.id.test_image);
        mFloatWindow = findViewById(R.id.float_window_animator);
        mFloatWindow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mValueAnimatorButton) {
            // 在2S内将view横向拉长为2倍，纵向压缩为0
            // 创建0－1的一个过程,任何复杂的过程都可以采用归一化，然后在addUpdateListener回调里去做自己想要的变化
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            // 设置过程的时间为2S
            valueAnimator.setDuration(SCALE_ANIM_TIME);
            // 设置这个过程是速度不断变快的
            valueAnimator.setInterpolator(new AccelerateInterpolator());
            // 这个过程中不断执行的回调
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    // 不断回调的在0-1这个范围内，经过插值器插值之后的返回值
                    float value = (Float) animation.getAnimatedValue();
                    // ViewHelper可直接用于修改view属性
                    // 将宽在2S内放大一倍
                    ViewHelper.setScaleX(mTestImage, 1 + value);
                    // 将高在2S内压缩为0
                    ViewHelper.setScaleY(mTestImage, 1 - value);
                }
            });
            // AnimatorListenerAdapter是AnimatorListener的空实现，根据需要覆盖的方法自行选择
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    Toast.makeText(getApplicationContext(), "onAnimationStart", Toast.LENGTH_SHORT)
                            .show();
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    Toast.makeText(getApplicationContext(), "onAnimationEnd", Toast.LENGTH_SHORT)
                            .show();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    super.onAnimationCancel(animation);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    super.onAnimationRepeat(animation);
                }
            });
            valueAnimator.start();
        } else if (v == mObjectAnimatorButton) {
            AnimatorSet animatorSet = new AnimatorSet();
            // 将view在x方向上从原大小放大2倍
            ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(mTestImage, "scaleX", 1, 2);
            scaleXAnimator.setDuration(SCALE_ANIM_TIME);
            // 将view在y方向上从原大小压缩为0
            ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(mTestImage, "scaleY", 1, 0);
            scaleYAnimator.setDuration(SCALE_ANIM_TIME);
            // 设置加速模式
            animatorSet.setInterpolator(new AccelerateInterpolator());
            // 设置回调，当然也可以设置在单独的animator上，eg：scaleXAnimator
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    Toast.makeText(getApplicationContext(), "onAnimationStart", Toast.LENGTH_SHORT)
                            .show();
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    Toast.makeText(getApplicationContext(), "onAnimationEnd", Toast.LENGTH_SHORT)
                            .show();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    super.onAnimationCancel(animation);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    super.onAnimationRepeat(animation);
                }
            });
            animatorSet.playTogether(scaleXAnimator, scaleYAnimator);
            animatorSet.start();
        } else if (v == mFloatWindow) {
            if (mWindowManager == null) {
                // 使用WindowManager添加window需要android.permission.SYSTEM_ALERT_WINDOW权限
                mWindowManager = (WindowManager) this.getSystemService(WINDOW_SERVICE);
                mIconLayoutParams = new WindowManager.LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT, LayoutParams.TYPE_SYSTEM_ALERT,
                        LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSPARENT);
                mIconLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;

                mRocketLayoutParams = new WindowManager.LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT, LayoutParams.TYPE_PHONE,
                        LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSPARENT);
                mRocketLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;

                mScreenWidth = mWindowManager.getDefaultDisplay().getWidth();
                mScreenHeight = mWindowManager.getDefaultDisplay().getHeight();

                mIconLayoutParams.x = 0;
                // 将浮窗的位置初始化在中间
                mIconLayoutParams.y = mScreenHeight / 2;

                mIconContent = View.inflate(this, R.layout.float_icon_layout, null);
                mIconContent.setOnTouchListener(this);
                mIconWidth = mIconContent.getWidth();
                mIconHeight = mIconContent.getHeight();

                mRocketContent = View.inflate(this, R.layout.float_rocket_layout, null);
                mRocketPad = mRocketContent.findViewById(R.id.rocket_pad);
                mRocketFire = (ImageView) mRocketContent.findViewById(R.id.rocket_fire);
                mFireDrawable = (AnimationDrawable) mRocketFire.getDrawable();
                mSmokePad = mRocketContent.findViewById(R.id.desktop_smoke_m);
                mSmoketop = mRocketContent.findViewById(R.id.desktop_smoke_t);
                mBottomRocket = mRocketContent.findViewById(R.id.rocket_bottom);

                // 计算通知栏高度
                View rootView = mIconContent.getRootView();
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                mStatusBarHeight = r.top;
                mStatusBarHeight = (mStatusBarHeight == 0) ? mFixYHeight : mStatusBarHeight;
            }
            if (mVibrator == null) {
                // 使用震动需要android.permission.VIBRATE权限
                mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            }
            log("mStatusBarHeight = " + mStatusBarHeight);

            mIconImage = (ImageView) mIconContent.findViewById(R.id.float_image);
            if (!mIsIconContentAdded) {
                mWindowManager.addView(mIconContent, mIconLayoutParams);
                mIsIconContentAdded = true;
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float rawX = event.getRawX();
        float rawY = event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                moveIconContent(rawX, rawY);
                mIconImage.setImageDrawable(mFLoatRocketDrabable);
                // 为了确保任何手机上都能顺利播放，而不卡在第一帧
                mFLoatRocketDrabable.stop();
                mFLoatRocketDrabable.start();

                if (!mIsRocketContentAdded) {
                    // 添加火箭界面
                    mWindowManager.addView(mRocketContent, mRocketLayoutParams);
                    mIsRocketContentAdded = true;
                    // 为了确保任何手机上都能顺利播放，而不卡在第一帧
                    mFireDrawable.stop();
                    mFireDrawable.start();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                moveIconContent(rawX, rawY);
                // 处理移动时底部是否震动
                handleMoveVibrate(event);
                break;
            case MotionEvent.ACTION_UP:
                if (mVibrator != null) {
                    mVibrator.cancel();
                }

                if (isInLaunchSpace(event)) {
                    // 如果放手时在发射区，则发射火箭
                    handleLaunchRocket();

                    // 移除拖动的火箭展示，发射完成之后再添加回来
                    if (mIsIconContentAdded) {
                        mWindowManager.removeView(mIconContent);
                        mIsIconContentAdded = false;
                    }
                } else {
                    // 移除火箭展示
                    if (mIsRocketContentAdded) {
                        mWindowManager.removeView(mRocketContent);
                        mIsRocketContentAdded = false;
                    }
                    handleActionUpNotLaunch(mIconContent, rawX, mScreenWidth);
                }
                break;

            default:
                break;
        }
        return false;
    }

    // 处理火箭发射动效流程
    private void handleLaunchRocket() {
        /**
         * 1.底座相关消失 2.底部烟雾出现 3.火箭向上飞，向上飞的时候烟雾线往上拉出 4.移除整个火箭页面
         */

        mRocketPad.setVisibility(View.INVISIBLE);
        mRocketFire.setVisibility(View.INVISIBLE);
        Animation alphaIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_in);
        alphaIn.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                // 底部烟雾出现完成，飞机出现并执行火箭往上飞
                mBottomRocket.setVisibility(View.VISIBLE);
                ObjectAnimator transY = ObjectAnimator.ofFloat(mBottomRocket, "translationY",
                        0, -mScreenHeight);
                transY.setDuration(1000);
                transY.setInterpolator(new DecelerateInterpolator());

                // 向上飞的时候烟雾线往上拉出
                ObjectAnimator smokeTopAnim = ObjectAnimator.ofFloat(mSmoketop, "scaleY", 0, 1);
                // 设置缩放基准点为
                ViewHelper.setPivotX(mSmoketop, mSmoketop.getMeasuredWidth() * 0.5f);
                ViewHelper.setPivotY(mSmoketop, mSmoketop.getMeasuredHeight());
                smokeTopAnim.setDuration(700);
                ObjectAnimator smokeAlphaAnim = ObjectAnimator.ofFloat(mSmoketop, "alpha", 0, 1);
                smokeTopAnim.setInterpolator(new DecelerateInterpolator());
                smokeTopAnim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        mSmoketop.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // 移除火箭页面，添加icon
                        if (mIsRocketContentAdded) {
                            mWindowManager.removeView(mRocketContent);
                            mIsRocketContentAdded = false;
                        }
                        if (!mIsIconContentAdded) {
                            if (mIconLayoutParams.x < mScreenWidth / 2) {
                                mIconLayoutParams.x = 0;
                            } else {
                                mIconLayoutParams.x = mScreenWidth - mIconWidth / 2;
                            }
                            mIconImage.setImageDrawable(mCircleDrawable);
                            mWindowManager.addView(mIconContent, mIconLayoutParams);
                            mIsIconContentAdded = true;
                        }

                        // 将view的显示状态重置
                        mSmokePad.setVisibility(View.INVISIBLE);
                        mSmoketop.setVisibility(View.INVISIBLE);
                        mRocketPad.setVisibility(View.VISIBLE);
                        mRocketFire.setVisibility(View.VISIBLE);
                    }
                });

                AnimatorSet animatorSet = new AnimatorSet();
                // 同时执行上面两个效果
                animatorSet.playTogether(transY, smokeTopAnim, smokeAlphaAnim);
                animatorSet.start();
            }
        });
        mSmokePad.setVisibility(View.VISIBLE);
        mSmokePad.startAnimation(alphaIn);

    }

    // 当移动到底座部分时震动,移除时停止震动
    private void handleMoveVibrate(MotionEvent event) {
        // 如果在发射区，则震动
        if (isInLaunchSpace(event)) {
            log("---In Launch Space");
            mVibrator.vibrate(VIBRATOR_PATTERN, 0);
        } else {
            // 否则取消震动
            log("---not In Launch Space");
            if (mVibrator != null)
                mVibrator.cancel();
        }
    }

    // 处理放手后回到边界的效果
    private void handleActionUpNotLaunch(View view, float rawX, int totalWidth) {
        float toX = 0;
        if (rawX < totalWidth / 2) {
            toX = 0;
        } else {
            toX = totalWidth - view.getWidth() / 2;
        }
        // 创建从当前x到目标x的过程
        ValueAnimator mSlideToBoundaryAnim = ValueAnimator.ofFloat(rawX, toX);
        // 设置动画运行时间
        mSlideToBoundaryAnim.setDuration(SLIDE_TO_BOUNDARY_TIME);
        // 减速效果，为了贴边的时候显得柔和
        mSlideToBoundaryAnim.setInterpolator(new DecelerateInterpolator());
        mSlideToBoundaryAnim.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                // 拿出当前时刻回调的值
                float currentValue = (Float) valueAnimator.getAnimatedValue();
                // 将值不断设给浮动小火箭的layoutParams，从而形成动效
                mIconLayoutParams.x = (int) currentValue;
                fixLayoutParams(mIconLayoutParams);
                // 改变WindowManager添加的view的位置
                mWindowManager.updateViewLayout(mIconContent, mIconLayoutParams);
            }
        });
        mSlideToBoundaryAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 在动画结束的时候把火箭重新设置回之前的小icon
                // 当然可以给两个icon加上更加柔和的切换动画，我这里就不加了
                mIconImage.setImageDrawable(mCircleDrawable);
            }
        });
        mSlideToBoundaryAnim.start();

    }

    /**
     * 判断是否在发射区域
     *
     * @param event
     * @return
     */
    boolean isInLaunchSpace(MotionEvent event) {
        int launchHeight = mScreenHeight - mVirbrateHeight - mStatusBarHeight;
        int minLaunchWidth = (mScreenWidth - mVirbrateWidth) / 2;
        int maxLaunchWidth = minLaunchWidth + mVirbrateWidth;
        float rawX = event.getRawX();
        float rawY = event.getRawY();
        if (rawY >= launchHeight && rawX >= minLaunchWidth && rawX <= maxLaunchWidth) {
            return true;
        } else {
            return false;
        }
    }

    private void moveIconContent(float x, float y) {
        mIconLayoutParams.x = (int) x;
        // y值要减去通知栏的高度
        mIconLayoutParams.y = (int) y - mStatusBarHeight;
        fixLayoutParams(mIconLayoutParams);
        mWindowManager.updateViewLayout(mIconContent, mIconLayoutParams);
    }

    // 对x,y的值做修复
    private void fixLayoutParams(LayoutParams layoutParams) {
        int x = layoutParams.x;
        int y = layoutParams.y;
        y = y > mScreenHeight ? mScreenHeight : y < 0 ? 0 : y;
        x = x < 0 ? 0 : x > (mScreenHeight - mIconWidth) ? (mScreenHeight - mIconWidth) : x;

        layoutParams.x = x;
        layoutParams.y = y;
    }

    private void log(String msg) {
        Log.i(TAG, msg);

    }
}
