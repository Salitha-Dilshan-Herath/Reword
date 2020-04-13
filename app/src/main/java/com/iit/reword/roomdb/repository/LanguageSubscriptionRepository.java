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

    public LiveData<List<LanguageSubscription>> getAll(int u_id) {
        return  languageSubscriptionDao.getAll(u_id);
    }

    public void insert(LanguageSubscription language) {
        DbHandler.databaseWriteExecutor.execute(() -> {
            languageSubscriptionDao.insert(language);
        });
    }

    public void delete(int u_id) {
        DbHandler.databaseWriteExecutor.execute(() -> {
            languageSubscriptionDao.delete(u_id);
        });
    }
}
