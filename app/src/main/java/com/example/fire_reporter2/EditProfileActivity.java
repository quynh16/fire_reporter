package com.example.fire_reporter2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = "EditttProfileActivity";
    BottomNavigationView navbar;
    FirebaseDatabase database;
    DatabaseReference reference;

    TextInputEditText editName, editEmail, oldPassword, newPassword, confirmPassword;
    ImageButton backBtn, saveBtn;

    private String user_name, user_email, user_id, user_password;

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
        oldPassword = findViewById(R.id.old_password);
        newPassword = findViewById(R.id.new_password);
        confirmPassword = findViewById(R.id.confirm_password);

        showAllUserData();

        backBtn = (ImageButton)findViewById(R.id.back_to_profile_btn);
        backBtn.setOnClickListener((View v) -> {
            Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
            intent.putExtra("id", user_id);
            startActivity(intent);
        });

        saveBtn = (ImageButton)findViewById(R.id.save_profile_btn);
        saveBtn.setOnClickListener((View v) -> {
            if (update(v)) {
                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                intent.putExtra("id", user_id);
                startActivity(intent);
            }
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

    private void showAllUserData() {
        Intent intent = getIntent();
        user_id = intent.getStringExtra("id");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                user_name = dataSnapshot.child(user_id).child("name").getValue(String.class);
                user_email = dataSnapshot.child(user_id).child("email").getValue(String.class);
                user_password = dataSnapshot.child(user_id).child("password").getValue(String.class);
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
        if (passwordChanged() && !validatePasswordChange()) {
            // user's attempt to change password failed
            return false;
        } else if (nameChanged() || emailChanged() || passwordChanged()) {
            Toast.makeText(this, "Profile has been updated", Toast.LENGTH_LONG).show();
            return true;
        } else if (!nameChanged() && !emailChanged() && !passwordChanged()) {
            Toast.makeText(this, "Nothing has been updated", Toast.LENGTH_LONG).show();
            return true;
        }
        return true;
    }

    private boolean isEmpty(TextInputEditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    private boolean emailChanged() {
        if (!user_email.equals(editEmail.getText().toString())) {
            reference.child(user_id).child("email").setValue(editEmail.getText().toString());
            return true;
        } else {
            return false;
        }
    }

    private boolean validatePasswordChange() {
        if (!isEmpty(oldPassword) && !isEmpty(newPassword) && !isEmpty(confirmPassword)) {
            String old_password = oldPassword.getText().toString();
            String new_password = newPassword.getText().toString();
            String confirm_password = confirmPassword.getText().toString();

            if (!user_password.equals(old_password)) {
                Toast.makeText(this, "Incorrect password", Toast.LENGTH_LONG).show();
            } else {
                if (!new_password.equals(confirm_password)) {
                    Toast.makeText(this, "New passwords do not match", Toast.LENGTH_LONG).show();
                } else {
                    reference.child(user_id).child("password").setValue(new_password);
                    return true;
                }
            }
        } else if (isEmpty(confirmPassword) && !isEmpty(oldPassword) && !isEmpty(newPassword)) {
            Toast.makeText(this, "Please re-enter your new password", Toast.LENGTH_LONG).show();
        } else if (isEmpty(oldPassword) && (!isEmpty(newPassword) || !isEmpty(confirmPassword))) {
            Toast.makeText(this, "Please enter your old password", Toast.LENGTH_LONG).show();
        } else if (isEmpty(newPassword) && !isEmpty(oldPassword)) {
            Toast.makeText(this, "Please enter your new password", Toast.LENGTH_LONG).show();
        }
        return false;
    }
    private boolean passwordChanged() {
//      user did not try to change password
        if (isEmpty(oldPassword) && isEmpty(newPassword) && isEmpty(confirmPassword)) {
            return false;
        }
        return true;
    }

    private boolean nameChanged() {
        if (!user_name.equals(editName.getText().toString())) {
            reference.child(user_id).child("name").setValue(editName.getText().toString());
            return true;
        } else {
            return false;
        }
    }
}