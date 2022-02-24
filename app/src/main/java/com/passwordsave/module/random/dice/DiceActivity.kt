package com.passwordsave.module.random.dice

import android.graphics.drawable.Animatable
import android.view.View
import com.bumptech.glide.Glide
import com.passwordsave.R
import com.passwordsave.base.BaseActivity
import kotlinx.android.synthetic.main.fragment_coin.*
import kotlinx.android.synthetic.main.fragment_dice.*
import kotlinx.android.synthetic.main.layout_top.*

class DiceActivity : BaseActivity() {

    override fun layoutId(): Int {
        return R.layout.fragment_dice
    }

    override fun initData() {
        iv_back.visibility = View.VISIBLE
        top_title.text = "随机骰子"
        Glide.with(this)
            .load(R.drawable.to_dice1)
            .into(iv_dice)
    }

    override fun initView() {
    }

    override fun initListener() {

        dice_root.setOnClickListener {
            val result = (1..6).random()
            iv_dice.setImageResource(
                when(result){
                    1-> R.drawable.to_dice1
                    2-> R.drawable.to_dice2
                    3-> R.drawable.to_dice3
                    4-> R.drawable.to_dice4
                    5-> R.drawable.to_dice5
                    6-> R.drawable.to_dice6
                    else -> R.drawable.to_dice1
                })
            (iv_dice.drawable as Animatable).start()
        }
        iv_back.setOnClickListener { finish() }
    }

    override fun start() {
    }
}