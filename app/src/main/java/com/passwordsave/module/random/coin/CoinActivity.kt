package com.passwordsave.module.random.coin
import android.graphics.drawable.Animatable
import android.view.View
import com.bumptech.glide.Glide
import com.passwordsave.R
import com.passwordsave.base.BaseActivity
import kotlinx.android.synthetic.main.fragment_coin.*
import kotlinx.android.synthetic.main.layout_top.*

class CoinActivity : BaseActivity() {


    override fun layoutId(): Int {
        return R.layout.fragment_coin
    }

    override fun initData() {
        iv_back.visibility = View.VISIBLE
        top_title.text = "随机硬币"
        Glide.with(this)
            .load(R.drawable.to_frontside)
            .into(iv_coin)
    }

    override fun initView() {

        coin_root.setOnClickListener {
            val random = (1..2).random()
            iv_coin.setImageResource(
                when(random){
                    1-> R.drawable.to_backside
                    2-> R.drawable.to_frontside
                    else->R.drawable.to_backside
                })
            (iv_coin.drawable as Animatable).start()
        }
    }

    override fun initListener() {
        iv_back.setOnClickListener {
            finish()
        }
    }

    override fun start() {
    }
}