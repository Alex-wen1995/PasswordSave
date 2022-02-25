package com.passwordsave.module.ad;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.passwordsave.R;
import com.passwordsave.module.main.MainActivity;
import com.passwordsave.module.main.Term1Activity;
import com.passwordsave.module.main.Term2Activity;
import com.passwordsave.utils.ExtensionsKt;
import com.socks.library.KLog;
import com.tencent.mmkv.MMKV;
import com.wei.android.lib.fingerprintidentify.FingerprintIdentify;
import com.wei.android.lib.fingerprintidentify.base.BaseFingerprint;

import java.util.List;

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

    /**
     * 指纹识别
     * */
    private FingerprintIdentify mFingerprintIdentify;
    private static final int MAX_AVAILABLE_TIMES = 5;

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
                if (MMKV.defaultMMKV().decodeBool("hasFingerPrint", false)) {
                    mFingerprintIdentify = new FingerprintIdentify(getApplicationContext());
                    mFingerprintIdentify.setSupportAndroidL(true);
                    mFingerprintIdentify.setExceptionListener(new BaseFingerprint.ExceptionListener() {
                        @Override
                        public void onCatchException(Throwable exception) {
                        }
                    });
                    mFingerprintIdentify.init();
                    if (!mFingerprintIdentify.isFingerprintEnable()) {
                        KLog.e("isFingerprintEnable","请检查指纹硬件可用并已经录入指纹");
                        Toast.makeText(AdActivity.this,getString(R.string.fingerprintEnable_hint),Toast.LENGTH_SHORT).show();
                        return;
                    }
                    startCheck();
                } else {
                    startActivity(new Intent(AdActivity.this, MainActivity.class));
                    finish();
                }
            }
        };
        if (MMKV.defaultMMKV().getBoolean("read_term", false)) {
            if (hasPermission()) {
                timerStart();
            } else {
                checkPermission();
            }
        } else {
            showMsgDialog(this);
        }
    }

    private void startCheck(){
        View view  = LayoutInflater.from(this).inflate(R.layout.dialog_fingerprint, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.CustomProgressDialog);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
        mFingerprintIdentify.startIdentify(MAX_AVAILABLE_TIMES, new BaseFingerprint.IdentifyListener() {
            @Override
            public void onSucceed() {
                startActivity(new Intent(AdActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onNotMatch(int availableTimes) {
                ExtensionsKt.showToast(AdActivity.this,getString(R.string.not_match, availableTimes));
            }

            @Override
            public void onFailed(boolean isDeviceLocked) {
                ExtensionsKt.showToast(AdActivity.this,getString(R.string.failed));
                finish();
            }

            @Override
            public void onStartFailedByDeviceLocked() {
                ExtensionsKt.showToast(AdActivity.this,getString(R.string.start_failed));
                KLog.e("onStartFailedByDeviceLocked","设备暂时锁定,应用关闭");
                finish();
            }
        });
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
    protected void onPause() {
        super.onPause();
        if(mFingerprintIdentify!=null){
            mFingerprintIdentify.cancelIdentify();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerCancel();
        if(mFingerprintIdentify!=null){
            mFingerprintIdentify.cancelIdentify();
        }
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
                new AlertDialog.Builder(AdActivity.this)
                        .setTitle("温馨提示")
                        .setMessage("根据相关法律法规，你同意后即可继续使用账号管家的服务。我们会严格保护你的信息安全；若你拒绝，将无法继续使用我们的服务。\n如你同意，请点击“同意”开始接受我们的服务。")
                        .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                            @SuppressLint("ApplySharedPref")
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                agreeTerm();
                            }
                        })
                        .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //退出App
                                System.exit(0);
                            }
                        })
                        .show();
            }
        });

        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                agreeTerm();

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

    private void agreeTerm(){
        MMKV.defaultMMKV().putBoolean("read_term", true);
        if (!hasPermission()) {
            checkPermission();
        } else {
            timerStart();
        }
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
        if (MMKV.defaultMMKV().getBoolean("read_term", false)) {
            timerStart();
        } else {
            showMsgDialog(this);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
