package com.iit.reword.roomdb.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.iit.reword.model.OfflineTranslate;
import com.iit.reword.roomdb.DbHandler;
import com.iit.reword.roomdb.model.Translate;
import com.iit.reword.roomdb.repository.TranslateRepository;

import java.util.List;

public class TranslateViewModel extends AndroidViewModel {

    private TranslateRepository translateRepository;

    public TranslateViewModel(@NonNull Application application) {
        super(application);
        translateRepository = new TranslateRepository(application);

    }

    public void insert(Translate translate){

        DbHandler.databaseWriteExecutor.execute(() -> {
            translateRepository.insert(translate);
        });
    }

    public LiveData<Translate> isExistsPhrase(int pId, String lanId, int userId) {
        return translateRepository.isExistsPhrase(pId, lanId, userId);
    }

    public LiveData<List<Translate>> isExistsPhrase() {
        return translateRepository.isExistsPhrase();
    }

    public LiveData<List<OfflineTranslate>>getTranslateWords(String lanId, int userId){

        return translateRepository.getTranslateWords(lanId,userId);
    }
}
