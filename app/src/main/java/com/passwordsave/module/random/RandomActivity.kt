package com.passwordsave.module.random

import android.view.View
import com.blankj.utilcode.util.ActivityUtils
import com.passwordsave.R
import com.passwordsave.base.BaseActivity
import com.passwordsave.module.random.coin.CoinActivity
import com.passwordsave.module.random.dice.DiceActivity
import kotlinx.android.synthetic.main.activity_random.*
import kotlinx.android.synthetic.main.layout_top.*

class RandomActivity : BaseActivity() {
    override fun layoutId(): Int {
        return R.layout.activity_random
    }

    override fun initData() {
        top_title.text = "随机事件"
        iv_back.visibility = View.VISIBLE
    }

    override fun initView() {
    }

    override fun initListener() {
        iv_back.setOnClickListener { finish() }
        random_menu_1.setOnClickListener { ActivityUtils.startActivity(CoinActivity::class.java) }
        random_menu_2.setOnClickListener {  ActivityUtils.startActivity(DiceActivity::class.java) }
    }

    override fun start() {

    }
}