package com.iit.reword.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.iit.reword.R;
import com.iit.reword.adapters.PhraseEditAdapter;
import com.iit.reword.roomdb.DbHandler;
import com.iit.reword.roomdb.model.Phrase;
import com.iit.reword.utility.Constant;
import com.iit.reword.utility.interfaces.EditPhraseRadioClickListener;
import com.iit.reword.utility.Utility;

import java.util.List;

public class EditPhrasesActivity extends AppCompatActivity implements EditPhraseRadioClickListener {

    private RecyclerView recyclerView;
    private TextInputLayout editTextInputLayout;
    private Button btnEdit;
    private Button btnSave;
    private PhraseEditAdapter phraseEditAdapter;
    private List<Phrase> phrases;
    private Phrase selectedPhrase = null;
    private int selectedIndex     = -1;
    private Boolean isEditMode    = false;


    //MARK: Life Cycle methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phrases);

        setupView();
    }

    //MARK: Setup Activity
    private void setupView() {

        recyclerView = findViewById(R.id.recycleViewEditPhrase);
        editTextInputLayout = findViewById(R.id.editPhraseTextInputLayout);
        btnEdit = findViewById(R.id.btnEditPhrase);
        btnSave = findViewById(R.id.btnSavePhrase);
        editTextInputLayout.getEditText().setEnabled(false);
        btnSave.setEnabled(false);

        phrases = DbHandler.getAppDatabase(EditPhrasesActivity.this).phraseDao().getAll(Constant.LOGGING_USER.getU_id());
        phraseEditAdapter = new PhraseEditAdapter(phrases, this);
        recyclerView.setAdapter(phraseEditAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        setupListeners();
    }

    //MARK: setup button listeners
    private void setupListeners() {

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selectedPhrase != null) {
                    editTextInputLayout.getEditText().setText(selectedPhrase.getPhrase());
                    editTextInputLayout.getEditText().setEnabled(true);
                    btnSave.setEnabled(true);
                    isEditMode = true;
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String updatePhrase = editTextInputLayout.getEditText().getText().toString().trim().toLowerCase();

                if (updatePhrase.equals("")){
                    Toast.makeText(EditPhrasesActivity.this, "Please enter phrase or word.",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                updatePhrase(updatePhrase);

            }
        });
    }

    //MARK: Update database
    private void updatePhrase(String updatePhrase) {

        int exists = DbHandler.getAppDatabase(EditPhrasesActivity.this).phraseDao().isExists(updatePhrase, Constant.LOGGING_USER.getU_id());

        if (exists > 0) {
            Toast.makeText(EditPhrasesActivity.this, updatePhrase + " already exists, Try another phrase",
                    Toast.LENGTH_LONG).show();
            return;
        }



        int updateRespones = DbHandler.getAppDatabase(EditPhrasesActivity.this).phraseDao().update(updatePhrase, selectedPhrase.getPid());

        if (updateRespones > 0) {

            resetView();

            phrases.get(selectedIndex).setPhrase(updatePhrase);
            selectedPhrase = phrases.get(selectedIndex);
            phraseEditAdapter.notifyDataSetChanged();

            Toast.makeText(EditPhrasesActivity.this, "Phrase update success",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(EditPhrasesActivity.this, "Phrase update fail",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void resetView() {

        Utility.hideSoftKeyboard(EditPhrasesActivity.this); // hide keyboard
        isEditMode = false; // change edit status
        editTextInputLayout.getEditText().setText(null); // remove exits word
        btnSave.setEnabled(false);
        editTextInputLayout.getEditText().clearFocus();

    }

    @Override
    public void onRadioClick(Phrase phrase, int index) {

        selectedPhrase = phrase;
        selectedIndex = index;

        if (isEditMode)
            editTextInputLayout.getEditText().setText(selectedPhrase.getPhrase());
    }
}
