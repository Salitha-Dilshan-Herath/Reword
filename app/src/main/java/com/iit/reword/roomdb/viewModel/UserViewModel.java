package com.iit.reword.roomdb.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.iit.reword.roomdb.model.User;
import com.iit.reword.roomdb.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {

    private UserRepository userRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    public LiveData<User> getLoginUser () {
        return  userRepository.getLoginUser();
    }

    public LiveData<User> isExistsUsers(String email) {
        return  userRepository.isExistsUsers(email);
    }

    public LiveData<User> getUser(String id) {
        return userRepository.getUser(id);
    }

    public void insert(User user){
         userRepository.insert(user);
    }

    public void updateUserStatus(int id, Boolean status) {

        userRepository.updateUserStatus(id, status);

    }
}
