package com.passwordsave.module.scanner

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import cn.bingoogolapple.qrcode.core.BarcodeType
import cn.bingoogolapple.qrcode.core.QRCodeView
import com.passwordsave.R
import com.passwordsave.base.BaseActivity
import com.passwordsave.utils.showToast
import kotlinx.android.synthetic.main.activity_scanner.*

class ScannerKit : BaseActivity(), QRCodeView.Delegate {

    override fun layoutId(): Int {
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
       showToast(result)
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

    override fun onScanQRCodeOpenCameraError() {
        Log.e("ScannerKit", "打开相机出错")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        zbarview.showScanRect()
    }
}