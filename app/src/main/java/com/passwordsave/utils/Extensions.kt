package com.passwordsave.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.passwordsave.app.MyApplication
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.ParseException
import java.util.*


/**
 * Created by quan on 2017/11/14.
 */

fun Fragment.showToast(content: String): Toast {
    val toast = Toast.makeText(this.activity?.applicationContext, content, Toast.LENGTH_SHORT)
    toast.show()
    return toast
}

fun Context.showToast(content: String): Toast {
    val toast = Toast.makeText(MyApplication.context, content, Toast.LENGTH_SHORT)
    toast.show()
    return toast
}

/**
 * 检查指纹硬件是否可用
 * 1、传感器当前不可用，清稍后再试
 * 11、信息没有录入，比如还没录入指纹
 * 12、没有合适的传感器或者没设置密码，例如手机没有指纹传感器
 * 15、传感器存在已知的漏洞，在更新修复漏洞前，传感器不可用
 * -2、设置的一些验证条件，当前手机的Android版本无法满足
 * -1、不知道是否可以进行验证。通常在旧版本的Android手机上出现，当出现这个错误是，仍然可以尝试进行验证
 * 0、可以进行验证
 */
fun Context.isFingerprintAvailable(context: Context): Int {
    val manager: BiometricManager = BiometricManager.from(context)
    return manager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)
}

/**
 * 开始验证
 *
 * @param activity
 * @param callBack 验证结果回调
 */
public fun authenticate(activity: FragmentActivity, callBack:BiometricPrompt.AuthenticationCallback ) {
    val promptInfo = createUi()
    val prompt = BiometricPrompt(activity, ContextCompat.getMainExecutor(activity), callBack)
    prompt.authenticate(promptInfo)
}

fun createUi() : BiometricPrompt.PromptInfo{
    return BiometricPrompt.PromptInfo.Builder()
        .setTitle("验证")
        .setSubtitle("请触摸指纹传感器")
        .setNegativeButtonText("取消")
        .build()
}


fun Context.startActivity(className:Class<*>){
    startActivity(Intent(this,className))
}

fun View.dip2px(dipValue: Float): Int {
    val scale = this.resources.displayMetrics.density
    return (dipValue * scale + 0.5f).toInt()
}

fun View.px2dip(pxValue: Float): Int {
    val scale = this.resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}

fun durationFormat(duration: Long?): String {
    val minute = duration!! / 60
    val second = duration % 60
    return if (minute <= 9) {
        if (second <= 9) {
            "0$minute' 0$second''"
        } else {
            "0$minute' $second''"
        }
    } else {
        if (second <= 9) {
            "$minute' 0$second''"
        } else {
            "$minute' $second''"
        }
    }
}

/**
 * 数据流量格式化
 */
fun Context.dataFormat(total: Long): String {
    var result: String
    var speedReal: Int = (total / (1024)).toInt()
    result = if (speedReal < 512) {
        speedReal.toString() + " KB"
    } else {
        val mSpeed = speedReal / 1024.0
        (Math.round(mSpeed * 100) / 100.0).toString() + " MB"
    }
    return result
}
fun Context.startActivityNoParam(clazz: Class<*>) {
    this.startActivity(Intent(this, clazz))
}

fun Context.base64ToBitmap(code:String): Bitmap {
    val bytes= Base64.decode(code.toByteArray(),Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(bytes,0,bytes.size)
}

fun Context.bitmapToBase64(bitmap: Bitmap):String{
    var result = ""
    val baos= ByteArrayOutputStream()
    try {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        baos.flush()
        baos.close()
        val bitmapBytes: ByteArray = baos.toByteArray()
        result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT)
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        try {
            baos.flush()
            baos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return result
}



fun Context.dateStringToMill(date:String): Long {
    var timeInMilliseconds:Long = 0
    val sdf=java.text.SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    try {
        val mDate: Date = sdf.parse(date)
        timeInMilliseconds = mDate.time
        println("Date in milli :: $timeInMilliseconds")
        return timeInMilliseconds
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return timeInMilliseconds
}



