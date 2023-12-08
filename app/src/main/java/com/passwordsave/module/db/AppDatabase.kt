package com.passwordsave.module.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.passwordsave.app.MyApplication
import com.passwordsave.module.account.Account

@Database(entities = [Account::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun accountDao(): AccountDao?
    companion object {
        var instance = Room.databaseBuilder(MyApplication.context, AppDatabase::class.java, "android_room_dev.db")
        .allowMainThreadQueries()
        .build()
    }
}