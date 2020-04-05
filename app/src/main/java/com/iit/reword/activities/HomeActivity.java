package com.iit.reword.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.iit.reword.R;
import com.iit.reword.roomdb.DbHandler;
import com.iit.reword.utility.Constant;

public class HomeActivity extends AppCompatActivity {

    private Button btnLogout;
    private Button btnAddPhrases;
    private Button btnDisplayPhrase;
    private Button btnEditPhrase;
    private Button btnSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupview();
    }


    private void setupview() {

        btnLogout        = findViewById(R.id.btnLogout);
        btnAddPhrases    = findViewById(R.id.btnAddPhrases);
        btnDisplayPhrase = findViewById(R.id.btnDisplayPhrase);
        btnEditPhrase    = findViewById(R.id.btnEditPhrases);
        btnSubscription  = findViewById(R.id.btnSubscription);

        setupListeners();

    }

    private void setupListeners(){

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutAlert();
            }
        });

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

    }

    private void logoutAlert(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage("Do you want logout?");
        alertBuilder.setCancelable(true);

        alertBuilder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        DbHandler.getAppDatabase(HomeActivity.this).userDao().updateUserStatus(Constant.LOGGING_USER.getUsername(),false);

                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                        HomeActivity.this.finish();
                    }
                });

        alertBuilder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = alertBuilder.create();
        alert11.show();
    }
}
