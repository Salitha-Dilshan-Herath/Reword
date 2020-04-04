package com.iit.reword.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.iit.reword.R;
import com.iit.reword.roomdb.DbHandler;
import com.iit.reword.roomdb.model.Phrase;
import com.iit.reword.utility.Constant;

public class AddPhrasesActivity extends AppCompatActivity implements TextWatcher {

    private Button btnAddPhrase;
    private TextInputLayout phraseTextInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phrases);

        setupview();
    }

    private void setupview(){

        btnAddPhrase = findViewById(R.id.btnAddPhrase);

        phraseTextInputLayout = findViewById(R.id.addPhrasesTextInputLayout);
        phraseTextInputLayout.getEditText().addTextChangedListener(this);

        setupListeners();
    }

    private void setupListeners() {


        btnAddPhrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phrase = phraseTextInputLayout.getEditText().getText().toString().trim().toLowerCase();

                if (phrase.equals("")) {
                    phraseTextInputLayout.setError("Please enter phrase");
                    return ;
                }

               addPhraseToDB(phrase);

            }
        });
    }

    private void addPhraseToDB(String phrase) {
        Phrase phraseObj = new Phrase();
        phraseObj.setPhrase(phrase);
        phraseObj.setUser(Constant.LOGGING_USER.getUsername());

        int isExists = DbHandler.getAppDatabase(AddPhrasesActivity.this).phraseDao().isExists(phrase, Constant.LOGGING_USER.getUsername());

        if (isExists != 0 ) {
            Toast.makeText(AddPhrasesActivity.this, phrase +" already exists, Try another phrase",
                    Toast.LENGTH_LONG).show();

            return;
        }

        long response =  DbHandler.getAppDatabase(AddPhrasesActivity.this).phraseDao().insert(phraseObj);

        if (response != 0 ) {

            phraseTextInputLayout.getEditText().setText(null);

            Toast.makeText(AddPhrasesActivity.this, "Phrase added success",
                    Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(AddPhrasesActivity.this, "Phrase add fail",
                    Toast.LENGTH_LONG).show();
        }
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
