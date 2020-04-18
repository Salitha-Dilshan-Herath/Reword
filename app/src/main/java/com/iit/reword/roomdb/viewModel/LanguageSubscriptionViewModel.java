package com.iit.reword.roomdb.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.iit.reword.roomdb.DbHandler;
import com.iit.reword.roomdb.model.Language;
import com.iit.reword.roomdb.model.LanguageSubscription;
import com.iit.reword.roomdb.repository.LanguageSubscriptionRepository;

import java.util.List;

public class LanguageSubscriptionViewModel extends AndroidViewModel {

    private LanguageSubscriptionRepository languageSubscriptionRepository;

    public LanguageSubscriptionViewModel(@NonNull Application application) {
        super(application);
        languageSubscriptionRepository = new LanguageSubscriptionRepository(application);
    }

    public LiveData<List<LanguageSubscription>> getAll() {
        return  languageSubscriptionRepository.getAll();
    }

    public LiveData<List<LanguageSubscription>> getSubscribe() {
        return  languageSubscriptionRepository.getSubscribe();
    }

    public void insertAll(List<LanguageSubscription> languageSubscriptionList) {

        DbHandler.databaseWriteExecutor.execute(() -> {

            languageSubscriptionRepository.insertAll(languageSubscriptionList);
        });
    }
}
