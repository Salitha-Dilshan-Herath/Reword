package com.iit.reword.roomdb.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.iit.reword.roomdb.DbHandler;
import com.iit.reword.roomdb.dao.PhraseDao;
import com.iit.reword.roomdb.model.Phrase;

import java.util.List;

public class PhraseRepository {

    private PhraseDao phraseDao;

    public PhraseRepository(Application application) {
        phraseDao = DbHandler.getAppDatabaseLive(application).phraseDao();
    }

    public LiveData<List<Phrase>> getAll(int user) {
        return phraseDao.getAll(user);
    }

    public LiveData<Phrase> isExists(String phrase, int u_id) {
        return phraseDao.isExists(phrase,u_id);
    }

    public void insert(Phrase phrase){

        DbHandler.databaseWriteExecutor.execute(() -> {
            phraseDao.insert(phrase);
        });
    }

    public void update(String newPhrase, int id ){

        DbHandler.databaseWriteExecutor.execute(() -> {
            phraseDao.update(newPhrase, id );
        });

    }
}

