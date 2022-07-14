package com.passwordsave.module.account

import android.annotation.SuppressLint
import android.content.Intent
import android.text.InputType
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.passwordsave.R
import com.passwordsave.base.BaseFragment
import com.passwordsave.utils.showToast
import com.socks.library.KLog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.item_account.view.*


class AccountFragment : BaseFragment(){
    private val mAdapter = AccountAdapter(arrayListOf())

    override fun getLayoutId(): Int {
        return R.layout.fragment_account
    }

    override fun initView() {
        rv_account.layoutManager = LinearLayoutManager(requireContext())
        rv_account.adapter = mAdapter
    }

    override fun lazyLoad() {
        onRefresh()
    }

    override fun initListener() {
        smartLayout.setOnRefreshListener { onRefresh() }
        smartLayout.setEnableLoadMore(false)
        fab.setOnClickListener {
            startActivity(Intent(requireContext(), AddAccountActivity::class.java))
        }

        et_search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                //如果actionId是搜索的id，则进行下一步的操作
                getList()
                return@setOnEditorActionListener true
            }
            false
        }
    }

    private fun onRefresh() {
        getList()
    }


    @SuppressLint("CheckResult")
    private fun getList() {
        mAppDatabase.accountDao()!!
//            .loadAllAccount() //全部搜索
            .loadAccountByKeyword("%"+et_search.text.toString()+"%")!!//模糊搜索
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data ->
                if (data != null) {
                    KLog.e("t",data.size)
                    mAdapter.setNewData(data)
                }
                onRefreshComplete()
            }
    }

    private fun onRefreshComplete() { //刷新或加载更多完成
        if (smartLayout != null) {
            smartLayout.finishRefresh()
            smartLayout.finishLoadMore()
        }
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
            if (item.isShow) {
                itemView.iv_eye.setImageResource(R.drawable.ic_eye_2)
            } else {
                itemView.iv_eye.setImageResource(R.drawable.ic_eye_1)
            }
            itemView.iv_eye.setOnClickListener {
                if (item.isShow) {
                    itemView.iv_eye.setImageResource(R.drawable.ic_eye_1)
                    itemView.item_pwd.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                } else {
                    itemView.iv_eye.setImageResource(R.drawable.ic_eye_2)
                    itemView.item_pwd.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
                }
                item.isShow = !item.isShow
            }

            itemView.iv_copy.setOnClickListener {
                copyText(itemView.item_account)
            }

            itemView.iv_copy2.setOnClickListener {
                copyText(itemView.item_pwd)
            }
            itemView.delete_layout.setOnClickListener {
                val data = Account()//删除本地数据
                data.id = item.id
                mAppDatabase.accountDao()!!.deleteAccount(data)
                showToast("删除成功")
            }

            itemView.cl_item.setOnClickListener {
                startActivity(
                    Intent(requireContext(), UpdateAccountActivity::class.java)
                        .putExtra("id", item.id)
                        .putExtra("title", item.title)
                        .putExtra("account", item.account)
                        .putExtra("password", item.password)
                        .putExtra("remark", item.remark)
                )
            }
        }
    }

    fun copyText(tv: TextView) {
//        val manager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
//        val clipData = ClipData.newPlainText("text", tv.text)
//        manager.setPrimaryClip(clipData)
//        showToast("文本已复制")
    }



}