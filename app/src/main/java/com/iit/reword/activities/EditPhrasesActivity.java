package com.iit.reword.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.iit.reword.R;
import com.iit.reword.adapters.PhraseEditAdapter;
import com.iit.reword.roomdb.DbHandler;
import com.iit.reword.roomdb.model.Phrase;
import com.iit.reword.utility.Constant;

import java.util.List;

public class EditPhrasesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phrases);

        setupview();
    }


    private void setupview(){

        recyclerView = findViewById(R.id.recycleViewEditPhrase);

        List<Phrase> phrases = DbHandler.getAppDatabase(EditPhrasesActivity.this).phraseDao().getAll(Constant.LOGGING_USER.getUsername());

        PhraseEditAdapter phraseEditAdapter = new PhraseEditAdapter(phrases);

        recyclerView.setAdapter(phraseEditAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));



    }
}
