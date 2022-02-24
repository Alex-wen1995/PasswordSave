package com.passwordsave.module.setting

import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.passwordsave.R
import com.passwordsave.app.AppActivityManager
import com.passwordsave.base.BaseFragment
import com.passwordsave.module.login.LoginActivity
import com.passwordsave.module.main.Term2Activity
import com.passwordsave.module.setting.about.AboutActivity
import com.passwordsave.module.setting.fingerprint_identification.FingerSetActivity
import com.passwordsave.module.setting.pattern_lock.PatternSettingActivity
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
                SettingBean("指纹识别", 0),
//                SettingBean("手势密码", 1),
                SettingBean("关于", 2),
                SettingBean("隐私政策", 3),
                SettingBean("退出登录", 4)
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
                    0 -> mContext.startActivityNoParam(FingerSetActivity::class.java)
                    1 -> mContext.startActivityNoParam(PatternSettingActivity::class.java)
                    2 -> mContext.startActivityNoParam(AboutActivity::class.java)
                    3 -> mContext.startActivityNoParam(Term2Activity::class.java)
                    4 -> {
                        AlertDialog.Builder(requireContext())
                            .setTitle("确认")
                            .setMessage("退出登录？")
                            .setPositiveButton("是") { _, _ -> //退出登錄
                                AppActivityManager.getAppManager().finishAllActivity()
                                startActivity(
                                    Intent(
                                        context,
                                        LoginActivity::class.java
                                    )
                                )
                            }
                            .setNegativeButton("否", null)
                            .show()
                    }
                }
            }
        }
    }
}