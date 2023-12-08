package com.passwordsave.module.account

import android.util.Log
import android.view.View

import com.passwordsave.R
import com.passwordsave.base.BaseActivity
import com.passwordsave.module.db.AppDatabase
import com.passwordsave.utils.showToast

import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_account.*
import kotlinx.android.synthetic.main.layout_top.*


class UpdateAccountActivity : BaseActivity() {
    var id = 0
    override fun layoutId(): Int {
        return R.layout.activity_add_account
    }

    override fun initData() {
        top_title.text = "更新账号"
        iv_back.visibility = View.VISIBLE
    }

    override fun initView() {
        id = intent.getIntExtra("id",0)
        et_title.setText(intent.getStringExtra("title"))
        et_account.setText(intent.getStringExtra("account"))
        et_pwd.setText(intent.getStringExtra("password"))
        et_remark.setText(intent.getStringExtra("remark"))
    }

    override fun initListener() {
        iv_back.setOnClickListener {
            finish()
        }

        btn_save.setOnClickListener {
            val data = Account()
            data.id = id
            data.title = et_title.text.toString()
            data.account = et_account.text.toString()
            data.password = et_pwd.text.toString()
            data.remark = et_remark.text.toString()
            Log.e("data",data.toString())
            AppDatabase.instance.accountDao()!!.updateAccount(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<Int> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onSuccess(t: Int) {
                        showToast("更新成功")
                    }

                })
            finish()
        }
    }

    override fun start() {
    }
}