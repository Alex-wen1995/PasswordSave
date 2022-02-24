package com.passwordsave.module.setting.fingerprint_identification

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.passwordsave.R
import com.passwordsave.base.BaseActivity
import com.passwordsave.utils.showToast
import com.tencent.mmkv.MMKV
import com.wei.android.lib.fingerprintidentify.FingerprintIdentify
import com.wei.android.lib.fingerprintidentify.base.BaseFingerprint
import kotlinx.android.synthetic.main.activity_pattern_setting.*
import kotlinx.android.synthetic.main.layout_top.*

class FingerSetActivity : BaseActivity() {
    private lateinit var mFingerprintIdentify: FingerprintIdentify
    private val MAX_AVAILABLE_TIMES = 5
    /**
     * mFingerprintIdentify = new FingerprintIdentify(this);       // 构造对象
     * mFingerprintIdentify.setSupportAndroidL(true);              // 支持安卓L及以下系统
     * mFingerprintIdentify.setExceptionListener(listener);        // 错误回调（错误仅供开发使用）
     * mFingerprintIdentify.init();                                // 初始化，必须调用
     *
     * mFingerprintIdentify.isFingerprintEnable();                 // 指纹硬件可用并已经录入指纹
     * mFingerprintIdentify.isHardwareEnable();                    // 指纹硬件是否可用
     * mFingerprintIdentify.isRegisteredFingerprint();             // 是否已经录入指纹
     * mFingerprintIdentify.startIdentify(maxTimes, listener);     // 开始验证指纹识别
     * mFingerprintIdentify.cancelIdentify();                      // 关闭指纹识别
     * mFingerprintIdentify.resumeIdentify();                      // 恢复指纹识别并保证错误次数不变
     *
     * */
    override fun layoutId(): Int {
        return R.layout.activity_fingerprint_setting
    }

    override fun initData() {
        top_title.text = "指纹识别"
        iv_back.visibility = View.VISIBLE

    }

    override fun onResume() {
        super.onResume()
        sw.isChecked = MMKV.defaultMMKV().decodeBool("hasFingerPrint", false)
    }
    override fun initView() {
        mFingerprintIdentify = FingerprintIdentify(applicationContext)
        mFingerprintIdentify.setSupportAndroidL(true)
        mFingerprintIdentify.setExceptionListener { }
        mFingerprintIdentify.init()
        if (!mFingerprintIdentify.isFingerprintEnable) {
            return
        }
    }
    fun startCheck(isOn: Boolean) {
        val view  = LayoutInflater.from(this).inflate(R.layout.dialog_fingerprint, null)
        val builder: AlertDialog.Builder = AlertDialog.Builder(this,R.style.CustomProgressDialog)
        builder.setView(view)
        val dialog = builder.create()
        dialog.setCancelable(false)
        dialog.show()
        sw.isChecked = !isOn
        mFingerprintIdentify.startIdentify(MAX_AVAILABLE_TIMES,
            object : BaseFingerprint.IdentifyListener {
                override fun onSucceed() {
                    showToast(getString(R.string.fingerprint_success))
                    sw.isChecked = isOn
                    dialog.dismiss()
                    MMKV.defaultMMKV().putBoolean("hasFingerPrint", isOn)
                }

                override fun onNotMatch(availableTimes: Int) {
                    sw.isChecked = !isOn
                    showToast(getString(R.string.not_match, availableTimes))
                }

                override fun onFailed(isDeviceLocked: Boolean) {
                    sw.isChecked = !isOn
                    dialog.dismiss()
                    showToast(getString(R.string.failed))
                }

                override fun onStartFailedByDeviceLocked() {
                    sw.isChecked = !isOn
                    dialog.dismiss()
                    showToast(getString(R.string.start_failed_2))
                    finish()
                }
            })
    }

    override fun initListener() {
        iv_back.setOnClickListener { finish() }
        sw.setOnClickListener {
            if(MMKV.defaultMMKV().decodeBool("hasFingerPrint", false)){
                startCheck(false)
            }else{
                startCheck(true)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mFingerprintIdentify.cancelIdentify()
    }

    override fun onDestroy() {
        super.onDestroy()
        mFingerprintIdentify.cancelIdentify()
    }
    override fun start() {
    }
}