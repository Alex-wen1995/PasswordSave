package com.passwordsave.module.login

import android.util.Log
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.LogInListener
import cn.bmob.v3.listener.SaveListener
import com.google.android.material.snackbar.Snackbar
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
    }


    /**
     * 邮箱+密码登录
     */
    private fun loginByEmailPwd() {
        if(et_account.text.toString()==""||et_pwd.text.toString()==""){
            showToast("邮箱或密码不能为空！")
            return
        }
        BmobUser.loginByAccount(
            et_account.text.toString(),
            et_pwd.text.toString(),object : LogInListener<User?>() {
                override fun done(user: User?, e: BmobException?) {
                    if (e == null) {
                        showToast("登录成功")
                        startActivityNoParam(MainActivity::class.java)
                        finish()
                    } else {
                        Log.e("loginByEmailPwd",e.message!!)
                        if (e.errorCode==101){
                            showToast("账号或密码错误")
                        }
                    }
                }
            })
    }
    override fun start() {
    }
}