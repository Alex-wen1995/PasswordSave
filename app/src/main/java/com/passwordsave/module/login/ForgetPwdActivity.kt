package com.passwordsave.module.login

import android.util.Log
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.UpdateListener
import com.google.android.material.snackbar.Snackbar
import com.passwordsave.R
import com.passwordsave.base.BaseActivity
import com.passwordsave.utils.showToast
import kotlinx.android.synthetic.main.activity_forget_pwd.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.et_account
import kotlinx.android.synthetic.main.activity_login.tv_forget


class ForgetPwdActivity : BaseActivity() {
    override fun layoutId(): Int {
        return R.layout.activity_forget_pwd
    }

    override fun initData() {
    }

    override fun initView() {
    }

    override fun initListener() {
        tv_forget.setOnClickListener {
            resetPasswordByEmail()
        }
    }


    /**
     * 邮箱重置密码
     */
    private fun resetPasswordByEmail() {
        val email = et_account.text.toString()
        BmobUser.resetPasswordByEmail(email, object : UpdateListener() {
            override fun done(e: BmobException?) {
                if (e == null) {
                    showToast("重置密码请求成功，请到" + email + "邮箱进行密码重置操作")
                    finish()
                } else {
                    Log.e("BMOB", e.toString())
                    showToast("重置操作失败")
                }
            }
        })
    }

    override fun start() {
    }
}