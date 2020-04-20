package com.iit.reword.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.ibm.watson.language_translator.v3.model.IdentifiableLanguage;
import com.ibm.watson.language_translator.v3.model.IdentifiableLanguages;
import com.ibm.watson.language_translator.v3.model.TranslationResult;
import com.iit.reword.R;
import com.iit.reword.model.TranslateModel;
import com.iit.reword.roomdb.model.Language;
import com.iit.reword.roomdb.viewModel.LanguageViewModel;
import com.iit.reword.services.LanguageTranslatorService;
import com.iit.reword.utility.Utility;
import com.iit.reword.utility.interfaces.LanguageTranslatorServiceImpl;


public class LaunchActivity extends AppCompatActivity implements LanguageTranslatorServiceImpl {

    //MARK: UI Elements
    private ImageView imageView;

    //MARK: Instance Variable
    private LanguageViewModel languageViewModel;

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

        //Load initial GIF
        imageView = findViewById(R.id.imgGif);
        Glide.with(this).load(R.drawable.read).into(imageView);

        //Check network reachability
        if(Utility.isInternetReachability(this)){
            //Invoke language download method
            LanguageTranslatorService.getShareInstance().languageTranslatorServiceImpl = this;
            LanguageTranslatorService.getShareInstance().getAllLanguages();
        }else {
            Toast.makeText(this, "Your internet connection appears to be offline",
                    Toast.LENGTH_LONG).show();
        }


        //delay for initial page
        int secondsDelayed = 5; // late time
        new Handler().postDelayed(new Runnable() {
            public void run() {

                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();


            }
        }, secondsDelayed * 1000);
    }

    //MARK: Language Download Task
    @Override
    public void getLanguageList(IdentifiableLanguages languages) {

        System.out.println(languages);

        if (languages == null)
            return;

        for (IdentifiableLanguage language : languages.getLanguages()) {

            Language dbLanguage = new Language();
            dbLanguage.setName(language.getName());
            dbLanguage.setCode(language.getLanguage());

            languageViewModel.insert(dbLanguage);
        }

    }

    @Override
    public void getTranslateResult(TranslationResult result) {

    }

    @Override
    public void getTranslateListResult(TranslateModel model) {

    }
}
