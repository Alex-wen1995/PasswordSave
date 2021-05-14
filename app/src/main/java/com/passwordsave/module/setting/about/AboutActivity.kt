package com.passwordsave.module.setting.about

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.passwordsave.R
import com.passwordsave.base.BaseActivity
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.layout_top.*

class AboutActivity : BaseActivity() {
    override fun layoutId(): Int {
        return R.layout.activity_about
    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        top_title.text="关于"
        iv_back.visibility = View.VISIBLE
        Glide.with(this)
            .load(R.drawable.logo)
            .into(iv_about_logo)
        tv_version.text ="版本号 " + getAppVersionName(this)
    }
    private fun getAppVersionName(context: Context): String? {
        var versionName: String? = null
        try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(context.packageName, 0)
            versionName = pi.versionName
        } catch (e: Exception) {
            Log.e("VersionInfo", "Exception", e)
        }
        return versionName
    }
    override fun initView() {
    }

    override fun initListener() {
        iv_back.setOnClickListener { finish() }
    }

    override fun start() {
    }
}