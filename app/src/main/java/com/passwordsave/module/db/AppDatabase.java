package com.passwordsave.module.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.passwordsave.module.local_account.Account2;

@Database(entities = {Account2.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract AccountDao accountDao();

}
