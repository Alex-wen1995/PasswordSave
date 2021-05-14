package com.passwordsave.module.setting.pattern_lock

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.ihsg.demo.ui.whole.RippleLockerHitCellView
import com.github.ihsg.patternlocker.DefaultLockerNormalCellView
import com.github.ihsg.patternlocker.OnPatternChangeListener
import com.github.ihsg.patternlocker.PatternLockerView
import com.passwordsave.R
import com.passwordsave.base.BaseActivity
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.activity_whole_pattern_checking.*
import kotlinx.android.synthetic.main.layout_top.*

class WholePatternCancelActivity : BaseActivity() {

    private var patternHelper: PatternHelper? = null


    override fun layoutId(): Int {
        return R.layout.activity_whole_pattern_checking
    }

    override fun initData() {
    }

    override fun initView() {
        val decorator = (this.patternLockerView.normalCellView as DefaultLockerNormalCellView).styleDecorator

        this.patternLockerView.hitCellView = RippleLockerHitCellView()
            .setHitColor(decorator.hitColor)
            .setErrorColor(decorator.errorColor)

        this.patternLockerView.setOnPatternChangedListener(object : OnPatternChangeListener {
            override fun onStart(view: PatternLockerView) {}

            override fun onChange(view: PatternLockerView, hitIndexList: List<Int>) {}

            override fun onComplete(view: PatternLockerView, hitIndexList: List<Int>) {
                val isError = !isPatternOk(hitIndexList)
                view.updateStatus(isError)
                patternIndicatorView.updateState(hitIndexList, isError)
                MMKV.defaultMMKV().encode("hasLock", false)
                updateMsg()
            }

            override fun onClear(view: PatternLockerView) {
                finishIfNeeded()
            }
        })

        this.textMsg.text = "绘制解锁图案"
        this.patternHelper = PatternHelper()

        iv_back.visibility = View.VISIBLE
    }

    override fun initListener() {
        iv_back.setOnClickListener {
            finish()
        }
    }

    override fun start() {
        top_title.text = "手势密码"
    }


    private fun isPatternOk(hitIndexList: List<Int>): Boolean {
        this.patternHelper!!.validateForChecking(hitIndexList)
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
            val intent = Intent(context, WholePatternCancelActivity::class.java)
            context.startActivity(intent)
        }
    }
}
