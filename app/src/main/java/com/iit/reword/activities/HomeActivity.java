package com.iit.reword.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.iit.reword.R;
import com.iit.reword.utility.Typewriter;

public class HomeActivity extends AppCompatActivity {

    //MARK: - UI Elements
    private Typewriter txtHeader;
    private Button btnAddPhrases;
    private Button btnDisplayPhrase;
    private Button btnEditPhrase;
    private Button btnSubscription;
    private Button btnTranslator;

    //MARK: + Life Cycle events
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupView();
    }

    //MARK: - Setup activity
    private void setupView() {
        btnAddPhrases    = findViewById(R.id.btnAddPhrases);
        btnDisplayPhrase = findViewById(R.id.btnDisplayPhrase);
        btnEditPhrase    = findViewById(R.id.btnEditPhrases);
        btnSubscription  = findViewById(R.id.btnSubscription);
        btnTranslator    = findViewById(R.id.btnTranslator);
        txtHeader        = findViewById(R.id.txtHeader);

        //header animation
        txtHeader.setCharacterDelay(150);
        txtHeader.animateText("Reword");

        //Invoke listeners
        setupListeners();

    }

    //MARK: - Setup Button Click listeners
    private void setupListeners(){

        btnAddPhrases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, AddPhrasesActivity.class);
                startActivity(intent);
            }
        });

        btnDisplayPhrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, DisplayPhrasesActivity.class);
                startActivity(intent);
            }
        });

        btnEditPhrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, EditPhrasesActivity.class);
                startActivity(intent);
            }
        });

        btnSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, LanguageSubscriptionActivity.class);
                startActivity(intent);
            }
        });

        btnTranslator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, TranslateActivity.class);
                startActivity(intent);

            }
        });
    }
}
