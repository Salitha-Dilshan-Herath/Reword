package com.iit.reword.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.iit.reword.R;
import com.iit.reword.adapters.LanguageDropDownAdapter;
import com.iit.reword.adapters.PhraseDisplayAdapter;
import com.iit.reword.roomdb.DbHandler;
import com.iit.reword.roomdb.model.LanguageSubscription;
import com.iit.reword.roomdb.model.Phrase;
import com.iit.reword.utility.AdapterClickListener;
import com.iit.reword.utility.Constant;

import java.util.ArrayList;
import java.util.List;

public class TranslateActivity extends AppCompatActivity implements AdapterClickListener {

    private RecyclerView recyclerView;
    private ArrayAdapter<String> adapter;
    private Spinner spinnerLanguages;
    private TextView txtSelectedPhraseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        setupView();
    }

    private void setupView(){

        recyclerView          = findViewById(R.id.recycleViewPhraseTranslator);
        spinnerLanguages      = findViewById(R.id.spinnerLanguages);
        txtSelectedPhraseText = findViewById(R.id.txtSelectedPhrase);

        List<Phrase> phrases = DbHandler.getAppDatabase(TranslateActivity.this).phraseDao().getAll(Constant.LOGGING_USER.getU_id());
        PhraseDisplayAdapter phraseDisplayAdapter = new PhraseDisplayAdapter(phrases, this);
        recyclerView.setAdapter(phraseDisplayAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        setSpinnerValues();

    }

    //MARK: Load Data to spinner
    private void setSpinnerValues() {

        List<String> stringList = new ArrayList();
        List<LanguageSubscription> languageSubscriptions = DbHandler.getAppDatabase(TranslateActivity.this).languageSubscriptionDao().getAll(Constant.LOGGING_USER.getU_id());

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

    @Override
    public void onCellClick(Phrase phrase, int index) {

        if (phrase != null){
            txtSelectedPhraseText.setText(phrase.getPhrase());
        }

    }
}
