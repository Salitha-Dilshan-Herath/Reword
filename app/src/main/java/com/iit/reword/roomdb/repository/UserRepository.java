package com.iit.reword.roomdb.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.iit.reword.roomdb.DbHandler;
import com.iit.reword.roomdb.dao.UserDao;
import com.iit.reword.roomdb.model.User;

public class UserRepository {

    private UserDao userDao;

    public UserRepository(Application application) {
        userDao = DbHandler.getAppDatabaseLive(application).userDao();
    }

    public LiveData<User> getLoginUser () {
        return  userDao.getLoginUser();
    }

    public LiveData<User> isExistsUsers(String email) {
        return  userDao.isExistsUsers(email);
    }

    public LiveData<User> getUser(String id) {
        return userDao.getUser(id);
    }

    public void insert(User user){

        DbHandler.databaseWriteExecutor.execute(() -> {
              userDao.insert(user);
        });

    }

    public void updateUserStatus(int id, Boolean status) {

        DbHandler.databaseWriteExecutor.execute(() -> {
            userDao.updateUserStatus(id,status);
        });

    }
}
