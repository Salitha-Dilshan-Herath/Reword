package com.iit.reword.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.watson.language_translator.v3.model.IdentifiableLanguages;
import com.ibm.watson.language_translator.v3.model.Translation;
import com.ibm.watson.language_translator.v3.model.TranslationResult;
import com.iit.reword.R;
import com.iit.reword.adapters.LanguageDropDownAdapter;
import com.iit.reword.adapters.PhraseDisplayAdapter;
import com.iit.reword.model.TranslateModel;
import com.iit.reword.roomdb.DbHandler;
import com.iit.reword.roomdb.model.Language;
import com.iit.reword.roomdb.model.LanguageSubscription;
import com.iit.reword.roomdb.model.Phrase;
import com.iit.reword.services.LanguageTranslatorService;
import com.iit.reword.services.TextToSpeechService;
import com.iit.reword.utility.interfaces.AdapterClickListener;
import com.iit.reword.utility.Constant;
import com.iit.reword.utility.interfaces.LanguageTranslatorServiceImpl;
import com.iit.reword.utility.interfaces.TextSpeechServiceImpl;
import com.iit.reword.utility.Utility;

import java.util.ArrayList;
import java.util.List;

public class TranslateActivity extends AppCompatActivity implements AdapterClickListener, LanguageTranslatorServiceImpl, TextSpeechServiceImpl {

    //MARK: UI Elements
    private RecyclerView recyclerView;
    private Spinner spinnerLanguages;
    private TextView txtSelectedPhraseText;
    private TextView txtTranslatedPhrase;
    private ImageView imgRefresh;
    private Button btnTranslate;
    private Button btnSpeech;
    private ConstraintLayout constraintViwExtra;
    private ImageView imagePronounceRefresh;

    //MARK: Instance Variables
    private ArrayAdapter adapter;
    private List<LanguageSubscription> languageSubscriptions;

    //MARK: Life cycle events
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        setupView();
    }

    //MARK: Custom methods
    private void setupView(){

        recyclerView          = findViewById(R.id.recycleViewPhraseTranslator);
        spinnerLanguages      = findViewById(R.id.spinnerLanguages);
        txtSelectedPhraseText = findViewById(R.id.txtSelectedPhrase);
        imgRefresh            = findViewById(R.id.imgRefresh);
        btnTranslate          = findViewById(R.id.btnTranslate);
        txtTranslatedPhrase   = findViewById(R.id.txtTranslatedPhrase);
        constraintViwExtra    = findViewById(R.id.constraintViwExtra);
        btnSpeech             = findViewById(R.id.btnSpeech);
        imagePronounceRefresh = findViewById(R.id.imagePronounceRefresh);

        constraintViwExtra.setVisibility(View.INVISIBLE);
        imgRefresh.setVisibility(View.INVISIBLE);
        imagePronounceRefresh.setVisibility(View.INVISIBLE);
        btnTranslate.setEnabled(false);

        List<Phrase> phrases = DbHandler.getAppDatabase(TranslateActivity.this).phraseDao().getAll(Constant.LOGGING_USER.getU_id());
        PhraseDisplayAdapter phraseDisplayAdapter = new PhraseDisplayAdapter(phrases, this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);

        recyclerView.setAdapter(phraseDisplayAdapter);
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

                LanguageSubscription selectedLanguage = languageSubscriptions.get(spinnerLanguages.getSelectedItemPosition() - 1);
                Language language = DbHandler.getAppDatabase(TranslateActivity.this).languageDao().get(selectedLanguage.getName());

                System.out.println(language.getCode());
                LanguageTranslatorService.getShareInstance().languageTranslatorServiceImpl = TranslateActivity.this;

                TranslateModel translateModel = new TranslateModel(txtSelectedPhraseText.getText().toString(), language.getCode());
                LanguageTranslatorService.getShareInstance().translate(translateModel);

                TextToSpeechService.getShareInstance().textSpeechServiceImpl = TranslateActivity.this;

            }
        });

        spinnerLanguages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                constraintViwExtra.setVisibility(View.INVISIBLE);
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
                TextToSpeechService.getShareInstance().speech(s);
            }
        });

    }

    //MARK: Load Data to spinner
    private void setSpinnerValues() {

        ArrayList stringList = new ArrayList();
        languageSubscriptions = DbHandler.getAppDatabase(TranslateActivity.this).languageSubscriptionDao().getAll(Constant.LOGGING_USER.getU_id());

        for(LanguageSubscription subscription: languageSubscriptions){
            stringList.add(subscription.getName());
        }

        //set data to spinner
        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, stringList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerLanguages.setAdapter(
                new LanguageDropDownAdapter(
                        adapter,
                        R.layout.spinner_defualt_value,
                        this));

    }

    //MARK: Adapter Cell click Listener
    @Override
    public void onCellClick(Phrase phrase, int index) {

        if (phrase != null){

            constraintViwExtra.setVisibility(View.INVISIBLE);
            txtTranslatedPhrase.setText("");
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

        constraintViwExtra.setVisibility(View.VISIBLE);
        btnTranslate.setVisibility(View.VISIBLE);
        imgRefresh.setVisibility(View.INVISIBLE);
        imgRefresh.clearAnimation();


        if (result != null){

            if (result.getWordCount() > 0){

                for(Translation translation: result.getTranslations()){

                    txtTranslatedPhrase.setText( txtTranslatedPhrase.getText() + translation.getTranslation() );
                }
            }
        }
    }

    //MARK: Text to Speech service implement
    @Override
    public void isSuccessSpeech(Boolean status) {

        btnSpeech.setVisibility(View.VISIBLE);
        imagePronounceRefresh.setVisibility(View.INVISIBLE);
        imagePronounceRefresh.clearAnimation();

    }
}
