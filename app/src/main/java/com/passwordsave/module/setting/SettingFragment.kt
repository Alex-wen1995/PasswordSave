package com.passwordsave.module.setting

import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.passwordsave.R
import com.passwordsave.base.BaseFragment
import com.passwordsave.module.main.Term2Activity
import com.passwordsave.utils.startActivityNoParam
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.item_menu.view.*
import kotlinx.android.synthetic.main.layout_top.*

class SettingFragment : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_setting
    }

    override fun initView() {
        top_title.text = "设置"
        rv_setting.layoutManager = LinearLayoutManager(requireContext())
        rv_setting.adapter = SettingAdapter(
            R.layout.item_menu, arrayListOf(
                SettingBean("手势密码", 1),
                SettingBean("关于", 2),
                SettingBean("隐私政策", 3)
            )
        )
    }

    override fun lazyLoad() {
    }

    override fun initListener() {
    }

    inner class SettingAdapter(layoutResId: Int, data: MutableList<SettingBean>) :
        BaseQuickAdapter<SettingBean, BaseViewHolder>(layoutResId, data) {
        override fun convert(helper: BaseViewHolder, item: SettingBean) {
            val itemView = helper.itemView
            itemView.item_profile_name.text = item.title

            itemView.setOnClickListener {
                when (item.type) {
//                    1 -> mContext.startActivityNoParam(EditProfileActivity::class.java)
//                    2 -> mContext.startActivityNoParam(SettingActivity::class.java)
                    3 -> mContext.startActivityNoParam(Term2Activity::class.java)

                }
            }
        }
    }
}