package com.iit.reword.roomdb.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.iit.reword.roomdb.model.Language;
import com.iit.reword.roomdb.model.LanguageSubscription;

import java.util.List;

@Dao
public interface LanguageSubscriptionDao {

    @Query("SELECT * FROM language_subscription")
    LiveData<List<LanguageSubscription>> getAll();

    @Query("SELECT * FROM language_subscription where isSub=1")
    LiveData<List<LanguageSubscription>> getSubscribe();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<LanguageSubscription> languageSubscriptionList);


}
