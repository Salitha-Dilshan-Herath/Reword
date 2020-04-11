package com.iit.reword.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.iit.reword.model.OfflineTranslate;
import com.iit.reword.roomdb.model.Translate;

import java.util.List;

@Dao
public interface TranslateDao {

    @Insert
    long insert(Translate translate);

    @Query("SELECT COUNT(*) FROM translate where p_id = :pId AND language_id = :lanId AND user = :userId")
    int isExistsPhrase(int pId, String lanId, int userId);

    @Query("SELECT * from translate INNER JOIN phrase on translate.p_id = phrase.p_id WHERE language_id = :lanId AND translate.user = :userId")
    List<OfflineTranslate> getTranslateWord(String lanId, int userId);


}
