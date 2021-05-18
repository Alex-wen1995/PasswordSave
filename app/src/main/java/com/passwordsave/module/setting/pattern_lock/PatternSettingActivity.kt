package com.passwordsave.module.setting.pattern_lock

import android.view.View
import com.passwordsave.R
import com.passwordsave.base.BaseActivity
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.activity_pattern_setting.*
import kotlinx.android.synthetic.main.layout_top.*

class PatternSettingActivity : BaseActivity() {
    override fun layoutId(): Int {
        return R.layout.activity_pattern_setting
    }

    override fun initData() {
        top_title.text = "手势密码"
    }

    override fun initView() {
        iv_back.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        sw.isChecked = MMKV.defaultMMKV().decodeBool("hasLock", false)

    }

    override fun initListener() {
        iv_back.setOnClickListener { finish() }
//        sw.setOnClickListener { compoundButton, b ->
//            if (b) {
//                WholePatternSettingActivity.startAction(this@PatternSettingActivity)
//            } else {
//                WholePatternCheckingActivity.startAction(this@PatternSettingActivity)
//            }
//        }

        sw.setOnClickListener {
            if(MMKV.defaultMMKV().decodeBool("hasLock", false)){
                WholePatternCancelActivity.startAction(this@PatternSettingActivity)
            }else{
                WholePatternSettingActivity.startAction(this@PatternSettingActivity)
            }
        }
    }

    override fun start() {
    }
}