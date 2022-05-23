package com.passwordsave.module.db

import androidx.room.*
import com.passwordsave.module.account.Account
import io.reactivex.Flowable

@Dao
interface AccountDao {
    /**
     * @Insert注解可以设置一个属性： onConflict：默认值是OnConflictStrategy.ABORT，表示当插入有冲突的时候的处理策略。OnConflictStrategy封装了Room解决冲突的相关策略：
     * 1. OnConflictStrategy.REPLACE：冲突策略是取代旧数据同时继续事务。
     *
     * 2. OnConflictStrategy.ROLLBACK：冲突策略是回滚事务。
     *
     * 3. OnConflictStrategy.ABORT：冲突策略是终止事务。
     *
     * 4. OnConflictStrategy.FAIL：冲突策略是事务失败。
     *
     * 5. OnConflictStrategy.IGNORE：冲突策略是忽略冲突。
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAccount(vararg accounts: Account?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAccount(vararg accounts: Account?): Int

    @Delete
    fun deleteAccount(vararg accounts: Account?)

    //删全部
    @Query("DELETE FROM Account")
    fun deleteAll()

    @Query("SELECT * FROM Account")
    fun loadAllAccount(): Flowable<List<Account?>?>?

    @Query("SELECT * FROM Account WHERE title LIKE +:search OR account LIKE :search")
    fun loadAccountByKeyword(search: String?): Flowable<List<Account?>?>?
}