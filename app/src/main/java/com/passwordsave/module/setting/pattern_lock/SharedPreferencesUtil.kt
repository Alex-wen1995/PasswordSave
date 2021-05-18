package com.passwordsave.module.setting.pattern_lock

import android.content.SharedPreferences
import com.passwordsave.app.MyApplication

/**
 * Created by quan on 14/10/2017.
 */
internal class SharedPreferencesUtil private constructor() {
    private val prefer: SharedPreferences = MyApplication.context
        .getSharedPreferences("test", android.content.Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = prefer.edit()

    fun saveString(name: String?, data: String?) {
        editor.putString(name, data)
        editor.commit()
    }

    fun getString(name: String?): String? {
        return prefer.getString(name, null)
    }

    companion object {
        @JvmStatic
        var instance: SharedPreferencesUtil? = null
            get() {
                if (field == null) {
                    synchronized(SharedPreferencesUtil::class.java) {
                        if (field == null) {
                            field = SharedPreferencesUtil()
                        }
                    }
                }
                return field
            }
            private set
    }
}