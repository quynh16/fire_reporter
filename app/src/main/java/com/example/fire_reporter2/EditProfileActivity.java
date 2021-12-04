package com.example.fire_reporter2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = "EditProfileActivity";
    BottomNavigationView navbar;
    LinearLayout report_history;
    FirebaseDatabase database;
    DatabaseReference reference;

    TextInputEditText editName, editEmail, editPassword;

    private String user_name, user_email, user_id;

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

        getUserDataFromDB();

        editName.setText(user_name);
        editEmail.setText(user_email);

        ImageButton backBtn = (ImageButton)findViewById(R.id.back_to_profile_btn);
        backBtn.setOnClickListener((View v) -> {
            Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
            intent.putExtra("id", user_id);
            startActivity(intent);
        });

        ImageButton saveBtn = (ImageButton)findViewById(R.id.save_profile_btn);
        saveBtn.setOnClickListener((View v) -> {
            update(v);
            Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
            intent.putExtra("id", user_id);
            startActivity(intent);
        });

        navbar.setSelectedItemId(R.id.home);
        navbar.setOnItemSelectedListener((@NonNull MenuItem item) -> {
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
        });
    }

    private void getUserDataFromDB() {
        Intent intent = getIntent();
        user_id = intent.getStringExtra("id");
        Log.d(TAG, "User ID: " + user_id);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                user_name = dataSnapshot.child(user_id).child("name").getValue(String.class);
                user_email = dataSnapshot.child(user_id).child("email").getValue(String.class);
                editName.setText(user_name);
                editEmail.setText(user_email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
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