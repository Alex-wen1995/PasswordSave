package com.passwordsave.module.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.passwordsave.module.account.Account2;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface AccountDao {
    /**
     * @Insert注解可以设置一个属性： onConflict：默认值是OnConflictStrategy.ABORT，表示当插入有冲突的时候的处理策略。OnConflictStrategy封装了Room解决冲突的相关策略：
     *        1. OnConflictStrategy.REPLACE：冲突策略是取代旧数据同时继续事务。
     * <p>
     *        2. OnConflictStrategy.ROLLBACK：冲突策略是回滚事务。
     * <p>
     *        3. OnConflictStrategy.ABORT：冲突策略是终止事务。
     * <p>
     *        4. OnConflictStrategy.FAIL：冲突策略是事务失败。
     * <p>
     *        5. OnConflictStrategy.IGNORE：冲突策略是忽略冲突。
     * <p>
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAccount(Account2... accounts);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateAccount(Account2... accounts);

    @Delete
    void deleteAccount(Account2... accounts);

    //删全部
    @Query("DELETE FROM Account2")
    void deleteAll();


    @Query("SELECT * FROM Account2")
    Flowable<List<Account2>> loadAllAccount();

    @Query("SELECT * FROM Account2 WHERE title LIKE +:search OR account LIKE :search")
    Flowable<List<Account2>> loadAccountByKeyword(String search);

    @Query("SELECT * FROM Account2 WHERE isCollect == :isCollect and (title LIKE +:search OR account LIKE :search)")
    Flowable<List<Account2>> loadAccountByCollect(boolean isCollect, String search);


}
