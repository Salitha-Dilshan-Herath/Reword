package com.iit.reword.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.iit.reword.R;
import com.iit.reword.roomdb.DbHandler;
import com.iit.reword.utility.Typewriter;


public class LoginActivity extends AppCompatActivity implements TextWatcher{

    private Typewriter txtWelcome;
    private Button     btnSignup;
    private Button     btnLogin;
    private TextInputLayout usernameTextInputLayout;
    private TextInputLayout passwordTextInputLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        setupActivity();
    }


    private void setupActivity() {

        txtWelcome = findViewById(R.id.txtWelcome);
        btnSignup  = findViewById(R.id.btnSignup);
        btnLogin   = findViewById(R.id.btnLogin);

        usernameTextInputLayout = findViewById(R.id.usernameTextInputLayout);
        passwordTextInputLayout = findViewById(R.id.passwordTextInputLayout);

        usernameTextInputLayout.getEditText().addTextChangedListener(this);
        passwordTextInputLayout.getEditText().addTextChangedListener(this);

        txtWelcome.setCharacterDelay(150);
        txtWelcome.animateText("Hello there, welcome back");
        //txtWelcome.animateText("ආයුබෝවන්, නැවත සාදරයෙන් පිළිගනිමු");



        setupListeners();


    }

    private void setupListeners() {

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = usernameTextInputLayout.getEditText().getText().toString();
                String password = passwordTextInputLayout.getEditText().getText().toString();

                if (username.trim().equals("")) {
                    usernameTextInputLayout.setError("Please enter username");
                    return;
                }

                if (password.trim().equals("")) {
                    passwordTextInputLayout.setError("Please enter password");
                    return;
                }

                int isExists = DbHandler.getAppDatabase(LoginActivity.this).userDao().isExistsUsers(username);

                if (isExists != 0){
                    Toast.makeText(LoginActivity.this, "User login successful",
                            Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(LoginActivity.this, "Invalid username or password",
                            Toast.LENGTH_LONG).show();
                }
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
        usernameTextInputLayout.setError(null);
        passwordTextInputLayout.setError(null);
    }
}
