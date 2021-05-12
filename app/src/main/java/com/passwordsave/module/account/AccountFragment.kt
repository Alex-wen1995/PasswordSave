package com.passwordsave.module.account

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.passwordsave.R
import com.passwordsave.base.BaseFragment
import com.passwordsave.module.db.Account
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.item_account.view.*
import kotlinx.android.synthetic.main.layout_top.*
import java.util.ArrayList
import java.util.HashMap


class AccountFragment : BaseFragment() {
    private val mAdapter = AccountAdapter(arrayListOf())
    private val dataList: ArrayList<Account> =
        ArrayList()
    override fun getLayoutId(): Int {
        return R.layout.fragment_account
    }


    override fun initView() {
        top_title.text = "我的账号"

        rv_account.layoutManager = LinearLayoutManager(requireContext())
        rv_account.adapter = mAdapter


    }

    override fun lazyLoad() {

    }
    override fun initListener() {
        smartLayout.setOnRefreshListener { onRefresh() }
        smartLayout.setOnLoadMoreListener { onLoadMore() }
        fab.setOnClickListener {
            startActivity(Intent(requireContext(),AddAccountActivity::class.java))
        }
    }
    private fun onRefresh() {
        dataList.clear()
        getList()
    }

    private fun onLoadMore() {
        getList()
    }

    @SuppressLint("CheckResult")
    private fun getList() {
        mAppDatabase.accountDao()!!
            .loadAllAccount()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { t ->
                onRefreshComplete()
                if (t != null) {
                   mAdapter.setNewData(t)
                }
            }
    }

    fun onRefreshComplete() { //刷新或加载更多完成
        smartLayout.finishRefresh()
        smartLayout.finishLoadMore()
    }

    inner class AccountAdapter(data: MutableList<Account>) :
        BaseQuickAdapter<Account, BaseViewHolder>(
            R.layout.item_account,
            data
        ) {
        @SuppressLint("SetTextI18n")
        override fun convert(helper: BaseViewHolder, item: Account) {
            val itemView = helper.itemView
            itemView.item_title.text = item.title
            itemView.item_account.text = item.account
            itemView.item_pwd.text = item.password
            itemView.item_title.text = item.account
            if(item.isShow){
                itemView.iv_eye.setImageResource(R.drawable.ic_eye_2)
            }else{
                itemView.iv_eye.setImageResource(R.drawable.ic_eye_1)

            }
            itemView.iv_eye.setOnClickListener {
                if(item.isShow){
                    itemView.iv_eye.setImageResource(R.drawable.ic_eye_1)
                }else{
                    itemView.iv_eye.setImageResource(R.drawable.ic_eye_2)
                }
                item.isShow = !item.isShow
            }
        }
    }
}