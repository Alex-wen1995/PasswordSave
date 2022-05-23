package com.passwordsave.module.main


import android.Manifest
import android.content.Intent
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.PermissionUtils
import com.passwordsave.R
import com.passwordsave.base.BaseActivity
import com.passwordsave.module.account.Account
import com.passwordsave.module.account.AccountActivity
import com.passwordsave.module.account.AddAccountActivity
import com.passwordsave.module.random.RandomActivity
import com.passwordsave.module.scanner.ScannerKit
import com.passwordsave.module.setting.SettingActivity
import com.passwordsave.utils.showToast
import com.socks.library.KLog
import kotlinx.android.synthetic.main.activity_add_account.*
import kotlinx.android.synthetic.main.fragment_account.fab
import kotlinx.android.synthetic.main.fragment_collect.*
import kotlinx.android.synthetic.main.layout_top.*
import pub.devrel.easypermissions.EasyPermissions
import java.io.BufferedReader
import java.io.InputStreamReader


class MainActivity : BaseActivity(){


    override fun layoutId(): Int {
        return R.layout.activity_main
    }

    override fun initData() {
        top_title.text = "首页"
    }

    override fun initView() {
//        val list = GsonUtils.fromJson<ArrayList<Account>>(readTxt(), GsonUtils.getListType(Account::class.java))
//        list.forEach {
//            val data = Account()
//            data.title = it.title
//            data.account = it.account
//            data.password = it.password
//            data.remark = it.remark
//            KLog.e("data", data.toString())
//            mAppDatabase.accountDao()!!.insertAccount(data)
//        }
    }
    fun readTxt(): String {
        var str = ""
        try {
            val assetManager = this.applicationContext.assets
            val isr = InputStreamReader(assetManager.open("json.txt"), "UTF-8")
            val br = BufferedReader(isr)
            var mimeTypeLine: String? = null
            while (br.readLine().also { mimeTypeLine = it } != null) {
                str += mimeTypeLine
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return str
    }

    override fun initListener() {

        fab.setOnClickListener {
            startActivity(Intent(this, AddAccountActivity::class.java))
        }

        menu_1.setOnClickListener {
            startActivity(Intent(this, AccountActivity::class.java))
        }
        menu_2.setOnClickListener {
            if(!PermissionUtils.isGranted(Manifest.permission.CAMERA)){
                EasyPermissions.requestPermissions(this, getString(R.string.need_permission), 0,
                    Manifest.permission.CAMERA
                )
            }else{
                ScannerKit.startCameraAsync(this)
            }
        }
        menu_3.setOnClickListener {
            ActivityUtils.startActivity(RandomActivity::class.java)
        }
        menu_4.setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
        }
    }
    private var exitTime: Long = 0
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event!!.action == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(applicationContext, "再按一次退出程序", Toast.LENGTH_SHORT).show()
                exitTime = System.currentTimeMillis()
            } else {
                AppUtils.exitApp()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        super.onPermissionsDenied(requestCode, perms)
        showToast("扫一扫功能需要打开摄像头权限")
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        ScannerKit.startCameraAsync(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
    override fun start() {

    }

}