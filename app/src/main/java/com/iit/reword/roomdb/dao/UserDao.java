package com.iit.reword.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.Query;

import com.iit.reword.roomdb.model.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT COUNT(*) FROM user where username = :email")
    int isExistsUsers(String email);

    @Insert
    long insert(User user);

    @Query("SELECT * FROM user where username = :email")
    User getUser(String email);

    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("UPDATE USER SET isLogin = :status WHERE username = :user")
    void updateUserStatus(String user, Boolean status);

    @Query("SELECT * FROM user where isLogin = 1")
    User getLoginUser();
}
