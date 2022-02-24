package com.passwordsave.module.scanner

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import cn.bingoogolapple.qrcode.core.BarcodeType
import cn.bingoogolapple.qrcode.core.QRCodeView
import com.blankj.utilcode.util.BarUtils
import com.passwordsave.R
import com.passwordsave.base.BaseActivity
import com.passwordsave.utils.showToast
import kotlinx.android.synthetic.main.activity_scanner.*

class ScannerKit : BaseActivity(), QRCodeView.Delegate{

    override fun layoutId(): Int {
        BarUtils.transparentStatusBar(this)
        return R.layout.activity_scanner
    }

    override fun initData() {
    }
    override fun initView() {

        zbarview.setDelegate(this)
        zbarview.setType(BarcodeType.ALL, null)
    }

    override fun initListener() {
        v_back.setOnClickListener { finish() }
    }

    override fun start() {
    }

    override fun onStart() {
        super.onStart()
        zbarview.startCamera()
        zbarview.changeToScanQRCodeStyle()
        zbarview.startSpotAndShowRect() // 显示扫描框，并开始识别
    }
    override fun onDestroy() {
        zbarview.onDestroy()
        super.onDestroy()

    }

    override fun onStop() {
        zbarview.stopCamera()
        super.onStop()
    }

    companion object{
        @JvmStatic
        fun startCameraAsync(context: Context) {
            val intent = Intent(context, ScannerKit::class.java)
            context.startActivity(intent)
        }
    }

    override fun onScanQRCodeSuccess(result: String) {
        val view  = LayoutInflater.from(this).inflate(R.layout.dialog_scan_result, null)
        val resultTv = view.findViewById<TextView>(R.id.tv_result)
        resultTv.text = result
        val btnCopy = view.findViewById<Button>(R.id.btn_copy)
        val builder: AlertDialog.Builder = AlertDialog.Builder(this,R.style.CustomProgressDialog)
        builder.setView(view)
        val dialog = builder.create()
        btnCopy.setOnClickListener {
            copyText(resultTv,dialog)
        }
        dialog.show()
    }
    fun copyText(tv: TextView,dialog: AlertDialog) {
        val manager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", tv.text)
        manager.setPrimaryClip(clipData)
        dialog.dismiss()
        showToast("文本已复制")
    }
    override fun onCameraAmbientBrightnessChanged(isDark: Boolean) {
        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
        var tipText: String = zbarview.scanBoxView.tipText
        val ambientBrightnessTip = "\n环境过暗，请打开闪光灯"
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                zbarview.scanBoxView.tipText = tipText + ambientBrightnessTip
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip))
                zbarview.scanBoxView.tipText = tipText
            }
        }
    }



    override fun onPointerCaptureChanged(hasCapture: Boolean) {}
    override fun onScanQRCodeOpenCameraError() {
        Log.e("ScannerKit", "打开相机出错")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        zbarview.showScanRect()
    }
}