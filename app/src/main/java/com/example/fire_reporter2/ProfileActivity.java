package com.example.fire_reporter2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    BottomNavigationView navbar;
    LinearLayout report_history;
    FirebaseDatabase database;
    DatabaseReference reference;

    private ImageView profilePicture;
    private TextView name, email;
    private ImageButton editBtn, profileBtn;

    private String user_name, user_email, user_id;

    private static final String USERS = "users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // user information
        name = findViewById(R.id.user_name);
        email = findViewById(R.id.user_email);
        profilePicture = findViewById(R.id.profile_picture);
        editBtn =  findViewById(R.id.edit_profile_btn);

        profileBtn = findViewById(R.id.profile_btn);
        profileBtn.setVisibility(View.GONE);

        navbar = findViewById(R.id.bottom_navbar);
        report_history = findViewById(R.id.report_history);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference(USERS);

        getUserID();
        showAllUserData();

        editBtn.setOnClickListener((View v) -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            intent.putExtra("id", user_id);
            startActivity(intent);
        });

        navbar.setSelectedItemId(R.id.home);
        navbar.setOnItemSelectedListener((@NonNull MenuItem item) -> {
            Intent intent;
            int id = item.getItemId();
            switch (id){
                case R.id.home:
                    intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.putExtra("id", user_id);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    return true;
                case R.id.reporting:
                    intent = new Intent(getApplicationContext(), ReportingActivity.class);
                    intent.putExtra("id", user_id);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    return true;
                case R.id.map:
                    intent = new Intent(getApplicationContext(), MapActivity.class);
                    intent.putExtra("id", user_id);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        });
        for (int i = 0; i < 5; i++) {
            createReportCard(findViewById(android.R.id.content));
        }
    }

    private void getUserID() {
        Intent intent = getIntent();
        user_id = intent.getStringExtra("id");
    }

    private void showAllUserData() {
        Log.d(TAG, "User ID: " + user_id);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                user_name = dataSnapshot.child(user_id).child("name").getValue(String.class);
                user_email = dataSnapshot.child(user_id).child("email").getValue(String.class);
                name.setText(user_name);
                email.setText(user_email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void createReportCard(View v) {
        View view = getLayoutInflater().inflate(R.layout.report_history_card, (ViewGroup) v, false);
        report_history.addView(view);
    }
}