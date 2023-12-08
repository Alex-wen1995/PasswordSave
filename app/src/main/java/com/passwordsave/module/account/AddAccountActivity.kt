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
            Log.e("data", data.toString())
            AppDatabase.instance.accountDao()!!
                .searchSameAccount(data.title, data.account, data.password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterSuccess {
                    if (it.isNotEmpty()) {
                        showToast("已存在数据！")
                    } else {
                        AppDatabase.instance.accountDao()!!.insertAccount(data)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(object : SingleObserver<List<Long>> {
                                override fun onSubscribe(d: Disposable) {

                                }

                                override fun onError(e: Throwable) {
                                }

                                override fun onSuccess(t: List<Long>) {
                                    showToast("添加成功")
                                    finish()
                                }

                            })

                    }
                }
                .subscribe()
        }
    }

    override fun start() {
    }
}