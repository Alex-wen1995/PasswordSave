package com.passwordsave.module.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.passwordsave.module.account.Account
import com.passwordsave.module.db.AccountDao

@Database(entities = [Account::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao?
}