package com.passwordsave.module.net_account

import android.view.View
import com.passwordsave.R
import com.passwordsave.base.BaseActivity
import com.passwordsave.module.local_account.Account2
import com.socks.library.KLog
import kotlinx.android.synthetic.main.activity_add_account.*
import kotlinx.android.synthetic.main.layout_top.*

class UpdateAccountActivity : BaseActivity() {
    var is_collect = false
    var id = 0
    override fun layoutId(): Int {
        return R.layout.activity_add_account
    }

    override fun initData() {
        top_title.text = "更新账号"
        iv_back.visibility = View.VISIBLE
        iv_collect.visibility = View.VISIBLE
    }

    override fun initView() {
        id = intent.getIntExtra("id",0)
        et_title.setText(intent.getStringExtra("title"))
        et_account.setText(intent.getStringExtra("account"))
        et_pwd.setText(intent.getStringExtra("password"))
        et_remark.setText(intent.getStringExtra("remark"))
        is_collect=intent.getBooleanExtra("isCollect",false)
        if (is_collect) {
            iv_collect.setImageResource(R.drawable.ic_collect_2)
        } else {
            iv_collect.setImageResource(R.drawable.ic_collect)
        }
    }

    override fun initListener() {
        iv_back.setOnClickListener {
            finish()
        }
        iv_collect.setOnClickListener {
            if (is_collect) {
                iv_collect.setImageResource(R.drawable.ic_collect)
            } else {
                iv_collect.setImageResource(R.drawable.ic_collect_2)
            }
            is_collect = !is_collect
        }

        btn_save.setOnClickListener {

            finish()
        }
    }

    override fun start() {
    }
}