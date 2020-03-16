package com.iit.reword;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;

public class SignupActivity extends AppCompatActivity {

    private TextInputLayout txtUsername;
    private TextInputLayout txtPassword;
    private TextInputLayout txtRePassword;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        setupview();
    }


    private void setupview() {

        txtUsername   = findViewById(R.id.txtUsername);
        txtPassword   = findViewById(R.id.txtPassword);
        txtRePassword = findViewById(R.id.txtRePassword);

        username      = txtUsername.getEditText().getText().toString();
    }
}
