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

    @Query("DELETE FROM language_subscription where user = :u_id")
    void delete(int u_id);

    @Query("SELECT * FROM language_subscription where user = :u_id")
    LiveData<List<LanguageSubscription>> getAll(int u_id);

}
