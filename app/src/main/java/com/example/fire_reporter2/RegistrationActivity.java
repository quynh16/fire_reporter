package com.example.fire_reporter2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegistrationActivity extends AppCompatActivity {
    //Defining EditText and Button variables
    private EditText e_reg_email;
    private EditText e_reg_password;
    private EditText e_reg_full_name;
    private Button e_register;

    public static Credentials credentials;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Associating variables with the XML
        e_reg_email = findViewById(R.id.et_reg_email);
        e_reg_password = findViewById(R.id.et_reg_password);
        e_reg_full_name = findViewById(R.id.et_full_name);
        e_register = findViewById(R.id.btn_register);

        //Set on-click listener for the button
        e_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reg_email = e_reg_email.getText().toString();
                String reg_password = e_reg_password.getText().toString();
                String reg_full_name = e_reg_full_name.getText().toString();

                if(validate(reg_email, reg_password, reg_full_name)){
                    Credentials credentials = new Credentials(reg_email, reg_password, reg_full_name);
                    //Link to Firebase:
                    //NOTE: Remove from here to "END REMOVE" if Login via database does not work.
                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference("users");
                    reference.child(reg_full_name).setValue(credentials);
                    //END REMOVE
                    //Take user back to Login Screen:
                    Intent intent = new Intent(RegistrationActivity.this, LogonActivity.class); //Recent change 11/28
                    startActivity(intent);
                    Toast.makeText(RegistrationActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //Validate fields:
    private boolean validate(String email, String password, String full_name){
        if(email.isEmpty() || password.length() < 8 || full_name.isEmpty()){
            Toast.makeText(this, "Please enter all user details. Password should be at least 8 characters.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}