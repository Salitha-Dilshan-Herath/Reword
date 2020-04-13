package com.iit.reword.roomdb.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.iit.reword.roomdb.DbHandler;
import com.iit.reword.roomdb.dao.LanguageDao;
import com.iit.reword.roomdb.model.Language;

import java.util.List;

public class LanguageRepository {

    private LanguageDao languageDao;
    private LiveData<List<Language>> allLanguage;

    public LanguageRepository(Application application) {
        languageDao = DbHandler.getAppDatabaseLive(application).languageDao();
        allLanguage = languageDao.getAllLive();
    }

    public LiveData<List<Language>> getAllWords() {
        return allLanguage;
    }

    public void insert(final Language language) {
        DbHandler.databaseWriteExecutor.execute(() -> {
           languageDao.insert(language);
        });
    }

    public LiveData<Language> getLive(String name) {
        return languageDao.getLive(name);
    }

}
