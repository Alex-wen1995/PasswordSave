package com.passwordsave.module.main


import android.Manifest
import android.content.Intent
import android.util.Log
import android.view.inputmethod.EditorInfo
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.PermissionUtils
import com.passwordsave.R
import com.passwordsave.base.BaseActivity
import com.passwordsave.module.account.AccountActivity
import com.passwordsave.module.account.AddAccountActivity
import com.passwordsave.module.random.RandomActivity
import com.passwordsave.module.scanner.ScannerKit
import com.passwordsave.module.setting.SettingActivity
import com.passwordsave.utils.showToast
import kotlinx.android.synthetic.main.fragment_account.fab
import kotlinx.android.synthetic.main.fragment_collect.*
import kotlinx.android.synthetic.main.layout_top.*
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : BaseActivity(){


    override fun layoutId(): Int {
        return R.layout.activity_main
    }

    override fun initData() {
        top_title.text = "首页"

    }

    override fun initView() {

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