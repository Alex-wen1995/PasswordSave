package com.passwordsave.module.account

import android.view.View
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.UpdateListener
import com.passwordsave.R
import com.passwordsave.base.BaseActivity
import com.passwordsave.module.account.Account
import com.passwordsave.module.account.Account2
import com.passwordsave.utils.showToast
import com.socks.library.KLog
import kotlinx.android.synthetic.main.activity_add_account.*
import kotlinx.android.synthetic.main.layout_top.*


class UpdateAccountActivity : BaseActivity() {
    var is_collect = false
    var id = 0
    var objectId = ""
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
        objectId = intent.getStringExtra("objectId")!!
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
            val data = Account2()
            data.id = id
            data.title = et_title.text.toString()
            data.account = et_account.text.toString()
            data.password = et_pwd.text.toString()
            data.remark = et_remark.text.toString()
            data.isCollect = is_collect
            KLog.e("data",data.toString())
            mAppDatabase.accountDao()!!.updateAccount(data)


            //更新bmob Account表里面id为objectId的数据
            val b_data = Account()
            b_data.title = et_title.text.toString()
            b_data.account = et_account.text.toString()
            b_data.password = et_pwd.text.toString()
            b_data.remark = et_remark.text.toString()
            b_data.isCollect = is_collect


            b_data.update(objectId, object : UpdateListener() {
                override fun done(e: BmobException?) {
                    if (e == null) {
                        showToast("更新成功")
                        finish()
                    } else {
                        showToast("更新失败")
                    }
                }
            })
        }
    }

    override fun start() {
    }
}