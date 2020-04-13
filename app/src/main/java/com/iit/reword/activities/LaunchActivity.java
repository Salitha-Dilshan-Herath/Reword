package com.iit.reword.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.ibm.watson.language_translator.v3.model.IdentifiableLanguage;
import com.ibm.watson.language_translator.v3.model.IdentifiableLanguages;
import com.ibm.watson.language_translator.v3.model.TranslationResult;
import com.iit.reword.R;
import com.iit.reword.roomdb.DbHandler;
import com.iit.reword.roomdb.model.Language;
import com.iit.reword.roomdb.model.User;
import com.iit.reword.roomdb.viewModel.LanguageViewModel;
import com.iit.reword.roomdb.viewModel.UserViewModel;
import com.iit.reword.services.LanguageTranslatorService;
import com.iit.reword.utility.Constant;
import com.iit.reword.utility.interfaces.LanguageTranslatorServiceImpl;

public class LaunchActivity extends AppCompatActivity implements LanguageTranslatorServiceImpl {

    //MARK: UI Elements
    private ImageView imageView;

    //MARK: Instance Variable
    private LanguageViewModel languageViewModel;
    private UserViewModel     userViewModel;


    //MARK: Life cycle events
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        setupView();
    }

    //MARK: Custom methods
    private void setupView() {

        languageViewModel = new ViewModelProvider(this).get(LanguageViewModel.class);
        userViewModel     = new ViewModelProvider(this).get(UserViewModel.class);
        //Load initial GIF
        imageView = findViewById(R.id.imgGif);
        Glide.with(this).load(R.drawable.read).into(imageView);

        //Invoke language download method
        LanguageTranslatorService.getShareInstance().languageTranslatorServiceImpl = this;
        LanguageTranslatorService.getShareInstance().getAllLanguages();

        //delay for initial page
        int secondsDelayed = 5; // late time
        new Handler().postDelayed(new Runnable() {
            public void run() {

                //get isLogged user or not
                userViewModel.getLoginUser().observe(LaunchActivity.this, user -> {

                    if (user != null) {

                        Constant.LOGGING_USER = user;

                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                });



            }
        }, secondsDelayed * 1000);
    }

    //MARK: Language Download Task
    @Override
    public void getLanguageList(IdentifiableLanguages languages) {

        //System.out.println(languages);

        if (languages == null)
            return;

        for (IdentifiableLanguage language : languages.getLanguages()) {

            Language dbLanguage = new Language();
            dbLanguage.setName(language.getName());
            dbLanguage.setCode(language.getLanguage());

            languageViewModel.insert(dbLanguage);
        }

        languageViewModel.getLive("Afrikaans").observe(this, language -> {
            System.out.println(language);
        });

        languageViewModel.getAllWords().observe(this,  languageList -> {
            System.out.println(languageList);
        });


    }

    @Override
    public void getTranslateResult(TranslationResult result) {

    }
}
