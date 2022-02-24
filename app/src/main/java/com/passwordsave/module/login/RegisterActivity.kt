package com.passwordsave.module.login

import android.util.Log
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

        }
    }



    override fun start() {
    }
}