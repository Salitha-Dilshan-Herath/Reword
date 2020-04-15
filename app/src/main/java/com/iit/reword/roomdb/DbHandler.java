package com.iit.reword.roomdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.iit.reword.roomdb.dao.LanguageDao;
import com.iit.reword.roomdb.dao.LanguageSubscriptionDao;
import com.iit.reword.roomdb.dao.PhraseDao;
import com.iit.reword.roomdb.dao.TranslateDao;
import com.iit.reword.roomdb.model.Language;
import com.iit.reword.roomdb.model.LanguageSubscription;
import com.iit.reword.roomdb.model.Phrase;
import com.iit.reword.roomdb.model.Translate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Phrase.class, Language.class, LanguageSubscription.class, Translate.class}, version = 1)
public abstract class DbHandler extends RoomDatabase {

    private static DbHandler INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    public abstract PhraseDao phraseDao();
    public abstract LanguageDao languageDao();
    public abstract LanguageSubscriptionDao languageSubscriptionDao();
    public abstract TranslateDao translateDao();

    public static DbHandler getAppDatabaseLive(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), DbHandler.class, "reword-database")
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
