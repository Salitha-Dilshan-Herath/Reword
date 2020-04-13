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

    public LiveData<List<LanguageSubscription>> getAll(int u_id) {
        return  languageSubscriptionRepository.getAll(u_id);
    }

    public void insert(LanguageSubscription languageSubscription) { languageSubscriptionRepository.insert(languageSubscription); }

    public void delete(int u_id) {
        DbHandler.databaseWriteExecutor.execute(() -> {
            languageSubscriptionRepository.delete(u_id);
        });
    }
}
