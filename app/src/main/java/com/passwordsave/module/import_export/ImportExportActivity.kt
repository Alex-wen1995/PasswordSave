package com.passwordsave.module.import_export

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.menu.MenuAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.PathUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.github.gzuliyujiang.filepicker.ExplorerConfig
import com.github.gzuliyujiang.filepicker.FilePicker
import com.github.gzuliyujiang.filepicker.annotation.ExplorerMode
import com.github.gzuliyujiang.filepicker.contract.OnFilePickedListener
import com.passwordsave.R
import com.passwordsave.base.BaseActivity
import com.passwordsave.module.account.Account
import com.passwordsave.module.account.AccountData
import com.passwordsave.module.db.AppDatabase
import com.passwordsave.module.setting.SettingBean
import com.passwordsave.utils.isAndroid11
import com.passwordsave.utils.showToast
import kotlinx.android.synthetic.main.activity_import_export.rv_import
import kotlinx.android.synthetic.main.item_menu.view.item_profile_name
import kotlinx.android.synthetic.main.layout_top.iv_back
import kotlinx.android.synthetic.main.layout_top.top_title
import org.json.JSONArray
import org.json.JSONObject
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

class ImportExportActivity : BaseActivity() , OnFilePickedListener {

    val backupsFileDir = PathUtils.getExternalStoragePath()+"/passwordSaveBackups.txt"
    //配置需要取的权限
    val PERMISSION = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,  // 写入权限
            Manifest.permission.READ_EXTERNAL_STORAGE,  //读取权限
        )

//打开文件管理意图
private val enableExternalStorageManager =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        EasyPermissions.requestPermissions(this, "导出、导入文件需要获取手机的存储和文件管理权限,请允许以下权限", 0, *PERMISSION)
    }
    //android 11检测是否开启文件管理权限
    private fun android11CheckExternalStorageManager(){
        if (isAndroid11()){
            if (!Environment.isExternalStorageManager()){
                //Android11暂不做处理
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = Uri.parse("package:" + this.packageName)
                enableExternalStorageManager.launch(intent)
            }else{
                EasyPermissions.requestPermissions(this, "导出、导入文件需要获取手机的存储和文件管理权限,请允许以下权限", 0, *PERMISSION)
            }
        }else{
            EasyPermissions.requestPermissions(this, "导出、导入文件需要获取手机的存储和文件管理权限,请允许以下权限", 0, *PERMISSION)
        }
    }

    override fun layoutId(): Int {
        return R.layout.activity_import_export
    }

    override fun initData() {
        //判断有无权限
        if (!EasyPermissions.hasPermissions(this,*PERMISSION)) {
            showMsgDialog(this)
        }
    }

    override fun initView() {
        top_title.text = "导入导出"
        iv_back.visibility = View.VISIBLE
        rv_import.layoutManager = LinearLayoutManager(this)
        rv_import.adapter = MenuAdapter(
            R.layout.item_menu, arrayListOf(
                SettingBean("导入数据", 0),
                SettingBean("导出数据", 1),
                SettingBean("清除数据", 2),
            )
        )
    }
    inner class MenuAdapter(layoutResId: Int, data: MutableList<SettingBean>) :
        BaseQuickAdapter<SettingBean, BaseViewHolder>(layoutResId, data) {
        override fun convert(helper: BaseViewHolder, item: SettingBean) {
            val itemView = helper.itemView
            itemView.item_profile_name.text = item.title

            itemView.setOnClickListener {
                when (item.type) {
                    0 -> {
                        //导入数据
                        onFilePick()
                    }
                    1 -> {
                        FileUtils.createOrExistsDir(backupsFileDir)
                        val file = File(backupsFileDir)
                        if(file.exists()){
                            file.delete()
                        }
                        val data = AccountData.dataList
                        val array = JSONArray()
                        for (i in data.indices) {
                            val dataObject = JSONObject()
                            dataObject.put("account",data[i].account)
                            dataObject.put("remark",data[i].remark)
                            dataObject.put("title",data[i].title)
                            dataObject.put("password",data[i].password)
                            array.put(dataObject)
                        }

                        FileIOUtils.writeFileFromString(file, array.toString())
                        showToast("已导出至：$backupsFileDir")
                    }
                    2->{
                        AppDatabase.instance.accountDao()!!.deleteAll()
                        showToast("清除成功")
                    }
                }
            }
        }
    }
    fun onFilePick() {
        val config = ExplorerConfig(this)
        config.rootDir = Environment.getExternalStorageDirectory()
        config.isLoadAsync = false
        config.explorerMode = ExplorerMode.FILE
        config.onFilePickedListener = this
        val picker = FilePicker(this)
        picker.setExplorerConfig(config)
        picker.show()


    }
    override fun initListener() {
        iv_back.setOnClickListener {
            finish()
        }
    }

    override fun start() {
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        showToast("权限不足")
        finish()
    }

    override fun onFilePicked(file: File) {
        val list: ArrayList<Account>
        try {
            list = GsonUtils.fromJson(readTxt(file), GsonUtils.getListType(Account::class.java))
            list.reverse()
            list.forEach {
                val data = Account()
                data.title = it.title
                data.account = it.account
                data.password = it.password
                data.remark = it.remark
                Log.e("data", data.toString())
                AppDatabase.instance.accountDao()!!.insertAccount(data)
            }
            showToast("导入完成！")
        } catch (e:Exception){
            showToast("文件格式错误")
        }

    }

    fun readTxt(file: File): String {
        return FileIOUtils.readFile2String(file)
    }

    private fun showMsgDialog(context: Context) {
        val builder = AlertDialog.Builder(context, R.style.CustomProgressDialog)
        val dialog = View.inflate(context, R.layout.dialog_permission_msg, null)
        val refuse = dialog.findViewById<TextView>(R.id.tv_refuse)
        val agree = dialog.findViewById<TextView>(R.id.tv_agree)
        builder.setView(dialog)
        val alertDialog = builder.create()
        agree.setOnClickListener {
            alertDialog.dismiss()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                android11CheckExternalStorageManager()
            } else {
                EasyPermissions.requestPermissions(this, "导出、导入文件需要获取手机的存储和文件管理权限,请允许以下权限", 0, *PERMISSION)
            }
        }
        refuse.setOnClickListener {
            alertDialog.dismiss()
            finish()
        }
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.show()
    }
}