package com.iit.reword.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.iit.reword.roomdb.model.Phrase;

import java.util.List;

@Dao
public interface PhraseDao {

    @Query("SELECT COUNT(*) FROM phrase where phrase = :phrase")
    int isExistsPhrase(String phrase);

    @Query("SELECT * FROM phrase where user = :user ORDER BY phrase ASC")
    List<Phrase> getAll(String user);

    @Insert
    long insert(Phrase phrase);
}
