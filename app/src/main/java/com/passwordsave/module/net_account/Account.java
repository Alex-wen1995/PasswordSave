package com.passwordsave.module.net_account;

import cn.bmob.v3.BmobObject;

public class Account extends BmobObject {
    public int id;
    public String user_id;
    public String username;
    public String title;
    public String account;
    public String password;
    public String remark;
    public boolean isCollect;

    public transient boolean isShow = false;

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", user_id='" + user_id + '\'' +
                ", username='" + username + '\'' +
                ", title='" + title + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", remark='" + remark + '\'' +
                ", isCollect=" + isCollect +
                ", isShow=" + isShow +
                '}';
    }
}
