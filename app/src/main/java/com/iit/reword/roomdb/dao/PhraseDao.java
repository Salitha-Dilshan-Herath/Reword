package com.iit.reword.roomdb.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.iit.reword.roomdb.model.Phrase;

import java.util.List;

@Dao
public interface PhraseDao {

    @Query("SELECT * FROM phrase where phrase = :phrase")
    LiveData<Phrase> isExists(String phrase);

    @Query("SELECT * FROM phrase ORDER BY phrase ASC")
    LiveData<List<Phrase>> getAll();

    @Insert
    void insert(Phrase phrase);

    @Query("UPDATE PHRASE SET phrase = :newPhrase WHERE p_id = :id")
    void update(String newPhrase, int id );
}
