package com.passwordsave.module.ad

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.*
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.passwordsave.R
import com.passwordsave.module.main.MainActivity
import com.passwordsave.module.main.Term1Activity
import com.passwordsave.module.main.Term2Activity
import com.passwordsave.utils.authenticate
import com.passwordsave.utils.isFingerprintAvailable
import com.passwordsave.utils.showToast
import com.socks.library.KLog
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.activity_ad.*
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.EasyPermissions.PermissionCallbacks


/**
 * Created by quan on 2018/9/20.
 */
class AdActivity : FragmentActivity(), PermissionCallbacks {
    /**
     * 倒数计时器
     */
    private var timer: CountDownTimer? = null
    private fun checkPermission() {
        EasyPermissions.requestPermissions(
            this,
            getString(R.string.need_permission),
            0,
            *PERMISSION
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        setContentView(R.layout.activity_ad)
        initView()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private fun hasPermission(): Boolean {
        return EasyPermissions.hasPermissions(this, *PERMISSION)
    }

    private fun initView() {
        Glide.with(this)
            .load(R.drawable.startup)
            .into(ad_Iv)
        ad_Iv.setOnClickListener { }
        ad_close.setOnClickListener {
            timerCancel()
            startActivity(Intent(this@AdActivity, MainActivity::class.java))
            finish()
        }
        timer = object : CountDownTimer(2 * 1000, 1000) {
            /**
             * 固定间隔被调用,就是每隔countDownInterval会回调一次方法onTick
             * @param millisUntilFinished
             */
            override fun onTick(millisUntilFinished: Long) {}

            /**
             * 倒计时完成时被调用
             */
            override fun onFinish() {
                if (MMKV.defaultMMKV().decodeBool("hasFingerPrint", false)) {
                    if (isFingerprintAvailable(this@AdActivity)!=0) {
                        KLog.e("isFingerprintEnable","请检查指纹硬件可用并已经录入指纹");
                        showToast(getString(R.string.fingerprintEnable_hint))
                        return
                    }
                    startCheck()
                } else {
                    startActivity(Intent(this@AdActivity, MainActivity::class.java))
                    finish()
                }
            }
        }
        if (MMKV.defaultMMKV().getBoolean("read_term", false)) {
            if (hasPermission()) {
                timerStart()
            } else {
                checkPermission()
            }
        } else {
            showMsgDialog(this)
        }
    }
    private fun startCheck() {
        authenticate(this,object : BiometricPrompt.AuthenticationCallback(){
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                when (errorCode) {
                    ERROR_USER_CANCELED -> showToast("取消")//返回键取消
                    ERROR_LOCKOUT -> showToast("失败5次，已锁定，请30秒后在试")
                    ERROR_LOCKOUT_PERMANENT -> showToast("失败次数太多，指纹验证已锁定，请改用密码，图案等方式解锁")
                    ERROR_NEGATIVE_BUTTON -> showToast("取消")//取消键取消
                    ERROR_NO_DEVICE_CREDENTIAL -> showToast("尚未设置密码，图案等解锁方式")
                    ERROR_NO_SPACE -> showToast("可用空间不足")
                    ERROR_TIMEOUT -> showToast("验证超时")
                }
                finish()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                showToast("验证失败，请重试")
            }

            override fun onAuthenticationSucceeded(result: AuthenticationResult) {
                showToast("验证成功")
                startActivity(Intent(this@AdActivity, MainActivity::class.java))
                finish()
            }
        })
    }
    /**
     * 取消倒计时
     */
    fun timerCancel() {
        if (timer != null) {
            timer!!.cancel()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timerCancel()
    }

    /**
     * 开始倒计时
     */
    fun timerStart() {
        timer!!.start()
    }

    private fun showMsgDialog(context: Context) {
        val builder = AlertDialog.Builder(context, R.style.CustomProgressDialog)
        val dialog = View.inflate(context, R.layout.dialog_msg, null)
        val refuse = dialog.findViewById<TextView>(R.id.tv_refuse)
        val agree = dialog.findViewById<TextView>(R.id.tv_agree)
        val term1 = dialog.findViewById<TextView>(R.id.tv_term_1)
        val term2 = dialog.findViewById<TextView>(R.id.tv_term_2)
        builder.setView(dialog)
        val alertDialog = builder.create()
        refuse.setOnClickListener {
            alertDialog.dismiss()
            AlertDialog.Builder(this@AdActivity)
                .setTitle("温馨提示")
                .setMessage("根据相关法律法规，你同意后即可继续使用账号管家的服务。我们会严格保护你的信息安全；若你拒绝，将无法继续使用我们的服务。\n如你同意，请点击“同意”开始接受我们的服务。")
                .setPositiveButton("同意") { dialog, which -> agreeTerm() }
                .setNegativeButton("拒绝") { dialog, which -> //退出App
                    System.exit(0)
                }
                .show()
        }
        agree.setOnClickListener {
            alertDialog.dismiss()
            agreeTerm()
        }
        term1.setOnClickListener {
            startActivity(
                Intent(
                    this@AdActivity,
                    Term1Activity::class.java
                )
            )
        }
        term2.setOnClickListener {
            startActivity(
                Intent(
                    this@AdActivity,
                    Term2Activity::class.java
                )
            )
        }
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.show()
    }

    private fun agreeTerm() {
        MMKV.defaultMMKV().putBoolean("read_term", true)
        if (!hasPermission()) {
            checkPermission()
        } else {
            timerStart()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        if (MMKV.defaultMMKV().getBoolean("read_term", false)) {
            timerStart()
        } else {
            showMsgDialog(this)
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (MMKV.defaultMMKV().getBoolean("read_term", false)) {
            timerStart()
        } else {
            showMsgDialog(this)
        }
    }

    override fun onPointerCaptureChanged(hasCapture: Boolean) {}

    companion object {
        /**
         * 6.0以下版本(系统自动申请) 不会弹框
         * 有些厂商修改了6.0系统申请机制，他们修改成系统自动申请权限了
         */
        val PERMISSION = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,  // 写入权限
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }
}