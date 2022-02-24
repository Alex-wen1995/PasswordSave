package com.passwordsave.module.login;

import com.passwordsave.module.account.Account;

import java.util.List;

/**
 * Created on 2021/05/18 16:51
 *
 * @author quan
 */
public class User {
    private List<Account> accounts;
    private String  pwdStr;

    public String getPwdStr() {
        return pwdStr;
    }

    public void setPwdStr(String pwdStr) {
        this.pwdStr = pwdStr;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}
