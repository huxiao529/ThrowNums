package com.l99.thrownums;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    @BindView(R.id.shape)
    LinearLayout mShape;
    @BindViews({R.id.line1, R.id.line2, R.id.line3})//3排点数控件
    List<DiyLinearLayout> mLines;
    @BindView(R.id.result)
    TextView mResult;
    private Vibrator vibrator;
    private Context mContext;
    private static ObjectAnimator mAnimator;
    private int mRandomNum;//1~6的随机数

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);
        initSensorAndListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAnimator = null;
    }

    /**
     * 初始化传感器和 传感器事件监听器
     */
    private void initSensorAndListener() {
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * 重力感应监听
     */
    private SensorEventListener sensorEventListener = new SensorEventListener() {

        private long mTime;

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (mTime > 0 && Math.abs(mTime - System.currentTimeMillis()) < 400) {//每次触发间隔不能小于1s
                return;
            }
            mTime = System.currentTimeMillis();
            // 传感器信息改变时执行该方法
            float[] values = event.values;
            float x = values[0]; // x轴方向的重力加速度，向右为正
            float y = values[1]; // y轴方向的重力加速度，向前为正
            float z = values[2]; // z轴方向的重力加速度，向上为正
            Log.i(TAG, "x轴方向的重力加速度" + x + "；y轴方向的重力加速度" + y + "；z轴方向的重力加速度" + z);
            // 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。
            int medumValue = 12;
            if (Math.abs(x) > medumValue && Math.abs(y) > medumValue
                    || Math.abs(y) > medumValue && Math.abs(z) > medumValue
                    || Math.abs(x) > medumValue && Math.abs(z) > medumValue
                    ) {
                shakeMobilePhone();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    /**
     * 触发摇手机后,需要执行的逻辑
     */
    @OnClick(R.id.btn_shake)
    public void shakeMobilePhone() {
//        vibrator.vibrate(200);//震动200ms
        //得到一个1到6的随机数
        mRandomNum = getRandom();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showAnimation();
            }
        });
    }

    private int getRandom() {
        int max = 6;
        int min = 1;
        Random random = new Random();
        return random.nextInt(100) % (max - min + 1) + min;
    }

    private void showMsg() {
        mResult.setText(getString(R.string.random_num, mRandomNum));
    }

    private void clearMsg() {
        mResult.setText("");
    }

    /**
     * 展示 动画
     */
    private void showAnimation() {
        if (mAnimator == null) {
            PropertyValuesHolder rotation = PropertyValuesHolder.ofFloat("rotation", 0, 1080);
            PropertyValuesHolder translationY = PropertyValuesHolder.ofFloat("translationY", -200, -600, -200, 0);
            mAnimator = ObjectAnimator.ofPropertyValuesHolder(mShape, rotation, translationY).setDuration(1000);
            mAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    clearMsg();
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    showNumResult();
                    showMsg();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        if (!mAnimator.isRunning()) {
            mAnimator.start();
        }
    }

    /**
     * 展示点数结果
     */
    void showNumResult() {
        switch (mRandomNum) {
            case 1:
                showLines(1);
                mLines.get(0).showPosition(0).gonePosition(1).gonePosition(2);
                break;
            case 2:
                showLines(1);
                mLines.get(0).showPosition(0).showPosition(1).gonePosition(2);
                break;
            case 3:
                showLines(3);
                mLines.get(0).showPosition(0).hidePosition(1).hidePosition(2);
                mLines.get(1).hidePosition(0).showPosition(1).hidePosition(2);
                mLines.get(2).hidePosition(0).hidePosition(1).showPosition(2);
                break;
            case 4:
                showLines(2);
                mLines.get(0).showPosition(0).showPosition(1).gonePosition(2);
                mLines.get(1).showPosition(0).showPosition(1).gonePosition(2);
                break;
            case 5:
                showLines(3);
                mLines.get(0).showPosition(0).gonePosition(1).showPosition(2);
                mLines.get(1).gonePosition(0).showPosition(1).gonePosition(2);
                mLines.get(2).showPosition(0).gonePosition(1).showPosition(2);
                break;
            case 6:
                showLines(2);
                mLines.get(0).showPosition(0).showPosition(1).showPosition(2);
                mLines.get(1).showPosition(0).showPosition(1).showPosition(2);
                break;
        }
    }

    /**
     * 需要展示多少个 点数行控件
     */
    void showLines(int size) {
        for (int i = 0; i < mLines.size(); i++) {
            if (i <= size - 1) {
                mLines.get(i).setVisibility(View.VISIBLE);
            } else {
                mLines.get(i).setVisibility(View.GONE);
            }
        }
    }
}
