package com.iit.reword.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.iit.reword.R;
import com.iit.reword.roomdb.model.Phrase;
import com.iit.reword.roomdb.viewModel.PhraseViewModel;

public class AddPhrasesActivity extends AppCompatActivity implements TextWatcher {

    //MARK: UI Elements
    private Button btnAddPhrase;
    private TextInputLayout phraseTextInputLayout;

    //MARK: Instance Variable
    private PhraseViewModel phraseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phrases);

        setupview();
    }

    private void setupview(){

        phraseViewModel = new ViewModelProvider(this).get(PhraseViewModel.class);
        btnAddPhrase = findViewById(R.id.btnAddPhrase);

        phraseTextInputLayout = findViewById(R.id.addPhrasesTextInputLayout);
        phraseTextInputLayout.getEditText().addTextChangedListener(this);

        setupListeners();
    }

    private void setupListeners() {


        btnAddPhrase.setOnClickListener(view -> {

            String phrase = phraseTextInputLayout.getEditText().getText().toString().trim().toLowerCase();

            if (phrase.equals("")) {
                phraseTextInputLayout.setError("Please enter phrase");
                return ;
            }

           addPhraseToDB(phrase);

        });
    }

    private void addPhraseToDB(String txtphrase) {


        final LiveData<Phrase> isExistsPhrasesObservable = phraseViewModel.isExists(txtphrase);

        isExistsPhrasesObservable.observe(this, new Observer<Phrase>() {

            @Override
            public void onChanged(Phrase phrase) {

                isExistsPhrasesObservable.removeObserver(this);

                System.out.println(phrase);
                if (phrase !=null){
                    Toast.makeText(AddPhrasesActivity.this, txtphrase +" already exists, Try another phrase/word",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                Phrase phraseObj = new Phrase();
                phraseObj.setPhrase(txtphrase);
                phraseViewModel.insert(phraseObj);

                phraseTextInputLayout.getEditText().setText(null);
                Toast.makeText(AddPhrasesActivity.this, "Phrase added successfully",
                        Toast.LENGTH_LONG).show();


            }
        });

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        phraseTextInputLayout.setError(null);
    }
}
