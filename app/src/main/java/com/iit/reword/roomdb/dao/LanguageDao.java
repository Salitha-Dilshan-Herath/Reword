package com.iit.reword.roomdb.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.iit.reword.roomdb.model.Language;
import com.iit.reword.roomdb.model.Phrase;

import java.util.List;

@Dao
public interface LanguageDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Language language);

    @Query("SELECT * FROM language")
    List<Language> getAll();

    @Query("SELECT * FROM language where name = :name")
    Language get(String name);

    //new
    @Query("SELECT * FROM language")
    LiveData<List<Language>>  getAllLive();

    @Query("SELECT * FROM language where name = :name")
    LiveData<Language> getLive(String name);

}
