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
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.iit.reword.services.TextToSpeechService;
import com.iit.reword.utility.Constant;
import com.iit.reword.utility.Utility;
import com.iit.reword.utility.interfaces.AdapterClickListener;
import com.iit.reword.utility.interfaces.LanguageTranslatorServiceImpl;
import com.iit.reword.utility.interfaces.OfflineAdapterCellListener;
import com.iit.reword.utility.interfaces.TextSpeechServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class TranslateOfflineActivity extends AppCompatActivity implements LanguageTranslatorServiceImpl, OfflineAdapterCellListener, TextSpeechServiceImpl {

    private Spinner spinnerLanguages;
    private RecyclerView recyclerView;
    private TextView txtHeaderSelectedLanguage;
    private ProgressDialog pd ;
    private Button btnPronounsAll;

    //MARK: Instance Variables
    private PhraseViewModel phraseViewModel;
    private List<Phrase> phraseList;
    private List<TranslateModel> translateModelList = new ArrayList();
    private String languageCode;
    private String languageName;
    private TranslateModel selectedModel;
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
        btnPronounsAll              = findViewById(R.id.btnPronounsAll);
        pd                          = new ProgressDialog(TranslateOfflineActivity.this);

        if (!Utility.isInternetReachability(this)) {
           btnPronounsAll.setVisibility(View.GONE);
        }

        Bundle bundle = getIntent().getExtras();
        languageCode  = bundle.getString("lan_code");
        languageName  = bundle.getString("lan_name");

        txtHeaderSelectedLanguage.setText(languageName);

        LanguageTranslatorService.getShareInstance().languageTranslatorServiceImpl = this;
        TextToSpeechService.getShareInstance().textSpeechServiceImpl = this;


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
                                 isAlreadyTranslate = false;
                                 TranslateModel translateModel = new TranslateModel(phraseObj.getPhrase(),TranslateOfflineActivity.this.languageCode);
                                 translateModel.setListIndex(index);
                                 translateModel.setPhraseId(phraseObj.getPid());
                                 translateModel.setLanguageName(TranslateOfflineActivity.this.languageName);
                                 LanguageTranslatorService.getShareInstance().translateList(translateModel);

                             }else {

                                 TranslateModel translateModel = new TranslateModel(phraseObj.getPhrase(), TranslateOfflineActivity.this.languageName);
                                 translateModel.setTranslatedWord(translate.getTranslatePhrase());
                                 translateModelList.add(translateModel);
                             }

                             if(index == phrases.size() -1 && isAlreadyTranslate){
                                 setupRecycleView(translateModelList);
                                 System.out.println("Translate Complete Done......!  with isAlreadyTranslate");
                                 pd.dismiss();
                             }
                         }
                     });

                 }
            }
        });

        setupListener();
    }

    private void  setupListener() {
        btnPronounsAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selectedModel == null){
                    Toast.makeText(TranslateOfflineActivity.this, "Please select phrase / word",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                btnPronounsAll.setEnabled(false);
                String s = selectedModel.getTranslatedWord();
                TextToSpeechService.getShareInstance().setLanguageCode(languageCode);
                TextToSpeechService.getShareInstance().speech(s);

            }
        });
    }

    private void setupRecycleView(List<TranslateModel> translateModelList){

        OfflinePhraseAdapter offlinePhraseAdapter = new OfflinePhraseAdapter(translateModelList, this);
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

        translateViewModel.insert(translate);
        System.out.println("Save data to db success " + translate.toString());
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
            System.out.println(translateModelList.size() + " Translate Complete Done......! " + model.getTranslatedWord());
            setupRecycleView(translateModelList);
            pd.dismiss();
        }
    }

    @Override
    public void onCellClick(TranslateModel phrase, int index) {

        this.selectedModel = phrase;
    }

    @Override
    public void isSuccessSpeech(Boolean status) {
        btnPronounsAll.setEnabled(true);
    }
}
