package com.passwordsave.module.local_account;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Account2 {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public String account;
    public String password;
    public String remark;
    public boolean isCollect;

    @Ignore
    public boolean isShow = false;

    @Ignore
    @Override
    public String toString() {
        return "Account2{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", remark='" + remark + '\'' +
                ", isCollect=" + isCollect +
                ", isShow=" + isShow +
                '}';
    }
}
