package com.example.fire_reporter2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileActivity extends AppCompatActivity {
    BottomNavigationView navbar;
    LinearLayout report_history;
    FirebaseDatabase database;
    DatabaseReference reference;

    TextInputEditText editName, editEmail, editPassword;

    private String user_id, user_name, user_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ImageButton profileBtn = findViewById(R.id.profile_btn);
        profileBtn.setVisibility(View.GONE);

        navbar = findViewById(R.id.bottom_navbar);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");

        editName = findViewById(R.id.edit_name);
        editEmail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_password);

        showAllUserData();

        ImageButton save_btn = (ImageButton)findViewById(R.id.save_profile_btn);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                if (update(v)) {
                    intent.putExtra("name", user_name);
                    intent.putExtra("email", user_email);
                    intent.putExtra("id", user_id);
                }
                startActivity(intent);
            }
        });

        ImageButton backBtn = (ImageButton)findViewById(R.id.back_to_profile_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                intent.putExtra("id", user_id);
                startActivity(intent);
            }
        });

        ImageButton saveBtn = (ImageButton)findViewById(R.id.save_profile_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserHelperClass helper = new UserHelperClass(user_id, user_name, user_email);
                reference.child(user_id).setValue(helper);
                startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class));
            }
        });

        navbar.setSelectedItemId(R.id.home);
        navbar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.reporting:
                        startActivity(new Intent(getApplicationContext(), ReportingActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.map:
                        startActivity(new Intent(getApplicationContext(), MapActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    private void showAllUserData() {
        Intent intent = getIntent();
        user_name = intent.getStringExtra("name");
        user_email = intent.getStringExtra("email");
        user_id = intent.getStringExtra("id");

        editName.setText(user_name);
        editEmail.setText(user_email);
    }

    public boolean update(View view) {
        if (nameChanged() || emailChanged()) {
            Toast.makeText(this, "Profile has been updated", Toast.LENGTH_LONG).show();
            return true;
        } else {
            Toast.makeText(this, "Nothing has been updated", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private boolean emailChanged() {
        if (!user_email.equals(editEmail.getText().toString())) {
            reference.child(user_id).child("email").setValue(editEmail.getText().toString());
            user_email = editEmail.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean passwordChanged() {
        return true;
    }

    private boolean nameChanged() {
        if (!user_name.equals(editName.getText().toString())) {
            reference.child(user_id).child("name").setValue(editName.getText().toString());
            user_name = editEmail.getText().toString();
            return true;
        } else {
            return false;
        }
    }
}