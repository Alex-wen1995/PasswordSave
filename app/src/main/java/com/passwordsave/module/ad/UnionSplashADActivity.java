package com.passwordsave.module.ad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.domob.sdk.common.util.AdError;
import com.domob.sdk.unionads.splash.UnionSplashAD;
import com.domob.sdk.unionads.splash.UnionSplashAdListener;
import com.passwordsave.R;
import com.passwordsave.module.main.MainActivity;


public class UnionSplashADActivity extends Activity implements UnionSplashAdListener {
    private UnionSplashAD splashAD;
    private boolean canJump = false;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        hideSystemNavigationBar();

        FrameLayout contentView = this.findViewById(R.id.splash_container);
        splashAD = new UnionSplashAD(this, "96AgWYzA0XMUAoOBkl", "A1211172019", this, 5000);
        splashAD.fetchAndShowIn(contentView);

    }

    private void hideSystemNavigationBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

    }

    @Override
    public void onAdDismissed() {
        Log.d("unions_ads", "页面消失了");
        startActivity(new Intent(UnionSplashADActivity.this, MainActivity.class));

        this.finish();
    }

    @Override
    public void onNoAd(AdError error) {
        final String msg = error.getErrorMsg();
        final int code = error.getErrorCode();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.putExtra("result", "code : " + code + "  msg : " + msg);
                setResult(RESULT_OK, intent);
                finish();
            }
        }, 3000);
        Log.d("unions_ads", "code : " + error.getErrorCode() + "  msg : " + error.getErrorMsg());
    }

    @Override
    public void onAdPresent() {
        Log.d("unions_ads", "展现了");
    }

    @Override
    public void onAdClicked() {
        finish();
        Log.d("unions_ads", "点击了页面内容");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //此步骤主要是处理点击广告之后，退出当前的开屏页面，开发者可以根据自己的需求自己处理，不一定按照此方式处理
        if (canJump) {
            this.finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        canJump = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清空Handler队列中的所有消息，如果开发者在onNoAd()方法中没有使用Handler方式处理，可以不用在此处添加这行代码
        handler.removeCallbacksAndMessages(null);
    }
}
