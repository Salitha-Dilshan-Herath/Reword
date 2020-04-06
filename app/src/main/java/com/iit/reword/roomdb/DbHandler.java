package com.iit.reword.roomdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.iit.reword.roomdb.dao.LanguageDao;
import com.iit.reword.roomdb.dao.LanguageSubscriptionDao;
import com.iit.reword.roomdb.dao.PhraseDao;
import com.iit.reword.roomdb.dao.UserDao;
import com.iit.reword.roomdb.model.Language;
import com.iit.reword.roomdb.model.LanguageSubscription;
import com.iit.reword.roomdb.model.Phrase;
import com.iit.reword.roomdb.model.User;

@Database(entities = {User.class, Phrase.class, Language.class, LanguageSubscription.class}, version = 1)
public abstract class DbHandler extends RoomDatabase {

    private static DbHandler INSTANCE;

    public abstract UserDao userDao();
    public abstract PhraseDao phraseDao();
    public abstract LanguageDao languageDao();
    public abstract LanguageSubscriptionDao languageSubscriptionDao();

    public static DbHandler getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), DbHandler.class, "reword-database")
                            .allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
