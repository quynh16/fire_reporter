package com.example.fire_reporter2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    private static String TAG = "LogonActivity";
    private String user_id = "";

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
                String entered_email = e_email.getText().toString().trim();
                Log.w(TAG, entered_email);
                String entered_password = e_password.getText().toString().trim();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
                Query check_email = reference.orderByChild("email").equalTo(entered_email);

                check_email.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            e_email.setError(null);

                            String database_email = "";
                            String database_password = "";
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                user_id = ds.getKey().toString();
                                Log.w(TAG, user_id);
                                database_email = ds.child("email").getValue().toString();
                                database_password = ds.child("password").getValue().toString();
                            }

                            if (database_email.equals(entered_email) && database_password.equals(entered_password)){
                                Intent intent = new Intent(LogonActivity.this, HomeActivity.class);
                                intent.putExtra("id", user_id);
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
}
