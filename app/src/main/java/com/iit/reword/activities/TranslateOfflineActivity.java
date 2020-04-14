package com.iit.reword.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.ibm.watson.language_translator.v3.model.IdentifiableLanguages;
import com.ibm.watson.language_translator.v3.model.TranslationResult;
import com.iit.reword.R;
import com.iit.reword.adapters.LanguageDropDownAdapter;
import com.iit.reword.adapters.LanguageOfflineDropDownAdapter;
import com.iit.reword.adapters.OfflinePhraseAdapter;
import com.iit.reword.model.OfflineTranslate;
import com.iit.reword.roomdb.DbHandler;
import com.iit.reword.roomdb.model.Language;
import com.iit.reword.roomdb.model.LanguageSubscription;
import com.iit.reword.roomdb.model.Phrase;
import com.iit.reword.roomdb.viewModel.LanguageSubscriptionViewModel;
import com.iit.reword.roomdb.viewModel.LanguageViewModel;
import com.iit.reword.roomdb.viewModel.PhraseViewModel;
import com.iit.reword.services.LanguageTranslatorService;
import com.iit.reword.utility.Constant;
import com.iit.reword.utility.interfaces.LanguageTranslatorServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class TranslateOfflineActivity extends AppCompatActivity implements LanguageTranslatorServiceImpl {

    private Spinner spinnerLanguages;
    private RecyclerView recyclerView;
    private TextView txtHeaderSelectedLanguage;

    //MARK: Instance Variables
    private PhraseViewModel phraseViewModel;
    private String languageCode;
    private String languageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate_offline);

        setupView();
    }

    //MARK: Custom methods
    private void setupView(){

        phraseViewModel             = new ViewModelProvider(this).get(PhraseViewModel.class);

        recyclerView                =  findViewById(R.id.recycleViewOffline);
        txtHeaderSelectedLanguage   = findViewById(R.id.txtHeaderSelectedLanguage);

        Bundle bundle = getIntent().getExtras();
        languageCode  = bundle.getString("lan_code");
        languageName  = bundle.getString("lan_name");

        txtHeaderSelectedLanguage.setText(languageName);

        LanguageTranslatorService.getShareInstance().languageTranslatorServiceImpl = this;

        final LiveData<List<Phrase>> phrasesListObservable  = phraseViewModel.getAll(Constant.LOGGING_USER.getU_id());



    }

    @Override
    public void getLanguageList(IdentifiableLanguages languages) {

    }

    @Override
    public void getTranslateResult(TranslationResult result) {

    }
}
