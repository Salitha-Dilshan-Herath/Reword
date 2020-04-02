package com.iit.reword.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.iit.reword.R;
import com.iit.reword.roomdb.DbHandler;
import com.iit.reword.roomdb.model.User;
import com.iit.reword.utility.Constant;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        int secondsDelayed = 5;
        new Handler().postDelayed(new Runnable() {
            public void run() {

                User user = DbHandler.getAppDatabase(getApplicationContext()).userDao().getLoginUser();

                if (user != null) {

                    Constant.LOGGING_USER = user;

                    Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, secondsDelayed * 1000);
    }
}
