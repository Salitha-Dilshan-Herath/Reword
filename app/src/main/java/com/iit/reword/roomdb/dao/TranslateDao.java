package com.iit.reword.roomdb.dao;

import androidx.lifecycle.LiveData;
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

    @Query("SELECT * FROM translate where p_id = :pId AND language_id = :lanId ")
    LiveData<Translate> isExistsPhrase(int pId, String lanId);

    @Query("SELECT * FROM translate WHERE language_id = :lanId  AND p_id = :pId")
    LiveData<Translate> getExistsPhrase(int pId, String lanId);

    @Query("DELETE FROM translate WHERE p_id= :pid")
    void delete(int pid);

}
