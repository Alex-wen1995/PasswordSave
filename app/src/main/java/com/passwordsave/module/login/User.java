package com.passwordsave.module.login;

import com.passwordsave.module.account.Account2;

import java.util.List;

/**
 * Created on 2021/05/18 16:51
 *
 * @author quan
 */
public class User {
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
