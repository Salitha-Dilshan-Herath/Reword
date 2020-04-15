package com.iit.reword.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.ibm.watson.language_translator.v3.model.IdentifiableLanguages;
import com.ibm.watson.language_translator.v3.model.Translation;
import com.ibm.watson.language_translator.v3.model.TranslationResult;
import com.iit.reword.R;
import com.iit.reword.adapters.LanguageDropDownAdapter;
import com.iit.reword.adapters.LanguageOfflineDropDownAdapter;
import com.iit.reword.adapters.OfflinePhraseAdapter;
import com.iit.reword.model.OfflineTranslate;
import com.iit.reword.model.TranslateModel;
import com.iit.reword.roomdb.DbHandler;
import com.iit.reword.roomdb.model.Language;
import com.iit.reword.roomdb.model.LanguageSubscription;
import com.iit.reword.roomdb.model.Phrase;
import com.iit.reword.roomdb.model.Translate;
import com.iit.reword.roomdb.viewModel.LanguageSubscriptionViewModel;
import com.iit.reword.roomdb.viewModel.LanguageViewModel;
import com.iit.reword.roomdb.viewModel.PhraseViewModel;
import com.iit.reword.roomdb.viewModel.TranslateViewModel;
import com.iit.reword.services.LanguageTranslatorService;
import com.iit.reword.utility.Constant;
import com.iit.reword.utility.interfaces.LanguageTranslatorServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class TranslateOfflineActivity extends AppCompatActivity implements LanguageTranslatorServiceImpl {

    private Spinner spinnerLanguages;
    private RecyclerView recyclerView;
    private TextView txtHeaderSelectedLanguage;
    private ProgressDialog pd ;

    //MARK: Instance Variables
    private PhraseViewModel phraseViewModel;
    private List<Phrase> phraseList;
    private List<TranslateModel> translateModelList = new ArrayList();
    private String languageCode;
    private String languageName;
    private TranslateViewModel translateViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate_offline);

        setupView();
    }

    //MARK: Custom methods
    private void setupView(){

        phraseViewModel             = new ViewModelProvider(this).get(PhraseViewModel.class);
        translateViewModel          = new ViewModelProvider(this).get(TranslateViewModel.class);

        recyclerView                =  findViewById(R.id.recycleViewOffline);
        txtHeaderSelectedLanguage   = findViewById(R.id.txtHeaderSelectedLanguage);
        pd                          = new ProgressDialog(TranslateOfflineActivity.this);

        Bundle bundle = getIntent().getExtras();
        languageCode  = bundle.getString("lan_code");
        languageName  = bundle.getString("lan_name");

        txtHeaderSelectedLanguage.setText(languageName);

        LanguageTranslatorService.getShareInstance().languageTranslatorServiceImpl = this;

        pd.setMessage("Translating.....");
        pd.show();
        final LiveData<List<Phrase>> phrasesListObservable  = phraseViewModel.getAll();

        phrasesListObservable.observe(this, new Observer<List<Phrase>>() {
            @Override
            public void onChanged(List<Phrase> phrases) {

                 phrasesListObservable.removeObserver(this);
                 phraseList = phrases;

                 for(int i=0; i < phrases.size(); i++){

                     Phrase phraseObj = phrases.get(i);

                     final LiveData<Translate> getExistsPhrase  = translateViewModel.getExistsPhrase(phraseObj.getPid(), TranslateOfflineActivity.this.languageName);

                     int index = i;
                     getExistsPhrase.observe(TranslateOfflineActivity.this, new Observer<Translate>() {
                         @Override
                         public void onChanged(Translate translate) {
                             getExistsPhrase.removeObserver(this);

                             boolean isAlreadyTranslate = true;

                             if(translate == null){
                                 TranslateModel translateModel = new TranslateModel(phraseObj.getPhrase(),TranslateOfflineActivity.this.languageCode);
                                 translateModel.setListIndex(index);
                                 translateModel.setPhraseId(phraseObj.getPid());
                                 translateModel.setLanguageName(TranslateOfflineActivity.this.languageName);
                                 LanguageTranslatorService.getShareInstance().translateList(translateModel);
                                 isAlreadyTranslate = false;
                             }else {

                                 TranslateModel translateModel = new TranslateModel(phraseObj.getPhrase(), TranslateOfflineActivity.this.languageName);
                                 translateModel.setTranslatedWord(translate.getTranslatePhrase());
                                 translateModelList.add(translateModel);
                             }

                             if(index == phrases.size() -1 && isAlreadyTranslate){
                                 setupRecycleView();
                                 System.out.println("Translate Complete Done......!");
                                 pd.dismiss();
                             }
                         }
                     });

                 }
            }
        });
    }

    private void setupRecycleView(){

        OfflinePhraseAdapter offlinePhraseAdapter = new OfflinePhraseAdapter(this.translateModelList);
        recyclerView.setAdapter(offlinePhraseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void savePhrase(TranslateModel model) {

        if (model.getTranslatedWord() == null)
            return;

        Translate translate = new Translate();
        translate.setP_id(model.getPhraseId());
        translate.setLanguageId(model.getLanguageName());
        translate.setTranslatePhrase(model.getTranslatedWord());

        final LiveData<Translate> isExistsObservable  = translateViewModel.isExistsPhrase(translate.getP_id(),translate.getLanguageId());

        isExistsObservable.observe(this, new Observer<Translate>() {
            @Override
            public void onChanged(Translate isTranslate) {

                isExistsObservable.removeObserver(this);

                if (isTranslate == null){
                    translateViewModel.insert(translate);
                    System.out.println("Save data to db success " + translate.toString());
                }
            }
        });
    }

    @Override
    public void getLanguageList(IdentifiableLanguages languages) {

    }

    @Override
    public void getTranslateResult(TranslationResult result) {

    }

    @Override
    public void getTranslateListResult(TranslateModel model) {

        System.out.println(model.getTranslatedWord());
        translateModelList.add(model);
        savePhrase(model);

        if(model.getListIndex() == phraseList.size() - 1){
            setupRecycleView();
            System.out.println("Translate Complete Done......!");
            pd.dismiss();
        }
    }
}
