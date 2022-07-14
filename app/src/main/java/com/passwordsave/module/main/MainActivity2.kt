package com.passwordsave.module.main


import android.Manifest
import android.content.Intent
import android.view.KeyEvent
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.PermissionUtils
import com.nightonke.boommenu.BoomButtons.HamButton
import com.nightonke.boommenu.ButtonEnum
import com.passwordsave.R
import com.passwordsave.base.BaseActivity
import com.passwordsave.module.account.AccountFragment
import com.passwordsave.module.account.AddAccountActivity
import com.passwordsave.module.random.RandomActivity
import com.passwordsave.module.scanner.ScannerKit
import com.passwordsave.module.setting.SettingActivity
import com.passwordsave.utils.showToast
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.fragment_account.fab
import kotlinx.android.synthetic.main.layout_top.*
import pub.devrel.easypermissions.EasyPermissions
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity2 : BaseActivity() {


    override fun layoutId(): Int {
        return R.layout.activity_main2
    }


    override fun initData() {
        top_title.text = "我的账号"
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        var mTab1 = supportFragmentManager.findFragmentByTag(AccountFragment::class.java.name)
        if (mTab1 == null) {
            mTab1 = AccountFragment()
            transaction.add(R.id.fl_main, mTab1,AccountFragment::class.java.name)
        } else {
            transaction.show(mTab1)
        }
        transaction.commitAllowingStateLoss()
        initBmb()
    }

    override fun onStop() {
        super.onStop()
        bmb.reboomImmediately()
    }
    private fun initBmb() {
        bmb.buttonEnum = ButtonEnum.Ham
//        bmb.addBuilder(HamButton.Builder()
//                .normalImageRes(R.drawable.ic_menu_2)
//                .normalText("我的账号")
//                .pieceColor(ContextCompat.getColor(this, R.color.color_B4C8ED))
//                .normalColor(ContextCompat.getColor(this, R.color.color_B4C8ED)))
        bmb.addBuilder(HamButton.Builder()
            .normalImageRes(R.drawable.ic_menu_1)
            .normalText("扫一扫")
            .subNormalText("查看二维码信息")
            .pieceColor(ContextCompat.getColor(this, R.color.color_F4D2D0))
            .normalColor(ContextCompat.getColor(this, R.color.color_F4D2D0))
            .listener {
                bmb.reboomImmediately()
                if(!PermissionUtils.isGranted(Manifest.permission.CAMERA)){
                    EasyPermissions.requestPermissions(this@MainActivity2, getString(R.string.need_permission), 0,
                        Manifest.permission.CAMERA
                    )
                }else{
                    ScannerKit.startCameraAsync()
                }
            }
        )
        bmb.addBuilder(HamButton.Builder()
            .normalImageRes(R.drawable.ic_menu_3)
            .normalText("随机事件")
            .subNormalText("随机选择器")
            .pieceColor(ContextCompat.getColor(this, R.color.color_A29988))
            .normalColor(ContextCompat.getColor(this, R.color.color_A29988))
            .listener {
                bmb.reboomImmediately()
                ActivityUtils.startActivity(RandomActivity::class.java)
            }
        )
        bmb.addBuilder(HamButton.Builder()
            .normalImageRes(R.drawable.ic_menu_4)
            .normalText("设置")
            .subNormalText("App各项设置")
            .pieceColor(ContextCompat.getColor(this, R.color.color_9FD5C7))
            .normalColor(ContextCompat.getColor(this, R.color.color_9FD5C7))
            .listener {
                bmb.reboomImmediately()
                ActivityUtils.startActivity(SettingActivity::class.java)
            }
        )
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
//        ic_menu.setOnClickListener { }
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
        ScannerKit.startCameraAsync()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun start() {

    }

}