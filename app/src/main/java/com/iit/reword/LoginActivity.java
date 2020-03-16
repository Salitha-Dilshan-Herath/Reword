package com.iit.reword;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.iit.reword.utility.Typewriter;

public class LoginActivity extends AppCompatActivity {

    private Typewriter txtWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        setupview();
    }

    private void setupview() {

        txtWelcome = findViewById(R.id.txtWelcome);
        txtWelcome.setCharacterDelay(150);
        txtWelcome.animateText("Hello there, welcome back");
    }
}
