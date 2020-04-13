package com.iit.reword.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.iit.reword.R;
import com.iit.reword.roomdb.model.User;
import com.iit.reword.roomdb.viewModel.UserViewModel;
import com.iit.reword.utility.Constant;
import com.iit.reword.utility.Typewriter;


public class LoginActivity extends AppCompatActivity implements TextWatcher {

    private Typewriter txtWelcome;
    private Button btnSignup;
    private Button btnLogin;
    private TextInputLayout usernameTextInputLayout;
    private TextInputLayout passwordTextInputLayout;

    //MARK: Instance Variable
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        setupActivity();
    }


    private void setupActivity() {

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        txtWelcome = findViewById(R.id.txtWelcome);
        btnSignup = findViewById(R.id.btnSignup);
        btnLogin = findViewById(R.id.btnLogin);

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

                validateCredential();
            }
        });
    }

    private Boolean isValidateUserInputs(String username, String password) {

        if (username.trim().equals("")) {
            usernameTextInputLayout.setError("Please enter username");
            return false;
        }

        if (password.trim().equals("")) {
            passwordTextInputLayout.setError("Please enter password");
            return false;
        }

        return true;
    }

    private void validateCredential() {

        String username = usernameTextInputLayout.getEditText().getText().toString();
        String password = passwordTextInputLayout.getEditText().getText().toString();


        if (!isValidateUserInputs(username, password)) {
            return;
        }

        final LiveData<User> isExistsUsersObservable = userViewModel.isExistsUsers(username);

        isExistsUsersObservable.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {

                isExistsUsersObservable.removeObserver(this);

                if (user != null) {
                    if (user.getPassword().equals(password)) {

                        userViewModel.updateUserStatus(user.getU_id(), true);
                        Constant.LOGGING_USER = user;

                        Toast.makeText(LoginActivity.this, "User login successful",
                                Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();

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
