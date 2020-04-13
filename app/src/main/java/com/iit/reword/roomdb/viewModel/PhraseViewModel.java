package com.iit.reword.roomdb.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.iit.reword.roomdb.DbHandler;
import com.iit.reword.roomdb.model.Phrase;
import com.iit.reword.roomdb.repository.PhraseRepository;

import java.util.List;

public class PhraseViewModel extends AndroidViewModel {

    private PhraseRepository phraseRepository;

    public PhraseViewModel(@NonNull Application application) {
        super(application);
        phraseRepository = new PhraseRepository(application);
    }

    public LiveData<List<Phrase>> getAll(int user) {
        return phraseRepository.getAll(user);
    }
    public LiveData<Phrase> isExists(String phrase, int u_id) {
        return phraseRepository.isExists(phrase,u_id);
    }

    public void insert(Phrase phrase){
         phraseRepository.insert(phrase);
    }

    public void update(String newPhrase, int id ){

        DbHandler.databaseWriteExecutor.execute(() -> {
            phraseRepository.update(newPhrase, id );
        });

    }
}
