package com.iit.reword.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ibm.watson.language_translator.v3.model.IdentifiableLanguage;
import com.ibm.watson.language_translator.v3.model.IdentifiableLanguages;
import com.ibm.watson.language_translator.v3.model.TranslationResult;
import com.iit.reword.R;
import com.iit.reword.adapters.LanguageSubscriptionAdapter;
import com.iit.reword.adapters.PhraseEditAdapter;
import com.iit.reword.model.LanguageDisplay;
import com.iit.reword.model.TranslateModel;
import com.iit.reword.roomdb.DbHandler;
import com.iit.reword.roomdb.model.Language;
import com.iit.reword.roomdb.model.LanguageSubscription;
import com.iit.reword.roomdb.viewModel.LanguageSubscriptionViewModel;
import com.iit.reword.roomdb.viewModel.LanguageViewModel;
import com.iit.reword.services.LanguageTranslatorService;
import com.iit.reword.utility.Constant;
import com.iit.reword.utility.Utility;
import com.iit.reword.utility.interfaces.LanguageTranslatorServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class LanguageSubscriptionActivity extends AppCompatActivity implements LanguageTranslatorServiceImpl {

    //MARK: UI Element
    private RecyclerView recyclerView;
    private Button btnRetry;
    private ProgressBar progressBarWait;
    private ConstraintLayout viwErrorPanel;

    //MARK: Instance Variable
    private LanguageViewModel languageViewModel;
    private LanguageSubscriptionViewModel languageSubscriptionViewModel;
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

        languageViewModel             = new ViewModelProvider(this).get(LanguageViewModel.class);
        languageSubscriptionViewModel = new ViewModelProvider(this).get(LanguageSubscriptionViewModel.class);

        recyclerView  = findViewById(R.id.recycleViewLanguageSubscription);
        viwErrorPanel = findViewById(R.id.viwErrorPanel);
        btnRetry      = findViewById(R.id.btnRetry);
        progressBarWait = findViewById(R.id.progressBarWait);
        viwErrorPanel.setVisibility(View.INVISIBLE);
        progressBarWait.setVisibility(View.INVISIBLE);

        btnUpdate = findViewById(R.id.btnUpdate);

        loadData();
        setupListeners();
    }

    private void setupListeners() {

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("update click");

                boolean isSuccess = true;

                List<LanguageSubscription> languagesList = new ArrayList<>();
                for(LanguageDisplay languageDisplay: languageSubscriptionAdapter.getLanguageDisplayList()){

                    LanguageSubscription language = new LanguageSubscription();
                    language.setName(languageDisplay.getName());
                    language.setLan_code(languageDisplay.getLanCode());
                    language.setIsSub(languageDisplay.isSubscribe());
                    languagesList.add(language);
                }

                languageSubscriptionViewModel.insertAll(languagesList);
                if (isSuccess){
                    Toast.makeText(LanguageSubscriptionActivity.this, "Your subscription success",
                            Toast.LENGTH_LONG).show();

                }else {
                    Toast.makeText(LanguageSubscriptionActivity.this, "Your subscription fail",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnRetry.setVisibility(View.INVISIBLE);
                progressBarWait.setVisibility(View.VISIBLE);

                //Check network reachability
                if(Utility.isInternetReachability(LanguageSubscriptionActivity.this)){
                    //Invoke language download method
                    LanguageTranslatorService.getShareInstance().languageTranslatorServiceImpl = LanguageSubscriptionActivity.this;
                    LanguageTranslatorService.getShareInstance().getAllLanguages();
                }else {
                    btnRetry.setVisibility(View.VISIBLE);
                    progressBarWait.setVisibility(View.INVISIBLE);
                    Toast.makeText(LanguageSubscriptionActivity.this, "Your internet connection appears to be offline",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void loadData(){

        final LiveData<List<Language>> languageListObservable  = languageViewModel.getAllWords();
        final LiveData<List<LanguageSubscription>> languageSubListObservable  = languageSubscriptionViewModel.getAll();

        languageListObservable.observe(this, new Observer<List<Language>>() {
            @Override
            public void onChanged(List<Language> languageList) {

                languageListObservable.removeObserver(this);

                if(languageList.size() == 0){
                    viwErrorPanel.setVisibility(View.VISIBLE);
                    return;
                }

                languageSubListObservable.observe(LanguageSubscriptionActivity.this, new Observer<List<LanguageSubscription>>() {
                    @Override
                    public void onChanged(List<LanguageSubscription> languageSubscriptions) {

                        languageSubListObservable.removeObserver(this);

                        for(Language language: languageList){
                            LanguageDisplay display = new LanguageDisplay();

                            display.setLanCode(language.getCode());
                            display.setSubscribe(false);
                            display.setName(language.getName());
                            languageDisplayList.add(display);
                        }

                        for(LanguageSubscription languageSubscription : languageSubscriptions){
                            LanguageDisplay findObj = findObject(languageSubscription.getName());

                            if (findObj != null){
                                findObj.setSubscribe(languageSubscription.getIsSub());
                            }
                        }

                        languageSubscriptionAdapter = new LanguageSubscriptionAdapter(languageDisplayList);
                        recyclerView.setAdapter(languageSubscriptionAdapter);

                    }
                });

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void retryDownloadDataBind(List<Language> languageList){
        for(Language language: languageList){
            LanguageDisplay display = new LanguageDisplay();

            display.setLanCode(language.getCode());
            display.setSubscribe(false);
            display.setName(language.getName());
            languageDisplayList.add(display);
        }

        viwErrorPanel.setVisibility(View.INVISIBLE);
        languageSubscriptionAdapter = new LanguageSubscriptionAdapter(languageDisplayList);
        recyclerView.setAdapter(languageSubscriptionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private LanguageDisplay findObject( String  code) {

        LanguageDisplay languageDisplay = null;

        for(LanguageDisplay display: languageDisplayList) {
            if (display.getName().equals(code)){
                languageDisplay = display;
                break;
            }
        }

        return  languageDisplay;
    }

    @Override
    public void getLanguageList(IdentifiableLanguages languages) {

        System.out.println(languages);

        if (languages == null){
            btnRetry.setVisibility(View.VISIBLE);
            progressBarWait.setVisibility(View.INVISIBLE);
            return;

        }

        int index = 0;
        List<Language> languagesListDownload = new ArrayList<>();
        for (IdentifiableLanguage language : languages.getLanguages()) {

            Language dbLanguage = new Language();
            dbLanguage.setName(language.getName());
            dbLanguage.setCode(language.getLanguage());
            languagesListDownload.add(dbLanguage);

            languageViewModel.insert(dbLanguage);
            index++;

            if (index == languages.getLanguages().size()){
                retryDownloadDataBind(languagesListDownload);
            }
        }




    }

    @Override
    public void getTranslateResult(TranslationResult result) {

    }

    @Override
    public void getTranslateListResult(TranslateModel model) {

    }
}
