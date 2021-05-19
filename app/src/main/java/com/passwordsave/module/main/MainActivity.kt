package com.passwordsave.module.main

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.passwordsave.R
import com.passwordsave.base.BaseActivity
import com.passwordsave.module.collect.CollectFragment
import com.passwordsave.module.account.Account2
import com.passwordsave.module.account.Account
import com.passwordsave.module.account.AccountFragment
import com.passwordsave.module.setting.SettingFragment
import com.passwordsave.utils.showToast
import com.socks.library.KLog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    private var mTab1: Fragment? = null
    private var mTab2: Fragment? = null
    private var mTab3: Fragment? = null

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
            .addItem(
                BottomNavigationItem(
                    R.drawable.ic_set_1,
                    ""
                ).setInactiveIconResource(R.drawable.ic_set_2)
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
        setSelect(0)
    }

    override fun onResume() {
        super.onResume()
        getList()
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
            2 -> if (mTab3 == null) {
                mTab3 = SettingFragment()
                transaction.add(R.id.view_stub_main, mTab3 as SettingFragment)
            } else {
                transaction.show(mTab3!!)
            }
            else -> {
            }
        }
        transaction.commit()
    }

    private fun getList() {
        val query: BmobQuery<Account> = BmobQuery("Account")
        query.addWhereEqualTo("username", BmobUser.getCurrentUser().username) //查询当前用户
        query.order("-createdAt")
            .findObjects(object : FindListener<Account?>() {
                override fun done(list: List<Account?>?, e: BmobException?) {
                    if (e == null) {
                        if(list!!.isEmpty()){
                            showToast("暂无数据！")
                        }else{
                            mAppDatabase.accountDao()!!.deleteAll()
                            for (i in list){
                                val data =
                                    Account2()
                                i!!.let {
                                    data.title =i.title
                                    data.objectId =i.objectId
                                    data.account = i.account
                                    data.password = i.password
                                    data.remark = i.remark
                                    data.isCollect = i.isCollect
                                    KLog.e("data",data.toString())
                                    mAppDatabase.accountDao()!!.insertAccount(data)

                                }
                            }
                        }
                    } else {
                        showToast("暂无数据！")
                    }
                }
            })
    }


    private fun hideFragment(transaction: FragmentTransaction) {
        if (mTab1 != null) {
            transaction.hide(mTab1!!)
        }
        if (mTab2 != null) {
            transaction.hide(mTab2!!)
        }
        if (mTab3 != null) {
            transaction.hide(mTab3!!)
        }
    }



}