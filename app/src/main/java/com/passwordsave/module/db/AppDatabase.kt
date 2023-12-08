package com.passwordsave.module.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.passwordsave.app.MyApplication
import com.passwordsave.module.account.Account

@Database(entities = [Account::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun accountDao(): AccountDao?
    companion object {
        val migration1to2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE Account ADD COLUMN createTime INTEGER")
            }
        }
        var instance = Room.databaseBuilder(MyApplication.context, AppDatabase::class.java, "android_room_dev.db")
        .allowMainThreadQueries()
        .addMigrations(migration1to2)
        .build()
    }
}