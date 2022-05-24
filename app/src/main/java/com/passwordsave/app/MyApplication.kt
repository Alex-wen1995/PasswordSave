package com.passwordsave.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.room.Room

import com.passwordsave.module.db.AppDatabase
import com.passwordsave.utils.PreviewImageLoader
import com.previewlibrary.ZoomMediaLoader
import com.socks.library.KLog
import com.tencent.mmkv.MMKV
import com.passwordsave.utils.DisplayManager
import kotlin.properties.Delegates


/**
 * Created by xuhao on 2017/11/16.
 *
 */

class MyApplication : Application(){

    private lateinit var mAppDatabase: AppDatabase

    companion object {
        private val TAG = "MyApplication"
        var context: Context by Delegates.notNull()
            private set
    }

    fun getAppDatabase() : AppDatabase {
        return mAppDatabase
    }
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        KLog.init(true)
        DisplayManager.init(this)
        registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks)
        ZoomMediaLoader.getInstance().init(PreviewImageLoader())
        MMKV.initialize(this)
        mAppDatabase = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "android_room_dev.db")
            .allowMainThreadQueries()
            .build()
    }

    private val mActivityLifecycleCallbacks = object : ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            Log.d(TAG, "onCreated: " + activity.componentName.className)
        }

        override fun onActivityStarted(activity: Activity) {
            Log.d(TAG, "onStart: " + activity.componentName.className)
        }

        override fun onActivityResumed(activity: Activity) {

        }

        override fun onActivityPaused(activity: Activity) {

        }

        override fun onActivityStopped(activity: Activity) {

        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

        }

        override fun onActivityDestroyed(activity: Activity) {
            Log.d(TAG, "onDestroy: " + activity.componentName.className)
        }
    }


}
