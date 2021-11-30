package com.example.fire_reporter2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class LogonActivity extends AppCompatActivity {
    private EditText e_email;
    private EditText e_password;
    private Button e_logon;
    private TextView e_attempts_info;
    private TextView e_register;

    boolean isValid = false;
    private int counter = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        e_email = findViewById(R.id.et_email);
        e_password = findViewById(R.id.et_password);
        e_logon = findViewById(R.id.btn_logon);
        e_attempts_info = findViewById(R.id.tv_attempt_info);
        e_register = findViewById(R.id.tv_register);

        //on click listener for the logon button:
        e_logon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input_email = e_email.getText().toString();
                String input_password = e_password.getText().toString();

                if (input_email.isEmpty() || input_password.isEmpty()) {
                    Toast.makeText(LogonActivity.this, "Please enter your logon information correctly!", Toast.LENGTH_SHORT).show();
                } else {
                    isValid = validate(input_email, input_password);

                    if (!isValid) {
                        counter--;
                        Toast.makeText(LogonActivity.this, "Incorrect credentials entered", Toast.LENGTH_SHORT).show();
                        e_attempts_info.setText("Number of Attempts Remaining: " + counter);

                        if (counter == 0) {
                            e_logon.setEnabled(false);
                        }
                    } else {
                        Toast.makeText(LogonActivity.this, "Logon was successful!", Toast.LENGTH_SHORT).show();
                        //Add code to send user to Profile OR Homepage. Which one is TBD. Currently set to Homepage
                        Intent intent = new Intent(LogonActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });

        //On click listener for the New User textview
        e_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogonActivity.this, RegistrationActivity.class));
            }
        });
    }

    //Function for validating logon information.
    private boolean validate(String Email, String Password) {
        if (RegistrationActivity.credentials != null) {
            if (Email.equals(RegistrationActivity.credentials.getEmail()) && Password.equals(RegistrationActivity.credentials.getPassword())) {
                return true;
            }
        }
        return false;
    }
}
