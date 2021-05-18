package com.passwordsave.module.ad;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.passwordsave.R;
import com.passwordsave.module.login.LoginActivity;
import com.passwordsave.module.main.MainActivity;
import com.passwordsave.module.main.Term1Activity;
import com.passwordsave.module.main.Term2Activity;
import com.passwordsave.module.setting.pattern_lock.WholePatternCheckingActivity;
import com.tencent.mmkv.MMKV;

import java.util.List;

import cn.bmob.v3.BmobUser;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by quan on 2018/9/20.
 */

public class AdActivity extends Activity implements EasyPermissions.PermissionCallbacks {
    private ImageView iv;
    private String url = "";
    private ImageView close;

    /**
     * 倒数计时器
     */
    private CountDownTimer timer;


    /**
     * 6.0以下版本(系统自动申请) 不会弹框
     * 有些厂商修改了6.0系统申请机制，他们修改成系统自动申请权限了
     */
    static final String[] PERMISSION = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,// 写入权限
            Manifest.permission.READ_EXTERNAL_STORAGE,  //读取权限
            Manifest.permission.READ_PHONE_STATE,  //设备信息

    };

    private void checkPermission() {
        EasyPermissions.requestPermissions(this, getString(R.string.need_permission), 0, PERMISSION);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ad);
        initView();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    private boolean hasPermission() {
        return EasyPermissions.hasPermissions(this, PERMISSION);
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

        timer = new CountDownTimer(2 * 1000, 1000) {
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
                if (MMKV.defaultMMKV().decodeBool("hasLock", false)) {
                    startActivity(new Intent(AdActivity.this, WholePatternCheckingActivity.class));
                } else {
                    if(BmobUser.isLogin()){
                        startActivity(new Intent(AdActivity.this, MainActivity.class));
                    }else {
                        startActivity(new Intent(AdActivity.this, LoginActivity.class));
                    }
                }
                finish();
            }
        };
        if (MMKV.defaultMMKV().getBoolean("read_term", false)) {
            if (hasPermission()){
                timerStart();
            }else {
                checkPermission();
            }
        } else {
            showMsgDialog(this);
        }

    }


    /**
     * 取消倒计时
     */
    public void timerCancel() {
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerCancel();
    }

    /**
     * 开始倒计时
     */
    public void timerStart() {
        timer.start();
    }


    private void showMsgDialog(Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomProgressDialog);
        final View dialog = View.inflate(context, R.layout.dialog_msg, null);
        TextView refuse = dialog.findViewById(R.id.tv_refuse);
        TextView agree = dialog.findViewById(R.id.tv_agree);
        TextView term1 = dialog.findViewById(R.id.tv_term_1);
        TextView term2 = dialog.findViewById(R.id.tv_term_2);
        builder.setView(dialog);
        final AlertDialog alertDialog = builder.create();
        refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                System.exit(0);
            }
        });

        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MMKV.defaultMMKV().putBoolean("read_term", true);
                alertDialog.dismiss();
                if (!hasPermission()) {
                    checkPermission();
                }else {
                    timerStart();
                }

            }
        });
        term1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdActivity.this, Term1Activity.class));
            }
        });
        term2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdActivity.this, Term2Activity.class));
            }
        });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (MMKV.defaultMMKV().getBoolean("read_term", false)) {
            timerStart();
        } else {
            showMsgDialog(this);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        finish();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
