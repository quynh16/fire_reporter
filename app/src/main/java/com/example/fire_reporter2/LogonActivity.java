package com.example.fire_reporter2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


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

                //NOTE: Remove from here to "END REMOVE" if Login via database does not work.
                String entered_email = e_email.getText().toString().trim();
                String entered_password = e_password.getText().toString().trim();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
                Query check_email = reference.orderByChild("email").equalTo(entered_email);

                check_email.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            e_email.setError(null);

                            String database_email = snapshot.child(entered_email).child("email").getValue(String.class);
                            String database_password = snapshot.child(entered_email).child("password").getValue(String.class);

                            if (database_email.equals(entered_email) && database_password.equals(entered_password)){
                                Intent intent = new Intent(LogonActivity.this, HomeActivity.class);
                                intent.putExtra("email", database_email);
                                intent.putExtra("password", database_password);
                                startActivity(intent);
                            }
                            else{
                                e_password.setError("Wrong password.");
                            }
                        }
                        else{
                            e_email.setError("There is no account associated with this email.");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                //END REMOVE
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
