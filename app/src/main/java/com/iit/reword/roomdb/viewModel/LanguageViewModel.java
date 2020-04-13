package com.iit.reword.roomdb.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.iit.reword.roomdb.model.Language;
import com.iit.reword.roomdb.repository.LanguageRepository;

import java.util.List;

public class LanguageViewModel extends AndroidViewModel {

    private LanguageRepository languageRepository;

    private LiveData<List<Language>> allWords;

    public LanguageViewModel(@NonNull Application application) {
        super(application);
        languageRepository = new LanguageRepository(application);
        allWords = languageRepository.getAllWords();
    }

    public LiveData<List<Language>> getAllWords() { return allWords; }

    public void insert(Language language) { languageRepository.insert(language); }

    public LiveData<Language> getLive (String s) { return languageRepository.getLive(s); }

}
