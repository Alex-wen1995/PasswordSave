package com.passwordsave.module.login

import android.util.Log

import com.passwordsave.R
import com.passwordsave.base.BaseActivity
import com.passwordsave.module.main.MainActivity
import com.passwordsave.utils.showToast
import com.passwordsave.utils.startActivityNoParam
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : BaseActivity() {
    override fun layoutId(): Int {
        return R.layout.activity_login
    }

    override fun initData() {
    }

    override fun initView() {
    }

    override fun initListener() {
        tv_register.setOnClickListener {
            startActivityNoParam(RegisterActivity::class.java)
        }
        tv_login.setOnClickListener {
            loginByEmailPwd()
        }

        tv_forget.setOnClickListener {

        }
    }


    /**
     * 邮箱+密码登录
     */
    private fun loginByEmailPwd() {
        if(et_account.text.toString()==""||et_pwd.text.toString()==""){
            showToast("邮箱或密码不能为空！")
            return
        }
    }
    override fun start() {
    }
}