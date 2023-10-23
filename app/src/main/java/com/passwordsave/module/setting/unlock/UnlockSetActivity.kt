package com.passwordsave.module.setting.unlock

import android.os.Bundle
import android.view.View
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.*
import androidx.fragment.app.FragmentActivity
import com.passwordsave.R
import com.passwordsave.module.setting.pattern_lock.WholePatternCancelActivity
import com.passwordsave.module.setting.pattern_lock.WholePatternSettingActivity
import com.passwordsave.utils.authenticate
import com.passwordsave.utils.showToast
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.activity_unlock_setting.sw
import kotlinx.android.synthetic.main.activity_unlock_setting.sw_2
import kotlinx.android.synthetic.main.layout_top.*


class UnlockSetActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unlock_setting)
        initData()
        initListener()
    }

    override fun onResume() {
        super.onResume()
        sw.isChecked = MMKV.defaultMMKV().decodeBool("hasFingerPrint", false)
        sw_2.isChecked = MMKV.defaultMMKV().decodeBool("hasLock", false)

    }
    fun initListener() {
        iv_back.setOnClickListener { finish() }
        sw.setOnClickListener {
            if(MMKV.defaultMMKV().decodeBool("hasFingerPrint", false)){
                MMKV.defaultMMKV().putBoolean("hasFingerPrint", false)
                sw.isChecked = false
            }else{
                startCheck()
            }
        }
        sw_2.setOnClickListener {
            if(MMKV.defaultMMKV().decodeBool("hasLock", false)){
                WholePatternCancelActivity.startAction(this@UnlockSetActivity)
            }else{
                WholePatternSettingActivity.startAction(this@UnlockSetActivity)
            }
        }
    }

    private fun startCheck() {
        authenticate(this,object : BiometricPrompt.AuthenticationCallback(){
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                when (errorCode) {
                    ERROR_USER_CANCELED -> showToast("取消") //返回键取消
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
                sw.isChecked = false
            }

            override fun onAuthenticationSucceeded(result: AuthenticationResult) {
                showToast("验证成功")
                MMKV.defaultMMKV().encode("hasFingerPrint", true)
                sw.isChecked = true
            }
        })
    }

    fun initData() {
        top_title.text = "指纹识别"
        iv_back.visibility = View.VISIBLE
    }



}