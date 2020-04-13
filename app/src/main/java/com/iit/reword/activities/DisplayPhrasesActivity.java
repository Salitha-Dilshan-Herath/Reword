package com.iit.reword.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.iit.reword.R;
import com.iit.reword.adapters.PhraseDisplayAdapter;
import com.iit.reword.roomdb.DbHandler;
import com.iit.reword.roomdb.model.Phrase;
import com.iit.reword.roomdb.viewModel.PhraseViewModel;
import com.iit.reword.utility.Constant;

import java.util.List;

public class DisplayPhrasesActivity extends AppCompatActivity {

    //MARK: UI Elements
    private RecyclerView recyclerView;

    //MARK: Instance Variable
    private PhraseViewModel phraseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_phrases);

        setupView();
    }

    private void setupView(){

        phraseViewModel = new ViewModelProvider(this).get(PhraseViewModel.class);

        recyclerView = findViewById(R.id.recycleViewPhrase);

        phraseViewModel.getAll(Constant.LOGGING_USER.getU_id()).observe(this, phrases -> {
            PhraseDisplayAdapter phraseDisplayAdapter = new PhraseDisplayAdapter(phrases, null);
            recyclerView.setAdapter(phraseDisplayAdapter);
        });

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(dividerItemDecoration);

    }
}
