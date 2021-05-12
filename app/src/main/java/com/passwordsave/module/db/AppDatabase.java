package com.passwordsave.module.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Account.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract AccountDao accountDao();

}
