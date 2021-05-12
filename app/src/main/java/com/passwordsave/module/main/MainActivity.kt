package com.passwordsave.module.main

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.passwordsave.R
import com.passwordsave.base.BaseActivity
import com.passwordsave.module.account.AccountFragment
import com.passwordsave.module.collect.CollectFragment
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : BaseActivity() {
    private var mTab1: Fragment? = null
    private var mTab2: Fragment? = null

    override fun layoutId(): Int {
        return R.layout.activity_main
    }

    override fun initData() {
    }

    override fun initView() {
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED_NO_TITLE)
        bottomNavigationBar.setBarBackgroundColor(R.color.colorWhite)
        bottomNavigationBar
            .addItem(
                BottomNavigationItem(
                    R.drawable.ic_access_1,
                    ""
                ).setInactiveIconResource(R.drawable.ic_access)
                    .setActiveColor(R.color.colorPrimary)
            )
            .addItem(
                BottomNavigationItem(
                    R.drawable.ic_collect_1,
                    ""
                ).setInactiveIconResource(R.drawable.ic_collect)
                    .setActiveColor(R.color.colorPrimary)
            )
            .initialise()
    }

    override fun initListener() {
        bottomNavigationBar.setTabSelectedListener(object :
            BottomNavigationBar.OnTabSelectedListener {
            override fun onTabSelected(position: Int) {
                setSelect(position)
            }

            override fun onTabUnselected(position: Int) {}
            override fun onTabReselected(position: Int) {
                setSelect(position)
            }
        })
    }

    override fun start() {
        checkPermission()
        setSelect(0)
    }

    private fun setSelect(i: Int) {
        val fm: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fm.beginTransaction()
        hideFragment(transaction)
        when (i) {
            0 -> if (mTab1 == null) {
                mTab1 = AccountFragment()
                transaction.add(R.id.view_stub_main, mTab1 as AccountFragment)
            } else {
                transaction.show(mTab1!!)
            }
            1 -> if (mTab2 == null) {
                mTab2 = CollectFragment()
                transaction.add(R.id.view_stub_main, mTab2 as CollectFragment)
            } else {
                transaction.show(mTab2!!)
            }

            else -> {
            }
        }
        transaction.commit()
    }

    private fun hideFragment(transaction: FragmentTransaction) {
        if (mTab1 != null) {
            transaction.hide(mTab1!!)
        }
        if (mTab2 != null) {
            transaction.hide(mTab2!!)
        }
    }

    /**
     * 6.0以下版本(系统自动申请) 不会弹框
     * 有些厂商修改了6.0系统申请机制，他们修改成系统自动申请权限了
     */
    private fun checkPermission() {
        val perms = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        EasyPermissions.requestPermissions(this, getString(R.string.need_permission), 0, *perms)
    }
}