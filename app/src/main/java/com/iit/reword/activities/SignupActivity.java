package com.iit.reword.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.iit.reword.R;
import com.iit.reword.roomdb.DbHandler;
import com.iit.reword.roomdb.model.User;
import com.iit.reword.utility.Utility;

import java.util.Date;

public class SignupActivity extends AppCompatActivity implements TextWatcher {

    private TextInputLayout usernameTextInputLayout;
    private TextInputLayout passwordTextInputLayout;
    private TextInputLayout rePasswordTextInputLayout;

    private String username;
    private String password;
    private String rePassword;

    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        setupview();
    }


    private void setupview() {

        usernameTextInputLayout    = findViewById(R.id.txtUsername);
        passwordTextInputLayout    = findViewById(R.id.txtPassword);
        rePasswordTextInputLayout  = findViewById(R.id.txtRePassword);
        btnSignup                  = findViewById(R.id.btnSignupPage);

        usernameTextInputLayout.getEditText().addTextChangedListener(this);
        passwordTextInputLayout.getEditText().addTextChangedListener(this);
        rePasswordTextInputLayout.getEditText().addTextChangedListener(this);

        setupListeners();

    }

    private void setupListeners() {

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username = usernameTextInputLayout.getEditText().getText().toString();
                password = passwordTextInputLayout.getEditText().getText().toString();
                rePassword = rePasswordTextInputLayout.getEditText().getText().toString();

                if (username.trim().equals("")) {
                    usernameTextInputLayout.setError("Please enter username");
                    return;
                }

                if (!Utility.isValidEmail(username.trim())) {
                    usernameTextInputLayout.setError("Invalid email");
                    return;
                }

                if (password.trim().equals("")) {
                    passwordTextInputLayout.setError("Please enter password");
                    return;
                }

                if (rePassword.trim().equals("")) {
                    rePasswordTextInputLayout.setError("Please enter Re-password");
                    return;
                }

                if (!rePassword.trim().equals(password.trim())) {

                    System.out.println("Password and Re-password didn't match");
                    return;
                }

                insertNewUser();

            }
        });
    }

    private void insertNewUser () {
        int isExists = DbHandler.getAppDatabase(SignupActivity.this).userDao().isExistsUsers(username);

        if (isExists == 0){

            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setCreatedDate(new Date().toString());
            user.setLogin(false);

            long createdUser = DbHandler.getAppDatabase(SignupActivity.this).userDao().insert(user);

            if (createdUser != 0) {
                Toast.makeText(SignupActivity.this, "User registered successful",
                        Toast.LENGTH_LONG).show();

                SignupActivity.this.finish();

            }else {

                Toast.makeText(SignupActivity.this, "User registration failed",
                        Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(SignupActivity.this, "This user is already registered. Please enter another email",
                    Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


        if (rePasswordTextInputLayout.getEditText().hasFocus()) {
            rePassword = rePasswordTextInputLayout.getEditText().getText().toString().trim();
            password = passwordTextInputLayout.getEditText().getText().toString().trim();

            if (!rePassword.equals("")) {
                if (!password.equals(rePassword)) {
                    rePasswordTextInputLayout.setError("Password didn't match");
                } else {
                    rePasswordTextInputLayout.setError(null);
                }
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        usernameTextInputLayout.setError(null);
        passwordTextInputLayout.setError(null);
    }
}
