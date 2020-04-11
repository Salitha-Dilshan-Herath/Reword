package com.iit.reword.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ibm.watson.language_translator.v3.model.IdentifiableLanguage;
import com.ibm.watson.language_translator.v3.model.IdentifiableLanguages;
import com.ibm.watson.language_translator.v3.model.TranslationResult;
import com.iit.reword.R;
import com.iit.reword.roomdb.DbHandler;
import com.iit.reword.roomdb.model.Language;
import com.iit.reword.roomdb.model.User;
import com.iit.reword.services.LanguageTranslatorService;
import com.iit.reword.utility.Constant;
import com.iit.reword.utility.interfaces.LanguageTranslatorServiceImpl;

public class LaunchActivity extends AppCompatActivity implements LanguageTranslatorServiceImpl {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        LanguageTranslatorService.getShareInstance().languageTranslatorServiceImpl = this;
        LanguageTranslatorService.getShareInstance().getAllLanguages();

        int secondsDelayed = 5;
        new Handler().postDelayed(new Runnable() {
            public void run() {

                User user = DbHandler.getAppDatabase(getApplicationContext()).userDao().getLoginUser();

                if (user != null) {

                    Constant.LOGGING_USER = user;

                    Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                    startActivity(intent);
                    finish();

                }else {
                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, secondsDelayed * 1000);
    }

    @Override
    public void getLanguageList(IdentifiableLanguages languages) {

        System.out.println(languages);

        if (languages == null)
            return;

        for(IdentifiableLanguage language : languages.getLanguages()){

            Language dbLanguage = new Language();
            dbLanguage.setName(language.getName());
            dbLanguage.setCode(language.getLanguage());

            DbHandler.getAppDatabase(LaunchActivity.this).languageDao().insert(dbLanguage);

        }

        System.out.println( DbHandler.getAppDatabase(LaunchActivity.this).languageDao().getAll().size());
    }

    @Override
    public void getTranslateResult(TranslationResult result) {

    }
}
