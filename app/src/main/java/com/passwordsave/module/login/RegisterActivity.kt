package com.passwordsave.module.login

import android.util.Log
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import com.google.android.material.snackbar.Snackbar
import com.passwordsave.R
import com.passwordsave.base.BaseActivity
import com.passwordsave.utils.showToast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.et_account
import kotlinx.android.synthetic.main.activity_register.et_pwd
import kotlinx.android.synthetic.main.activity_register.tv_register


class RegisterActivity : BaseActivity() {
    override fun layoutId(): Int {
        return R.layout.activity_register
    }

    override fun initData() {
    }

    override fun initView() {
    }

    override fun initListener() {
        tv_register.setOnClickListener {
            emailRegister()
        }
    }


    /**
     * 账号密码注册
     */
    private fun emailRegister() {
        if(et_account.text.toString()==""||et_pwd.text.toString()==""){
            showToast("邮箱或密码不能为空！")
            return
        }
        val user = User()
        user.username = et_account.text.toString()
        user.email = et_account.text.toString()
        user.setPassword(et_pwd.text.toString())
        user.pwdStr = et_pwd.text.toString()
        Log.e("user", et_account.text.toString() + "  " + et_pwd.text.toString())
        user.signUp(object : SaveListener<User>() {
            override fun done(user: User, e: BmobException?) {
                if (e == null) {
                    Snackbar.make(cl_reg, "注册成功", Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }

    override fun start() {
    }
}