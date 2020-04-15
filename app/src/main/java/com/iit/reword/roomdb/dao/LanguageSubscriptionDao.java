package com.iit.reword.roomdb.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.iit.reword.roomdb.model.Language;
import com.iit.reword.roomdb.model.LanguageSubscription;

import java.util.List;

@Dao
public interface LanguageSubscriptionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(LanguageSubscription language);

    @Query("DELETE FROM language_subscription")
    void delete();

    @Query("SELECT * FROM language_subscription ")
    LiveData<List<LanguageSubscription>> getAll();

}
