package com.iit.reword.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;

import com.iit.reword.R;
import com.iit.reword.adapters.LanguageSubscriptionAdapter;
import com.iit.reword.adapters.PhraseEditAdapter;
import com.iit.reword.model.LanguageDisplay;
import com.iit.reword.roomdb.DbHandler;
import com.iit.reword.roomdb.model.Language;
import com.iit.reword.utility.Constant;

import java.util.ArrayList;
import java.util.List;

public class LanguageSubscriptionActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button btnUpdate;
    private LanguageSubscriptionAdapter languageSubscriptionAdapter;
    private List<LanguageDisplay> languageDisplayList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_subscription);

        setupView();
    }

    //MARK: Setup Activity
    private void setupView() {

        recyclerView = findViewById(R.id.recycleViewLanguageSubscription);

        btnUpdate = findViewById(R.id.btnUpdate);


        List<Language> languages = DbHandler.getAppDatabase(LanguageSubscriptionActivity.this).languageDao().getAll();

        for(Language language: languages){
            LanguageDisplay display = new LanguageDisplay();

            display.setSubscribe(false);
            display.setName(language.getName());
            languageDisplayList.add(display);
        }

        languageSubscriptionAdapter = new LanguageSubscriptionAdapter(languageDisplayList);
        recyclerView.setAdapter(languageSubscriptionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


    }
}
