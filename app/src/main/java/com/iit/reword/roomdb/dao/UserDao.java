package com.iit.reword.roomdb.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.Query;

import com.iit.reword.roomdb.model.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user where username = :email")
    LiveData<User> isExistsUsers(String email);

    @Insert
    long insert(User user);

    @Query("SELECT * FROM user where username = :id")
    LiveData<User> getUser(String id);

    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("UPDATE USER SET isLogin = :status WHERE u_id = :id")
    void updateUserStatus(int id, Boolean status);

    @Query("SELECT * FROM user where isLogin = 1")
    LiveData<User> getLoginUser();
}
