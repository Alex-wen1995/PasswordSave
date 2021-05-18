package com.passwordsave.module.db;

import cn.bmob.v3.BmobObject;

public class Account2 extends BmobObject {
    public int id;
    public int user_id;
    public String title;
    public String account;
    public String password;
    public String remark;
    public boolean isCollect;

    public transient boolean isShow = false;


}
