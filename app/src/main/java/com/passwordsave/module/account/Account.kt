package com.passwordsave.module.account

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

//本地room数据库实体类
@Entity
class Account {
    @JvmField
    @PrimaryKey(autoGenerate = true)
    var id = 0
    @JvmField
    var title: String? = null
    @JvmField
    var account: String? = null
    @JvmField
    var password: String? = null
    @JvmField
    var remark: String? = null
    @JvmField
    var createTime: Long? = System.currentTimeMillis()

    @Ignore
    @Transient
    var isShow = false
    @Ignore
    override fun toString(): String {
        return "{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", remark='" + remark + '\'' +
                ", createTime='" + createTime + '\'' +
                ", isShow=" + isShow +
                '}'
    }
}