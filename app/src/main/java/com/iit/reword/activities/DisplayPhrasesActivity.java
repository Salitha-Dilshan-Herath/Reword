package com.iit.reword.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.iit.reword.R;
import com.iit.reword.adapters.PhraseDisplayAdapter;
import com.iit.reword.roomdb.viewModel.PhraseViewModel;


public class DisplayPhrasesActivity extends AppCompatActivity {

    //MARK: UI Elements
    private RecyclerView recyclerView;
    private TextView txtError;

    //MARK: Instance Variable
    private PhraseViewModel phraseViewModel;

    //MARK: LIfe Cycle methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_phrases);

        setupView();
    }

    //MARK: Custom Methods
    private void setupView(){

        phraseViewModel = new ViewModelProvider(this).get(PhraseViewModel.class);

        recyclerView = findViewById(R.id.recycleViewPhrase);
        txtError     = findViewById(R.id.txtError);

        txtError.setVisibility(View.INVISIBLE);

        //load data from database
        phraseViewModel.getAll().observe(this, phrases -> {

            if (phrases.size() == 0){
                txtError.setVisibility(View.VISIBLE);
                return;
            }

            PhraseDisplayAdapter phraseDisplayAdapter = new PhraseDisplayAdapter(phrases, null);
            recyclerView.setAdapter(phraseDisplayAdapter);
        });

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(dividerItemDecoration);

    }
}
