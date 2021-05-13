package com.passwordsave.module.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.passwordsave.R;

/**
 * Created by quan on 2018/9/20.
 */

public class AdActivity extends Activity {
    private ImageView iv;
    private String url = "";
    private ImageView close;

    /**
     * 倒数计时器
     */
    private CountDownTimer timer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //    WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_ad);
        initView();

    }

    private void initView() {
        iv = findViewById(R.id.ad_Iv);
        Glide.with(this)
                .load(R.drawable.startup)
                .into(iv);
        close = findViewById(R.id.ad_close);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                timerCancel();
                startActivity(new Intent(AdActivity.this, MainActivity.class));
                finish();
            }
        });

        timer = new CountDownTimer(3 * 1000, 1000) {
            /**
             * 固定间隔被调用,就是每隔countDownInterval会回调一次方法onTick
             * @param millisUntilFinished
             */
            @Override
            public void onTick(long millisUntilFinished) {

            }

            /**
             * 倒计时完成时被调用
             */
            @Override
            public void onFinish() {
                startActivity(new Intent(AdActivity.this, MainActivity.class));
                finish();
            }
        };
        timerStart();
    }




    /**
     * 取消倒计时
     */
    public void timerCancel() {
        if (timer != null) {
            timer.cancel();
        }
    }

    /**
     * 开始倒计时
     */
    public void timerStart() {
        timer.start();
    }

}
