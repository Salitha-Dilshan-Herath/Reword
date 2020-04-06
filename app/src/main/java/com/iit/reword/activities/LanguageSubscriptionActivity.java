package com.iit.reword.activities;

import androidx.appcompat.app.AppCompatActivity;
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
        List<LanguageSubscription> languageSubscriptions = DbHandler.getAppDatabase(LanguageSubscriptionActivity.this).languageSubscriptionDao().getAll(Constant.LOGGING_USER.getU_id());

        for(Language language: languages){
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
                DbHandler.getAppDatabase(LanguageSubscriptionActivity.this).languageSubscriptionDao().delete(Constant.LOGGING_USER.getU_id());

                boolean isSuccess = false;

                for(LanguageDisplay languageDisplay: languageSubscriptionAdapter.getLanguageDisplayList()){

                    if (languageDisplay.isSubscribe()){

                        LanguageSubscription language = new LanguageSubscription();
                        language.setName(languageDisplay.getName());
                        language.setU_id(Constant.LOGGING_USER.getU_id());

                        long insertRespones = DbHandler.getAppDatabase(LanguageSubscriptionActivity.this).languageSubscriptionDao().insert(language);

                        if (insertRespones > 0 ){
                            isSuccess = true;
                        }else {
                            isSuccess = false;
                        }
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
