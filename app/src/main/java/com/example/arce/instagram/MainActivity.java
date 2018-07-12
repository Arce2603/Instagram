package com.example.arce.instagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView (R.id.etUser)EditText userInput;
    @BindView (R.id.etPassword)EditText passwordInput;
    @BindView(R.id.btnLogin)Button loginBtn;
    @BindView(R.id.btnSign) Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (ParseUser.getCurrentUser()!=null){
            Intent i = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(i);
        }

        else {
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String username = userInput.getText().toString();
                    final String password = passwordInput.getText().toString();

                    login(username, password);
                }
            });

            //TODO SIGN UP CHECK!
            signUpBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String username = userInput.getText().toString();
                    final String password = passwordInput.getText().toString();

                     signUp(username,password);
                }
            });
        }

    }

    //Method used for login to an already existing account
    private void login(String username, String password){
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e == null) {
                    Log.d("LOGIN", "Successfull");
                    Intent i = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();
                }
                else{
                    Toast.makeText(MainActivity.this,"Username or password does not match",Toast.LENGTH_LONG).show();
                    Log.d("LOGIN", "Failure");
                    e.printStackTrace();
                }
            }
        });
    }

    private void signUp(final String username, final String password){
        // Create the ParseUser
        ParseUser user = new ParseUser();
        // Set core properties
        user.setUsername(username);
        user.setPassword(password);

        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {

            public void done(ParseException e) {
                if (e == null) {
                    login(username,password);
                    Log.d("Sign", "Successfull");
                } else {
                    Log.d("Sign", "FAIL");
                    e.printStackTrace();
                }
            }
        });
    }
}
