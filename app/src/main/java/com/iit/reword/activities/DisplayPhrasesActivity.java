package com.iit.reword.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.iit.reword.R;
import com.iit.reword.adapters.PhraseDisplayAdapter;
import com.iit.reword.roomdb.DbHandler;
import com.iit.reword.roomdb.model.Phrase;
import com.iit.reword.utility.Constant;

import java.util.List;

public class DisplayPhrasesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_phrases);

        setupview();
    }

    private void setupview(){

        recyclerView = findViewById(R.id.recycleViewPhrase);

        List<Phrase> phrases = DbHandler.getAppDatabase(DisplayPhrasesActivity.this).phraseDao().getAll(Constant.LOGGING_USER.getUsername());

        PhraseDisplayAdapter phraseDisplayAdapter = new PhraseDisplayAdapter(phrases);

        recyclerView.setAdapter(phraseDisplayAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));



    }
}
