package com.passwordsave.module.login;

import com.passwordsave.module.db.Account2;

import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created on 2021/05/18 16:51
 *
 * @author quan
 */
public class User extends BmobUser {
    private List<Account2> accounts;
    private String  pwdStr;

    public String getPwdStr() {
        return pwdStr;
    }

    public void setPwdStr(String pwdStr) {
        this.pwdStr = pwdStr;
    }

    public List<Account2> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account2> accounts) {
        this.accounts = accounts;
    }
}
