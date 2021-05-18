package com.passwordsave.module.setting.pattern_lock

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.ihsg.patternlocker.DefaultLockerNormalCellView
import com.github.ihsg.patternlocker.OnPatternChangeListener
import com.github.ihsg.patternlocker.PatternLockerView
import com.passwordsave.R
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.activity_whole_pattern_setting.*
import kotlinx.android.synthetic.main.layout_top.*

class WholePatternSettingActivity : AppCompatActivity() {

    private var patternHelper: PatternHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = getColor(R.color.colorWhite)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setContentView(R.layout.activity_whole_pattern_setting)

        top_title.text = "设置手势密码"
        iv_back.visibility = View.VISIBLE


        val decorator = (this.patternLockerView.normalCellView as DefaultLockerNormalCellView).styleDecorator

        this.patternLockerView.hitCellView = RippleLockerHitCellView()
                .setHitColor(decorator.hitColor)
                .setErrorColor(decorator.errorColor)
        
        this.patternLockerView!!.setOnPatternChangedListener(object : OnPatternChangeListener {
            override fun onStart(view: PatternLockerView) {}

            override fun onChange(view: PatternLockerView, hitIndexList: List<Int>) {}

            override fun onComplete(view: PatternLockerView, hitIndexList: List<Int>) {
                val isOk = isPatternOk(hitIndexList)
                view.updateStatus(!isOk)
                patternIndicatorView!!.updateState(hitIndexList, !isOk)
                if (patternHelper!!.isFinish){
                    MMKV.defaultMMKV().encode("hasLock", true)
                    btn_clean.visibility = View.GONE
                    finishIfNeeded()
                }

                updateMsg()
            }

            override fun onClear(view: PatternLockerView) {
                finishIfNeeded()
            }
        })

        this.textMsg!!.text = "设置解锁图案"
        this.patternHelper = PatternHelper()

        btn_clean.setOnClickListener { patternLockerView!!.clearHitState() }
        iv_back.setOnClickListener { finish() }
    }

    private fun isPatternOk(hitIndexList: List<Int>): Boolean {
        this.patternHelper!!.validateForSetting(hitIndexList)
        return this.patternHelper!!.isOk
    }

    private fun updateMsg() {
        this.textMsg.text = this.patternHelper!!.message
        this.textMsg.setTextColor(if (this.patternHelper!!.isOk)
            ContextCompat.getColor(this, R.color.colorPrimaryDark)
        else
            ContextCompat.getColor(this, R.color.color_red))
    }

    private fun finishIfNeeded() {
        if (this.patternHelper!!.isFinish) {
            finish()
        }
    }

    companion object {

        fun startAction(context: Context) {
            val intent = Intent(context, WholePatternSettingActivity::class.java)
            context.startActivity(intent)
        }
    }
}
