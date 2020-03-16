package com.iit.reword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.iit.reword.utility.Typewriter;

public class LoginActivity extends AppCompatActivity {

    private Typewriter txtWelcome;
    private Button     btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        setupview();
    }


    private void setupview() {

        txtWelcome = findViewById(R.id.txtWelcome);
        btnSignup  = findViewById(R.id.btnSignup);

        txtWelcome.setCharacterDelay(150);
        txtWelcome.animateText("Hello there, welcome back");
        //txtWelcome.animateText("ආයුබෝවන්, නැවත සාදරයෙන් පිළිගනිමු");

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}
