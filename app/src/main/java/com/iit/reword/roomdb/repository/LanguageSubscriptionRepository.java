package com.iit.reword.roomdb.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.iit.reword.roomdb.DbHandler;
import com.iit.reword.roomdb.dao.LanguageSubscriptionDao;
import com.iit.reword.roomdb.model.LanguageSubscription;

import java.util.List;

public class LanguageSubscriptionRepository {

    private LanguageSubscriptionDao languageSubscriptionDao;

    public LanguageSubscriptionRepository(Application application) {
        languageSubscriptionDao = DbHandler.getAppDatabaseLive(application).languageSubscriptionDao();
    }

    public LiveData<List<LanguageSubscription>> getAll() {
        return  languageSubscriptionDao.getAll();
    }

    public LiveData<List<LanguageSubscription>> getSubscribe() {
        return  languageSubscriptionDao.getSubscribe();
    }


    public void insertAll(List<LanguageSubscription> languageSubscriptionList) {

        DbHandler.databaseWriteExecutor.execute(() -> {

            languageSubscriptionDao.insertAll(languageSubscriptionList);
        });
    }

}
