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
import android.widget.Button;
import android.widget.Toast;

import com.iit.reword.R;
import com.iit.reword.adapters.LanguageSubscriptionAdapter;
import com.iit.reword.adapters.PhraseEditAdapter;
import com.iit.reword.model.LanguageDisplay;
import com.iit.reword.roomdb.DbHandler;
import com.iit.reword.roomdb.model.Language;
import com.iit.reword.roomdb.model.LanguageSubscription;
import com.iit.reword.roomdb.viewModel.LanguageSubscriptionViewModel;
import com.iit.reword.roomdb.viewModel.LanguageViewModel;
import com.iit.reword.utility.Constant;

import java.util.ArrayList;
import java.util.List;

public class LanguageSubscriptionActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button btnUpdate;
    private LanguageSubscriptionAdapter languageSubscriptionAdapter;
    private List<LanguageDisplay> languageDisplayList = new ArrayList();

    //MARK: Instance Variable
    private LanguageViewModel languageViewModel;
    private LanguageSubscriptionViewModel languageSubscriptionViewModel;

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

        recyclerView = findViewById(R.id.recycleViewLanguageSubscription);

        btnUpdate = findViewById(R.id.btnUpdate);

        final LiveData<List<Language>> languageListObservable  = languageViewModel.getAllWords();
        final LiveData<List<LanguageSubscription>> languageSubListObservable  = languageSubscriptionViewModel.getAll(Constant.LOGGING_USER.getU_id());

        languageListObservable.observe(this, new Observer<List<Language>>() {
            @Override
            public void onChanged(List<Language> languageList) {

                languageListObservable.removeObserver(this);

                languageSubListObservable.observe(LanguageSubscriptionActivity.this, new Observer<List<LanguageSubscription>>() {
                    @Override
                    public void onChanged(List<LanguageSubscription> languageSubscriptions) {

                        languageSubListObservable.removeObserver(this);

                        for(Language language: languageList){
                            LanguageDisplay display = new LanguageDisplay();

                            display.setSubscribe(false);
                            display.setName(language.getName());
                            languageDisplayList.add(display);
                        }

                        for(LanguageSubscription languageSubscription : languageSubscriptions){

                            LanguageDisplay findObj = findObject(languageSubscription.getName());

                            if (findObj != null){
                                findObj.setSubscribe(true);
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
        setupListeners();
    }

    private void setupListeners() {

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("update click");

                languageSubscriptionViewModel.delete(Constant.LOGGING_USER.getU_id());
                boolean isSuccess = true;

                languageSubscriptionViewModel.getAll(Constant.LOGGING_USER.getU_id()).observe(LanguageSubscriptionActivity.this, languageSubscriptions -> {
                    System.out.println("clear count");
                    System.out.println(languageSubscriptions.size());
                });

                for(LanguageDisplay languageDisplay: languageSubscriptionAdapter.getLanguageDisplayList()){


                    if (languageDisplay.isSubscribe()){
                        System.out.println(languageDisplay.toString());

                        LanguageSubscription language = new LanguageSubscription();
                        language.setName(languageDisplay.getName());
                        language.setU_id(Constant.LOGGING_USER.getU_id());

                        languageSubscriptionViewModel.insert(language);

                    }
                }

                if (isSuccess){
                    Toast.makeText(LanguageSubscriptionActivity.this, "Your subscription success",
                            Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(LanguageSubscriptionActivity.this, "Your subscription fail",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
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

}
