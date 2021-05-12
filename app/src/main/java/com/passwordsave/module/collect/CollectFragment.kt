package com.passwordsave.module.collect

import com.passwordsave.R
import com.passwordsave.base.BaseFragment
import kotlinx.android.synthetic.main.layout_top.*

class CollectFragment : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_collect
    }

    override fun initView() {
        top_title.text = "我的收藏"
    }

    override fun lazyLoad() {
    }

    override fun initListener() {
    }
}