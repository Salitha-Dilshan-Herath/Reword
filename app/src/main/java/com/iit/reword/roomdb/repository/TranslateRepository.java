package com.iit.reword.roomdb.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.iit.reword.model.OfflineTranslate;
import com.iit.reword.roomdb.DbHandler;
import com.iit.reword.roomdb.dao.TranslateDao;
import com.iit.reword.roomdb.model.Translate;

import java.util.List;

public class TranslateRepository {

    private TranslateDao translateDao;

    public TranslateRepository(Application application){

        translateDao = DbHandler.getAppDatabaseLive(application).translateDao();
    }

    public void insert(Translate translate){

        DbHandler.databaseWriteExecutor.execute(() -> {
            translateDao.insert(translate);
        });
    }

    public LiveData<Translate> isExistsPhrase(int pId, String lanId, int userId) {
        return translateDao.isExistsPhrase(pId, lanId, userId);
    }

    public LiveData<List<Translate>> isExistsPhrase() {
        return translateDao.isExistsPhrase();
    }

    public LiveData<List<OfflineTranslate>>getTranslateWords(String lanId, int userId){

        return translateDao.getTranslateWord(lanId,userId);
    }
}
