package com.passwordsave.module.account


import android.view.View
import com.passwordsave.R
import com.passwordsave.base.BaseActivity
import com.passwordsave.utils.showToast
import com.socks.library.KLog
import kotlinx.android.synthetic.main.activity_add_account.*
import kotlinx.android.synthetic.main.layout_top.*

class AddAccountActivity : BaseActivity() {
    override fun layoutId(): Int {
        return R.layout.activity_add_account
    }

    override fun initData() {
        top_title.text = "添加账号"
        iv_back.visibility = View.VISIBLE
    }

    override fun initView() {
    }

    override fun initListener() {
        iv_back.setOnClickListener {
            finish()
        }

        btn_save.setOnClickListener {
            val data = Account()
            data.title = et_title.text.toString()
            data.account = et_account.text.toString()
            data.password = et_pwd.text.toString()
            data.remark = et_remark.text.toString()
            KLog.e("data", data.toString())
            mAppDatabase.accountDao()!!.insertAccount(data)
            showToast("添加成功")
            finish()
        }
    }

    override fun start() {
    }
}