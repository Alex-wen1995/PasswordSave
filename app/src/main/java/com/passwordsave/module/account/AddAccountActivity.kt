package com.passwordsave.module.account

import android.util.Log
import android.view.View
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.passwordsave.R
import com.passwordsave.base.BaseActivity
import com.passwordsave.module.db.Account
import com.passwordsave.module.db.Account2
import com.passwordsave.utils.showToast
import com.socks.library.KLog
import kotlinx.android.synthetic.main.activity_add_account.*
import kotlinx.android.synthetic.main.layout_top.*

class AddAccountActivity : BaseActivity() {
    var is_collect = false
    override fun layoutId(): Int {
        return R.layout.activity_add_account
    }

    override fun initData() {
        top_title.text = "添加账号"
        iv_back.visibility = View.VISIBLE
        iv_collect.visibility = View.VISIBLE
    }

    override fun initView() {
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
            data.title = et_title.text.toString()
            data.account = et_account.text.toString()
            data.password = et_pwd.text.toString()
            data.remark = et_remark.text.toString()
            data.isCollect = is_collect
            KLog.e("data",data.toString())
            data.save(object : SaveListener<String>() {
                override fun done(objectId: String?, e: BmobException?) {
                    if(e==null){
                        showToast("添加数据成功，返回objectId为：$objectId")
                    }else{
                        showToast("创建数据失败："+ e.message)
                    }
                }
            })
            finish()
//            val ids: List<Long> = mAppDatabase.accountDao()!!.insertAccount(data)
//            for (id in ids) {
//                Log.e("Account", "id = $id")
//            }
        }
    }

    override fun start() {
    }
}