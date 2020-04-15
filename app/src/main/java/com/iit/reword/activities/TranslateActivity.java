package com.iit.reword.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ibm.watson.language_translator.v3.model.IdentifiableLanguages;
import com.ibm.watson.language_translator.v3.model.Translation;
import com.ibm.watson.language_translator.v3.model.TranslationResult;
import com.iit.reword.R;
import com.iit.reword.adapters.LanguageDropDownAdapter;
import com.iit.reword.adapters.PhraseDisplayAdapter;
import com.iit.reword.model.TranslateModel;
import com.iit.reword.roomdb.model.LanguageSubscription;
import com.iit.reword.roomdb.model.Phrase;
import com.iit.reword.roomdb.model.Translate;
import com.iit.reword.roomdb.viewModel.LanguageSubscriptionViewModel;
import com.iit.reword.roomdb.viewModel.PhraseViewModel;
import com.iit.reword.roomdb.viewModel.TranslateViewModel;
import com.iit.reword.services.LanguageTranslatorService;
import com.iit.reword.services.TextToSpeechService;
import com.iit.reword.utility.Constant;
import com.iit.reword.utility.Utility;
import com.iit.reword.utility.interfaces.AdapterClickListener;
import com.iit.reword.utility.interfaces.LanguageTranslatorServiceImpl;
import com.iit.reword.utility.interfaces.TextSpeechServiceImpl;

import java.util.ArrayList;
import java.util.List;


public class TranslateActivity extends AppCompatActivity implements AdapterClickListener, LanguageTranslatorServiceImpl, TextSpeechServiceImpl {

    //MARK: UI Elements
    private RecyclerView recyclerView;
    private Spinner spinnerLanguages;
    private TextView txtSelectedPhraseText;
    private TextView txtTranslatedPhrase;
    private ImageView imgRefresh;
    private ImageView imagePronounceRefresh;
    private Button btnTranslate;
    private Button btnSpeech;
    private Button btnViewAll;
    private ConstraintLayout constraintViwExtra;

    //MARK: Instance Variables
    private ArrayAdapter adapter;
    private List<LanguageSubscription> languageSubscriptionsList;
    private Phrase selectedPhrase;
    private LanguageSubscription selectedLanguage;
    private PhraseViewModel phraseViewModel;
    private LanguageSubscriptionViewModel languageSubscriptionViewModel;
    private TranslateViewModel translateViewModel;

    //MARK: Life cycle events
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        setupView();
    }

    //MARK: Custom methods
    private void setupView(){

        phraseViewModel               = new ViewModelProvider(this).get(PhraseViewModel.class);
        languageSubscriptionViewModel = new ViewModelProvider(this).get(LanguageSubscriptionViewModel.class);
        translateViewModel            = new ViewModelProvider(this).get(TranslateViewModel.class);

        recyclerView          = findViewById(R.id.recycleViewPhraseTranslator);
        spinnerLanguages      = findViewById(R.id.spinnerLanguages);
        txtSelectedPhraseText = findViewById(R.id.txtSelectedPhrase);
        imgRefresh            = findViewById(R.id.imgRefresh);
        btnTranslate          = findViewById(R.id.btnTranslate);
        txtTranslatedPhrase   = findViewById(R.id.txtTranslatedPhrase);
        constraintViwExtra    = findViewById(R.id.constraintViwExtra);
        btnSpeech             = findViewById(R.id.btnSpeech);
        imagePronounceRefresh = findViewById(R.id.imagePronounceRefresh);
        btnViewAll            = findViewById(R.id.btnViewAll);

        constraintViwExtra.setVisibility(View.INVISIBLE);
        imgRefresh.setVisibility(View.INVISIBLE);
        imagePronounceRefresh.setVisibility(View.INVISIBLE);
        btnTranslate.setEnabled(false);

        LanguageTranslatorService.getShareInstance().languageTranslatorServiceImpl = TranslateActivity.this;


        final LiveData<List<Phrase>> phrasesListObservable  = phraseViewModel.getAll();

        phrasesListObservable.observe(this, new Observer<List<Phrase>>() {
            @Override
            public void onChanged(List<Phrase> phrases) {
                phrasesListObservable.removeObserver(this);
                PhraseDisplayAdapter phraseDisplayAdapter = new PhraseDisplayAdapter(phrases, TranslateActivity.this);
                recyclerView.setAdapter(phraseDisplayAdapter);
            }
        });

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(dividerItemDecoration);

        setSpinnerValues();
        setupListeners();

    }

    private void setupListeners(){

        btnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (txtSelectedPhraseText.getText().toString().equals("")){
                    Toast.makeText(TranslateActivity.this, "Please select phrase / word",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                imgRefresh.setVisibility(View.VISIBLE);
                imgRefresh.startAnimation(Utility.refreshAnimation());
                btnTranslate.setVisibility(View.INVISIBLE);

                System.out.println(selectedLanguage.getLan_code());

                translatePhrase();

            }
        });

        spinnerLanguages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (spinnerLanguages.getSelectedItemPosition() <= 0){
                    return;
                }

                selectedLanguage = languageSubscriptionsList.get(spinnerLanguages.getSelectedItemPosition() - 1);
                constraintViwExtra.setVisibility(View.INVISIBLE);
                btnTranslate.setVisibility(View.VISIBLE);
                txtTranslatedPhrase.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (txtTranslatedPhrase.getText().toString().equals(""))
                    return;

                imagePronounceRefresh.setVisibility(View.VISIBLE);
                imagePronounceRefresh.startAnimation(Utility.refreshAnimation());
                btnSpeech.setVisibility(View.INVISIBLE);
                String s = txtTranslatedPhrase.getText().toString();
                TextToSpeechService.getShareInstance().setLanguageCode(selectedLanguage.getLan_code());
                TextToSpeechService.getShareInstance().speech(s);
            }
        });

        btnViewAll.setOnClickListener(view -> {
            Intent intent = new Intent(TranslateActivity.this, TranslateOfflineActivity.class);
            intent.putExtra("lan_code", selectedLanguage.getLan_code());
            intent.putExtra("lan_name", selectedLanguage.getName());
            startActivity(intent);
        });
    }

    //MARK: Load Data to spinner
    private void setSpinnerValues() {

        ArrayList stringList = new ArrayList();
        final LiveData<List<LanguageSubscription>> languageSubListObservable  = languageSubscriptionViewModel.getAll();

        //set data to spinner
        languageSubListObservable.observe(this, new Observer<List<LanguageSubscription>>() {
            @Override
            public void onChanged(List<LanguageSubscription> languageSubscriptions) {

                languageSubscriptionsList = languageSubscriptions;
                languageSubListObservable.removeObserver(this);

                for(LanguageSubscription subscription: languageSubscriptions){
                    stringList.add(subscription.getName());
                }

                //set data to spinner
                adapter = new ArrayAdapter(TranslateActivity.this, android.R.layout.simple_spinner_item, stringList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinnerLanguages.setAdapter(
                        new LanguageDropDownAdapter(
                                adapter,
                                R.layout.spinner_defualt_value,
                                TranslateActivity.this));
            }
        });
    }

    private void savePhrase() {

        Translate translate = new Translate();
        translate.setP_id(selectedPhrase.pid);
        translate.setLanguageId(selectedLanguage.getName());
        translate.setTranslatePhrase(txtTranslatedPhrase.getText().toString().toLowerCase());

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

    private void translatePhrase() {

        Translate translate = new Translate();
        translate.setP_id(selectedPhrase.pid);
        translate.setLanguageId(selectedLanguage.getName());
        translate.setTranslatePhrase(txtTranslatedPhrase.getText().toString().toLowerCase());

        final LiveData<Translate> isExistsObservable  = translateViewModel.isExistsPhrase(translate.getP_id(),translate.getLanguageId());

        isExistsObservable.observe(this, new Observer<Translate>() {
            @Override
            public void onChanged(Translate isTranslate) {

                isExistsObservable.removeObserver(this);

                if (isTranslate == null){
                    TranslateModel translateModel = new TranslateModel(txtSelectedPhraseText.getText().toString(), selectedLanguage.getLan_code());
                    LanguageTranslatorService.getShareInstance().translate(translateModel);
                }else {

                    constraintViwExtra.setVisibility(View.VISIBLE);
                    btnTranslate.setVisibility(View.INVISIBLE);
                    imgRefresh.setVisibility(View.INVISIBLE);
                    imgRefresh.clearAnimation();

                    System.out.println(isTranslate.toString());
                    txtTranslatedPhrase.setText(isTranslate.getTranslatePhrase());

                }

            }
        });



        TextToSpeechService.getShareInstance().textSpeechServiceImpl = TranslateActivity.this;
    }

    //MARK: Adapter Cell click Listener
    @Override
    public void onCellClick(Phrase phrase, int index) {

        if (phrase != null){

            selectedPhrase = phrase;
            constraintViwExtra.setVisibility(View.INVISIBLE);
            txtTranslatedPhrase.setText("");
            btnTranslate.setVisibility(View.VISIBLE);
            btnTranslate.setEnabled(true);
            txtSelectedPhraseText.setText(phrase.getPhrase());
        }

    }

    //MARK: Language Translate service implement
    @Override
    public void getLanguageList(IdentifiableLanguages languages) {

    }

    @Override
    public void getTranslateResult(TranslationResult result) {

        if (result != null){

            if (result.getWordCount() > 0){

                constraintViwExtra.setVisibility(View.VISIBLE);
                btnTranslate.setVisibility(View.VISIBLE);
                imgRefresh.setVisibility(View.INVISIBLE);
                imgRefresh.clearAnimation();

                for(Translation translation: result.getTranslations()){

                    txtTranslatedPhrase.setText( txtTranslatedPhrase.getText() + translation.getTranslation() );
                }

                savePhrase();

            } else {
                btnTranslate.setVisibility(View.VISIBLE);
                imgRefresh.setVisibility(View.INVISIBLE);
                imgRefresh.clearAnimation();

                Toast.makeText(TranslateActivity.this, "Failed translation",
                        Toast.LENGTH_LONG).show();
            }
        }else {
            btnTranslate.setVisibility(View.VISIBLE);
            imgRefresh.setVisibility(View.INVISIBLE);
            imgRefresh.clearAnimation();

            Toast.makeText(TranslateActivity.this, "Failed translation",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void getTranslateListResult(TranslateModel model) {

    }

    //MARK: Text to Speech service implement
    @Override
    public void isSuccessSpeech(Boolean status) {

        btnSpeech.setVisibility(View.VISIBLE);
        imagePronounceRefresh.setVisibility(View.INVISIBLE);
        imagePronounceRefresh.clearAnimation();

    }
}
