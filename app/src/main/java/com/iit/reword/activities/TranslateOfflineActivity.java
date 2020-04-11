package com.iit.reword.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.iit.reword.R;
import com.iit.reword.adapters.LanguageDropDownAdapter;
import com.iit.reword.adapters.LanguageOfflineDropDownAdapter;
import com.iit.reword.adapters.OfflinePhraseAdapter;
import com.iit.reword.model.OfflineTranslate;
import com.iit.reword.roomdb.DbHandler;
import com.iit.reword.roomdb.model.Language;
import com.iit.reword.roomdb.model.LanguageSubscription;
import com.iit.reword.utility.Constant;

import java.util.ArrayList;
import java.util.List;

public class TranslateOfflineActivity extends AppCompatActivity {

    private Spinner spinnerLanguages;
    private RecyclerView recyclerView;

    //MARK: Instance Variables
    private ArrayAdapter adapter;
    private List<LanguageSubscription> languageSubscriptions;
    private OfflinePhraseAdapter offlinePhraseAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate_offline);

        setupView();
    }

    //MARK: Custom methods
    private void setupView(){

        spinnerLanguages = findViewById(R.id.spinnerLanguagesOffline);
        recyclerView     =  findViewById(R.id.recycleViewOffline);
        setSpinnerValues();
        setupListeners();
    }

    //MARK: Load Data to spinner
    private void setSpinnerValues() {

        ArrayList stringList = new ArrayList();
        languageSubscriptions = DbHandler.getAppDatabase(TranslateOfflineActivity.this).languageSubscriptionDao().getAll(Constant.LOGGING_USER.getU_id());

        for(LanguageSubscription subscription: languageSubscriptions){
            stringList.add(subscription.getName());
        }

        //set data to spinner
        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, stringList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerLanguages.setAdapter(
                new LanguageOfflineDropDownAdapter(
                        adapter,
                        R.layout.offline_spinner_defualt_value,
                        this));



    }

    private void setupListeners (){
        spinnerLanguages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (spinnerLanguages.getSelectedItemPosition() <= 0 )
                    return;

                LanguageSubscription selectedLanguage = languageSubscriptions.get(spinnerLanguages.getSelectedItemPosition() - 1);
                Language language = DbHandler.getAppDatabase(TranslateOfflineActivity.this).languageDao().get(selectedLanguage.getName());

                System.out.println(language.getCode());

                List<OfflineTranslate> offlineTranslatesList = DbHandler.getAppDatabase(TranslateOfflineActivity.this).translateDao().getTranslateWord(language.getName(),Constant.LOGGING_USER.getU_id());

                offlinePhraseAdapter = new OfflinePhraseAdapter(offlineTranslatesList);
                recyclerView.setAdapter(offlinePhraseAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(TranslateOfflineActivity.this, LinearLayoutManager.VERTICAL, false));
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
                recyclerView.addItemDecoration(dividerItemDecoration);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
}
