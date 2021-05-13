package com.passwordsave.module.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

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
    List<Long> insertAccount(Account... accounts);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateAccount(Account... accounts);

    @Delete
    void deleteAccount(Account... accounts);

    @Query("SELECT * FROM Account")
    Flowable<List<Account>> loadAllAccount();

    @Query("SELECT * FROM Account WHERE title LIKE +:search OR account LIKE :search")
    Flowable<List<Account>> loadAccountByKeyword(String search);

    @Query("SELECT * FROM Account WHERE isCollect == :isCollect and (title LIKE +:search OR account LIKE :search)")
    Flowable<List<Account>> loadAccountByCollect(boolean isCollect,String search);


}
