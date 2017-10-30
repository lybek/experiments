/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Boolean signUpModeActive = true;

    TextView changeSignupModeTextView;

    EditText passwordEditText;

    EditText repeatPassword;

    EditText usernameEditText;

    public void showUserList() {
        Intent i = new Intent(getApplicationContext(), UserListActivity.class);
        startActivity(i);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.changeSignupModeTextView) {

            Button signupButton = (Button) findViewById(R.id.signupButton);

            if (signUpModeActive) {

                signUpModeActive = false;
                signupButton.setText("Login");
                changeSignupModeTextView.setText("Signup");
                repeatPassword.setVisibility(View.INVISIBLE);
                passwordEditText.setText("");
                usernameEditText.setText("");
                repeatPassword.setText("");
                usernameEditText.requestFocus();

            } else {

                signUpModeActive = true;
                signupButton.setText("Signup");
                changeSignupModeTextView.setText("Login");
                repeatPassword.setVisibility(View.VISIBLE);
                passwordEditText.setText("");
                usernameEditText.setText("");
                repeatPassword.setText("");
                usernameEditText.requestFocus();

            }

        } else if (view.getId() == R.id.logo || view.getId() == R.id.background) {

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        }

    }

    public void signUp(View view) {

        if (usernameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")) {

            Toast.makeText(this, "A username and password are required.", Toast.LENGTH_SHORT).show();

        } else {

            if (signUpModeActive) {

                if (!passwordEditText.getText().toString().equals(repeatPassword.getText().toString())) {

                    Toast.makeText(getApplicationContext(), "Passwords do not match!", Toast.LENGTH_LONG).show();
                    return;

                }

                ParseUser user = new ParseUser();

                user.setUsername(usernameEditText.getText().toString());
                user.setPassword(passwordEditText.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {

                            Log.i("Signup", "Successful");

                            showUserList();

                        } else {

                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            } else {

                ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {

                        if (user != null) {

                            Log.i("Signup", "Login successful");

                            showUserList();

                        } else {

                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        }
    }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
      setTitle("Instagram");

      changeSignupModeTextView = (TextView) findViewById(R.id.changeSignupModeTextView);

      usernameEditText = (EditText) findViewById(R.id.usernameEditText);

      passwordEditText = (EditText) findViewById(R.id.passwordEditText);

      repeatPassword = (EditText) findViewById(R.id.repeatPassword);

      RelativeLayout layout = (RelativeLayout)findViewById(R.id.background);

      ImageView logo = (ImageView)findViewById(R.id.logo);

      layout.setOnClickListener(this);

      logo.setOnClickListener(this);

      if (ParseUser.getCurrentUser() != null) {
          showUserList();
      }

      passwordEditText.setOnKeyListener(new View.OnKeyListener() {
          @Override
          public boolean onKey(View v, int keyCode, KeyEvent event) {

              if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                  signUp(v);
              }

              return false;
          }
      });

      changeSignupModeTextView.setOnClickListener(this);


    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

}
