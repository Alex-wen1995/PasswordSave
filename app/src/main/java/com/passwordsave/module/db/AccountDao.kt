package com.passwordsave.module.db

import androidx.room.*
import com.passwordsave.module.account.Account
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

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


    /**
     * 返回的类型是Long也只能是Long，否则无法通过编译。
     * 返回的Long值，是指的插入的行id。
     * */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAccount(vararg accounts: Account):Single<List<Long>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAccountNotEvent(vararg accounts: Account)

    /**
     * 返回的类型为Integer也只能是Integer，否则无法通过编译。
     * 返回的Integer值，指的是该次操作影响到的总行数，比如该次操作更新了5条，就返回5。
     * */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAccount(vararg accounts: Account):Single<Int>

    @Delete
    fun deleteAccount(vararg accounts: Account):Single<Int>

    //删全部
    @Query("DELETE FROM Account")
    fun deleteAll()

    @Query("SELECT * FROM Account")
    fun loadAllAccount(): Flowable<List<Account>>

//    @Query("SELECT * FROM Account WHERE title LIKE +:search OR account LIKE :search order by id desc") //模糊搜索title跟account
    @Query("SELECT * FROM Account WHERE title LIKE +:search order by id desc")//模糊搜索title
    fun loadAccountByKeyword(search: String?): Flowable<List<Account>>

    //搜索是否有相同的数据
    @Query("SELECT * FROM Account WHERE title = :title AND password = :password AND account = :account" )
    fun searchSameAccount(title: String?,account: String?,password:String?): Maybe<List<Account>>
}